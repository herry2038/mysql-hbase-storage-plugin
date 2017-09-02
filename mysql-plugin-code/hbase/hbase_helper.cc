#include "Hbase_helper.h"
#include "sql_priv.h"
#include "sql_class.h"           // MYSQL_HANDLERTON_INTERFACE_VERSION

boost::shared_ptr<CHbaseWrapper> Hbase_helper::hbase;

char* Hbase_helper::ip = NULL;

ulong Hbase_helper::port = 9090;
ulong Hbase_helper::pool_size = (ulong)-1;
ulong Hbase_helper::idle_size = 20;
ulong Hbase_helper::bulk_size = 20;

Hbase_helper::StrMap Hbase_helper::attributes;
int Hbase_helper::EMPTY_RESULT = -201;


static inline ulong field_offset(Field* field, TABLE* table) {
	return((ulong)(field->ptr - table->record[0]));
}

void Hbase_helper::pathToTableName(const char* path, std::string& table)
{
#ifdef WIN32
	const static char path_sep = '\\' ;
#else
	const static char path_sep = '/';
#endif
	const char* table_pos = strrchr(path, path_sep);
		
	const char* db_pos = table_pos - 1; 
	for (; *db_pos != path_sep; --db_pos);

	table.append(db_pos + 1, table_pos);
	table.append(".");
	if (*(table_pos + 1) == '#') table_pos++;
	table.append(table_pos + 1);
}



int Hbase_helper::pack_row(TABLE* table, std::vector<Mutation>& mutations)
{
	char insert_field_value_buffer[STRING_BUFFER_USUAL_SIZE];
	String insert_field_value_string(insert_field_value_buffer,
		sizeof(insert_field_value_buffer),
		&my_charset_bin);
	insert_field_value_string.length(0);

	for (uint i = 0; i < table->s->fields; ++i)
	{
		Field *field = table->field[i];
		if (! is_primary(table, i)) {
			if (field->is_null()) {
				field->set_default();
				field->set_notnull();
			}

			Mutation mutation;
			mutation.column.append("entry:").append(field->field_name);
			if (!field->is_null()) {
				field->val_str(&insert_field_value_string);				
				mutation.value.assign(insert_field_value_string.c_ptr(), insert_field_value_string.length()) ;				
				insert_field_value_string.length(0);
			}
			mutations.push_back(mutation);
		}
	}
	return 0;
}


int Hbase_helper::pack_key(TABLE* table, uint key_id, std::string& key) 
{
	return parse_key(table, key_id, NULL, HA_WHOLE_KEY, key);
	/*
	char insert_field_value_buffer[STRING_BUFFER_USUAL_SIZE];
	String insert_field_value_string(insert_field_value_buffer,
		sizeof(insert_field_value_buffer),
		&my_charset_bin);
	insert_field_value_string.length(0);

	//DEBUG("key:%s has %u parts", table->key_info[key_id].name, table->key_info[key_id].key_parts);
	for (uint i = 0; i < table->key_info[key_id].usable_key_parts; ++i)
	{
		Field* field = table->key_info[key_id].key_part[i].field;
		field->val_str(&insert_field_value_string);
		
		key.append(insert_field_value_string.c_ptr(), insert_field_value_string.length());
		key.append("-");
		insert_field_value_string.length(0);
	}
	key.erase(key.length() - 1);
	return 0;
	*/
	/*
	char __tmp[1024];
	int len = 0;
	len += snprintf(__tmp + len, sizeof(__tmp) - len, "packed key`%s`: ", table->s->key_info[key_id].name);
	for (uchar *p = (uchar *)to; p < end; ++p)
	{
	len += snprintf(__tmp + len, sizeof(__tmp) - len, "0x%02X ", *p);
	}
	DEBUG("%s", __tmp);
	*/	
}

bool Hbase_helper::is_primary(TABLE* table, uint pos)
{
	for (uint i = 0; i < table->key_info[table->s->primary_key].usable_key_parts; ++i)
	{
		if (table->key_info[table->s->primary_key].key_part[i].field == table->field[pos])
			return true;
	}
	return false;
}

int Hbase_helper::read_row(TABLE* table, uchar* buf, const std::string& tableName, const std::string& key, std::string& errmsg)
{
	memset(buf, 0, table->s->null_bytes);

	std::vector<TRowResult> results;

	int ret = hbase->getRow(tableName, key, results, errmsg);
	if (ret != 0) return ret;

	if (results.empty()) return EMPTY_RESULT;

	my_bitmap_map *old_map = dbug_tmp_use_all_columns(table, table->write_set);
	TRowResult& row = results[0];

	unpack_key(table, table->s->primary_key, row.row, buf);

	unpack_row(table, row.columns, buf);
	dbug_tmp_restore_column_map(table->write_set, old_map);

	return ret;
}


int Hbase_helper::read_row(TABLE* table, uchar* buf, ScannerID scanid, std::string& errmsg)
{
	memset(buf, 0, table->s->null_bytes);

	
	std::vector<TRowResult> results;
	int ret = hbase->scannerGet(results, scanid, errmsg) ;
	if (ret != 0) return ret;

	if (results.empty()) return EMPTY_RESULT;
	my_bitmap_map *old_map = dbug_tmp_use_all_columns(table, table->write_set);
	TRowResult& row = results[0];

	unpack_key(table, table->s->primary_key, row.row, buf);

	unpack_row(table, row.columns, buf);
	dbug_tmp_restore_column_map(table->write_set, old_map);
		
	return ret; 
}


int Hbase_helper::unpack_key(TABLE* table, uint key_id, const std::string& key, uchar* buf)
{
	/*
	std::auto_ptr<char> msg(new char[key.length() + 1]);
	memcpy(msg.get(), key.c_str(), key.length());

	std::vector<char*> parts;
	split((char*)msg.get(), "-", parts);
	if (parts.size() != table->key_info[key_id].usable_key_parts) {
		sql_print_error("Hbase_helper::unpack_key error:%s", key.c_str());
		return -1;
	}
	*/

	uint real_key_id = key.at(0) - '0';
	

	const char* tmp = key.data() + 1 ;

	for (uint i = 0; i < table->key_info[key_id].actual_key_parts; ++i)
	{
		Field* field = table->key_info[key_id].key_part[i].field;
		switch (field->type()) {
		case MYSQL_TYPE_LONG:
		case MYSQL_TYPE_LONGLONG:
		case MYSQL_TYPE_TINY:
		case MYSQL_TYPE_SHORT:
		case MYSQL_TYPE_INT24:
		{
								 longlong value = herry_uint8korr((uchar*)tmp);
								 Field_num* fieldNum = (Field_num*)field;
								 field->store(value, fieldNum->unsigned_flag);
								 tmp += sizeof(longlong);
								 break;
		}
		case MYSQL_TYPE_VAR_STRING:	/* old <= 4.1 VARCHAR */
		case MYSQL_TYPE_VARCHAR:	/* new >= 5.0.3 true VARCHAR */
		case MYSQL_TYPE_BIT:
		case MYSQL_TYPE_STRING:
		{
								  int len = strlen(tmp);
								  field->store(tmp, len, &my_charset_bin);
								  tmp += len + 1;
								  break;
		}
		case MYSQL_TYPE_TIME:
		case MYSQL_TYPE_DATETIME:
		case MYSQL_TYPE_TIMESTAMP:
		{
									 longlong value = mi_uint8korr(tmp);
									 field->store(value, false);
									 tmp += sizeof(longlong);
									 break;
		}
		default:
			break;
		}
		field->set_notnull();		
	}
	return 0;
}


int Hbase_helper::unpack_row(TABLE* table, std::map<Text, TCell>& columns, uchar* buf)
{
	for (uint i = 0; i < table->s->fields; ++i)
	{
		Field *field = table->field[i];
		if (!is_primary(table, i)) {
			std::map<Text, TCell>::iterator it = columns.find(std::string("entry:")+ field->field_name);
			if (it != columns.end()) {
				field->store(it->second.value.c_str(), it->second.value.length(), &my_charset_bin);
				field->set_notnull();
			}
			else {
				field->set_null();
				field->reset();
			}
		}
	}

	return 0;
}


int Hbase_helper::pack_key(TABLE* table, uint key, const uchar* record, key_part_map keypart_map, std::string& buf)
{
	KEY *key_info = table->key_info + key;

	uint length = 0;

	String value;
	buf.push_back((char)(key + '0'));

	//const uchar* ptr = key_buf;

	KEY_PART_INFO *key_part = key_info->key_part;
	KEY_PART_INFO *end_key_part = key_part + key_info->actual_key_parts;

	while (key_part < end_key_part && keypart_map)
	{
		switch (key_part->field->type())
		{
		case MYSQL_TYPE_LONG:
		case MYSQL_TYPE_LONGLONG:
		case MYSQL_TYPE_TINY:
		case MYSQL_TYPE_SHORT:
		case MYSQL_TYPE_INT24: 
		{
								longlong lv = (record == NULL ? key_part->field->val_int() : key_part->field->val_int((uchar *)(record + field_offset(key_part->field, table))));

								buf.resize(buf.size() + 8);			
								herry_int8store(buf.data() + buf.size() - 8, lv, (lv >= 0 ? 1:0));

								//buf.append((char*)(&lv), sizeof(longlong));
								break;
		}
		case MYSQL_TYPE_VAR_STRING:	/* old <= 4.1 VARCHAR */
		case MYSQL_TYPE_VARCHAR:	/* new >= 5.0.3 true VARCHAR */			
		case MYSQL_TYPE_BIT:
		case MYSQL_TYPE_STRING:	{					
									if (record == NULL) {
										key_part->field->val_str(&value);
										buf.append(value.c_ptr(), value.length());
									}
									else {
										uchar* ptr = (uchar *)(record + field_offset(key_part->field, table));

										//if (key_part->key_part_flag & HA_VAR_LENGTH_PART) {
										//	uint var_length = uint2korr(ptr);
										//	buf.append((char*)ptr + HA_KEY_BLOB_LENGTH, var_length);
										//}
										//else 
										{
											key_part->field->val_str(&value, ptr);
											buf.append(value.c_ptr(), value.length());
										}
									}
									buf.push_back(0);
									
									break;
		}
		case MYSQL_TYPE_TIME:
		case MYSQL_TYPE_DATETIME:
		case MYSQL_TYPE_TIMESTAMP:
		{
									 longlong lv = (record == NULL ? key_part->field->val_int() : key_part->field->val_int((uchar *)(record + field_offset(key_part->field, table))));
									 
									 buf.resize(buf.size() + 8);

									 mi_int8store(buf.data() + buf.size() - 8, lv);
									 //buf.append((char*)(&lv), sizeof(longlong));
									 break;
		}
			/*
			switch (field->real_type()) {
			case MYSQL_TYPE_TIME:
			case MYSQL_TYPE_DATETIME:
			case MYSQL_TYPE_TIMESTAMP:
				return(DATA_INT);
			default: 
				DBUG_ASSERT((ulint)MYSQL_TYPE_DECIMAL < 256);
			case MYSQL_TYPE_TIME2:
			case MYSQL_TYPE_DATETIME2:
			case MYSQL_TYPE_TIMESTAMP2:
				return(DATA_FIXBINARY);
			}
			*/
		default:
			sql_print_error("Hbase_helper::parse_key unspported key col type:%d", key_part->field->type());
			return -1;
		}
		//if ( ptr != NULL )
		//	ptr += key_part->store_length ; // TODO：考虑部分主键为NULL的情况
		keypart_map >>= 1;
		key_part++;
	}
}



int Hbase_helper::parse_key(TABLE* table, uint key, const uchar* key_buf, key_part_map keypart_map, std::string& buf)
{
	KEY *key_info = table->key_info + key;

	uint length = 0;

	String value;
	buf.push_back((char)(key + '0'));

	const uchar* ptr = key_buf;

	KEY_PART_INFO *key_part = key_info->key_part;
	KEY_PART_INFO *end_key_part = key_part + key_info->actual_key_parts;

	while (key_part < end_key_part && keypart_map)
	{
		switch (key_part->field->type())
		{
		case MYSQL_TYPE_LONG:
		case MYSQL_TYPE_LONGLONG:
		case MYSQL_TYPE_TINY:
		case MYSQL_TYPE_SHORT:
		case MYSQL_TYPE_INT24:
		{
								 longlong lv = (ptr == NULL ? key_part->field->val_int() : key_part->field->val_int(ptr));

								 buf.resize(buf.size() + 8);
								 herry_int8store(buf.data() + buf.size() - 8, lv, (lv >= 0 ? 1 : 0));

								 //buf.append((char*)(&lv), sizeof(longlong));
								 break;
		}
		case MYSQL_TYPE_VAR_STRING:	/* old <= 4.1 VARCHAR */
		case MYSQL_TYPE_VARCHAR:	/* new >= 5.0.3 true VARCHAR */
		case MYSQL_TYPE_BIT:
		case MYSQL_TYPE_STRING:	{
									if (ptr == NULL) {
										key_part->field->val_str(&value);
										buf.append(value.c_ptr(), value.length());
									}
									else {						
										if (key_part->key_part_flag & HA_VAR_LENGTH_PART) {
											uint var_length = uint2korr(ptr);
											buf.append((char*)ptr + HA_KEY_BLOB_LENGTH, var_length);
										}
										else {
											key_part->field->val_str(&value, ptr);
											buf.append(value.c_ptr(), value.length());
										}
									}
									buf.push_back(0);

									break;
		}
		case MYSQL_TYPE_TIME:
		case MYSQL_TYPE_DATETIME:
		case MYSQL_TYPE_TIMESTAMP:
		{
									 longlong lv = (ptr == NULL ? key_part->field->val_int() : key_part->field->val_int(ptr));

									 buf.resize(buf.size() + 8);

									 mi_int8store(buf.data() + buf.size() - 8, lv);
									 //buf.append((char*)(&lv), sizeof(longlong));
									 break;
		}
			/*
			switch (field->real_type()) {
			case MYSQL_TYPE_TIME:
			case MYSQL_TYPE_DATETIME:
			case MYSQL_TYPE_TIMESTAMP:
			return(DATA_INT);
			default:
			DBUG_ASSERT((ulint)MYSQL_TYPE_DECIMAL < 256);
			case MYSQL_TYPE_TIME2:
			case MYSQL_TYPE_DATETIME2:
			case MYSQL_TYPE_TIMESTAMP2:
			return(DATA_FIXBINARY);
			}
			*/
		default:
			sql_print_error("Hbase_helper::parse_key unspported key col type:%d", key_part->field->type());
			return -1;
		}
		if ( ptr != NULL )
			ptr += key_part->store_length ; // TODO：考虑部分主键为NULL的情况
		keypart_map >>= 1;
		key_part++;
	}
}

void Hbase_helper::init_pool()
{
	boost::shared_ptr<CHbaseConnectionFactory> f(new CHbaseConnectionFactory(ip, port));
	hbase.reset(new CHbaseWrapper(pool_size, idle_size, f));
}

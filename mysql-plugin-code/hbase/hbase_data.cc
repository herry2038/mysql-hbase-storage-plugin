#include "Hbase_data.h"
#include "Hbase_helper.h"

Hbase_data::Hbase_data(const char* tableName) {

	Hbase_helper::pathToTableName(tableName, tableName_);
}

int Hbase_data::create_table(const char* path, TABLE* table, std::string& errmsg) 
{
	std::string tableName;
	Hbase_helper::pathToTableName(path, tableName);



	return Hbase_helper::hbase->createTable(tableName, errmsg);	
}

int Hbase_data::delete_table(const char* path, std::string& errmsg)
{
	std::string tableName;
	Hbase_helper::pathToTableName(path, tableName);

	
	return Hbase_helper::hbase->deleteTable(tableName, errmsg);
	
}


int Hbase_data::write_row(uchar* data, TABLE* table, std::string& errmsg)
{
	my_bitmap_map *old_map = dbug_tmp_use_all_columns(table, table->read_set);
	std::vector<Mutation> mutations;
	std::string key;
	Hbase_helper::pack_key(table, table->s->primary_key, key);
	Hbase_helper::pack_row(table, mutations);

	dbug_tmp_restore_column_map(table->read_set, old_map);	
	return Hbase_helper::hbase->mutateRow(tableName_, key, mutations, errmsg);	
}


int Hbase_data::update_row(uchar* old_data, uchar* new_data, TABLE* table, std::string& errmsg)
{
	my_bitmap_map *old_map = dbug_tmp_use_all_columns(table, table->read_set);

	std::vector<BatchMutation> mutations;
	BatchMutation mutation;
	mutation.__isset.mutations = true;
	mutation.__isset.row = true; 

	Hbase_helper::pack_key(table, table->s->primary_key, mutation.row);
	Hbase_helper::pack_row(table, mutation.mutations);
	mutations.push_back(mutation);

	std::string pk = mutation.row;

	std::string old_key, new_key;
	for (uint key = table->s->primary_key + 1; key < table->s->keys; key++) {
		
		old_key.clear(); new_key.clear();
		Hbase_helper::pack_key(table, key, old_data, HA_WHOLE_KEY, old_key);
		Hbase_helper::pack_key(table, key, new_data, HA_WHOLE_KEY, new_key);

		if (old_key != new_key) {
			mutation.mutations.clear();
			mutation.row = old_key;
			Mutation m;
			m.__set_isDelete(true);
			m.__set_column("entry:key");
			mutation.mutations.push_back(m);
			mutations.push_back(mutation);

			mutation.mutations.clear();
			mutation.row = new_key;
			m.__set_isDelete(false);
			m.__set_column("entry:key");
			m.__set_value(pk);
			mutation.mutations.push_back(m);
			mutations.push_back(mutation);
		}		
	}

	dbug_tmp_restore_column_map(table->read_set, old_map);
	return  Hbase_helper::hbase->mutateRows(tableName_, mutations, errmsg);	
}

int Hbase_data::delete_row(uchar* data, TABLE* table, std::string& errmsg)
{
	my_bitmap_map *old_map = dbug_tmp_use_all_columns(table, table->read_set);	
	std::string key_str;
	
	std::vector<Text> rows;
	
	for (uint key = 0; key < table->s->keys; key++) {		
		Hbase_helper::pack_key(table, key, key_str);
		rows.push_back(key_str);
		key_str.clear();
	}
	dbug_tmp_restore_column_map(table->read_set, old_map);
	return Hbase_helper::hbase->deleteRows(tableName_, rows, errmsg);		

	//return Hbase_helper::hbase->deleteAllRow(tableName_, key, errmsg);		
}



int Hbase_data::trunc_table(std::string& errmsg)
{
	return Hbase_helper::hbase->truncateTable(tableName_, errmsg);
}

int Hbase_data::get_row(const std::string& key, std::vector<TRowResult>& result, std::string& errmsg)
{
	return Hbase_helper::hbase->getRow(tableName_, key, result, errmsg);
}

int Hbase_data::get_index_pos(const std::string& key, ha_rkey_function find_flag, ScannerID& current_position, std::string& errmsg)
{		
	TScan scan;

	scan.__set_startRow(key);
	scan.__set_reversed(find_flag == HA_READ_KEY_OR_PREV || find_flag == HA_READ_BEFORE_KEY);

	return Hbase_helper::hbase->scannerOpenWithScan(tableName_, scan, current_position, errmsg);	
}


int Hbase_data::get_index_pos(const std::string& key_start, const std::string& key_end, bool reversed, ScannerID& current_position, std::string& errmsg)
{
	TScan scan;

	scan.__set_startRow(key_start);
	scan.__set_stopRow(key_end);
	scan.__set_reversed(reversed);

	return Hbase_helper::hbase->scannerOpenWithScan(tableName_, scan, current_position, errmsg);
}

void Hbase_data::return_connection(CHbaseConnection* c)
{
	Hbase_helper::hbase->getPool().unborrow(c);
}

int Hbase_data::read_row(uchar* data, TABLE* table, ScannerID scannId, bool isPk, std::string& errmsg)
{
	int rc = 0;
	if (isPk)
		rc = Hbase_helper::read_row(table, data, scannId, errmsg);
	else {
		std::vector<TRowResult> result;
		rc = Hbase_helper::hbase->scannerGet(result, scannId, errmsg);
		if (!rc) {
			if (result.empty()) rc = Hbase_helper::EMPTY_RESULT;
			else {
				const std::string& key_str = result[0].columns.begin()->second.value;
				rc = Hbase_helper::read_row(table, data, tableName_, key_str, errmsg);
			}
		}
	}
	return rc;
}


/* Copyright (C) 2003 MySQL AB, 2009 Sun Microsystems, Inc.

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; version 2 of the License.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA */

/**
@file ha_hbase.cc

@brief
The ha_hbase engine is a stubbed storage engine for example purposes only;
it does nothing at this point. Its purpose is to provide a source
code illustration of how to begin writing new storage engines; see also
/storage/example/ha_hbase.h.

@details
ha_hbase will let you create/open/delete tables, but
nothing further (for example, indexes are not supported nor can data
be stored in the table). Use this example as a template for
implementing the same functionality in your own storage engine. You
can enable the example storage engine in your build by doing the
following during your build process:<br> ./configure
--with-example-storage-engine

Once this is done, MySQL will let you create tables with:<br>
CREATE TABLE <table name> (...) ENGINE=EXAMPLE;

The example storage engine is set up to use table locks. It
implements an example "SHARE" that is inserted into a hash by table
name. You can use this to store information of state that any
example handler object will be able to see when it is using that
table.

Please read the object definition in ha_hbase.h before reading the rest
of this file.

@note
When you create an EXAMPLE table, the MySQL Server creates a table .frm
(format) file in the database directory, using the table name as the file
name as is customary with MySQL. No other files are created. To get an idea
of what occurs, here is an example select that would do a scan of an entire
table:

@code
ha_hbase::store_lock
ha_hbase::external_lock
ha_hbase::info
ha_hbase::rnd_init
ha_hbase::extra
ENUM HA_EXTRA_CACHE        Cache record in HA_rrnd()
ha_hbase::rnd_next
ha_hbase::rnd_next
ha_hbase::rnd_next
ha_hbase::rnd_next
ha_hbase::rnd_next
ha_hbase::rnd_next
ha_hbase::rnd_next
ha_hbase::rnd_next
ha_hbase::rnd_next
ha_hbase::extra
ENUM HA_EXTRA_NO_CACHE     End caching of records (def)
ha_hbase::external_lock
ha_hbase::extra
ENUM HA_EXTRA_RESET        Reset database to after open
@endcode

Here you see that the example storage engine has 9 rows called before
rnd_next signals that it has reached the end of its data. Also note that
the table in question was already opened; had it not been open, a call to
ha_hbase::open() would also have been necessary. Calls to
ha_hbase::extra() are hints as to what will be occuring to the request.

A Longer Example can be found called the "Skeleton Engine" which can be 
found on TangentOrg. It has both an engine and a full build environment
for building a pluggable storage engine.

Happy coding!<br>
-Brian
*/
#define MYSQL_SERVER 1
#ifdef USE_PRAGMA_IMPLEMENTATION
#pragma implementation        // gcc: Class implementation
#endif

#include "sql_priv.h"
#include "sql_class.h"           // MYSQL_HANDLERTON_INTERFACE_VERSION
#include "probes_mysql.h"
#include "sql_plugin.h"
#include <mysql/plugin.h>

#undef UNKNOWN
#include "ha_hbase.h"
#include "hbase_helper.h"


static handler *hbase_create_handler(handlerton *hton,
	TABLE_SHARE *table, 
	MEM_ROOT *mem_root);

handlerton *hbase_hton;

/* Variables for example share methods */

/* 
Hash used to track the number of open tables; variable for example share
methods
*/
static HASH hbase_open_tables;

/* The mutex used to init the hash; variable for example share methods */
mysql_mutex_t hbase_mutex;

/**
@brief
Function we use in the creation of our hash to get key.
*/

static uchar* hbase_get_key(hbase_SHARE *share, size_t *length,
	my_bool not_used __attribute__((unused)))
{
	*length=share->table_name_length;
	return (uchar*) share->table_name;
}

#ifdef HAVE_PSI_INTERFACE
static PSI_mutex_key ex_key_mutex_hbase, ex_key_mutex_hbase_SHARE_mutex;

static PSI_mutex_info all_hbase_mutexes[]=
{
	{ &ex_key_mutex_hbase, "hbase", PSI_FLAG_GLOBAL},
	{ &ex_key_mutex_hbase_SHARE_mutex, "hbase_SHARE::mutex", 0}
};

static void init_hbase_psi_keys()
{
	const char* category= "hbase";
	int count;

	if (PSI_server == NULL)
		return;

	count= array_elements(all_hbase_mutexes);
	PSI_server->register_mutex(category, all_hbase_mutexes, count);
}
#endif


static int hbase_init_func(void *p)
{
	DBUG_ENTER("hbase_init_func");

#ifdef HAVE_PSI_INTERFACE
	init_hbase_psi_keys();
#endif

	hbase_hton= (handlerton *)p;
	mysql_mutex_init(ex_key_mutex_hbase, &hbase_mutex, MY_MUTEX_INIT_FAST);
	(void) my_hash_init(&hbase_open_tables,system_charset_info,32,0,0,
		(my_hash_get_key) hbase_get_key,0,0);

	hbase_hton->state=   SHOW_OPTION_YES;
	hbase_hton->create=  hbase_create_handler;
	hbase_hton->flags = HTON_NO_PARTITION; // | HTON_CAN_RECREATE;

	Hbase_helper::init_pool();

	DBUG_RETURN(0);
}


static int hbase_done_func(void *p)
{
	int error= 0;
	DBUG_ENTER("hbase_done_func");

	if (hbase_open_tables.records)
		error= 1;
	my_hash_free(&hbase_open_tables);
	mysql_mutex_destroy(&hbase_mutex);

	DBUG_RETURN(error);
}


/**
@brief
Example of simple lock controls. The "share" it creates is a
structure we will pass to each example handler. Do you have to have
one of these? Well, you have pieces that are used for locking, and
they are needed to function.
*/

static hbase_SHARE *get_share(const char *table_name, TABLE *table)
{
	hbase_SHARE *share;
	uint length;
	char *tmp_name;

	mysql_mutex_lock(&hbase_mutex);
	length=(uint) strlen(table_name);

	if (!(share=(hbase_SHARE*) my_hash_search(&hbase_open_tables,
		(uchar*) table_name,
		length)))
	{
		if (!(share=(hbase_SHARE *)
			my_multi_malloc(MYF(MY_WME | MY_ZEROFILL),
			&share, sizeof(*share),
			&tmp_name, length+1,
			NullS)))
		{
			mysql_mutex_unlock(&hbase_mutex);
			return NULL;
		}

		share->use_count=0;
		share->table_name_length=length;
		share->table_name=tmp_name;
		strmov(share->table_name,table_name);
		if (my_hash_insert(&hbase_open_tables, (uchar*) share))
			goto error;
		thr_lock_init(&share->lock);
		/*  USER ADD CODE
		Create an instance of data class
		*/
		share->data_class = new Hbase_data(table_name);		
		mysql_mutex_init(ex_key_mutex_hbase_SHARE_mutex,
			&share->mutex, MY_MUTEX_INIT_FAST);
	}
	share->use_count++;
	mysql_mutex_unlock(&hbase_mutex);

	return share;

error:
	mysql_mutex_destroy(&share->mutex);
	mysql_mutex_unlock(&hbase_mutex);
	my_free(share);

	return NULL;
}


/**
@brief
Free lock controls. We call this whenever we close a table. If the table had
the last reference to the share, then we free memory associated with it.
*/

static int free_share(hbase_SHARE *share)
{
	mysql_mutex_lock(&hbase_mutex);
	if (!--share->use_count)
	{

		if (share->data_class != NULL)
			delete share->data_class;

		share->data_class = NULL;

		my_hash_delete(&hbase_open_tables, (uchar*) share);
		thr_lock_delete(&share->lock);
		mysql_mutex_destroy(&share->mutex);
		my_free(share);
	}
	mysql_mutex_unlock(&hbase_mutex);

	return 0;
}

static handler* hbase_create_handler(handlerton *hton,
	TABLE_SHARE *table, 
	MEM_ROOT *mem_root)
{
	return new (mem_root) ha_hbase(hton, table);
}

ha_hbase::ha_hbase(handlerton *hton, TABLE_SHARE *table_arg)
	:handler(hton, table_arg)
{
	current_position = 0;
	use_bulk_insert = false;
	//current_connection = NULL;
}


/**
@brief
If frm_error() is called then we will use this to determine
the file extensions that exist for the storage engine. This is also
used by the default rename_table and delete_table method in
handler.cc.

For engines that have two file name extentions (separate meta/index file
and data file), the order of elements is relevant. First element of engine
file name extentions array should be meta/index file extention. Second
element - data file extention. This order is assumed by
prepare_for_repair() when REPAIR TABLE ... USE_FRM is issued.

@see
rename_table method in handler.cc and
delete_table method in handler.cc
*/


static const char *ha_hbase_exts[] = {
	NullS
};

const char **ha_hbase::bas_ext() const
{
	return ha_hbase_exts;
}


/**
@brief
Used for opening tables. The name will be the name of the file.

@details
A table is opened when it needs to be opened; e.g. when a request comes in
for a SELECT on the table (tables are not open and closed for each request,
they are cached).

Called from handler.cc by handler::ha_open(). The server opens all tables by
calling ha_open() which then calls the handler specific open().

@see
handler::ha_open() in handler.cc
*/

int ha_hbase::open(const char *name, int mode, uint test_if_locked)
{
	DBUG_ENTER("ha_hbase::open");
	char name_buf[FN_REFLEN] ;

	if (!(share = get_share(name, table)))
		DBUG_RETURN(1);
	thr_lock_data_init(&share->lock, &lock, NULL);

	current_position = 0 ;

	//ref_length = sizeof(HbaseClient *)+sizeof(ScannerID* );
	ref_length = sizeof(ScannerID*);
	DBUG_PRINT("info", ("ref_length: %u", ref_length));


	DBUG_RETURN(0);
}



int ha_hbase::reset(void)
{
	//for (uint i = 0; i < results.elements; i++)
	//{
	//	MYSQL_RES *result;
	//	get_dynamic(&results, (uchar *)&result, i);
	//	mysql_free_result(result);
	//}
	//reset_dynamic(&results);

	int  ret = 0;
	if (current_position) {
		//(*current_connection)->scannerClose(current_position);
		ret = Hbase_helper::hbase->scannerClose(current_position, errmsg);
		current_position = 0;
	}
	/*
	if (current_connection != NULL) {
		share->data_class->return_connection(current_connection);
		current_connection = NULL;
	}
	*/
	return ret ;
}


/**
@brief
Closes a table. We call the free_share() function to free any resources
that we have allocated in the "shared" structure.

@details
Called from sql_base.cc, sql_select.cc, and table.cc. In sql_select.cc it is
only used to close up temporary tables or during the process where a
temporary table is converted over to being a myisam table.

For sql_base.cc look at close_data_tables().

@see
sql_base.cc, sql_select.cc and table.cc
*/

int ha_hbase::close(void)
{
	DBUG_ENTER("ha_hbase::close");

	DBUG_RETURN(free_share(share));
}


/**
@brief
write_row() inserts a row. No extra() hint is given currently if a bulk load
is happening. buf() is a byte array of data. You can use the field
information to extract the data from the native byte array type.

@details
Example of this would be:
@code
for (Field **field=table->field ; *field ; field++)
{
...
}
@endcode

See ha_tina.cc for an example of extracting all of the data as strings.
ha_berekly.cc has an example of how to store it intact by "packing" it
for ha_berkeley's own native storage type.

See the note for update_row() on auto_increments and timestamps. This
case also applies to write_row().

Called from item_sum.cc, item_sum.cc, sql_acl.cc, sql_insert.cc,
sql_insert.cc, sql_select.cc, sql_table.cc, sql_udf.cc, and sql_update.cc.

@see
item_sum.cc, item_sum.cc, sql_acl.cc, sql_insert.cc,
sql_insert.cc, sql_select.cc, sql_table.cc, sql_udf.cc and sql_update.cc
*/

void ha_hbase::start_bulk_insert(ha_rows rows)
{
	if (rows > 1) {
		use_bulk_insert = true;
		bulk_inserts.clear();
	}
}

int ha_hbase::end_bulk_insert()
{
	int ret = 0;
	if (use_bulk_insert) {
		if (!bulk_inserts.empty()) {
			ret = Hbase_helper::hbase->mutateRows(share->data_class->tableName_, bulk_inserts, errmsg);
			bulk_inserts.clear();
		}
		use_bulk_insert = false;
	}
	return ret;
}

int ha_hbase::write_row(uchar *buf)
{
	long long pos ;
	DBUG_ENTER("ha_hbase::write_row");
	ha_statistic_increment(&SSV::ha_write_count) ;
	
	int ret = 0;
	my_bitmap_map *old_map = dbug_tmp_use_all_columns(table, table->read_set);
	BatchMutation mutation;
	Hbase_helper::pack_key(table, table->s->primary_key, mutation.row);
	Hbase_helper::pack_row(table, mutation.mutations);
	mutation.__isset.mutations = true;
	mutation.__isset.row = true;
	bulk_inserts.push_back(mutation);

	Mutation m;
	m.__set_column("entry:key");
	m.__set_value(mutation.row);
	mutation.mutations.clear();
	mutation.mutations.push_back(m);

	for (uint key = table->s->primary_key + 1 ; key < table->s->keys; key++) {
		mutation.row.clear();
		Hbase_helper::pack_key(table, key, mutation.row);
		bulk_inserts.push_back(mutation);
	}

	dbug_tmp_restore_column_map(table->read_set, old_map);
	if ( !use_bulk_insert || bulk_inserts.size() >= Hbase_helper::bulk_size ) {			
		ret = Hbase_helper::hbase->mutateRows(share->data_class->tableName_, bulk_inserts, errmsg);
		bulk_inserts.clear();		
	}
	/*else {
		if ( ret = share->data_class->write_row(buf, table, errmsg) ) {
		sql_print_information("ha_hbase::write_row to %s error:%s!", share->table_name, errmsg.c_str());
		}
		}
		*/
	DBUG_RETURN(ret);
}


/**
@brief
Yes, update_row() does what you expect, it updates a row. old_data will have
the previous row record in it, while new_data will have the newest data in it.
Keep in mind that the server can do updates based on ordering if an ORDER BY
clause was used. Consecutive ordering is not guaranteed.

@details
Currently new_data will not have an updated auto_increament record, or
and updated timestamp field. You can do these for example by doing:
@code
if (table->timestamp_field_type & TIMESTAMP_AUTO_SET_ON_UPDATE)
table->timestamp_field->set_time();
if (table->next_number_field && record == table->record[0])
update_auto_increment();
@endcode

Called from sql_select.cc, sql_acl.cc, sql_update.cc, and sql_insert.cc.

@see
sql_select.cc, sql_acl.cc, sql_update.cc and sql_insert.cc
*/
int ha_hbase::update_row(const uchar *old_data, uchar *new_data)
{
	DBUG_ENTER("ha_hbase::update_row");	
	std::string errmsg;
	if (share->data_class->update_row((uchar*)old_data, new_data,
		table, errmsg) == -1) {
		sql_print_information("ha_hbase::update_row to %s error:%s!", share->table_name, errmsg.c_str());
		DBUG_RETURN(-1);
	}
	DBUG_RETURN(0);
}


/**
@brief
This will delete a row. buf will contain a copy of the row to be deleted.
The server will call this right after the current row has been called (from
either a previous rnd_nexT() or index call).

@details
If you keep a pointer to the last row or can access a primary key it will
make doing the deletion quite a bit easier. Keep in mind that the server does
not guarantee consecutive deletions. ORDER BY clauses can be used.

Called in sql_acl.cc and sql_udf.cc to manage internal table
information.  Called in sql_delete.cc, sql_insert.cc, and
sql_select.cc. In sql_select it is used for removing duplicates
while in insert it is used for REPLACE calls.

@see
sql_acl.cc, sql_udf.cc, sql_delete.cc, sql_insert.cc and sql_select.cc
*/

int ha_hbase::delete_row(const uchar *buf)
{
	DBUG_ENTER("ha_hbase::update_row");
	std::string errmsg;
	if (share->data_class->delete_row((uchar*)buf,
		table, errmsg) == -1) {
		sql_print_information("ha_hbase::delete_row to %s error:%s!", share->table_name, errmsg.c_str());
		DBUG_RETURN(-1);
	}
	DBUG_RETURN(0);
}


/**
@brief
Positions an index cursor to the index specified in the handle. Fetches the
row if available. If the key value is null, begin at the first key of the
index.
*/

int ha_hbase::index_read_map(uchar *buf, const uchar *key,
	key_part_map keypart_map __attribute__((unused)),
	enum ha_rkey_function find_flag
	__attribute__((unused)))
{
	
	DBUG_ENTER("ha_hbase::index_read_map");
	MYSQL_INDEX_READ_ROW_START(table_share->db.str, table_share->table_name.str);
	
	std::string key_str;	
	Hbase_helper::parse_key(table, active_index, key, keypart_map, key_str);

	int rc = HA_ERR_WRONG_COMMAND;

	rc = share->data_class->get_index_pos(key_str, find_flag, current_position, errmsg);

	if ( rc != 0 )
		DBUG_RETURN(rc);

	
	rc = share->data_class->read_row(buf, table, current_position, active_index == table->s->primary_key, errmsg);
	//rc = Hbase_helper::read_row(table, buf, current_position, errmsg);

	//if (rc == Hbase_helper::EMPTY_RESULT)
	//	DBUG_RETURN(HA_ERR_KEY_NOT_FOUND);

	MYSQL_INDEX_READ_ROW_DONE(rc);
	DBUG_RETURN(rc);
}

/**
@brief
优化基于主键的查询，不需要走范围查询了。
*/
int ha_hbase::index_read_idx_map(uchar * buf, uint index, const uchar * key, key_part_map keypart_map, enum ha_rkey_function find_flag) {
	DBUG_ENTER("ha_hbase::index_read_idx_map");
	MYSQL_INDEX_READ_ROW_START(table_share->db.str, table_share->table_name.str);

	int rc = HA_ERR_WRONG_COMMAND;
	std::string key_str;
	Hbase_helper::parse_key(table, index, key, keypart_map, key_str);

	if (index == table->s->primary_key)
		rc = Hbase_helper::read_row(table, buf, share->data_class->tableName_, key_str, errmsg);
	else {
		std::vector<TRowResult> result;
		rc = Hbase_helper::hbase->scannerGet(result, current_position, errmsg);
		if (!rc) {
			if (result.empty()) rc = Hbase_helper::EMPTY_RESULT;
			else {
				const std::string& key_str = result[0].columns.begin()->second.value;
				rc = Hbase_helper::read_row(table, buf, share->data_class->tableName_, key_str, errmsg);
			}
		}
	}

	if (rc == Hbase_helper::EMPTY_RESULT)
		DBUG_RETURN(HA_ERR_KEY_NOT_FOUND);

	MYSQL_INDEX_READ_ROW_DONE(rc);
	DBUG_RETURN(rc);

}

/**
@brief
Used to read forward through the index.


Notice that I had to move the index pointers around a bit to get the code for the next and
previous to work. Range queries generate two calls to the index class the first time it is used: the
first one gets the first key (index_read), and then the second calls the next key (index_next).
Subsequent index calls are made to index_next(). Therefore, I must call the hbase_index
class method get_prev_key() to reset the keys correctly. This would be another great opportunity
to rework the index class to work better with range queries in MySQL.
*/

int ha_hbase::index_next(uchar *buf)
{
	int rc;
	uchar* key = 0 ;
	long long pos ;
		
	DBUG_ENTER("ha_hbase::index_next");	
	MYSQL_INDEX_READ_ROW_START(table_share->db.str, table_share->table_name.str);

	rc = share->data_class->read_row(buf, table, current_position, active_index == table->s->primary_key, errmsg);

	/*
	if ( active_index == table->s->primary_key )
		rc = Hbase_helper::read_row(table, buf, current_position, errmsg);
	else {
		std::vector<TRowResult> result;
		rc = Hbase_helper::hbase->scannerGet(result, current_position, errmsg);
		if ( !rc) {
			if (result.empty()) rc = Hbase_helper::EMPTY_RESULT;
			else {
				const std::string& key_str = result[0].columns.begin()->second.value;
				rc = Hbase_helper::read_row(table, buf, share->data_class->tableName_, key_str, errmsg);
			}
		}
	}
	*/
	if (rc == Hbase_helper::EMPTY_RESULT)
		DBUG_RETURN(HA_ERR_END_OF_FILE);

	MYSQL_INDEX_READ_ROW_DONE(rc);
	DBUG_RETURN(rc);
}


/**
@brief
Used to read backwards through the index.
*/

int ha_hbase::index_prev(uchar *buf)
{
	int rc;
	uchar* key = 0 ;
	long long pos ;
	DBUG_ENTER("ha_hbase::index_prev");
	MYSQL_INDEX_READ_ROW_START(table_share->db.str, table_share->table_name.str);

	rc = share->data_class->read_row(buf, table, current_position, active_index == table->s->primary_key, errmsg);

	/*if (active_index == table->s->primary_key)
		rc = Hbase_helper::read_row(table, buf, current_position, errmsg);
	else {
		std::vector<TRowResult> result;
		rc = Hbase_helper::hbase->scannerGet(result, current_position, errmsg);
		if (!rc) {
			if (result.empty()) rc = Hbase_helper::EMPTY_RESULT;
			else {
				const std::string& key_str = result[0].columns.begin()->second.value;
				rc = Hbase_helper::read_row(table, buf, share->data_class->tableName_, key_str, errmsg);
			}
		}
	}*/
	if (rc == Hbase_helper::EMPTY_RESULT)
		DBUG_RETURN(HA_ERR_END_OF_FILE);

	MYSQL_INDEX_READ_ROW_DONE(rc);
	DBUG_RETURN(rc);
}


/**
@brief
index_first() asks for the first key in the index.

@details
Called from opt_range.cc, opt_sum.cc, sql_handler.cc, and sql_select.cc.

@see
opt_range.cc, opt_sum.cc, sql_handler.cc and sql_select.cc
*/
int ha_hbase::index_first(uchar *buf)
{
	int rc;
	uchar *key = 0 ;
	DBUG_ENTER("ha_hbase::index_first");
	MYSQL_INDEX_READ_ROW_START(table_share->db.str, table_share->table_name.str);

	std::string start_key(1, '0' + active_index);
	std::string end_key(1, '1' + active_index);
	rc = share->data_class->get_index_pos(start_key, end_key, false, current_position, errmsg);
	if ( rc != 0 ) 
		DBUG_RETURN(rc);

	
	if ((rc = share->data_class->read_row(buf, table, current_position, active_index == table->s->primary_key, errmsg)) == Hbase_helper::EMPTY_RESULT) {
			DBUG_RETURN(HA_ERR_END_OF_FILE);
	}
	
	MYSQL_INDEX_READ_ROW_DONE(rc);
	DBUG_RETURN(rc);
}


/**
@brief
index_last() asks for the last key in the index.

@details
Called from opt_range.cc, opt_sum.cc, sql_handler.cc, and sql_select.cc.

@see
opt_range.cc, opt_sum.cc, sql_handler.cc and sql_select.cc
*/
int ha_hbase::index_last(uchar *buf)
{
	int rc;
	uchar *key = 0;
	DBUG_ENTER("ha_hbase::index_first");
	MYSQL_INDEX_READ_ROW_START(table_share->db.str, table_share->table_name.str);

	std::string start_key(1, '1' + active_index);
	std::string end_key(1, '0' + active_index);
	rc = share->data_class->get_index_pos(start_key, end_key, true, current_position, errmsg);

	//rc = share->data_class->get_index_pos("", HA_READ_BEFORE_KEY, current_position, errmsg);
	if (rc != 0)
		DBUG_RETURN(rc);

	if ((rc = share->data_class->read_row(buf, table, current_position, active_index == table->s->primary_key, errmsg)) == Hbase_helper::EMPTY_RESULT) {
		DBUG_RETURN(HA_ERR_END_OF_FILE);
	}

	MYSQL_INDEX_READ_ROW_DONE(rc);
	DBUG_RETURN(rc);
}

int ha_hbase::index_end()
{
	DBUG_ENTER("ha_hbase::index_end");
	int ret = 0;
	if (current_position) {
		ret = Hbase_helper::hbase->scannerClose(current_position, errmsg);
		current_position = 0;
	}
	active_index = MAX_KEY;
	DBUG_RETURN(ret);
}

/**
@brief
rnd_init() is called when the system wants the storage engine to do a table
scan. See the example in the introduction at the top of this file to see when
rnd_init() is called.

@details
Called from filesort.cc, records.cc, sql_handler.cc, sql_select.cc, sql_table.cc,
and sql_update.cc.

@see
filesort.cc, records.cc, sql_handler.cc, sql_select.cc, sql_table.cc and sql_update.cc
*/
int ha_hbase::rnd_init(bool scan)
{
	DBUG_ENTER("ha_hbase::rnd_init");
	
	
	records = 0;
	int rc = share->data_class->get_index_pos("0", "1", false, current_position, errmsg);
	if ( rc != 0 ) {
		sql_print_error("ha_hbase::rnd_init get index pos error!");
		DBUG_RETURN(-1);
	}	
	DBUG_RETURN(0);
}

int ha_hbase::rnd_end()
{	
	DBUG_ENTER("ha_hbase::rnd_end");
	int ret = 0;
	if (current_position) {
		ret = Hbase_helper::hbase->scannerClose(current_position, errmsg);
		current_position = 0;
	}
	DBUG_RETURN(ret);
}

bool ha_hbase::get_error_message(int error, String* buf)
{
	buf->set(errmsg.c_str(), errmsg.length(), &my_charset_bin);
	return TRUE;
}

/**
@brief
This is called for each row of the table scan. When you run out of records
you should return HA_ERR_END_OF_FILE. Fill buff up with the row information.
The Field structure for the table is the key to getting data into buf
in a manner that will allow the server to understand it.

@details
Called from filesort.cc, records.cc, sql_handler.cc, sql_select.cc, sql_table.cc,
and sql_update.cc.

@see
filesort.cc, records.cc, sql_handler.cc, sql_select.cc, sql_table.cc and sql_update.cc
*/
int ha_hbase::rnd_next(uchar *buf)
{
	int rc;
	DBUG_ENTER("ha_hbase::rnd_next");
	MYSQL_READ_ROW_START(table_share->db.str, table_share->table_name.str,
		TRUE);
	rc= HA_ERR_END_OF_FILE;
	ha_statistic_increment(&SSV::ha_read_rnd_next_count);
	/*
	 Read the row from the data file.
	 */

	rc = Hbase_helper::read_row(table, buf, current_position, errmsg);
	
	if ( rc == Hbase_helper::EMPTY_RESULT )
		DBUG_RETURN(HA_ERR_END_OF_FILE) ;
	
	records ++ ;
	MYSQL_READ_ROW_DONE(rc);
	DBUG_RETURN(rc);
}


/**
@brief
position() is called after each call to rnd_next() if the data needs
to be ordered. You can do something like the following to store
the position:
@code
my_store_ptr(ref, ref_length, current_position);
@endcode

@details
The server uses ref to store data. ref_length in the above case is
the size needed to store current_position. ref is just a byte array
that the server will maintain. If you are using offsets to mark rows, then
current_position should be the offset. If it is a primary key like in
BDB, then it needs to be a primary key.

Called from filesort.cc, sql_select.cc, sql_delete.cc, and sql_update.cc.

@see
filesort.cc, sql_select.cc, sql_delete.cc and sql_update.cc
*/
void ha_hbase::position(const uchar *record)
{
	DBUG_ENTER("ha_hbase::position");

	//memcpy(ref, current_connection, sizeof(HbaseClient*));
	memcpy(ref, &current_position, sizeof(ScannerID*));
	//my_store_ptr(ref,ref_length,current_position) ;
	DBUG_VOID_RETURN;
}


/**
@brief
This is like rnd_next, but you are given a position to use
to determine the row. The position will be of the type that you stored in
ref. You can use ha_get_ptr(pos,ref_length) to retrieve whatever key
or position you saved when position() was called.

@details
Called from filesort.cc, records.cc, sql_insert.cc, sql_select.cc, and sql_update.cc.

@see
filesort.cc, records.cc, sql_insert.cc, sql_select.cc and sql_update.cc
*/
int ha_hbase::rnd_pos(uchar *buf, uchar *pos)
{	
	int rc ;
	DBUG_ENTER("ha_hbase::rnd_pos");
	MYSQL_READ_ROW_START(table_share->db.str, table_share->table_name.str,
		TRUE);
	rc= HA_ERR_WRONG_COMMAND;
	ha_statistic_increment(&SSV::ha_read_next_count) ;
	//current_position = (off_t)my_get_ptr(pos,ref_length) ;

	//memcpy(current_connection, ref, sizeof(HbaseClient*));
	memcpy(&current_position, ref, sizeof(ScannerID*));

	rc = Hbase_helper::read_row(table, buf, current_position, errmsg) ;
	if (rc == Hbase_helper::EMPTY_RESULT)
		rc = HA_ERR_END_OF_FILE; // TODO: 不知道找不到数据应该返回什么，就先返回END_OF_FILE吧，也许应该返回 NOT_FOUND
	MYSQL_READ_ROW_DONE(0);
	DBUG_RETURN(rc);
}


/**
@brief
::info() is used to return information to the optimizer. See my_base.h for
the complete description.

@details
Currently this table handler doesn't implement most of the fields really needed.
SHOW also makes use of this data.

You will probably want to have the following in your code:
@code
if (records < 2)
records = 2;
@endcode
The reason is that the server will optimize for cases of only a single
record. If, in a table scan, you don't know the number of records, it
will probably be better to set records to two so you can return as many
records as you need. Along with records, a few more variables you may wish
to set are:
records
deleted
data_file_length
index_file_length
delete_length
check_time
Take a look at the public variables in handler.h for more information.

Called in filesort.cc, ha_heap.cc, item_sum.cc, opt_sum.cc, sql_delete.cc,
sql_delete.cc, sql_derived.cc, sql_select.cc, sql_select.cc, sql_select.cc,
sql_select.cc, sql_select.cc, sql_show.cc, sql_show.cc, sql_show.cc, sql_show.cc,
sql_table.cc, sql_union.cc, and sql_update.cc.

@see
filesort.cc, ha_heap.cc, item_sum.cc, opt_sum.cc, sql_delete.cc, sql_delete.cc,
sql_derived.cc, sql_select.cc, sql_select.cc, sql_select.cc, sql_select.cc,
sql_select.cc, sql_show.cc, sql_show.cc, sql_show.cc, sql_show.cc, sql_table.cc,
sql_union.cc and sql_update.cc
*/
int ha_hbase::info(uint flag)
{
	DBUG_ENTER("ha_hbase::info");
	/* This is a lie,but you don't want the optimizer to see zero of 1 */
	records = 100000;
	//if ( records < 2 )
	//	records = 2 ;
	DBUG_RETURN(0);
}


/**
@brief
extra() is called whenever the server wishes to send a hint to
the storage engine. The myisam engine implements the most hints.
ha_innodb.cc has the most exhaustive list of these hints.

@see
ha_innodb.cc
*/
int ha_hbase::extra(enum ha_extra_function operation)
{
	DBUG_ENTER("ha_hbase::extra");
	DBUG_RETURN(0);
}


/**
@brief
Used to delete all rows in a table, including cases of truncate and cases where
the optimizer realizes that all rows will be removed as a result of an SQL statement.

@details
Called from item_sum.cc by Item_func_group_concat::clear(),
Item_sum_count_distinct::clear(), and Item_func_group_concat::clear().
Called from sql_delete.cc by mysql_delete().
Called from sql_select.cc by JOIN::reinit().
Called from sql_union.cc by st_select_lex_unit::exec().

@see
Item_func_group_concat::clear(), Item_sum_count_distinct::clear() and
Item_func_group_concat::clear() in item_sum.cc;
mysql_delete() in sql_delete.cc;
JOIN::reinit() in sql_select.cc and
st_select_lex_unit::exec() in sql_union.cc.
*/
int ha_hbase::delete_all_rows()
{
	DBUG_ENTER("ha_hbase::delete_all_rows");
	//mysql_mutex_lock(&share->mutex) ;  没有必要加表锁

	int ret = share->data_class->trunc_table(errmsg);	

	//mysql_mutex_unlock(&share->mutex) ; 没有必要加表锁
	DBUG_RETURN(ret);
}


/**
@brief
Used for handler specific truncate table.  The table is locked in
exclusive mode and handler is responsible for reseting the auto-
increment counter.

@details
Called from Truncate_statement::handler_truncate.
Not used if the handlerton supports HTON_CAN_RECREATE, unless this
engine can be used as a partition. In this case, it is invoked when
a particular partition is to be truncated.

@see
Truncate_statement in sql_truncate.cc
Remarks in handler::truncate.
*/
int ha_hbase::truncate()
{
	DBUG_ENTER("ha_hbase::truncate");

	DBUG_RETURN(delete_all_rows());	
}


/**
@brief
This create a lock on the table. If you are implementing a storage engine
that can handle transacations look at ha_berkely.cc to see how you will
want to go about doing this. Otherwise you should consider calling flock()
here. Hint: Read the section "locking functions for mysql" in lock.cc to understand
this.

@details
Called from lock.cc by lock_external() and unlock_external(). Also called
from sql_table.cc by copy_data_between_tables().

@see
lock.cc by lock_external() and unlock_external() in lock.cc;
the section "locking functions for mysql" in lock.cc;
copy_data_between_tables() in sql_table.cc.
*/
int ha_hbase::external_lock(THD *thd, int lock_type)
{
	DBUG_ENTER("ha_hbase::external_lock");
	DBUG_RETURN(0);
}


/**
@brief
The idea with handler::store_lock() is: The statement decides which locks
should be needed for the table. For updates/deletes/inserts we get WRITE
locks, for SELECT... we get read locks.

@details
Before adding the lock into the table lock handler (see thr_lock.c),
mysqld calls store lock with the requested locks. Store lock can now
modify a write lock to a read lock (or some other lock), ignore the
lock (if we don't want to use MySQL table locks at all), or add locks
for many tables (like we do when we are using a MERGE handler).

Berkeley DB, for example, changes all WRITE locks to TL_WRITE_ALLOW_WRITE
(which signals that we are doing WRITES, but are still allowing other
readers and writers).

When releasing locks, store_lock() is also called. In this case one
usually doesn't have to do anything.

In some exceptional cases MySQL may send a request for a TL_IGNORE;
This means that we are requesting the same lock as last time and this
should also be ignored. (This may happen when someone does a flush
table when we have opened a part of the tables, in which case mysqld
closes and reopens the tables and tries to get the same locks at last
time). In the future we will probably try to remove this.

Called from lock.cc by get_lock_data().

@note
In this method one should NEVER rely on table->in_use, it may, in fact,
refer to a different thread! (this happens if get_lock_data() is called
from mysql_lock_abort_for_thread() function)

@see
get_lock_data() in lock.cc
*/
THR_LOCK_DATA **ha_hbase::store_lock(THD *thd,
	THR_LOCK_DATA **to,
	enum thr_lock_type lock_type)
{
	if (lock_type != TL_IGNORE && lock.type == TL_UNLOCK)
	lock.type=lock_type;
	*to++= &lock;
	return to;
}


/**
@brief
Used to delete a table. By the time delete_table() has been called all
opened references to this table will have been closed (and your globally
shared references released). The variable name will just be the name of
the table. You will need to remove any files you have created at this point.

@details
If you do not implement this, the default delete_table() is called from
handler.cc and it will delete all files with the file extensions returned
by bas_ext().

Called from handler.cc by delete_table and ha_create_table(). Only used
during create if the table_flag HA_DROP_BEFORE_CREATE was specified for
the storage engine.

@see
delete_table and ha_create_table() in handler.cc
*/
int ha_hbase::delete_table(const char *name)
{
	DBUG_ENTER("ha_hbase::delete_table");
	/* This is not implemented but we want someone to be able that it works. */
	char name_buff[FN_REFLEN] ;


	/*
	 Begin critical section by locking the hbase mutex variable.
	 */
	mysql_mutex_lock(&hbase_mutex) ;
	if (!(share = get_share(name, table))) {
		mysql_mutex_unlock(&hbase_mutex);
		DBUG_RETURN(1);
	}
	
	if (share->data_class->delete_table(name, errmsg)) {
		sql_print_error("delete from [%s] got a error:%s", name, errmsg.c_str());
		mysql_mutex_unlock(&hbase_mutex);
		DBUG_RETURN(-1);
	}
	
	mysql_mutex_unlock(&hbase_mutex);
	DBUG_RETURN(0);
}


/**
@brief
Renames a table from one name to another via an alter table call.

@details
If you do not implement this, the default rename_table() is called from
handler.cc and it will delete all files with the file extensions returned
by bas_ext().

Called from sql_table.cc by mysql_rename_table().

@see
mysql_rename_table() in sql_table.cc
*/
int ha_hbase::rename_table(const char * from, const char * to)
{
	DBUG_ENTER("ha_hbase::rename_table ");
	char data_from[FN_REFLEN] ;
	char data_to[FN_REFLEN];
	char index_from[FN_REFLEN] ;
	char index_to[FN_REFLEN] ;

	if ( !(share = get_share(from,table))) 
		DBUG_RETURN(1) ;
	/*
	 Begin critical section by locking the hbase mutex variable.
	 */
	//mysql_mutex_lock(&hbase_mutex) ;
	mysql_mutex_lock(&share->mutex) ;

	std::string newTableName;
	Hbase_helper::pathToTableName(to, newTableName);
	int ret = Hbase_helper::hbase->renameTable(share->data_class->tableName_, newTableName, errmsg);
	mysql_mutex_unlock(&share->mutex) ;

	DBUG_RETURN(ret);
}


/**
@brief
Given a starting key and an ending key, estimate the number of rows that
will exist between the two keys.

@details
end_key may be empty, in which case determine if start_key matches any rows.

Called from opt_range.cc by check_quick_keys().

@see
check_quick_keys() in opt_range.cc
*/
ha_rows ha_hbase::records_in_range(uint inx, key_range *min_key,
	key_range *max_key)
{
	DBUG_ENTER("ha_hbase::records_in_range");
	DBUG_RETURN(10);                         // low number to force index usage
}


/**
@brief
create() is called to create a database. The variable name will have the name
of the table.

@details
When create() is called you do not need to worry about
opening the table. Also, the .frm file will have already been
created so adjusting create_info is not necessary. You can overwrite
the .frm file at this point if you wish to change the table
definition, but there are no methods currently provided for doing
so.

Called from handle.cc by ha_create_table().

@see
ha_create_table() in handle.cc
*/

int ha_hbase::create(const char *name, TABLE *table_arg,
	HA_CREATE_INFO *create_info)
{
	DBUG_ENTER("ha_hbase::create");
	/*
	This is not implemented but we want someone to be able to see that it
	works.
	*/
	char name_buff[FN_REFLEN] ;
	
	if ( !(share = get_share(name,table)))
		DBUG_RETURN(1) ;

	
	if (share->data_class->create_table(name, table, errmsg)) {
		sql_print_error("create table [%s] error:%s", name, errmsg.c_str());
		free_share(share);
		DBUG_RETURN(-1);
	}

	free_share(share) ;
	DBUG_RETURN(0);
}

struct st_mysql_storage_engine hbase_storage_engine=
{ MYSQL_HANDLERTON_INTERFACE_VERSION };

static ulong srv_enum_var= 0;
static ulong srv_ulong_var= 0;

const char *enum_var_names[]=
{
	"e1", "e2", NullS
};

TYPELIB enum_var_typelib=
{
	array_elements(enum_var_names) - 1, "enum_var_typelib",
	enum_var_names, NULL
};

static void ip_update(
	THD* thd,
	struct st_mysql_sys_var* var, 
	void* var_ptr, /*!< out: where the formal string goes */
	const void* save) /*!< in: immediate result from check function */
{
	assert(var_ptr != NULL); assert(save != NULL);

	*static_cast<const char**>(var_ptr) =
		*static_cast<const char* const *>(save);
	//strcpy(Hbase_helper::ip, *static_cast<const char* const *>(save));
	Hbase_helper::reset_ip();
}

static void port_update(THD* thd, //in: thread handle
	struct st_mysql_sys_var* var, // in: pointer to system variable
	void* var_ptr, // out: where the formal string goes
	const void* save) // in: immediate result from check function
{
	assert(var_ptr != NULL); assert(save != NULL);
	Hbase_helper::port = *static_cast<const ulong*>(save);
}

static void pool_size_update(THD* thd, //in: thread handle
struct st_mysql_sys_var* var, // in: pointer to system variable
	void* var_ptr, // out: where the formal string goes
	const void* save) // in: immediate result from check function
{
	assert(var_ptr != NULL); assert(save != NULL);
	Hbase_helper::pool_size = *static_cast<const ulong*>(save);
}


static void idle_size_update(THD* thd, //in: thread handle
struct st_mysql_sys_var* var, // in: pointer to system variable
	void* var_ptr, // out: where the formal string goes
	const void* save) // in: immediate result from check function
{
	assert(var_ptr != NULL); assert(save != NULL);
	Hbase_helper::idle_size = *static_cast<const ulong*>(save);
}

static void default_long_value_update(THD* thd,
struct st_mysql_sys_var* var, // in: pointer to system variable
	void* var_ptr, // out: where the formal string goes
	const void* save) // in: immediate result from check function
{
	assert(var_ptr != NULL); assert(save != NULL);
	*static_cast<ulong*>(var_ptr) =
		*static_cast<const ulong* >(save);
}


static MYSQL_SYSVAR_ENUM(
	enum_var,                       // name
	srv_enum_var,                   // varname
	PLUGIN_VAR_RQCMDARG,            // opt
	"Sample ENUM system variable.", // comment
	NULL,                           // check
	NULL,                           // update
	0,                              // def
	&enum_var_typelib);             // typelib

static MYSQL_SYSVAR_ULONG(
	ulong_var,
	srv_ulong_var,
	PLUGIN_VAR_RQCMDARG,
	"0..1000",
	NULL,
	NULL,
	8,
	0,
	1000,
	0);

static MYSQL_SYSVAR_STR(
	ip,
	Hbase_helper::ip,
	PLUGIN_VAR_RQCMDARG | PLUGIN_VAR_MEMALLOC,
	"The write code.",
	NULL,
	ip_update, 
	"herrypc");

static MYSQL_SYSVAR_ULONG(
	port,
	Hbase_helper::port,
	PLUGIN_VAR_RQCMDARG,
	"1024..65535",
	NULL,
	port_update,
	9090,
	1024,
	65535,
	0);

static MYSQL_SYSVAR_ULONG(
	pool_size,
	Hbase_helper::pool_size,
	PLUGIN_VAR_RQCMDARG,
	"NO limit",
	NULL,
	pool_size_update,
	(ulong)-1,
	20,
	(ulong)-1,
	0);

static MYSQL_SYSVAR_ULONG(
	idle_size,
	Hbase_helper::idle_size,
	PLUGIN_VAR_RQCMDARG,
	"0..pool size",
	NULL,
	idle_size_update,
	10,
	0,
	(ulong)-1,
	0);

static MYSQL_SYSVAR_ULONG(
	bulk_size,
	Hbase_helper::bulk_size,
	PLUGIN_VAR_RQCMDARG,
	"0..pool size",
	NULL,
	default_long_value_update,
	20,
	0,
	(ulong)-1,
	0);

static struct st_mysql_sys_var* hbase_system_variables[]= {
	MYSQL_SYSVAR(enum_var),
	MYSQL_SYSVAR(ulong_var),
	MYSQL_SYSVAR(ip),
	MYSQL_SYSVAR(port),
	MYSQL_SYSVAR(pool_size),
	MYSQL_SYSVAR(idle_size),	
	MYSQL_SYSVAR(bulk_size),
	NULL
};

// this is an example of SHOW_FUNC and of my_snprintf() service
static int show_func_hbase(MYSQL_THD thd, struct st_mysql_show_var *var,
	char *buf)
{
	var->type= SHOW_CHAR;
	var->value= buf; // it's of SHOW_VAR_FUNC_BUFF_SIZE bytes
	my_snprintf(buf, SHOW_VAR_FUNC_BUFF_SIZE,
		"enum_var is %u, ulong_var is %lu, %.6b", // %b is MySQL extension
		srv_enum_var, srv_ulong_var, "really");
	return 0;
}

static struct st_mysql_show_var func_status[]=
{
	{"hbase_func_hbase",  (char *)show_func_hbase, SHOW_FUNC},
	{0,0,SHOW_UNDEF}
};


mysql_declare_plugin(hbase)
{
	MYSQL_STORAGE_ENGINE_PLUGIN,
		&hbase_storage_engine,
		"hbase",
		"Herry Yu",
		"hbase storage engine",
		PLUGIN_LICENSE_GPL,
		hbase_init_func,                            /* Plugin Init */
		hbase_done_func,                            /* Plugin Deinit */
		0x0100 /* 1.0 */,
		func_status,                                  /* status variables */
		hbase_system_variables,                     /* system variables */
		NULL                                          /* config options */
}
mysql_declare_plugin_end;

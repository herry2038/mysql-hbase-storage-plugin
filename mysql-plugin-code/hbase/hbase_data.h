
#pragma once
#pragma unmanaged
#include "my_global.h"
#include "sql_class.h"
#include "my_sys.h"
#include <thrift/transport/TSocket.h>
#include <thrift/transport/TBufferTransports.h>
#include <thrift/protocol/TBinaryProtocol.h>
#include "gen-cpp/Hbase.h"
#include "HbaseConnection.h"



class Hbase_data {	
public:
	std::string tableName_;
public:
	Hbase_data(const char* tableName);
	~Hbase_data()
	{
	}

	int create_table(const char* path, TABLE* table, std::string& errmsg);
	int delete_table(const char* path, std::string& errmsg);

	int write_row(uchar* data, TABLE* table, std::string& errmsg);
	int update_row(uchar* old_data, uchar* new_data, TABLE* table, std::string& errmsg);
	int delete_row(uchar* data, TABLE* table, std::string& errmsg);
	
	int read_row(uchar* data, TABLE* table, ScannerID scannId, bool isPk, std::string& errmsg);
	int get_row(const std::string& key, std::vector<TRowResult>& result, std::string& errmsg);

	int get_index_pos(const std::string& key, ha_rkey_function find_flag, ScannerID& current_position, std::string& errmsg);
	int get_index_pos(const std::string& key_start, const std::string& key_end, bool reversed, ScannerID& current_position, std::string& errmsg);
	
	int trunc_table(std::string& errmsg);

	void return_connection(CHbaseConnection* c);
};


#pragma once
#pragma unmanaged
#include "my_global.h"
#include "sql_class.h"
#include "my_sys.h"
#include <thrift/transport/TSocket.h>
#include <thrift/transport/TBufferTransports.h>
#include <thrift/protocol/TBinaryProtocol.h>
#include "gen-cpp/Hbase.h"
#include "HbaseWrapper.h"

using namespace apache::thrift;
using namespace apache::thrift::protocol;
using namespace apache::thrift::transport;
using namespace apache::hadoop::hbase::thrift;

#define herry_int4store(T,A,FLAG)   { uint32 def_temp= ((uint32) (A)) ;\
	((uchar*)(T))[3] = (uchar)(def_temp); \
	((uchar*)(T))[2] = (uchar)(def_temp >> 8); \
	((uchar*)(T))[1] = (uchar)(def_temp >> 16); \
	((uchar*)(T))[0] = (uchar)(FLAG << 7 | (def_temp >> 24) & 0x7F); }

#define herry_flag(A) (((uchar*)(A))[0] >> 7 & 0x1 )


#define herry_int8store(T,A, FLAG)   { uint32 def_temp3= (uint32) (A),\
	def_temp4 = (uint32)((A) >> 32); \
	herry_int4store((uchar*)(T)+0, def_temp4, FLAG); \
	mi_int4store((uchar*)(T)+4, def_temp3); }


#define herry_uint8korr(A) ((ulonglong)(((uint32) (((uchar*) (A))[7])) +\
	(((uint32)(((uchar*)(A))[6])) << 8) + \
	(((uint32)(((uchar*)(A))[5])) << 16) + \
	(((uint32)(((uchar*)(A))[4])) << 24)) + \
	(((ulonglong)herry_uint4korr(A)) << 32))


class Hbase_helper {
public:
	typedef std::vector<std::string> StrVec;
	typedef std::map<std::string, std::string> StrMap;
	typedef std::vector<ColumnDescriptor> ColVec;
	typedef std::map<std::string, ColumnDescriptor> ColMap;
	typedef std::vector<TCell> CellVec;
	typedef std::map<std::string, TCell> CellMap;
	
	//int get_index_pos(const std::string& key, ha_rkey_function find_flag, CHbaseConnection*& current_connection, ScannerID& current_position);
	
	static int read_row(TABLE* table, uchar* buf, ScannerID scanid, std::string& errmsg);

	static int read_row(TABLE* table, uchar* buf, const std::string& tableName, const std::string& key, std::string& errmsg);

	static int pack_key(TABLE* table, uint key, const uchar* record, key_part_map keypart_map, std::string& buf);	

	static int parse_key(TABLE* table, uint key, const uchar* key_buf, key_part_map keypart_map, std::string& buf);
	
	static void pathToTableName(const char* path, std::string& table);

	static int pack_key(TABLE* table, uint key_id, std::string& key);

	static bool is_primary(TABLE* table, uint i);

	static int pack_row(TABLE* table, std::vector<Mutation>& mutations);

	static int unpack_key(TABLE* table, uint key_id, const std::string& key, uchar* buf);

	static int unpack_row(TABLE* table, std::map<Text, TCell>& columns, uchar* buf);

	static void split(char *str, const char *delim, std::vector<char*> &list)
	{
		if (str == NULL)
		{
			return;
		}
		if (delim == NULL)
		{
			list.push_back(str);
			return;
		}

		static char* temp_str = "";
		char *s;
		const char *spanp;

		s = str;
		while (*s)
		{
			spanp = delim;
			while (*spanp)
			{
				if (*s == *spanp)
				{
					list.push_back(str);
					*s = '\0';
					str = s + 1;
					break;
				}
				spanp++;
			}
			s++;
		}
		if (*str)
		{
			list.push_back(str);
		}
		else
			list.push_back(temp_str);
	}


	static uint32 herry_uint4korr(uchar* buf)
	{
		uint32* a = (uint32*)buf;
		uint32 flag = (*a) & ((uint32)0x80);

		/*
		uchar t = ~(buf[0] & 0x7F);
		t = flag ? t & 7f : t | 0x80;
		t = buf[0] ^ t;
		//buf[0] = buf[0] ^ (0x80 | !(buf[0] & 0x7F));
		buf[0] = t;
		*/

		buf[0] = flag ? buf[0] & 0x7f : buf[0] | 0x80;

		uint32 v = mi_uint4korr(buf);

		buf[0] = flag ? buf[0] | 0x80 : buf[0] & 0x7f;
		return v;
		//uint32 A = mi_uint4korr(buf);

		//return (A & 0x7FFFFFFF) | (flag ^ 0x80000000);
		//return mi_uint4korr(&A);
	}

	static boost::shared_ptr<CHbaseWrapper> hbase;
	static char*							ip;
	static ulong							port;
	static ulong							pool_size;
	static ulong							idle_size;

	static ulong							bulk_size; // 用在bulk insert和bulk delete等中


	static void init_pool();
	static void reset_ip() { } 
	static void reset_port() {} 

	static StrMap attributes;
	static int EMPTY_RESULT;
};
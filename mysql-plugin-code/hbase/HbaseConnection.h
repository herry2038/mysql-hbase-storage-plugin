/*********************************************************************
*
* Copyright (C), 2011-2020, Herry2038.
* All rights reserved
*
* Program:      
* File name:     HbaseConnection.h
* Description:  
*
* Author:        Herry Yu 	Email:	yxbherry@126.com
*                  
*  Version   Date         Source File       Description
* ---------------------------------------------------------------------
* 1.01.001 | 2016-6-22 | HbaseConnection.h    | 程序新建立                                  
*
**********************************************************************/


#ifndef HbaseConnection_H_
#define HbaseConnection_H_

//////////////////////////////////////////////////////////////////////
// HbaseConnection.h: 
//////////////////////////////////////////////////////////////////////

#include "ConnectionPool.h"
#include <thrift/transport/TSocket.h>
#include <thrift/transport/TBufferTransports.h>
#include <thrift/protocol/TBinaryProtocol.h>

#include "gen-cpp/Hbase.h"


using namespace apache::thrift;
using namespace apache::thrift::protocol;
using namespace apache::thrift::transport;
using namespace apache::hadoop::hbase::thrift;



class CHbaseConnection : public herry::Connection
{
public:
    //////////////////////////////////////////////////////////////////////
    // Construction/Destruction
    //////////////////////////////////////////////////////////////////////
    CHbaseConnection(std::string& ip, int port) ;
    virtual ~CHbaseConnection() ;

	void open();
	bool reopen();
	void close();
	virtual bool opened() { return opened_;  }
	HbaseClient* operator->() { return client.get(); }
public:
	int createTable(const Text& tableName, std::string& errmsg);
	int deleteTable(const Text& tableName, std::string& errmsg);
	int truncateTable(const Text& tableName, std::string& errmsg);
	int mutateRow(const Text& tableName, const Text& row, const std::vector<Mutation> & mutations, std::string& errmsg);
	int mutateRows(const Text& tableName, const std::vector<BatchMutation> & mutations, std::string& errmsg);

	int scannerOpenWithScan(const Text& tableName, const TScan& scan, ScannerID& scannId, std::string& errmsg);
	int scannerGet(std::vector<TRowResult> & _return, const ScannerID id, std::string& errmg);
	int deleteAllRow(const Text& tableName, const Text& row, std::string& errmsg);
	int deleteRows(const Text& tableName, const std::vector<Text>& rows, std::string& errmsg);
	int scannerClose(const ScannerID id, std::string& errmg);
	int getRow(const Text& tableName, const Text& row, std::vector<TRowResult> & _return, std::string& errmsg);
	int renameTable(const Text& tableName, const Text& newTableName, std::string& errmsg);
private:
	boost::shared_ptr<TSocket> socket;
	boost::shared_ptr<TTransport> transport;
	boost::shared_ptr<TProtocol> protocol;
	boost::shared_ptr<HbaseClient> client;

	std::string ip_;
	int port_;
	bool opened_;

	static std::map<Text, Text> global_attributes;
};


class CHbaseConnectionFactory : public herry::ConnectionFactory {
public:
	CHbaseConnectionFactory(const std::string& ip, int port);
public:
	virtual herry::Connection* create();

private:
	std::string ip_;
	int port_;
};

#endif


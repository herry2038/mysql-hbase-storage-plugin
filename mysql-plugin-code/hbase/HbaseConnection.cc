/*********************************************************************
*
* Copyright (C), 2011-2020, Herry2038.
* All rights reserved
*
* Program:      
* File name:     HbaseConnection.cpp
* Description:  
*
* Author:        Herry Yu     Email:     yxbherry@126.com
*                  
*  Version   Date         Source File       Description
* ---------------------------------------------------------------------
* 1.01.001 | 2016-6-22 | HbaseConnection.cpp | 程序新建立                                  
*
**********************************************************************/


//////////////////////////////////////////////////////////////////////
// HbaseConnection.cpp: 
//////////////////////////////////////////////////////////////////////

#include "HbaseConnection.h"
//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////
std::map<Text, Text> CHbaseConnection::global_attributes;


CHbaseConnection::CHbaseConnection(std::string& ip, int port) : ip_(ip), port_(port), opened_(false)
{
}

CHbaseConnection::~CHbaseConnection()
{
}


void CHbaseConnection::open()
{
	if (opened_ ) return ;

	socket.reset(new TSocket(ip_, port_));
	socket->setConnTimeout(100);
	socket->setSendTimeout(200);
	socket->setRecvTimeout(5000);

	transport.reset(new TBufferedTransport(socket));
	protocol.reset(new TBinaryProtocol(transport));
	client.reset(new HbaseClient(protocol));
	
	try {
		transport->open();
		opened_ = true;		
	}
	catch (TTransportException& ex) {
		printf("open to %s:%d error:%s\n", ip_.c_str(), port_, ex.what());
		opened_ = false;
	}	
}

bool CHbaseConnection::reopen()
{
	opened_ = false;
	open();
	return opened_;
}

void CHbaseConnection::close() 
{
	opened_ = true;
}

CHbaseConnectionFactory::CHbaseConnectionFactory(const std::string& ip, int port) : ip_(ip), port_(port)
{
	//int i = 0;
	//i++;
	//i += 1;
}

herry::Connection* CHbaseConnectionFactory::create()
{
	CHbaseConnection* con = new CHbaseConnection(ip_, port_);
	con->open();
	if (!con->opened()) {
		delete con;
		return NULL;
	}
	return con;
}


int CHbaseConnection::createTable(const Text& tableName, std::string& errmsg)
{
	std::vector<ColumnDescriptor> columns;

	ColumnDescriptor colDesc;
	colDesc.maxVersions = 1;
	colDesc.name = "entry:";
	columns.push_back(colDesc);

	try {
		client->createTable(tableName, columns);
	}
	catch (IOError& ex) {
		errmsg = ex.message;
		//sql_print_error("[createTable] get a IOError:%s!", ex.message.c_str());
		return -1;
	}
	catch (AlreadyExists& ex) {
		errmsg = ex.message;
		//sql_print_error("[createTable] get a AlreadyExists for table:%s!", tableName.c_str());
		return -1;
	}
	catch (TTransportException& ex) {
		errmsg = ex.what();
		//sql_print_error("[createTable] get a TTransportException for table:%s!", tableName.c_str());
		return -2;
	}
	catch (TException& ex) {
		errmsg = ex.what();
		//sql_print_error("[createTable] [%s] get a TException %s!", tableName.c_str(), ex.what());
		return -1;
	}
	return 0;
}

int CHbaseConnection::deleteTable(const Text& tableName, std::string& errmsg)
{
	try {
		client->disableTable(tableName);
		client->deleteTable(tableName);
	}
	catch (IOError& ex) {
		errmsg = ex.message;
		//sql_print_error("[createTable] get a IOError:%s!", ex.message.c_str());
		return -1;
	}
	catch (AlreadyExists& ex) {
		errmsg = ex.message;
		//sql_print_error("[createTable] get a AlreadyExists for table:%s!", tableName.c_str());
		return -1;
	}
	catch (TTransportException& ex) {
		errmsg = ex.what();
		//sql_print_error("[createTable] get a TTransportException for table:%s!", tableName.c_str());
		return -2;
	}
	catch (TException& ex) {
		errmsg = ex.what();
		//sql_print_error("[createTable] [%s] get a TException %s!", tableName.c_str(), ex.what());
		return -1;
	}
	return 0;
}


int CHbaseConnection::truncateTable(const Text& tableName, std::string& errmsg)
{
	std::vector<ColumnDescriptor> columns;

	ColumnDescriptor colDesc;
	colDesc.maxVersions = 1;
	colDesc.name = "entry:";
	columns.push_back(colDesc);

	try {
		client->disableTable(tableName); 
		client->truncateTable(tableName);		
		//client->enableTable(tableName);
	}
	catch (IOError& ex) {
		errmsg = ex.message;
		return -1;
	}
	catch (AlreadyExists& ex) {
		errmsg = ex.message;
		return -1;
	}
	catch (TTransportException& ex) {
		errmsg = ex.what();
		return -2;
	}
	catch (TException& ex) {
		errmsg = ex.what();
		return -1;
	}
	return 0;
}


int CHbaseConnection::renameTable(const Text& tableName, const Text& newTableName, std::string& errmsg)
{
	std::vector<ColumnDescriptor> columns;

	ColumnDescriptor colDesc;
	colDesc.maxVersions = 1;
	colDesc.name = "entry:";
	columns.push_back(colDesc);

	try {
		client->renameTable(tableName, newTableName);		
	}
	catch (IOError& ex) {
		errmsg = ex.message;
		return -1;
	}
	catch (AlreadyExists& ex) {
		errmsg = ex.message;
		return -1;
	}
	catch (TTransportException& ex) {
		errmsg = ex.what();
		return -2;
	}
	catch (TException& ex) {
		errmsg = ex.what();
		return -1;
	}
	return 0;
}

int CHbaseConnection::mutateRow(const Text& tableName, const Text& row, const std::vector<Mutation> & mutations, std::string& errmsg)
{
	try {
		client->mutateRow(tableName, row, mutations, global_attributes);
	}
	catch (IOError& ex) {
		errmsg = ex.message;		
		return -1;
	}
	catch (AlreadyExists& ex) {
		errmsg = ex.message;		
		return -1;
	}
	catch (TTransportException& ex) {
		errmsg = ex.what();		
		return -2;
	}
	catch (TException& ex) {
		errmsg = ex.what();		
		return -1;
	}
	return 0;
}

int CHbaseConnection::mutateRows(const Text& tableName, const std::vector<BatchMutation> & mutations, std::string& errmsg)
{
	try {
		client->mutateRows(tableName, mutations, global_attributes);
	}
	catch (IOError& ex) {
		errmsg = ex.message;
		return -1;
	}
	catch (AlreadyExists& ex) {
		errmsg = ex.message;
		return -1;
	}
	catch (TTransportException& ex) {
		errmsg = ex.what();
		return -2;
	}
	catch (TException& ex) {
		errmsg = ex.what();
		return -1;
	}
	return 0;
}


int CHbaseConnection::scannerOpenWithScan(const Text& tableName, const TScan& scan, ScannerID& scannId, std::string& errmsg)
{	
	try {
		scannId = client->scannerOpenWithScan(tableName, scan, global_attributes);
	}
	catch (IOError& ex) {
		errmsg = ex.message;
		return -1;
	}
	catch (AlreadyExists& ex) {
		errmsg = ex.message;
		return -1;
	}
	catch (TTransportException& ex) {
		errmsg = ex.what();
		return -2;
	}
	catch (TException& ex) {
		errmsg = ex.what();
		return -1;
	}
	return 0;
}


int CHbaseConnection::scannerGet(std::vector<TRowResult> & _return, const ScannerID id, std::string& errmsg)
{
	try {
		client->scannerGet(_return, id);
	}
	catch (IOError& ex) {
		errmsg = ex.message;
		return -1;
	}
	catch (AlreadyExists& ex) {
		errmsg = ex.message;
		return -1;
	}
	catch (TTransportException& ex) {
		errmsg = ex.what();
		return -2;
	}
	catch (TException& ex) {
		errmsg = ex.what();
		return -1;
	}
	return 0;
}

int CHbaseConnection::scannerClose(const ScannerID id, std::string& errmsg)
{
	try {
		client->scannerClose(id);
	}
	catch (IOError& ex) {
		errmsg = ex.message;
		return -1;
	}
	catch (AlreadyExists& ex) {
		errmsg = ex.message;
		return -1;
	}
	catch (TTransportException& ex) {
		errmsg = ex.what();
		return -2;
	}
	catch (TException& ex) {
		errmsg = ex.what();
		return -1;
	}
	return 0;
}

int CHbaseConnection::deleteAllRow(
	const Text& tableName, const Text& row, std::string& errmsg)
{
	try {
		client->deleteAllRow(tableName, row, global_attributes);
	}
	catch (IOError& ex) {
		errmsg = ex.message;
		return -1;
	}
	catch (AlreadyExists& ex) {
		errmsg = ex.message;
		return -1;
	}
	catch (TTransportException& ex) {
		errmsg = ex.what();
		return -2;
	}
	catch (TException& ex) {
		errmsg = ex.what();
		return -1;
	}
	return 0;
}



int CHbaseConnection::deleteRows(
	const Text& tableName, const std::vector<Text>& rows, std::string& errmsg)
{
	try {
		client->deleteRows(tableName, rows);
	}
	catch (IOError& ex) {
		errmsg = ex.message;
		return -1;
	}
	catch (AlreadyExists& ex) {
		errmsg = ex.message;
		return -1;
	}
	catch (TTransportException& ex) {
		errmsg = ex.what();
		return -2;
	}
	catch (TException& ex) {
		errmsg = ex.what();
		return -1;
	}
	return 0;
}


int CHbaseConnection::getRow(
	const Text& tableName, const Text& row, std::vector<TRowResult>& _return, std::string& errmsg)
{
	try {
		client->getRow(_return, tableName, row, global_attributes);
	}
	catch (IOError& ex) {
		errmsg = ex.message;
		return -1;
	}
	catch (AlreadyExists& ex) {
		errmsg = ex.message;
		return -1;
	}
	catch (TTransportException& ex) {
		errmsg = ex.what();
		return -2;
	}
	catch (TException& ex) {
		errmsg = ex.what();
		return -1;
	}
	return 0;
}

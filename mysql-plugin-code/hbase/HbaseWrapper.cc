/*********************************************************************
*
* Copyright (C), 2011-2020, Herry2038.
* All rights reserved
*
* Program:      
* File name:     HbaseWrapper.cpp
* Description:  
*
* Author:        Herry Yu     Email:     yxbherry@126.com
*                  
*  Version   Date         Source File       Description
* ---------------------------------------------------------------------
* 1.01.001 | 2016-6-22 | HbaseWrapper.cpp | 程序新建立                                  
*
**********************************************************************/


//////////////////////////////////////////////////////////////////////
// HbaseWrapper.cpp: s
//////////////////////////////////////////////////////////////////////

#include "HbaseWrapper.h"

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////


CHbaseWrapper::CHbaseWrapper(size_t pool_size, size_t idle_size, boost::shared_ptr<CHbaseConnectionFactory> f) :	
	pool(pool_size, idle_size, f)
{
}

CHbaseWrapper::~CHbaseWrapper()
{
}


int CHbaseWrapper::createTable(const Text& tableName, std::string& errmsg)
{
	scoped_connection scoped(pool);
	CHbaseConnection* con = scoped.get();

	if (con == NULL) {
		//sql_print_error("[createTable] get hbase connection error!");
		errmsg = "get hbase connection error!";
		return -1;
	}
	int ret = con->createTable(tableName, errmsg);
	switch ( ret )
	{		
	case 0:
		return 0;
	case -1:		
		return -1;
	case -2:
		if (con->reopen()) {
			return con->createTable(tableName, errmsg);
		}
		return -2;		
	}	
	return 0;
}

int CHbaseWrapper::deleteTable(const Text& tableName, std::string& errmsg)
{
	scoped_connection scoped(pool);
	CHbaseConnection* con = scoped.get();
	if (con == NULL) {
		//sql_print_error("[deleteTable] get hbase connection error!");
		errmsg = "get hbase connection error!";
		return -1;
	}

	int ret = con->deleteTable(tableName, errmsg);
	if (ret == -2 && con->reopen() ) {
		return con->deleteTable(tableName, errmsg);
	}
	return ret;	
}


int CHbaseWrapper::mutateRow(const Text& tableName, const Text& row, const std::vector<Mutation> & mutations, std::string& errmsg)
{
	scoped_connection scoped(pool);
	CHbaseConnection* con = scoped.get();
	if (con == NULL) {		
		errmsg = "get hbase connection error!";
		return -1;
	}

	int ret = con->mutateRow(tableName, row, mutations, errmsg);
	if (ret == -2 && con->reopen()) {
		return con->mutateRow(tableName, row, mutations, errmsg);
	}
	return ret;
}


int CHbaseWrapper::mutateRows(const Text& tableName, const std::vector<BatchMutation> & mutations, std::string& errmsg)
{
	scoped_connection scoped(pool);
	CHbaseConnection* con = scoped.get();
	if (con == NULL) {
		errmsg = "get hbase connection error!";
		return -1;
	}

	int ret = con->mutateRows(tableName, mutations, errmsg);
	if (ret == -2 && con->reopen()) {
		return con->mutateRows(tableName, mutations, errmsg);
	}
	return ret;
}

int CHbaseWrapper::scannerOpenWithScan(const Text& tableName, const TScan& scan, ScannerID& scannerId, std::string& errmsg)
{
	scoped_connection scoped(pool);
	CHbaseConnection* con = scoped.get();
	if (con == NULL) {
		errmsg = "get hbase connection error!";
		return -1;
	}


	int ret = con->scannerOpenWithScan(tableName, scan, scannerId, errmsg);
	
	if (ret == -2 && con->reopen())
		return con->scannerOpenWithScan(tableName, scan, scannerId, errmsg);
	return ret;	
}

int CHbaseWrapper::scannerGet(std::vector<TRowResult> & _return, const ScannerID id, std::string& errmsg)
{
	scoped_connection scoped(pool);
	CHbaseConnection* con = scoped.get();
	if (con == NULL) {
		errmsg = "get hbase connection error!";
		return -1;
	}
	int ret = con->scannerGet(_return, id, errmsg);

	if (ret == -2 && con->reopen())
		return con->scannerGet(_return, id, errmsg);
	return ret;
}

int CHbaseWrapper::scannerClose(const ScannerID id, std::string& errmsg)
{
	scoped_connection scoped(pool);
	CHbaseConnection* con = scoped.get();
	if (con == NULL) {
		errmsg = "get hbase connection error!";
		return -1;
	}
	int ret = con->scannerClose(id, errmsg);

	if (ret == -2 && con->reopen())
		return con->scannerClose(id, errmsg);
	return ret;
}


int CHbaseWrapper::deleteAllRow(const Text& tableName, const Text& row, std::string& errmsg)
{
	scoped_connection scoped(pool);
	CHbaseConnection* con = scoped.get();
	if (con == NULL) {
		errmsg = "get hbase connection error!";
		return -1;
	}
	int ret = con->deleteAllRow(tableName, row, errmsg);

	if (ret == -2 && con->reopen())
		return con->deleteAllRow(tableName, row, errmsg);
	return ret;
}



int CHbaseWrapper::deleteRows(const Text& tableName, const std::vector<Text>& rows, std::string& errmsg)
{
	scoped_connection scoped(pool);
	CHbaseConnection* con = scoped.get();
	if (con == NULL) {
		errmsg = "get hbase connection error!";
		return -1;
	}
	int ret = con->deleteRows(tableName, rows, errmsg);

	if (ret == -2 && con->reopen())
		return con->deleteRows(tableName, rows, errmsg);
	return ret;
}


int CHbaseWrapper::truncateTable(const Text& tableName, std::string& errmsg)
{
	
	scoped_connection scoped(pool);
	CHbaseConnection* con = scoped.get();
	if (con == NULL) {
		errmsg = "get hbase connection error!";
		return -1;
	}
	int ret = con->truncateTable(tableName, errmsg);

	if (ret == -2 && con->reopen())
		return con->truncateTable(tableName, errmsg);
	return ret;
}

int CHbaseWrapper::renameTable(const Text& tableName, const Text& newTableName, std::string& errmsg)
{

	scoped_connection scoped(pool);
	CHbaseConnection* con = scoped.get();
	if (con == NULL) {
		errmsg = "get hbase connection error!";
		return -1;
	}
	int ret = con->renameTable(tableName, newTableName, errmsg);

	if (ret == -2 && con->reopen())
		return con->renameTable(tableName, newTableName, errmsg);
	return ret;
}

int CHbaseWrapper::getRow(const Text& tableName, const std::string& row, std::vector<TRowResult> & _return, std::string& errmsg)
{
	scoped_connection scoped(pool);
	CHbaseConnection* con = scoped.get();
	if (con == NULL) {
		errmsg = "get hbase connection error!";
		return -1;
	}
	int ret = con->getRow(tableName, row, _return, errmsg);

	if (ret == -2 && con->reopen())
		return con->getRow(tableName, row, _return, errmsg);
	return ret;
}
/*********************************************************************
*
* Copyright (C), 2011-2020, Herry2038.
* All rights reserved
*
* Program:      
* File name:     HbaseWrapper.h
* Description:  
*
* Author:        Herry Yu 	Email:	yxbherry@126.com
*                  
*  Version   Date         Source File       Description
* ---------------------------------------------------------------------
* 1.01.001 | 2016-6-22 | HbaseWrapper.h    | 程序新建立                                  
*
**********************************************************************/


#ifndef HbaseWrapper_H_
#define HbaseWrapper_H_

#include "ConnectionPool.h"
#include "HbaseConnection.h"


class CHbaseWrapper
{
public:

	class scoped_connection {
	public:
		scoped_connection(herry::ConnectionPool<CHbaseConnection>& pool):m_pool(pool) {
			m_con = pool.borrow();
		}

		~scoped_connection() {
			m_pool.unborrow(m_con);
		}

		CHbaseConnection* get() { if (m_con == NULL) throw TTransportException(TTransportException::UNKNOWN, "Hbase Closed!");  return m_con; }
	private:
		CHbaseConnection* m_con;
		herry::ConnectionPool<CHbaseConnection>& m_pool;
	};
    //////////////////////////////////////////////////////////////////////
    // Construction/Destruction
    //////////////////////////////////////////////////////////////////////
    CHbaseWrapper(size_t pool_size, size_t idle_size, boost::shared_ptr<CHbaseConnectionFactory> f) ;
    ~CHbaseWrapper() ;

	herry::ConnectionPool<CHbaseConnection>& getPool() { return pool;  }
public:
	int createTable(const Text& tableName, std::string& errmsg);
	int deleteTable(const Text& tableName, std::string& errmsg);
	int renameTable(const Text& tableName, const Text& newTableName, std::string& errmsg);
	int mutateRow(const Text& tableName, const Text& row, const std::vector<Mutation> & mutations, std::string& errmsg);
	int mutateRows(const Text& tableName, const std::vector<BatchMutation> & mutations, std::string& errmsg);

	int scannerOpenWithScan(const Text& tableName, const TScan& scan, ScannerID& scannerId, std::string& errmg);
	int scannerGet(std::vector<TRowResult> & _return, const ScannerID id, std::string& errmg);
	int deleteAllRow(const Text& tableName, const Text& row, std::string& errmsg);
	int deleteRows(const Text& tableName, const std::vector<Text>& rows, std::string& errmsg);
	int scannerClose(const ScannerID id, std::string& errmg);
	int truncateTable(const Text& tableName, std::string& errmsg);

	int getRow(const Text& tableName, const std::string& row, std::vector<TRowResult> & _return, std::string& errmsg);
private:
	herry::ConnectionPool<CHbaseConnection> pool;	

	
};

#endif

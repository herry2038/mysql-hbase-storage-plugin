/*********************************************************************
*
* Copyright (C), 2011-2020, Herry2038.
* All rights reserved
*
* Program:      
* File name:     ConnectionPool.h
* Description:  
*
* Author:        Herry Yu 	Email:	yxbherry@126.com
*                  
*  Version   Date         Source File       Description
* ---------------------------------------------------------------------
* 1.01.001 | 2016-6-22 | ConnectionPool.h    | 程序新建立                                  
*
**********************************************************************/


#ifndef ConnectionPool_H_
#define ConnectionPool_H_


#ifndef _DEBUG
#define _DEBUG(x)
#endif



#include <deque>
#include <set>
#include <boost/shared_ptr.hpp>
#include <boost/thread/mutex.hpp>
#include <exception>
#include <string>
using namespace std;

namespace herry {


	struct ConnectionUnavailable : std::exception {

		char const* what() const throw() {

			return "Unable to allocate connection";
		};
	};


	class Connection {

	public:
		Connection(){};
		virtual ~Connection(){};

		virtual bool opened() = 0;
	};

	class ConnectionFactory {

	public:
		virtual Connection* create() = 0;
	};

	struct ConnectionPoolStats {

		size_t pool_size;
		size_t borrowed_size;

	};

	template<class T>
	class ConnectionPool {
	public:
		ConnectionPoolStats get_stats() {
			// Lock
			boost::mutex::scoped_lock lock(this->io_mutex);

			// Get stats
			ConnectionPoolStats stats;
			stats.pool_size = this->pool.size();
			stats.borrowed_size = this->borrowed.size();

			return stats;
		};

		ConnectionPool(size_t pool_size, size_t idle_size, boost::shared_ptr<ConnectionFactory> factory){
			// Setup
			this->idle_size = idle_size;
			this->pool_size = pool_size;
			this->factory = factory;

			// Fill the pool
			while (this->pool.size() < this->idle_size){
				T* con = (T*)this->factory->create();
				if (con == NULL)
					break;
				this->pool.push_back(con);
			}
		};

		~ConnectionPool() {
			for (deque<T*>::iterator it = pool.begin(); it != pool.end(); ++it)
				delete *it;

			for (set<T*>::iterator it = borrowed.begin(); it != borrowed.end(); ++it)
				delete *it;
		};

		/**
		* Borrow
		*
		* Borrow a connection for temporary use
		*
		* When done, either (a) call unborrow() to return it, or (b) (if it's bad) just let it go out of scope.  This will cause it to automatically be replaced.
		* @retval a boost::shared_ptr to the connection object
		*/
		T* borrow(){
			// Lock
			boost::mutex::scoped_lock lock(this->io_mutex);

			// Check for a free connection
			if (this->pool.size() == 0){
				if (this->borrowed.size() < pool_size) {				
					// This connection has been abandoned! Destroy it and create a new connection
					try {
						// If we are able to create a new connection, return it
						printf("Creating new connection \n");
						Connection* conn = this->factory->create();		
						if (conn == NULL) return NULL;
						T* realCon = (T*)conn;
						this->borrowed.insert(realCon);
						return realCon;
					}
					catch (std::exception& e) {

						// Error creating a replacement connection
						throw ConnectionUnavailable();
					}					
				}

				// Nothing available
				throw ConnectionUnavailable();
			}

			// Take one off the front
			T* conn = this->pool.front();
			this->pool.pop_front();

			// Add it to the borrowed list
			this->borrowed.insert(conn);

			//return boost::static_pointer_cast<T>(conn);
			return conn;
		};

		/**
		* Unborrow a connection
		*
		* Only call this if you are returning a working connection.  If the connection was bad, just let it go out of scope (so the connection manager can replace it).
		* @param the connection
		*/
		void unborrow(T* conn) {
			if ( conn == NULL) return;
			// Lock
			boost::mutex::scoped_lock lock(this->io_mutex);

			// Unborrow
			this->borrowed.erase(conn);

			if (!(conn->opened() && this->pool.size() >= idle_size && this->borrowed.size() > idle_size / 2)) {
				// Push onto the pool
				this->pool.push_back(conn);
			}
			else {
				printf("Drop idle connection\n");
				delete conn;
			}
		};

	protected:
		boost::shared_ptr<ConnectionFactory> factory;
		size_t pool_size;
		size_t idle_size;
		deque<T*> pool;
		set<T*> borrowed;
		boost::mutex io_mutex;

	};
}

#endif

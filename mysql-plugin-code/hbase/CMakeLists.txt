

SET(HBASE_PLUGIN_STATIC  "hbase")
SET(HBASE_PLUGIN_DYNAMIC "ha_hbase")
#SET(HBASE_PLUGIN_MANDATORY TRUE)


file(GLOB_RECURSE gencpp_INCS "gen-cpp/*.h")
AUX_SOURCE_DIRECTORY(gen-cpp gencpp_SRCS)


file(GLOB_RECURSE main_INCS "*.h")
AUX_SOURCE_DIRECTORY(. main_SRCS)


#SET(HBASE_SOURCES  ha_hbase.cc ha_hbase.h hbase_data.cc hbase_data.h hbase_helper.cc hbase_helper.h HbaseConnection.h ConnectionPool.h HbaseConnection.cc ${gencpp_INCS} ${gencpp_SRCS})

SET(HBASE_SOURCES  ${main_INCS} ${main_SRCS} ${gencpp_INCS} ${gencpp_SRCS})
IF(NOT WITH_HBASE AND NOT WITH_HBASE_STORAGE_ENGINE)
 # Bug#45488- federated uses symbols that are not used anywhere in
 # mysqld and are optimized away by the linker.
 SET(HBASE_SOURCES ${HBASE_SOURCES} ${CMAKE_SOURCE_DIR}/mysys/string.c)
ENDIF()

INCLUDE_DIRECTORIES(
		    ${CMAKE_SOURCE_DIR}/include
		    ${CMAKE_SOURCE_DIR}/sql
		    ${CMAKE_SOURCE_DIR}/bld/include
		    ${CMAKE_SOURCE_DIR}/regex		    
		    ${CMAKE_SOURCE_DIR}/include/mysql
		    ${CMAKE_C_IMPLICIT_LINK_DIRECTORIES}
			$ENV{THIRDPARTY_HOME}/thrift/include
			$ENV{THIRDPARTY_HOME}/boost/include
		)

LINK_DIRECTORIES($ENV{THIRDPARTY_HOME}/boost/lib $ENV{THRIFT_LIBRARY}/lib)

MYSQL_ADD_PLUGIN(hbase ${HBASE_SOURCES} STORAGE_ENGINE)

if(MSVC)
	SET(CMAKE_CPP_FLAGS "${CMAKE_CPP_FLAGS} /EHsc")
	SET(VS_MULTITHREADED_DEBUG_IGNORE_LIBRARY_FLAGS
		"/NODEFAULTLIB:libcmt.lib"
	)		
	SET_TARGET_PROPERTIES(hbase PROPERTIES
       LINK_FLAGS_DEBUG
       ${VS_MULTITHREADED_DEBUG_IGNORE_LIBRARY_FLAGS})	
else()	
	TARGET_LINK_LIBRARIES(hbase boost_thread)
endif()

TARGET_LINK_LIBRARIES(hbase libthrift)

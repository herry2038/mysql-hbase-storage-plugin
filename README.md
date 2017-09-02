# mysql-hbase-storage-plugin
mysql的Hbase 存储插件代码，主要目的是为了做海量日志型存储。

里面的代码由两个部分组成：
## 1、mysql-plugin-code

这块是MySQL的hbase插件代码。

编译hbase存储引擎插件的步骤如下：
* 拷贝mysql-plugin-code目录下的hbase目录到 $MYSQL_CODE/storage目录下
* 准备thrift-0.9.2和boost的开发环境，把他们放到指定的目录下：/data/3dpartylib
   对应的子目录结构为：  
  /data/3dpartylib/
  
                  thrift/
                  
                         include/
                         
                         lib/
                  boost/ 
                  
                         include/
                         
                         lib/
         
* 设置环境变量：THIRDPARTY_HOME=/data/3dpartylib
* cd $MYSQL_CODE
* cmake ..
* make

## 2、hbase-thrift-server

hbase-thrift-server代码是经过改造过的hbase-thrift服务，因为原生的thrift-server代码是采用Thrift原生的服务框架，它缺省采用了一个连接一个线程的服务框架。
承载的服务能力有限。

所以我基于swift改造了hbase-thrift-server，基于Netty来实现高性能的Thrift服务，从而可以实在高并发的服务能力。



# 部署hbase存储引擎
## 1、准备hbase-thrift-server
  运行java org.apache.hadoop.hbase.swift.SwiftServer 
  
  这里省略了classpath的设置。也可以直接在eclispe项目中执行。
  
  会启动一个8899端口号的hbase的thrift服务。
 
## 2、启动hbase存储引擎插件
把生成的ha_hbase.so（或者ha_hbase.dll）文件拷贝到 mysql的plugin目录中。
启动好mysql以后：

install plugin hbase soname 'ha_hbase.so' ;

然后设置参数：

hbase_ip=127.0.01

hbase_port=8899

hbase_pool_size=5

hbase_idle_size=5

重启mysqld


也可以在在my.cnf中设置参数：

plugin-load="hbase=ha_hbase.dll"

hbase_ip=127.0.0.1

hbase_port=8899

hbase_pool_size=5

hbase_idle_size=5





# 使用hase存储引擎
   create table a (  
id int primary key,  
name varchar(32) ,  
t date  
) engine='hbase' ;



   create table a4 (  
id int primary key,  
name varchar(32) ,  
t date  
) engine='hbase' ;  


insert into a values ( 1, 'abc', now() ) ;  
insert into a values ( 2, 'ffff', now() ) ;  
insert into a values ( 3, 'bcd', now() ) ;  
insert into a values ( -1, 'vv', now() ) ;  
insert into a values ( 256, 'jj', now() ) ;  
insert into a values ( -256, 'x', now() ) ;  

   select * from a ;

# 已经支持的功能有

- 主键

- 第二索引



# 后记

本来我开发hbase存储引擎的初衷是为了屏蔽业务上使用MySQL不同引擎的表的不同，当业务上一个表成为历史表以后，可以考虑转移到hbase引擎上。
但是最终我们没有采用这种方案，所以开源的hbase存储引擎代码我没有经过线上的使用，也不建议大家把它用到线上运行环境中，仅仅作为交流学习使用。

后续我会继续丰富相关的文档和当初的设计思路。

当然如果大家对这块东西感兴趣，也可以联系我，一起交流学习，甚至于继续完善它，让它可以为线上业务服务。



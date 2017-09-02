package org.apache.hadoop.hbase.swift;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.security.UserProvider;

import com.facebook.nifty.core.NettyServerConfig;
import com.facebook.nifty.core.ThriftServerDef;
import com.facebook.swift.codec.ThriftCodecManager;
import com.facebook.swift.service.ThriftEventHandler;
import com.facebook.swift.service.ThriftServer;
import com.facebook.swift.service.ThriftServiceProcessor;
import com.google.common.collect.ImmutableList;

public class SwiftServer {

	public static void main(String[] args) throws IOException {
		ExecutorService taskWorkerExecutor;
        ThriftServer server;
        
        ExecutorService bossExecutor;
        ExecutorService ioWorkerExecutor;

        Configuration conf = HBaseConfiguration.create();
        UserProvider userProvider = UserProvider.instantiate(conf);
        ThriftServiceProcessor processor = new ThriftServiceProcessor(
                new ThriftCodecManager(),
                ImmutableList.<ThriftEventHandler>of(),
                new SwiftHbaseService(conf, userProvider)
        ); 

        taskWorkerExecutor = Executors.newFixedThreadPool(1);

        ThriftServerDef serverDef = ThriftServerDef.newBuilder()
                                                   .listen(8899)
                                                   .withProcessor(processor)
                                                   .using(taskWorkerExecutor)
                                                   .build();

        bossExecutor = Executors.newFixedThreadPool(1);
        ioWorkerExecutor = Executors.newCachedThreadPool() ;

        NettyServerConfig serverConfig = NettyServerConfig.newBuilder()
                                                          .setBossThreadExecutor(bossExecutor)
                                                          .setWorkerThreadExecutor(ioWorkerExecutor)
                                                          .build();
        
		
		server = new ThriftServer(serverConfig, serverDef) ;
		server.start();
	}
}

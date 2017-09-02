package org.apache.hadoop.hbase.swift2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.security.UserProvider;
import org.apache.hadoop.hbase.swift2.generated.TColumnValue;
import org.apache.hadoop.hbase.swift2.generated.TResult;
import org.apache.hadoop.hbase.util.ConnectionCache;

public class TestHbase {

	public static void main(String[] args) throws IOException {
        Configuration conf = HBaseConfiguration.create();
        UserProvider userProvider = UserProvider.instantiate(conf);
        
		final String CLEANUP_INTERVAL = "hbase.thrift.connection.cleanup-interval";
		final String MAX_IDLETIME = "hbase.thrift.connection.max-idletime";
		
	    int cleanInterval = conf.getInt(CLEANUP_INTERVAL, 10 * 1000);
	    int maxIdleTime = conf.getInt(MAX_IDLETIME, 10 * 60 * 1000);
	    
		ConnectionCache connectionCache = new ConnectionCache(
			      conf, userProvider, cleanInterval, maxIdleTime);
		
		Table t = connectionCache.getTable("example") ;
		Result result = t.get(new Get("foo".getBytes())) ;
		Cell[] raw = result.rawCells();
    
		for (Cell kv : raw) {
			System.out.print(new String(kv.getFamilyArray())) ;
			System.out.print(",") ;
			System.out.print(new String(kv.getQualifierArray())) ;
			System.out.print(",") ;
			System.out.print(new String(kv.getValueArray())) ;
			System.out.print(",") ;
			System.out.println(kv.getTimestamp()) ;
		}
	}
}

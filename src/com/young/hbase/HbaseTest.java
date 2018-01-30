package com.young.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * Created by young on 17/12/22.
 */
public class HbaseTest {

    private static final String HABAE_ZOOKEEPER = "hbase.zookeeper.quorum";
    static Configuration conf = null;
    static {
        conf = HBaseConfiguration.create();
        conf.set(HABAE_ZOOKEEPER,"127.0.0.1");
        conf.set("hbase.zookeeper.property.clientPort", "2181");
        //conf.set("hbase.master", "590340cb4213:60010");
    }
    public static void main(String[] args) throws Exception{
        //createTable();
        String columnFamilyName = "etl";
        String columnName = "a";
        Connection connection = ConnectionFactory.createConnection(conf);
        TableName tableName = TableName.valueOf("test");
        Table table = connection.getTable(tableName);
        ResultScanner scanner = table.getScanner(new Scan());

        Get get = new Get(Bytes.toBytes("row1"));
        //get.addColumn(Bytes.toBytes(columnFamilyName), Bytes.toBytes(columnName));
        Result result = table.get(get);
        byte[] value = result.getValue(Bytes.toBytes(columnFamilyName), Bytes.toBytes(columnName));
        System.out.println(Bytes.toString(value));
        System.out.println(result.getExists());
        System.out.println(result);
    }

    private static void createTable(String tableName, String[] family) throws IOException {
    }
}

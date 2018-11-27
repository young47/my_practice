package com.young.hash;

import net.spy.memcached.DefaultHashAlgorithm;
import sun.security.provider.MD5;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 不带虚拟node的一致性hash算法
 */
public class ConsistentHashing {

    private static MessageDigest md5;
    /*public static String[] servers = {"10.18.75.101", "10.18.75.102", "10.18.75.103", "10.18.75.104", "10.18.75.105",
            "10.18.75.111", "10.18.75.112", "10.18.75.113", "10.18.75.114", "10.18.75.115","10.17.02.19","10.16.78.119"};*/
    public static String[] servers = {"CentOS_txyz_75_101", "CentOS_txyz_79_102",
            "10.10.71.120", "CentOS_txyz_5_1", "10_12_34CentOS", "10.1.175.13", "10.20.05.124", "10.10.75.125","10.17.02.19",
            "CentOS_txyz_85_121"};
//,
    private static TreeMap<Integer, String> nodes = new TreeMap<>();


    static {
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        for (String server : servers) {
            nodes.put(getHash(server), server);
        }
    }

    protected static int getHash(String target) {
        int hash = 0;
        hash = FNV1_32_HASH(target);
        //hash = (int)DefaultHashAlgorithm.CRC_HASH.hash(target);

        return hash;
    }

    private static int FNV1_32_HASH(String target) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < target.length(); i++)
            hash = (hash ^ target.charAt(i)) * p;
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;

        // 如果算出来的值为负数则取其绝对值
        if (hash < 0)
            hash = Math.abs(hash);
        return hash;
    }

    protected static String getServer(String cid) {
        final int hash = getHash(cid);
        final SortedMap<Integer, String> tailMap = nodes.tailMap(hash);
        Integer key = nodes.firstKey();
        if (!tailMap.isEmpty()){
            key = tailMap.firstKey();
        }
        //System.out.println("cid="+cid+" hash="+hash+", server hash="+key);
        return nodes.get(key);
    }
}

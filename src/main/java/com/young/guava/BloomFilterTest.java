package com.young.guava;

import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.hash.PrimitiveSink;
import redis.clients.jedis.Jedis;

import java.io.*;
import java.util.Random;

public class BloomFilterTest {
    public static int nums = 2000_0000;
    //public static int nums = Integer.MAX_VALUE >>1 ;  // 5G左右
    private static final String oid = "322729577";
    public static final BloomFilter<String> bloomFilter = BloomFilter.create(new Funnel<String>() {
        @Override
        public void funnel(String from, PrimitiveSink into) {
            into.putString(from, Charsets.UTF_8);
        }
    }, nums, 0.0001d);

    public static void main(String[] args) throws FileNotFoundException {
        testFpp();
        //testRedis();
    }

    private static void testRedis() {
        final Jedis jedis = new Jedis("192.168.10.130");
        System.out.println(jedis.ping());
    }

    private static void testFpp() throws FileNotFoundException {
        initFilter();
        check();
    }

    private static void check() {
        String[] cids = {
                "6044546208654340184",
                "6187880380365189193",
                "6368066400229437547",
                "6446675055010558003",
                "6029558686463471646",
                "6465261740535230504",
                "6446675938800742481",
                "6450314075271770193",
                "6352391392341700646",
                "6352404463906893851",
                "6450313517265760269",
                "6368048240180768780",
                "6368043098228305951",
                "6352407458950328386",
                "6352407590169129063"};

        System.out.println("to find cid : " + cids.length);
        int s = 0;
        for (String c : cids) {
            if (bloomFilter.mightContain(c)) {
                s++;
                System.out.println("find: "+c);
            }
        }
        System.out.println("find=" + s);
        System.out.println(bloomFilter.expectedFpp());

    }

    private static void initFilter() throws FileNotFoundException {

        String file = "/Users/young/Desktop/temp/sub_total.cidlist";
        final BufferedReader br = new BufferedReader(new FileReader(new File(file)));
        String cid = null;
        int error = 0, total = 0;
        try {
            while ((cid = br.readLine()) != null) {
                cid += oid;
                if (bloomFilter.mightContain(cid)) {
                    //System.out.println("existed:"+cid);
                    error++;
                } else {
                    /*if ("6320137222670954602".equals(cid)){
                        System.out.println("AAAAAAAAAAAAA");
                    }*/
                    bloomFilter.put(cid);
                }
            }
        } catch (Exception e) {
            System.out.println("cid=" + cid);
            e.printStackTrace();
        } finally {
            try {
                br.close();
                //bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("error="+error);
    }
}

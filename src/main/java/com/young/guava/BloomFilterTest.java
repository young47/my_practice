package com.young.guava;

import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.hash.PrimitiveSink;

import java.io.*;

public class BloomFilterTest {
    public static int nums = 5_0000_0000;
    //public static int nums = Integer.MAX_VALUE >>1 ;  // 5G左右
    private static final String oid = "322729577";
    public static final BloomFilter<String> bloomFilter = BloomFilter.create(new Funnel<String>() {
        @Override
        public void funnel(String from, PrimitiveSink into) {
            into.putString(from, Charsets.UTF_8);
        }
    }, nums, 0.0001d);

    private static String base = "/Users/young/Desktop/temp";

    public static void main(String[] args) throws FileNotFoundException {
        if (args.length>0){
            base = args[0];
        }
        testFpp();
    }

    private static void testFpp() throws FileNotFoundException {
        initFilter();
    }

    private static void initFilter() throws FileNotFoundException {

        String file = base + "/test.cidlist";
        final BufferedReader br = new BufferedReader(new FileReader(new File(file)));
        String cid = null;
        int success = 0, total = 0;
        try {
            while ((cid = br.readLine()) != null) {
                cid += "," + oid;
                if (bloomFilter.mightContain(cid)) {
                    //System.out.println("existed:"+cid);
                } else {
                    bloomFilter.put(cid);
                    success++;
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
        System.out.println("success=" + success);
    }
}

package com.young.guava;

import java.io.*;
import java.util.HashMap;
import java.util.Random;

import static com.young.guava.BloomFilterTest.bloomFilter;

public class MyBloomFilterTest {
    public static void main(String[] args) throws FileNotFoundException {
        final MyBloomFilter bloomFilter = initMyBloom();
        check(bloomFilter);
        System.out.println("bitSize="+bloomFilter.getNumBits()+",bitCount="+bloomFilter.getBitSetCount());
        //readByte(bloomFilter);
    }

    private static void readByte(MyBloomFilter bloomFilter) {
        final HashMap<Character, Long> hashMap = new HashMap<Character, Long>(200);
        final byte[] aByte = bloomFilter.getByte();
        for (int i = 0; i < aByte.length; i++) {
            final char c = (char) (aByte[i] & 0x7F);
            if (hashMap.containsKey(c)){
                final long l = hashMap.get(c).longValue() + 1l;
                hashMap.put(c, l);
            }else {
                hashMap.put(c, 1l);
            }
        }
        System.out.println("byte length = "+aByte.length);
        for (Character character : hashMap.keySet()) {
            System.out.println(character+":"+hashMap.get(character));
        }

    }

    private static void check(MyBloomFilter myBloomFilter) {
        String[] cids = {"6044546208654340184",
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
        System.out.println("total cid : " + cids.length);
        int s = 0;
        for (String c : cids) {
            if (myBloomFilter.mightContain(c)) {
                s++;
                //System.out.println("find in filter:"+c);
            }
        }
        System.out.println("find=" + s);
    }

    public static MyBloomFilter initMyBloom() throws FileNotFoundException {
        final MyBloomFilter bloomFilter = MyBloomFilter.build(18064463, 0.0001d);
        String file = "/Users/young/Desktop/temp/sub_total.cidlist";
        final BufferedReader br = new BufferedReader(new FileReader(new File(file)));
        String cid = null;
        int error = 0, inserted = 0;
        try {
            while ((cid = br.readLine()) != null) {
                if (bloomFilter.mightContain(cid)) {
                    //System.out.println("exists:"+cid);
                    error++;
                } else {
                    bloomFilter.put(cid);
                }
                if (inserted++ == 1500_0000){
                    System.out.println("inserted completed");
                    break;
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
        return bloomFilter;
    }
}

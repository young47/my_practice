package com.young.temp;

import com.young.guava.MyBloomFilter;

import java.io.*;
import java.util.Date;

public class PushBloomFilterTest {

    private static final File DUMP = new File("/Users/young/Desktop/temp/bloom-filter-test.dump");
    private static final String oid = "322729577";
    public static void main(String[] args) throws FileNotFoundException {
        //final PushBloomFilter pushBloomFilter = PushBloomFilter.create(18064463, 0.0001d, DUMP);
        final PushBloomFilter pushBloomFilter = PushBloomFilter.create(BloomFilterParams.MAX_KEY_INSERTED, 0.0001d, DUMP);
        System.out.println("createTime="+new Date(pushBloomFilter.getCreateTime())+",keyCapacity="+pushBloomFilter
                .getKeyCapacity()+",keyInserted="+pushBloomFilter.getKeyInserted());
        fillingFilter(pushBloomFilter);
        check(pushBloomFilter);
    }

    private static void fillingFilter(PushBloomFilter pushBloomFilter) throws FileNotFoundException {
        String file = "/Users/young/Desktop/temp/sub_total.cidlist";
        final BufferedReader br = new BufferedReader(new FileReader(new File(file)));
        String cid = null;
        int error = 0, inserted = 0;
        try {
            while ((cid = br.readLine()) != null) {
                cid += oid;
                if (pushBloomFilter.mightContain(cid)) {
                    //System.out.println("exists:"+cid);
                    error++;
                } else {
                    pushBloomFilter.put(cid);
                    inserted++;
                }
                /*if (inserted++ == 1500_0000){
                    System.out.println("inserted completed");
                    break;
                }*/
                //Thread.sleep(10);
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
        System.out.println("error="+error+",inserted="+inserted+",keyInserted="+pushBloomFilter.getKeyInserted());
    }

    private static void check(PushBloomFilter pushBloomFilter) {
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
        int s = 0;
        for (String c : cids) {
            if (pushBloomFilter.mightContain(c+oid)) {
                s++;
                //System.out.println("find in filter:"+c);
            }
        }
        System.out.println("total=" + cids.length+",find=" + s);

        System.out.println("createTime="+new Date(pushBloomFilter.getCreateTime())+",keyCapacity="+pushBloomFilter
                .getKeyCapacity()+",bitSize="+pushBloomFilter.getBitNums()+",bitCount="+pushBloomFilter.getOneBitCount());
    }
}

package com.young.collection.map;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;

/**
 * Created by young on 18/1/26.
 */
public class LinkedHashMapTest {
    Map<String, String> map = new LinkedHashMap();
    public static void main(String[] args){
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        for (String s : list) {
            if (s.equals("c")){
                list.remove(s);
            }
        }
        System.out.println(list.size());

        HashMap<String, Long> map = new HashMap<>();
//        map.put("a", 2l);
        Long a = map.computeIfPresent("a", (key, val) -> ++val);
        System.out.println("a="+a);
        Long b = map.computeIfPresent("b", (key, val)-> val++);
        System.out.println("b="+b);
        Long c = map.computeIfAbsent("c", val -> 90l);
        System.out.println("c="+c);

        map.merge("e", 24l, (oldValue, newValue)->++newValue);
        System.out.println(map);

        StringBuilder sb = new StringBuilder();
        sb.append("abc");
        sb.append("bff");
        sb.append("cgr");
        for (String s : sb.toString().split(",")) {
            System.out.println(s);
        }

        HashMap<Long, Integer> hashMap = new HashMap<>();
        hashMap.put(1l, 10);
        System.out.println(hashMap.containsKey(1));


        try {
            RandomAccessFile file = new RandomAccessFile(new File("/Users/young/Desktop/temp/dump/push-time.dump"), "r");
            FileChannel channel = file.getChannel();
            MappedByteBuffer meta = channel.map(FileChannel.MapMode.READ_ONLY, 0, 21);
            System.out.println(meta.get(0) == (byte) 0xCC);
            System.out.println(meta.getLong(1));
            System.out.println(meta.getInt(9));
            System.out.println(meta.getLong(13));

            /*RandomAccessFile file = new RandomAccessFile(new File("/Users/young/Desktop/temp/dump/push-time.dump"), "rw");
            file.setLength(0);
            FileChannel channel = file.getChannel();

            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_WRITE, 21, 18064463 * 16);
            String file1 = "/Users/young/Desktop/temp/sub_total.cidlist";
            final BufferedReader br = new BufferedReader(new FileReader(new File(file1)));
            String cid = null;
            int put = 0;
            while ((cid = br.readLine()) != null) {
                try {
                    buffer.putLong(Long.valueOf(cid)).putLong(System.currentTimeMillis());
                    put++;
                *//*if (put == 1000_0000) {
                    break;
                }*//*
                } catch (Exception e) {
                    System.out.println("cid=" + cid);
                    e.printStackTrace();
                }
            }
            try {
                br.close();
                //bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            MappedByteBuffer meta = channel.map(FileChannel.MapMode.READ_WRITE, 0, 21);

            meta.put((byte) 0xCC).putLong(System.currentTimeMillis()).putInt(put).putLong(0);
            System.out.println("put=" + put);*/
        }catch (Exception e){

        }


    }

}

package com.young;

import com.alibaba.fastjson.JSONObject;
import com.sohu.push.cache.Cache;
import com.sohu.push.exception.CacheException;
import com.sohu.push.factory.CacheFactory;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Base64;

public class Temp {
    static {
        i = 1;
        //System.out.println(i);
    }
    static int i = 0;
    public static void main(String[] args) {
        System.out.println("0".equals(null));

        StringBuilder sb = new StringBuilder();
        sb.append("a");
        sb.append("b");
        sb.append("cc");
//        System.out.println(sb.toString());

        try {
            String back2url = URLEncoder.encode("[本+地·]跑&^前Ⅲ热身与跑后拉伸", "utf-8");
            System.out.println(back2url);

            String encode = URLDecoder.decode("%5B%E6%9C%AC+%E5%9C%B0%C2%B7%5D%E8%B7%91&%5E%E5%89%8D%E2%85%A2%E7%83%AD%E8%BA%AB%E4%B8%8E%E8%B7%91%E5%90%8E%E6%8B%89%E4%BC%B8", "utf-8");
            System.out.println(encode);

            encode = URLDecoder.decode("&~+%E2%85%A0%20%E2%85%A1%20%E2%85%A2%20%E2%85%A3%E3%8E%A1m%C2%B3%C2%B7%3C%C2%B7%3E", "utf-8");
            System.out.println(encode);

            final String decode = URLDecoder.decode
                    ("%5B%E6%9C%AC%2B%E5%9C%B0%C2%B7%5D%E8%B7%91%26%5E%E5%89%8D%E2%85%A2%E7%83%AD%E8%BA%AB%E4%B8%8E%E8" +
                    "%B7%91%E5%90%8E%E6%8B%89%E4%BC%B8", "utf-8");
            System.out.println(decode);
            System.out.println("0".equals(1));

            char c = ',';
            char d = '，';
            System.out.println("c="+(int)c+",d="+(int)d);

            System.out.println((char)(-24));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        final byte[] encodes = Base64.getEncoder().encode("0861135039542305200000058800CN01".getBytes());

        System.out.println(Base64.getEncoder().encodeToString("0861135039542305200000058800CN01".getBytes()));

        int[] arrays = {};
        System.out.println(arrays[0]);
//        System.out.println("123".compareTo("23"));

        /*try {
            final Cache cache = CacheFactory.getCache(CacheFactory.REDIS);
        } catch (CacheException e) {
            e.printStackTrace();
        }*/
        /*BufferedWriter bw = null;
        final File file = new File("/Users/young/Desktop/newlocation.mapping");
        try (BufferedReader br = new BufferedReader(new FileReader(new File("/Users/young/Desktop/location" +
                ".mapping")))) {
            bw = new BufferedWriter(new FileWriter(file));

            String line = null;
            while ((line = br.readLine()) != null) {
                final String[] split = line.split("\t");
                String newLine = split[1]+"="+split[0];
                bw.write(newLine);
                bw.write(System.lineSeparator());
            }
            br.close();
            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader br = new BufferedReader(new FileReader(new File("/Users/young/Desktop/newlocation" +
                ".mapping")))) {

            String line = null;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }
}

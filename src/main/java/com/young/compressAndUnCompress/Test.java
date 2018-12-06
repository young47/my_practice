package com.young.compressAndUnCompress;

import com.google.common.base.Charsets;
import com.young.guava.MyBloomFilter;
import com.young.guava.MyBloomFilterTest;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.*;

public class Test {
    private static final int _1M = 1024 * 1024, _1K = 1024;

    private static final String push = "{\"cid\":\"6459285550141976699\",\"intervalTime\":\"2700\",\"isFilter\":0,\"oidList\":[\"323497909\"," +
            "\"323671414\",\"323694817\",\"323667198\",\"323609135\",\"323281545\",\"320583964\",\"323504503\"," +
            "\"323212700\",\"323706929\",\"323373019\",\"323613666\",\"323500927\",\"323481442\",\"323500007\"," +
            "\"323310454\",\"323502477\",\"323389275\",\"323178874\",\"323655284\"],\"type\":\"h\"," +
            "\"userInfo\":\"{\\\"a\\\":44,\\\"ap\\\":32,\\\"c\\\":6459285550141976699,\\\"ch\\\":6508,\\\"dt\\\":\\\"\\\"," +
            "\\\"f\\\":0,\\\"im\\\":\\\"862704031207255\\\",\\\"p\\\":10,\\\"pm\\\":0," +
            "\\\"pt\\\":\\\"6459285550141976699\\\",\\\"pv\\\":0,\\\"s\\\":1,\\\"st\\\":0,\\\"t\\\":0,\\\"tm\\\":0," +
            "\\\"v\\\":545}\"}";
    public static void main(String[] args) throws IOException, DataFormatException {
        byte[] bytes = construct();
        System.out.println("压缩前length=" + bytes.length);

        zlibTest(bytes);

        System.out.println(" ");

        gzipTest(bytes);

        System.out.println(" ");

        zipTest(bytes);

    }

    private static void zipTest(byte[] bytes) {
    }

    private static void zlibTest(byte[] bytes) throws DataFormatException {
        byte[] compressed = zlibCompress(bytes);
        System.out.println("zlib压缩后length=" + compressed.length);

        byte[] deCompressed = zlibDecompress(compressed);
        System.out.println("zlib解压缩后length=" + deCompressed.length);
        System.out.println("zlib解压缩后 push=" + new String(deCompressed, Charsets.UTF_8).equals(push));

    }

    private static byte[] zlibDecompress(byte[] compressed) throws DataFormatException {
        final long begin = System.currentTimeMillis();
        final Inflater inflater = new Inflater();
        inflater.reset();
        inflater.setInput(compressed);

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(compressed.length);
        final byte[] buff = new byte[_1K];
        while (!inflater.finished()){
            int count = inflater.inflate(buff);
            outputStream.write(buff, 0, count);
        }
        inflater.end();
        final long end = System.currentTimeMillis();
        System.out.println("zlib解压缩cost: "+ (end-begin)+"ms");
        return outputStream.toByteArray();
    }

    private static byte[] zlibCompress(byte[] bytes) {
        final long begin = System.currentTimeMillis();
        // Compress the bytes
        byte[] buff = new byte[_1K];
        Deflater compresser = new Deflater();
        compresser.setInput(bytes);
        compresser.finish();
        final ByteArrayOutputStream bos = new ByteArrayOutputStream(bytes.length);
        while (!compresser.finished()){
            int count = compresser.deflate(buff);
            bos.write(buff, 0, count);
        }
        compresser.end();
        final long end = System.currentTimeMillis();
        System.out.println("zlib压缩cost: "+ (end-begin)+"ms");
        return bos.toByteArray();
    }

    private static void gzipTest(byte[] bytes) throws IOException {
        final byte[] compressed = gzipCompress(bytes);
        System.out.println("gzip压缩后length=" + compressed.length);

        final byte[] deCompressed = gzipDecompress(compressed);
        System.out.println("gzip解压缩后length=" + deCompressed.length);
        System.out.println("gzip解压缩后 push=" + new String(deCompressed, Charsets.UTF_8).equals(push));

        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] != deCompressed[i]) {
                System.out.println(bytes[i] + "++" + deCompressed[i]);
            }
        }
    }

    private static byte[] gzipDecompress(byte[] compressed) throws IOException {
        final long begin = System.currentTimeMillis();
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(compressed);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);
        final byte[] data = new byte[_1M];
        int count = 0;
        while ((count = gzipInputStream.read(data, 0, _1M)) != -1) {
            outputStream.write(data, 0, count);
        }
        inputStream.close();
        gzipInputStream.close();
        final long end = System.currentTimeMillis();
        System.out.println("gzip解压缩cost: "+ (end-begin)+"ms");
        return outputStream.toByteArray();
    }

    private static byte[] gzipCompress(byte[] input) throws IOException {
        final long begin = System.currentTimeMillis();
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(input);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream);
        final byte[] data = new byte[_1M];
        int count = 0;
        while ((count = byteArrayInputStream.read(data, 0, _1M)) != -1) {
            gzipOutputStream.write(data, 0, count);
        }
        gzipOutputStream.finish();
        gzipOutputStream.close();
        final long end = System.currentTimeMillis();
        System.out.println("gzip压缩cost: "+ (end-begin)+"ms");
        return outputStream.toByteArray();
    }

    private static byte[] construct() throws IOException {
        /*final MyBloomFilter myBloomFilter = MyBloomFilterTest.initMyBloom();
        final long[] data = myBloomFilter.getData();
        final byte[] bytes = myBloomFilter.getByte();
        if (data.length * 8 != bytes.length){
            System.out.println(data.length);
            System.out.println(bytes.length);
            System.exit(1);
        }*/

        /*for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte)(bytes[i]&0x7F);
        }*/
        Map<String, Long> hashMap = new ConcurrentHashMap<>(2000000);
        for (int i = 0; i < 1000_0000; i++) {
            hashMap.put(6459285550141976699l+i+"", System.currentTimeMillis());
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream);
        outputStream.writeObject(hashMap);
        return byteArrayOutputStream.toByteArray();
        //return push.getBytes(Charsets.UTF_8);
        /*String file = "/Users/young/Desktop/temp/sub_total.cidlist";
        final BufferedReader br = new BufferedReader(new FileReader(new File(file)));
        String cid = null;
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            while ((cid = br.readLine()) != null) {
                final byte[] bytes = cid.getBytes(Charsets.UTF_8);
                bos.write(bytes, 0, bytes.length);
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
        final byte[] bytes = bos.toByteArray();
        final HashMap<Character, Long> hashMap = new HashMap<Character, Long>(1000);
        for (int i = 0; i < bytes.length; i++) {
            final char c = (char) bytes[i];
            if (hashMap.containsKey(c)){
                final long l = hashMap.get(c).longValue() + 1l;
                hashMap.put(c, l);
            }else {
                hashMap.put(c, 1l);
            }
        }
        System.out.println("byte length = "+bytes.length);
        for (Character character : hashMap.keySet()) {
            System.out.println(character+":"+hashMap.get(character));
        }
        return bytes;*/
    }
}

package com.young.compressAndUnCompress;

import com.google.common.base.Charsets;
import com.young.guava.MyBloomFilter;
import com.young.guava.MyBloomFilterTest;

import java.io.*;
import java.util.HashMap;
import java.util.Random;
import java.util.zip.*;

public class Test {
    private static final int _1M = 1024 * 1024;

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

    }

    private static byte[] zlibDecompress(byte[] compressed) throws DataFormatException {
        final long begin = System.currentTimeMillis();
        final Inflater inflater = new Inflater();
        inflater.reset();
        inflater.setInput(compressed);

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(compressed.length);
        final byte[] buff = new byte[_1M];
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
        byte[] buff = new byte[_1M];
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

        final byte[] decompressed = gzipDecompress(compressed);
        System.out.println("gzip解压缩后length=" + decompressed.length);

        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] != decompressed[i]) {
                System.out.println(bytes[i] + "++" + decompressed[i]);
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
        gzipOutputStream.finish();
        gzipOutputStream.close();
        final long end = System.currentTimeMillis();
        System.out.println("gzip压缩cost: "+ (end-begin)+"ms");
        return outputStream.toByteArray();
    }

    private static byte[] construct() throws FileNotFoundException {
        final MyBloomFilter myBloomFilter = MyBloomFilterTest.initMyBloom();
        final long[] data = myBloomFilter.getData();
        final byte[] bytes = myBloomFilter.getByte();
        if (data.length * 8 != bytes.length){
            System.out.println(data.length);
            System.out.println(bytes.length);
            System.exit(1);
        }
        /*for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte)(bytes[i]&0x7F);
        }*/
        return bytes;
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

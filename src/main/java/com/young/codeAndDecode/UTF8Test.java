package com.young.codeAndDecode;


import java.io.UnsupportedEncodingException;

public class UTF8Test {

    public static void main(String[] args) throws UnsupportedEncodingException {
        test();
    }

    private static void test() throws UnsupportedEncodingException {
        //11101000 10110111 10101111 汉子 "路" 的utf-8编码
        //开头是1110 ，说明连续的三个字节表示一个字符， 且形式是 1110xxxx 10yyyyyy 10zzzzzz
        //xxxxyyyy yyzzzzzz 就是这个字符的unicode编码
        //10001101 11101111 = 0x8DEF 就是"路"的unicode编码
        //byte[] bytes = {(byte)232,(byte)183,(byte)175};
        byte[] bytes = {(byte)-126,(byte)33};



        try {
            final String s = new String(bytes, "utf-8");
            System.out.println("s="+s);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String ss = "12";
        final byte[] bytes1 = ss.getBytes("utf-8");
        for (int i = 0; i < bytes1.length; i++) {
            System.out.println(bytes1[i]);
            System.out.println(Integer.toBinaryString(bytes1[i]));
        }

        byte bytes2 = (byte)49;
        System.out.println(Integer.toBinaryString(bytes2));

        int i = -2147418112;
        System.out.println(Integer.toBinaryString(i));
    }

    /**
     *
     */

}

package com.young.jvm.OOM;

/**
 * Created by young on 18/1/24.
 */
public class StackOverflowTest {
    public static void main(String[] args) {
        test();
    }
    public static void test(){
        //int i = 0;
        //i++;
        test();
    }
}

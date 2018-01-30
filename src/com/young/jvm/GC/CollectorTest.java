package com.young.jvm.GC;

/**
 * Created by young on 18/1/8.
 */
public class CollectorTest {
int i = 1_000_000;
    public static void main(String... args){
        //test_1();
        //test_2();
        //test_3();
        //test_4();
        test_5();
    }

    /**
     * -XX:+UseConcMarkSweepGC
     */
    private static void test_5() {
        //par new generation
        //CMS
    }

    /**
     *
     */
    private static void test_4() {
        //PSYoungGen
        //ParOldGen
    }

    /**
     * -XX:+UseSerialGC
     */
    private static void test_3() {
        //def new generation
        //老年代没有特殊标志
    }

    /**
     * -XX:+UseParallelGC
     */
    private static void test_2() {
        //PSYoungGen
        //ParOldGen
    }

    /**
     * -XX:+UseParNewGC
     */
    private static void test_1() {
        //par new generation
        //老年代没有特殊标志
    }
}

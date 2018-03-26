package com.young.jvm.GC;

/**
 * -XX:+PrintSafepointStatistics –XX:PrintSafepointStatisticsCount=1
 */
public class SafePointTest {

    public static void main(String... args){
        int i = 0;
        while (i++<10000){
            if (i==5000){
                break;
            }
        }
        System.out.println("over");
    }
}

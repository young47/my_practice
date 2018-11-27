package com.young.exception;

public class RuntimeExceptionTest {

    public static void main(String[] args){
        test_1();
    }

    private static void test_1() {
        test_2();
    }

    private static void test_2() {
        try {
            int i = 1/0;
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}

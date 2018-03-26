package com.young.jvm.OOM;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * VM args: -Xmx20M -XX:MaxDirectMemorySize=5M
 *
 * error:
 * Exception in thread "main" java.lang.OutOfMemoryError
              at sun.misc.Unsafe.allocateMemory(Native Method)
              at com.young.jvm.OOM.DirectMemoryOOMTest.main(DirectMemoryOOMTest.java:18)
 */
public class DirectMemoryOOMTest {
    private static final int _1MB = 1024 * 1024;

    public static void main(String[] args) throws Exception {
        Field field = Unsafe.class.getDeclaredFields()[0];
        field.setAccessible(true);
        Unsafe unsafe = (Unsafe) field.get(null);
        while (true) {
            unsafe.allocateMemory(_1MB*10);
        }
    }
}

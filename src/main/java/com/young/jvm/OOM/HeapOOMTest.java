package com.young.jvm.OOM;

import java.util.ArrayList;

/**
 * VM args: -Xms20M -Xmx20M -XX:+HeapDumpOnOutOfMemoryError
 * <p>
 *
 *
 * error:  java.lang.OutOfMemoryError: Java heap space
 */
public class HeapOOMTest {
    public static void main(String[] args) {
        ArrayList<Object> objects = new ArrayList<>();
        while (true) {
            objects.add(new HeapOOMTest());
        }
    }
}

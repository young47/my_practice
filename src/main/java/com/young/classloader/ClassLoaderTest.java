package com.young.classloader;

import com.young.thread.volatileTest.Test;
import sun.misc.Launcher;

public class ClassLoaderTest {
    public static void main(String[] args){
        System.out.println(Test.class.getClassLoader());//AppClassLoader

        System.out.println(System.getProperty("sun.boot.class.path"));
        System.out.println(System.getProperty("java.ext.dirs"));
        System.out.println(String.class.getClassLoader());
        System.out.println(Launcher.class.getClassLoader());
    }
}

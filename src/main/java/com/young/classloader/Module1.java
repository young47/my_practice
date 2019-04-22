package com.young.classloader;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Module1 {
    public void hello(){
        System.out.println("I am module1");
        System.out.println(this.getClass().getClassLoader());
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}

package com.young;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;

public class HashCodeTest {
    public static void main(String... args) throws Exception{
        System.out.println("a占用内存大小："+"a".getBytes().length);
        File file = new File("/Users/young/Downloads/temp.txt");
        BufferedReader bf =new BufferedReader(new FileReader(file));
        String cid=null;
        HashSet<String> set = Sets.newHashSet();
        while ((cid=bf.readLine())!=null){
            int index = Math.abs(cid.hashCode() % 10);
            if (index == 1){
                set.add(cid);
            }
        }
        System.out.println(set);
        System.out.println(Joiner.on("|").join(set));

    }
}

package com.young.lamda;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;

public class ParallelTest {
    private static File sF = new File("/Users/young/Desktop/temp/loc_320000.cidlist");
    private static File mF = new File("/Users/young/Desktop/temp/loc_370000.cidlist");
    private static File bF = new File("/Users/young/Desktop/temp/loc_440000.cidlist");
    private static File hF = new File("/Users/young/Desktop/temp/sub_229.cidlist");
    private static List<File> fileList = Lists.asList(sF, mF, new File[]{bF, hF});
//    private static List<File> fileList = Lists.asList(hF, new File[]{});


    public static void main(String[] args) {
        long l = System.currentTimeMillis();
        for (File file : fileList) {
            System.out.println(file.getName() + " lines : " + readFile(file).size());
        }
        long s = System.currentTimeMillis();
        System.out.println("for loop takes : " + (s - l) + "ms");

        fileList.parallelStream().forEach(file -> {
            System.out.println(file.getName() + " lines : " + readFile(file).size());
        });
        long t = System.currentTimeMillis();
        System.out.println("parallelStream() takes : " + (t - s) + "ms");


        fileList.stream().parallel().forEach(file -> {
            System.out.println(file.getName() + " lines : " + readFile(file).size());
        });
        long k = System.currentTimeMillis();
        System.out.println("stream().parallel() takes : " + (k - t) + "ms");

        final Set<String> hSet = Sets.newHashSet();
        final Set<String> bSet = Sets.newHashSet();

        fileList.stream().forEach(file -> {
            Set<String> set = readFile(file);
            System.out.println(file.getName() + " lines : " + set.size());
            if (file.getName().equals("sub_229.cidlist")) hSet.addAll(set);
            if (file.getName().equals("loc_440000.cidlist")) bSet.addAll(set);
        });
        long p = System.currentTimeMillis();
        System.out.println("stream() takes : " + (p - k) + "ms");

        System.out.println(ReduceTest.retain(Lists.asList(hSet, new Set[]{bSet})).size());
        long u = System.currentTimeMillis();
        System.out.println("retainAll() takes : " + (u - p) + "ms");
    }

    public static Set<String> readFile(File file) {
        HashSet<String> set = Sets.newHashSet();
        long cnt = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = "";
            while ((line = br.readLine()) != null) {
                set.add(line);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return set;
    }
}

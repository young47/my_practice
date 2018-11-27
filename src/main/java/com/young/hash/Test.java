package com.young.hash;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Test {
    public static final ConcurrentHashMap<String, Integer> statistics = new ConcurrentHashMap<>(16);

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        /*for (String server : servers) {
            System.out.println(server + " hashCode:" + server.hashCode());
        }
        final MessageDigest md5 = MessageDigest.getInstance("MD5");

        for (String server : servers) {
            md5.update(server.getBytes());
            System.out.println(server + " md5:" + getHash(server));
        }*/
        final HashSet<String> temp = new HashSet<>(1000000);
        //String out = "/Users/young/Desktop/temp/sub_total.cidlist";
        //
        String file = "/Users/young/Desktop/temp/sub_total.cidlist";
        final BufferedReader br = new BufferedReader(new FileReader(new File(file)));
        //final BufferedWriter bw = new BufferedWriter(new FileWriter(new File(out)));
        String cid = null;
        int total = 0;
        try {
            while ((cid = br.readLine()) != null) {
                total++;
                /*cid=cid.split("\t")[0];
                if (temp.contains(cid)){
                    continue;
                }
                bw.write(cid);
                bw.write(System.lineSeparator());
                temp.add(cid);*/
                final String server = ConsistentHashing2.getServer(cid);
                statistics.compute(server, (key, value) -> value == null ? 1 : ++value);
            }
        }catch (Exception e){
            System.out.println("cid="+cid);
            e.printStackTrace();
        }finally {
            try {
                br.close();
                //bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("total : " + total);
        for (String server : statistics.keySet()) {
            System.out.println(server + " cid count : " + statistics.get(server));
        }
        System.out.println(" ");
        final Map<String, List<String>> changed = ConsistentHashing2.getChanged();
        for (String moved : changed.keySet()) {
            System.out.println(moved+" : "+changed.get(moved).size());
        }
    }
}

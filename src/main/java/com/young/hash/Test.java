package com.young.hash;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
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
        int total = 0;
        /*String file = "/Users/young/Desktop/temp/sub_total.cidlist";
        final BufferedReader br = new BufferedReader(new FileReader(new File(file)));
        String cid = null;
        try {
            while ((cid = br.readLine()) != null) {
                total++;
                final String server = ConsistentHashing2.getServer(cid);
                statistics.compute(server, (key, value) -> value == null ? 1 : ++value);
            }
            for (int i = 1; i <= 3000_0000; i++) {
                total++;
                final String server = ConsistentHashing2.getServer(i+"");
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
        }*/
        for (int i = 1; i <= 300_0000; i++) {
            total++;
            final String server = ConsistentHashing2.getServer(i+"");
            statistics.compute(server, (key, value) -> value == null ? 1 : ++value);
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

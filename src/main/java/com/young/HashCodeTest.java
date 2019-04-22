package com.young;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HashCodeTest {
    private static final String file = "/tmp/push-time.dump";
    private static int i = 0;
    public static void main(String... args) throws Exception{
        /*System.out.println("a占用内存大小："+"a".getBytes().length);
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
        System.out.println(Joiner.on("|").join(set));*/
        //Map<String, Integer> map = init();
        System.out.println(test1());
        System.out.println(i);

        //dump(map);

        //undump();

    }

    private static int test1() {
        return i++;
    }

    private static void undump() throws IOException, ClassNotFoundException {
        long begin = System.currentTimeMillis();
        ObjectInputStream inputStream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(new File(file))));
        ConcurrentHashMap map = (ConcurrentHashMap<String, Long>) inputStream.readObject();
        long end = System.currentTimeMillis();
        System.out.println("undump "+map.size()+" cost:"+(end-begin)+"ms");
        System.out.println("map size="+map.size());
    }

    private static void dump(Map<String, Integer> map) throws IOException {
        long begin = System.currentTimeMillis();
        ObjectOutputStream outputStream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(new File(file))));
        outputStream.writeObject(map);
        long end = System.currentTimeMillis();
        System.out.println("dump "+map.size()+" cost:"+(end-begin)+"ms");
    }

    private static Map<String,Integer> init() {
        int num = 1000_0000;
        long begin = System.currentTimeMillis();
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>(20000);
        for (int i = 0; i < num; i++) {
            map.put("6438994208421752841"+i, (int)(System.currentTimeMillis()/1000/60));
        }
        long end = System.currentTimeMillis();
        System.out.println("init "+map.size()+" cost:"+(end-begin)+"ms");
        return map;
    }
}

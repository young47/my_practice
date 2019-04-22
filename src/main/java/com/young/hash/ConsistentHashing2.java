package com.young.hash;

import com.google.common.collect.Lists;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 带虚拟node的一致性hash
 */
public class ConsistentHashing2 extends ConsistentHashing {
    private static TreeMap<Integer, String> virtualNodes = new TreeMap<>();
    //private static TreeMap<Integer, String> nodes = new TreeMap<>();
    private static int VIRTUAL_NODES = 1000;

    private static List<String> newNodes = Lists.newArrayList("server-0","10.16.78.119");

    private static Map<String, List<String>> dataMoved = new ConcurrentHashMap<>(128);
    static {
        for (String server : servers) {
            for (int i = 0; i < VIRTUAL_NODES; i++) {
                final String virtualNodeName = server + "&&VN" + i;
                final int hash = getHash(virtualNodeName);
                virtualNodes.put(hash, virtualNodeName);
            }
        }
        /*for (Integer integer : virtualNodes.keySet()) {
            System.out.print("["+integer+":"+virtualNodes.get(integer)+"] -> ");
        }*/
        /*for (String server : newNodes) {
            //System.out.println(virtualNodes.get(integer)+","+integer);
            final Integer integer = virtualNodes.lowerKey(155733514);
            System.out.println(virtualNodes.get(integer)+","+integer);
        }*/
    }

    public static String getServer(String cid){
        final int hash = getHash(cid);
        final SortedMap<Integer, String> tailMap = virtualNodes.tailMap(hash);
        Integer key = virtualNodes.firstKey();
        if (!tailMap.isEmpty()){
            key = tailMap.firstKey();
        }
        //System.out.println("cid="+cid+" hash="+hash+", server hash="+key);
        final String virtualNode = virtualNodes.get(key);

        final String substring = virtualNode.substring(0, virtualNode.indexOf("&&"));
        //新的节点，说明有数据迁移
        if (newNodes.contains(substring)) {
            //找后一个节点。此时实际上是把后一个节点的数据分流到了新的节点上
            final Integer integer = virtualNodes.higherKey(key);
            final String oldNodeName;
            if (integer != null){
                oldNodeName = virtualNodes.get(integer);
            }else{
                oldNodeName = virtualNodes.firstEntry().getValue();
            }

            String s = oldNodeName.substring(0, oldNodeName.indexOf("&&"))+"->"+substring;
            if (dataMoved.containsKey(s)){
                dataMoved.get(s).add(cid);
            }else{
                final ArrayList<String> list = new ArrayList<String>();
                list.add(cid);
                dataMoved.put(s, list);
            }
        }
        return substring;
    }

    public static Map<String, List<String>> getChanged(){
        return dataMoved;
    }
}

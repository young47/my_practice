/**
 * Project Name: PPushWorker
 * File Name: WorkerThriftImpl.java
 * Package Name: com.sohu.mrd.ppush.dispatch.thrift
 * Date: Jul 22, 201510:25:54 AM
 * Copyright (c) 2015, alexma@sohu-inc.com All Rights Reserved.
 */

package com.young.thrift;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.thrift.TException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class WorkerThriftImpl implements WorkerThrift.Iface {
    private static final Log LOG = LogFactory.getLog("worker");
    private static final Map<String, Integer> map = new ConcurrentHashMap();

    @Override
    public boolean submitPushJob(String objJsonStr) throws TException {
        return true;
    }

    @Override
    public int getPushJobProgress(String msgid) throws TException {
        return 0;
    }

    @Override
    public Map<String, Integer> getAllProgress(List<String> msgIdList) throws TException {
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        final HashMap<String, Integer> hashMap = new HashMap<>();
        msgIdList.stream().forEach(msgId -> {
            int delta = (int)(Math.random()*10);
            if (map.containsKey(msgId)) {
                map.merge(msgId, delta, (oldV, newV) -> (oldV + newV) > 100 ? 100 : oldV + newV);
            } else {
                map.put(msgId, delta);
            }
            hashMap.put(msgId, map.get(msgId));
        });
        System.out.println("thread : " + Thread.currentThread().getName() + ",  map:" + map);
        return hashMap;
    }
}

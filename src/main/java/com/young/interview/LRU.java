package com.young.interview;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRU {
    private LinkedHashMap<String, String> hashMap = new LinkedHashMap(10, 0.75f, true){
        @Override
        protected boolean removeEldestEntry(Map.Entry eldest) {
            return this.size() > 5;
        }
    };
}

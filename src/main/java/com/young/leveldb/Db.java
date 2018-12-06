package com.young.leveldb;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by jamesou on 2018/4/12.
 */
public interface Db<K, V> {

    public Iterator<Map.Entry<K, V>> getIterator();

    public void closeIterator();

    public V get(K k);

    public void put(K k, V k1);

    public void delete(K k);

}

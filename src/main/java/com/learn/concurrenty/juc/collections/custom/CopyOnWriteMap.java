package com.learn.concurrenty.juc.collections.custom;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 为什么jdk中没有实现 CopyOnWriteMap?
 * @ClassName: CopyOnWriteMap
 * @Description: 自定义一个CopyOnWriteMap
 * @Author: lin
 * @Date: 2020/4/20 15:54
 * History:
 * @<version> 1.0
 */
public class CopyOnWriteMap<K,V> implements Map<K,V> {
    private volatile Map<K, V> map;

    private ReentrantLock lock = new ReentrantLock();
    public CopyOnWriteMap(){
        this.init();
    }
    private void init(){
        this.map = new HashMap<>();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return null;
    }

    @Override
    public V put(K key, V value) {
        try {
            lock.lock();
            Map<K,V>  newMap = new HashMap<>(map);
            V val = newMap.put(key, value);
            this.map = newMap;
            return  val;
        }finally {
           lock.unlock();
        }

    }

    @Override
    public V remove(Object key) {
        try {
            //加锁
            lock.lock();
            // 创建一个新的,然后将原来的map放进行去
            Map<K,V>  newMap = new HashMap<>(map);
            V val = newMap.remove(key);
            this.map = newMap;
            return  val;
        }finally {
            lock.unlock();
        }
    }

    /**
     * 也是一个写操作，要加锁
     * @param m
     */
    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        try {
            //加锁
            lock.lock();
            // 创建一个新的,然后将原来的map放进行去
            Map<K,V>  newMap = new HashMap<>(map);
            // 将所有的元素放到这个map中去
            newMap.putAll(m);
            this.map = newMap;
        }finally {
            lock.unlock();
        }
    }

    @Override
    public void clear() {
        try {
            //加锁
            lock.lock();
           map = new HashMap<>();
        }finally {
            lock.unlock();
        }
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<V> values() {
        return map.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return map.entrySet();
    }
}

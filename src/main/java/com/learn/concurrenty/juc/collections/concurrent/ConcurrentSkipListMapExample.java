package com.learn.concurrenty.juc.collections.concurrent;

import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @ClassName: ConcurrentSkipListMapExample
 * @Description:
 * @Author: lin
 * @Date: 2020/4/20 14:27
 * History:
 * @<version> 1.0
 */
public class ConcurrentSkipListMapExample {

    public static  <K,V> ConcurrentSkipListMap<K,V> create(){
        return  new ConcurrentSkipListMap<>();
    }
}

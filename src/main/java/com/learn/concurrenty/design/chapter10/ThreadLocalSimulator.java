package com.learn.concurrenty.design.chapter10;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: ThreadLocalSimulator
 * @Description:
 * @Author: lin
 * @Date: 2020/3/27 17:39
 * History:
 * @<version> 1.0
 */
public class ThreadLocalSimulator<T> {
    private final Map<Thread, T> storage = new HashMap<>();

    public  void set(T t){
        synchronized (this){
            Thread key = Thread.currentThread();
            storage.put(key, t);
        }
    }

    public  T get(){
        synchronized (this){
            Thread key = Thread.currentThread();
            T t = storage.get(key);
            if(t == null){
                return  initialValue();
            }
            return  t;
        }
    }

    public  T initialValue(){
        return null;
    }
}

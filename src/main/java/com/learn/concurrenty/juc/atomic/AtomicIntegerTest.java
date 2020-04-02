package com.learn.concurrenty.juc.atomic;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName: AtomicIntegerTest
 * @Description:
 * @Author: lin
 * @Date: 2020/4/2 12:12
 * History:
 * @<version> 1.0
 */
public class AtomicIntegerTest {
    private static  final Set<Integer> SET = Collections.synchronizedSet(new HashSet<>());
    public static void main(String[] args) throws InterruptedException {

        final AtomicInteger value = new AtomicInteger();
        Thread t1 = new Thread(() -> {
            int x = 0;
            int count = 500;
            while (x < count){
                int v = value.getAndIncrement();
                SET.add(v);
                System.out.println(Thread.currentThread().getName() + ":" + v);
                x++;
            }
        });

        Thread t2 = new Thread(() -> {
            int x = 0;
            int count = 500;
            while (x < count){
                int v = value.getAndIncrement();
                SET.add(v);
                System.out.println(Thread.currentThread().getName() + ":" + v);
                x++;
            }
        });

        Thread t3 = new Thread(()->{
                int x = 0;
                int count = 500;
                while (x < count){
                    int v = value.getAndIncrement();
                    SET.add(v);
                    System.out.println(Thread.currentThread().getName() + ":" + v);
                    x++;
                }
           });

        t1.start();
        t2.start();
        t3.start();
        t1.join();
        t2.join();
        t3.join();
        System.out.println(SET.size());
    }
}

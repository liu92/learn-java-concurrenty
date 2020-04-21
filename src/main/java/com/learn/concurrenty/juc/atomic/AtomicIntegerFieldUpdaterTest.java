package com.learn.concurrenty.juc.atomic;


import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * @ClassName: AtomicIntegerFieldUpdaterTest
 * @Description:
 * @Author: lin
 * @Date: 2020/4/2 16:59
 * History:
 * @<version> 1.0
 */
public class AtomicIntegerFieldUpdaterTest {
    public static void main(String[] args) {
        final AtomicIntegerFieldUpdater<TestMe> updater = AtomicIntegerFieldUpdater.
                newUpdater(TestMe.class, "i");
        TestMe testMe = new TestMe();
        int t = 2 ;
        for (int i = 0; i < t; i++) {
            new Thread(){
                @Override
                public void run() {
                    final  int MAX = 20 ;
                    for (int i = 0; i < MAX; i++) {
                        int v = updater.getAndIncrement(testMe);
                        System.out.println(Thread.currentThread().getName() + "=>"+ v );
                    }
                } 
            };
        }
    }

    static class TestMe{
        volatile int i;
    }
}

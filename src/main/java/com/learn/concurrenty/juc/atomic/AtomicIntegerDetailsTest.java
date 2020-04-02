package com.learn.concurrenty.juc.atomic;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName: AtomicIntegerDetailsTest
 * @Description:
 * @Author: lin
 * @Date: 2020/4/2 14:18
 * History:
 * @<version> 1.0
 */
public class AtomicIntegerDetailsTest {
    public static void main(String[] args) {
        //get and set
//        AtomicInteger  getAndSet = new AtomicInteger(10);
//        int result = getAndSet.getAndAdd(10);
//        System.out.println(result);
//        System.out.println(getAndSet.get());


        AtomicInteger  atomicInteger = new AtomicInteger(10);
        // 将 期望值12和 当前值10 进行比较，在AtomicInteger中 value表示当前值
        // 最快失败
        atomicInteger.compareAndSet(12, 20);
    }

}

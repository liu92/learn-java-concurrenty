package com.learn.concurrenty.juc.atomic;

import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @ClassName: AtomicStampedReferenceTest
 * @Description:
 * @Author: lin
 * @Date: 2020/4/2 23:14
 * History:
 * @<version> 1.0
 */
public class AtomicStampedReferenceTest {
    private static AtomicStampedReference<Integer> atomicStampedReference = new AtomicStampedReference<>(1, 1);
    public static void main(String[] args) {

    }
}

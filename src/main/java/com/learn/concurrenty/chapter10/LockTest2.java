package com.learn.concurrenty.chapter10;

import java.util.Optional;
import java.util.concurrent.TimeoutException;
import java.util.stream.Stream;

/**
 * 测试自定义的锁, 等待锁 释放超时的情况
 * @ClassName: LockTest
 * @Description:
 * @Author: lin
 * @Date: 2020/3/24 12:14
 * History:
 * @<version> 1.0
 */
public class LockTest2 {
    public static void main(String[] args) throws InterruptedException {

        final  BooleanLock bk = new BooleanLock();

        Stream.of("T1", "T2", "T3", "T4")
                .forEach(name ->
                        new  Thread(()->{
                            try {
                                bk.lock(10L);
                                Optional.of(Thread.currentThread().getName() +
                                        " have the lock monitor").ifPresent(System.out::println);
                                work();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (TimeoutException e) {
                                Optional.of(Thread.currentThread().getName()+" time out")
                                        .ifPresent(System.out::println);
                            } finally {
                                bk.unLock();
                            }
                        },name).start()
                );

        //打印结果
//        T1 have the lock monitor
//        T1 is Working....
//        T2 time out
//        T4 time out
//        T3 time out
//        T1 release the lock monitor.

    }

    private static  void work() throws InterruptedException {
        Optional.of(Thread.currentThread().getName() + " is Working....")
        .ifPresent(System.out::println);
        Thread.sleep(10_000);
    }
}



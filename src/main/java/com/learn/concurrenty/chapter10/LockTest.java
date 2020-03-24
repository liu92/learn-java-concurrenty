package com.learn.concurrenty.chapter10;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * 测试自定义的锁, 这种方式存在问题，就是其他线程会争抢到锁
 * @ClassName: LockTest
 * @Description:
 * @Author: lin
 * @Date: 2020/3/24 12:14
 * History:
 * @<version> 1.0
 */
public class LockTest {
    public static void main(String[] args) throws InterruptedException {
        final  BooleanLock booleanLock = new BooleanLock();
        Stream.of("T1", "T2", "T3", "T4")
                .forEach(name ->
                        new  Thread(()->{
                            try {
                                booleanLock.lock();
                                Optional.of(Thread.currentThread().getName() +
                                        " have the lock monitor").ifPresent(System.out::println);
                                work();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }finally {
                                booleanLock.unLock();
                            }
                        },name).start()
                );

        // 这里取非法操作
        Thread.sleep(100);
        // 这里取非法操作,去释放锁，那么这种情况
        // 就会造成 一个线程抢到锁之后 还没有释放了锁，其他的线程也抢到了锁
        // 所以 只有谁加了锁，谁才去释放这个锁
        booleanLock.unLock();
    }

    private static  void work() throws InterruptedException {
        Optional.of(Thread.currentThread().getName() + " is Working....")
        .ifPresent(System.out::println);
        Thread.sleep(10_000);
    }
}



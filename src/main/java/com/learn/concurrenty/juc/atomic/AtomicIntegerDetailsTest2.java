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
public class AtomicIntegerDetailsTest2 {
    private final  static  CompareAndSetLock LOCK = new CompareAndSetLock();
    public static void main(String[] args) {
        for (int i = 0; i <2 ; i++) {
            new Thread(){
                @Override
                public void run() {
                    try {
//                        doSomething();
                        doSomething2();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    private static void doSomething() throws InterruptedException {
        synchronized (AtomicIntegerDetailsTest2.class){
            System.out.println(Thread.currentThread().getName()+" ");
            Thread.sleep(100000);
        }
    }

    /**
     * 通过 compareAndSet 尝试去 获取锁,如果获取不到锁就去做其他的
     * @throws InterruptedException
     */
    private static void doSomething2() throws InterruptedException {
        try {
            LOCK.tryLock();
            System.out.println(Thread.currentThread().getName()+" get the lock");
            Thread.sleep(100000);
        } catch (GetLockException e) {
            e.printStackTrace();
        }finally {
            // 这个地方有问题，就是有的线程没有抢到锁，但是会去释放锁，
            // 使用这个 compareAndSet(1,0) 去进行比较替换处理，其他线程抢到锁之后
            // 已经将其更新，而这时没有抢到锁的线程 就通过 其他线程更新的 value
            // 再来比较 compareAndSet(1,0) 而这时 去比较 期望值和当前值相等，就会将值更新为0
            // 这样就会造成 锁被多个线程拿到， 所以谁拿到锁就是谁去释放
            LOCK.unLock();
        }

    }


}

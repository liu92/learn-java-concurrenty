package com.learn.concurrenty.chapter7;

/**
 *  同步方法 和 synchronized(this)
 * @ClassName: SynchronizedTest
 * @Description:
 * @Author: lin
 * @Date: 2020/3/23 13:35
 * History:
 * @<version> 1.0
 */
public class SynchronizedThis {

    public static void main(String[] args) {
        ThisLock thisLock = new ThisLock();
        Thread thread = new Thread("t1") {
            @Override
            public void run() {
                thisLock.method();
            }
        };
        thread.start();

        Thread thread2 = new Thread("t2") {
            @Override
            public void run() {
                thisLock.method2();
            }
        };
        thread2.start();

        //首先测试，方法method2方法不加上synchronized， 那么这两个线程如何执行呢？
        // 打印结果如下，可以看到这个基本上都是同时执行，这说明 没有同步的方法可以去访问
        //t2没有加锁
        //t1抢占这个锁

        //如果这个方法method2方法加上synchronized， 那么这个两个线程在执行的时候会怎么样？
        //打印的结果是
        // 先 t1抢占这个锁 ，
        // 然后才是 t2抢占这个锁
        // 那么这就是说明了 这个锁是同一个，两个线程谁先争抢到，就先执行，
        // 线程1先抢到了这个锁，那么线程2 就要等待这个 线程1释放了这个锁才能获取。
        // 这里我们可以总结下 同步方法 这个锁就是this.所以多个线程就是抢这个this锁




    }
}

class ThisLock{

    private static  final Object LOCK = new Object();
    public synchronized  void method(){
        try {
            System.out.println(Thread.currentThread().getName() + "抢占这个锁");
            Thread.sleep(10_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized   void method2(){
        try {
            System.out.println(Thread.currentThread().getName() + "抢占这个锁");
            Thread.sleep(10_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 如果方法使用不同的 对象作为锁，那么在执行的时候基本上是同时执行
     * 这个 锁和 同步方法的锁 不一样。 这个显示的指定了对象锁
     */
    public void method3(){
        synchronized (LOCK) {
            try {
                System.out.println(Thread.currentThread().getName() + "抢占这个锁");
                Thread.sleep(10_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

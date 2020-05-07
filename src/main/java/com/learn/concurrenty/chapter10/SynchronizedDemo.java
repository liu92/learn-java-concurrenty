package com.learn.concurrenty.chapter10;

/**
 * @ClassName: SynchronizedDemo
 * @Description:
 * @Author: lin
 * @Date: 2020/4/27 16:00
 * History:
 * @<version> 1.0
 */
public class SynchronizedDemo {
    /**
     * synchronized修饰 ⾮静态⽅法
     * @throws InterruptedException
     */
    public synchronized void function() throws InterruptedException {
        for (int i = 0; i <3; i++) {
            Thread.sleep(1000);
            System.out.println("function running...");
        }
    }

    /**
     * synchronized修饰静态⽅法
     * @throws InterruptedException
     */
    public static synchronized void staticFunction()
            throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            Thread.sleep(1000);
            System.out.println("Static function running...");
        }
    }

    public static void main(String[] args) {
        final SynchronizedDemo demo = new SynchronizedDemo();
        // 创建线程执静态法
        Thread t1 = new Thread(() -> {
            try {
                staticFunction();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        // 创建线程执实例法
        Thread t2 = new Thread(() -> {
            try {
                demo.function();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        // 启动
        t1.start();
        t2.start();
    }

//    结果证明：类锁和对象锁是不会冲突的！
}

package com.learn.concurrenty.chapter7;

/**
 * 测试 synchronized 同步方法，这种方式和 同步块的区别
 * @ClassName: BankVersion1
 * @Description:
 * @Author: lin
 * @Date: 2020/3/20 10:13
 * History:
 * @<version> 1.0
 */
public class BankVersion2 {
    public static void main(String[] args) {

        // 同步方法的锁就是 this, 而这个this就是 SynchronizedRunnable的一个实例
        // 在下面的三个线程 中也是唯一的一个，所以三个线程去争抢这个实例
        final SynchronizedRunnable tr = new SynchronizedRunnable();

        Thread w1 = new Thread(tr, "一号窗口");
        Thread w2 = new Thread(tr, "二号窗口");
        Thread w3 = new Thread(tr, "三号窗口");
        w1.start();
        w2.start();
        w3.start();
    }
}

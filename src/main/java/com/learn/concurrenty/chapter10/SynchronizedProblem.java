package com.learn.concurrenty.chapter10;

import com.learn.concurrenty.chapter7.SynchronizeStatic;

/**
 * 尝试打断 synchronized ，但是实际没有
 * @ClassName: SynchronizedProblem
 * @Description:
 * @Author: lin
 * @Date: 2020/3/24 14:08
 * History:
 * @<version> 1.0
 */
public class SynchronizedProblem {
    /**
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {

        // 这种情况下 如果一个t1线程 一直工作，除非 工作完成
        // t2 线程才有可能 获取到这个锁
        Thread t1 = new Thread(() -> SynchronizedProblem.method());
        t1.start();
        Thread.sleep(1000);

        Thread t2 = new Thread(()-> SynchronizedProblem.method());
        t2.start();
        // 这里假等了一段时候，如果还没有抢到这个锁，那么就不去抢了
        // 去执行其他的逻辑
        Thread.sleep(1000);
        //尝试去打断
        t2.interrupt();
        System.out.println("打断标志"+ t2.isInterrupted());
        //打印结果, 可以看到这种情况 打断不了，所以这种情况 就是超时后 的处理方式
        // 自定义 CustomizeLock 中超时后 ，去处理
//        Thread[Thread-0,5,main]
//        打断标志true
    }

    /**
     * 类锁， 里面一直运行
     */
    private synchronized  static  void method(){
        System.out.println(Thread.currentThread());
        while (true){

        }
    }
}

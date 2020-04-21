package com.learn.concurrenty.chapter6;

import com.learn.concurrenty.DefaultThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程中断
 * @ClassName: ThreadInterrupt
 * @Description:
 * @Author: lin
 * @Date: 2020/3/22 20:40
 * History:
 * @<version> 1.0
 */
public class ThreadInterrupt2 {

    private static Object MONITOR = new Object();

    public static void main(String[] args) {

        /**
         * sleep 方式打断
         */
//        Thread t = new Thread(()->{
//            while (true){
//                try {
//                    Thread.sleep(10);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                    //当前线程
//                    System.out.println("收到打断信号");
//                }
//            }
//        });
//
//        t.start();
//        Thread.sleep(100);
//        System.out.println(t.isInterrupted());
//        t.interrupt();
//        // 打断标志是true, 这个程序没有退出是因为上面写的是死循环
//        System.out.println(t.isInterrupted());


        /**
         * wait 打断
         */
//        Thread t = new Thread(()->{
//            while (true){
//                synchronized (MONITOR){
//                    try {
                         // 这里wait的时候, wait的是当前线程t
                         // 这个时候 做t的 打断 没有问题
//                        MONITOR.wait();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                        //当前线程
//                        System.out.println(Thread.interrupted());
//                    }
//                }
//            }
//        });

        /**
         *
         * join 方式打断
         *  在join 方式中 join的是 main线程
         *  打断的是t 线程, 所以无法打断
         *  如何去打断main 线程？ 只需要获取当前线程 并调用当前线程进行 interrupt
         *   Thread mainThread = Thread.currentThread();
         */
        Thread t = new Thread(()->{
            while (true){

            }
        });

        t.start();

        Thread mainThread = Thread.currentThread();
        Thread t2 = new Thread(){
            @Override
            public void run() {
                try {
                    // 休眠100毫秒, 这个线程的作用就是将t线程打断
                    // 然后看看 是否抛异常
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 这里
                t.interrupt();
                System.out.println("========interrupt============");
            }
        };
        t2.start();


        try {
            // join的时候程序会block 住,这个join要等待子线程执行完后
            // 再执行主线程. 也就等到 t线程执行完后 再去执行try/catch 下面的代码
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}



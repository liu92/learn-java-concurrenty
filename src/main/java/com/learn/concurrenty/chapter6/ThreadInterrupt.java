package com.learn.concurrenty.chapter6;

import com.learn.concurrenty.DefaultThreadFactory;
import com.learn.concurrenty.chapter1.TryConcurrent;

import java.lang.annotation.Native;
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
public class ThreadInterrupt {


    public static void main(String[] args) {
        ExecutorService executorService = new ThreadPoolExecutor(2,
                5, 200,
                TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(6),
                new DefaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        /**
         *  调用 execute方法时，线程池 从工作线程中选一个线程 进行 t.start();
         * 在调用t.start() 方法 后 会去执行 Worker 中的 run方法 ，run方法中会去调用runWorker()方法
         */
//        MyThreadInterrupt myThreadInterrupt = new MyThreadInterrupt();
//        System.out.println("线程中断状态。。。"+ myThreadInterrupt.isInterrupted());
//        executorService.execute(myThreadInterrupt);
//        System.out.println("线程中断状态。。。"+ myThreadInterrupt.isInterrupted());
        /**
         * 使用shutdownNow() 中断线程, 因为在shutdownNow()方法里 将状态设置为 STOP状态，
         * 这种状态在执行runWork方法时，会调用getTask()方法 进行状态的判断然后返回null,导致线程的退出。
         *
         * 我们知道，使用shutdownNow方法，可能会引起报错，使用shutdown方法可能会导致线程关闭不了。
         *
         * 所以当我们使用shutdownNow方法关闭线程池时，一定要对【任务里进行异常捕获】。
         *
         *  当我们使用shutdown方法关闭线程池时，一定要确保任务里不会有永久阻塞等待的逻辑，否则线程池就关闭不了。
         */

//        executorService.shutdownNow();
        //打印结果如下 , 这种方式会去打断这个线程
//        收到打断信号。 pool-1-thread1
//        java.lang.InterruptedException: sleep interrupted
//        at java.lang.Thread.sleep(Native Method)
//        at com.learn.concurrenty.chapter6.MyThreadInterrupt.run(ThreadInterrupt.java:42)
//        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
//        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
//        at java.lang.Thread.run(Thread.java:748)


         Thread t = new Thread(){
             @Override
             public void run() {
                 while (true){

                 }
             }
         };
          t.start();

          Thread mainThread = Thread.currentThread();
        Thread t2 = new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // t.interrupt()并没有打断线程。
                // t.interrupt();
                mainThread.interrupt();
                System.out.println("interrupt");
            }
        };

        t2.start();
        try {
            //t.join他join的是main线程，而不是当前线程，
            // join的是main线程，打断是t线程 所以不能打断，
            // 那么怎么将当前线程打断喃？ 我们将上面的   t.interrupt();
            // 改成 mainThread.interrupt(); 这样就可以打断线程了， 所以会进入下面
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("进入这里。。。。。。。。。");
        }

        // 打印结果
//        interrupt
//        java.lang.InterruptedException
//        at java.lang.Object.wait(Native Method)
//        at java.lang.Thread.join(Thread.java:1252)
//        at java.lang.Thread.join(Thread.java:1326)
//        进入这里。。。。。。。。。
//        at com.learn.concurrenty.chapter6.ThreadInterrupt.main(ThreadInterrupt.java:91)
    }
}

class  MyThreadInterrupt extends Thread{
 private static  final Object MONITOR = new Object();
//
//
//    @Override
//    public void run(){
//        try {
//            Thread.sleep(10);
//        } catch (InterruptedException e) {
//            System.out.println("收到打断信号。 " + Thread.currentThread().getName());
//            e.printStackTrace();
//        }
//
//    }



//    @Override
//    public void run(){
//        while (true) {
//            synchronized (ThreadInterrupt.class) {
//                try {
//                    wait(10);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }
//    }

    //这种运行的结果是
//    Exception in thread "pool-1-thread1" java.lang.IllegalMonitorStateException
//    at java.lang.Object.wait( Native Method)
//    at com.learn.concurrenty.chapter6.MyThreadInterrupt.run(ThreadInterrupt.java:77)
//    at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
//    at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
//    at java.lang.Thread.run(Thread.java:748)


//    @Override
//    public void run(){
//        while (true) {
//            synchronized (MONITOR) {
//                try {
//                    MONITOR.wait(10);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }
//    }

    /**
     * 收到了一个中断信号，这个中断信号也被捕获了
     *  但是这个线程 没有退出，只是设置了一个中断flag,
     *
     */
    // 那么为什么这个线程没有退出喃？ 因为这里是循环 任务没有完成 ，所以这个程序一直在执行

//> Task :ThreadInterrupt.main()
//    java.lang.InterruptedException
//    at java.lang.Object.wait(Native Method)
//    at com.learn.concurrenty.chapter6.MyThreadInterrupt.run(ThreadInterrupt.java:103)
//    at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
//    at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
//    at java.lang.Thread.run(Thread.java:748)
//    java.lang.InterruptedException
//    at java.lang.Object.wait(Native Method)
//    at com.learn.concurrenty.chapter6.MyThreadInterrupt.run(ThreadInterrupt.java:103)
//    at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
//    at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
//    at java.lang.Thread.run(Thread.java:748)




}


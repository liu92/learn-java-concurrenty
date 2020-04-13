package com.learn.concurrenty.juc.untils.cyclibarrier;

import com.learn.concurrenty.DefaultThreadFactory;

import java.util.concurrent.*;

/**
 * @ClassName: CylicBarrierExample1
 * @Description:
 * @Author: lin
 * @Date: 2020/4/9 22:18
 * History:
 * @<version> 1.0
 */
public class CyclicBarrierExample1 {
    private static ExecutorService EXECUTOR_SERVICE = new ThreadPoolExecutor(5,
            20, 200,
            TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(20),
            new DefaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());

    public static void main(String[] args) {
        //可以进行回调
        final CyclicBarrier cyclicBarrier = new CyclicBarrier(2, () -> System.out.println("all of finished"));

        EXECUTOR_SERVICE.execute(new MyThread("T1", cyclicBarrier));
        EXECUTOR_SERVICE.execute(new MyThread("T2", cyclicBarrier));
        EXECUTOR_SERVICE.shutdown();

    }


    static class MyThread extends Thread{
        private String name;
        private CyclicBarrier cyclicBarrier;

        MyThread(String name, CyclicBarrier cyclicBarrier){
            this.name = name;
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(20);
                System.out.println("T1 finished");
                cyclicBarrier.await();
                System.out.println("T1 the other thread finished too.");
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }



}


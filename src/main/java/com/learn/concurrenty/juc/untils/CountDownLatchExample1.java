package com.learn.concurrenty.juc.untils;

import com.learn.concurrenty.DefaultThreadFactory;

import java.util.Random;
import java.util.concurrent.*;

/**
 * @ClassName: CountDownLatchExample1
 * @Description:
 * @Author: lin
 * @Date: 2020/4/8 16:57
 * History:
 * @<version> 1.0
 */
public class CountDownLatchExample1 {
   private static Random random = new Random(System.currentTimeMillis());
   private static CountDownLatch countDownLatch = new CountDownLatch(10);
   private static ExecutorService EXECUTOR_SERVICE = new ThreadPoolExecutor(2,
            2, 200,
            TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(6),
            new DefaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());

    private static ExecutorService EXECUTOR_SERVICE_FIXED = Executors.newFixedThreadPool(2);

    public static void main(String[] args) throws InterruptedException {
        //(1)
        int[] data = query();
        //(2)
        for (int i = 0; i <data.length; i++) {
            //异步执行
            // 什么时候countDownLatch减为零, 就是这个线程工作完了 就可以了
            EXECUTOR_SERVICE_FIXED.execute(new SimpleRunnable(data, i, countDownLatch));
        }
        //(3) 这一步应该等所有动作完成才执行

        // 可以使用shoutDown();但是这个只是打一个标志,
        // 等到所有的线程都空闲了才结束
        //EXECUTOR_SERVICE.shutdown();
        // 也可以使用这个来配合关闭
        //EXECUTOR_SERVICE.awaitTermination(1, TimeUnit.HOURS);

        //使用CountDownLatch, 阻塞,当CountDownLatch里面的计算器数减为0 时就结束
        countDownLatch.await();
        System.out.println("all of work finish done.");
        EXECUTOR_SERVICE_FIXED.shutdown();
    }


    static  class SimpleRunnable implements Runnable{

        private final int[] data;

        private final int index;
        private final  CountDownLatch countDownLatch;

        SimpleRunnable(int[] data, int index, CountDownLatch countDownLatch){
            this.data = data;
            this.index = index;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(random.nextInt(2000));

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int value = data[index];
            if(value % 2 == 0){
                data[index] = value * 2;
            }else{
                data[index] = value * 10;
            }
            System.out.println(Thread.currentThread().getName()+" finished.");
            //线程执行结束
            countDownLatch.countDown();
        }
    }

    private static  int[] query(){
        return  new int[]{1, 2 ,3, 4, 5, 6, 7, 8, 9, 10};
    }
}

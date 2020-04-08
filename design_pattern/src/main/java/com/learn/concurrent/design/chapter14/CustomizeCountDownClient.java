package com.learn.concurrent.design.chapter14;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

/**
 * @ClassName: CustomizeCountDownClient
 * @Description:
 * @Author: lin
 * @Date: 2020/3/28 21:57
 * History:
 * @<version> 1.0
 */
public class CustomizeCountDownClient {
    private static  final Random random = new Random(System.currentTimeMillis());
    public static void main(String[] args) throws InterruptedException {
        final CustomizeCountDown latch = new CustomizeCountDown(5);
        System.out.println("准备多线程处理任务.");
        IntStream.rangeClosed(1, 5).forEach(t ->
                new Thread(()->{
                    System.out.println(Thread.currentThread().getName() + "is working");
                    try {
                        Thread.sleep(random.nextInt(100));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    latch.down();
                }, String.valueOf(t)).start()
        );

        //等待某种请求到达，然后再执行
        latch.await();
        System.out.println("多线程任务全部结束，准备第二阶段任务");
        System.out.println("...............");
        System.out.println("FINISH");
    }
}

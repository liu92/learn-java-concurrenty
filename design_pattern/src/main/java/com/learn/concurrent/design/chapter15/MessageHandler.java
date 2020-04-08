package com.learn.concurrent.design.chapter15;

import com.learn.concurrent.design.DefaultThreadFactory;

import java.util.Random;
import java.util.concurrent.*;

/**
 * 这种设计 常用在服务端
 * @ClassName: MessageHandler
 * @Description:
 * @Author: lin
 * @Date: 2020/3/28 22:05
 * History:
 * @<version> 1.0
 */
public class MessageHandler {

    private static  final Random RANDOM = new Random(System.currentTimeMillis());

    /**
     * 创建线程池
     */
    private  final  static ExecutorService executorService = new ThreadPoolExecutor(2,
            5, 200,
            TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(6),
            new DefaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());

    /**
     * 线程的不断创建和销毁是非常好资源和耗时的
     * @param message
     */
     void  request(Message message){
        new Thread(()->{
            String value = message.getValue();
            System.out.println("The message will be handle by " + Thread.currentThread().getName());
        }).start();
    }

    /**
     * 使用线程池来实现
     * @param message
     */
    void  requestExecutor(Message message){
        executorService.execute(()->{
            String value = message.getValue();
            try {
                Thread.sleep(RANDOM.nextInt(1000));
                System.out.println("The message will be handle by " + Thread.currentThread().getName() + " " +value);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

    }

    void  shutDown(){
        executorService.shutdown();
    }
}

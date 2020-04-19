package com.learn.concurrenty.juc.blocking;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.SynchronousQueue;

/**
 * @ClassName: SynchronousQueueExample
 * @Description:
 * @Author: lin
 * @Date: 2020/4/17 17:31
 * History:
 * @<version> 1.0
 */
public class SynchronousQueueExample {
    public static <T> SynchronousQueue<T> creat(){
        return  new SynchronousQueue<>();
    }
}

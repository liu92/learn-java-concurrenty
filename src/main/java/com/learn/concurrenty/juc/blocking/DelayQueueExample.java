package com.learn.concurrenty.juc.blocking;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.SynchronousQueue;

/**
 *
 * 1.The delay queue will ordered by expired time? yes
 * 2.When poll the empty delay queue will return null? use take?
 * 3.when less  the expire time will return quickly?  yes
 * 4.Even though unexpired elements cannot be remove using {@code take} or {@code poll}
 * 5.This queue does not permit null elements.
 * 6.Use iterator can return quickly?
 *
 * NOTICE: The DelayQueue elements must implement the {@link java.util.concurrent.Delayed}
 * The DelayQueue is a unbounded queue.
 * @ClassName: DelayQueueExample
 * @Description:
 * @Author: lin
 * @Date: 2020/4/17 17:31
 * History:
 * @<version> 1.0
 */
public class DelayQueueExample {
    public static <T extends Delayed> DelayQueue<T> creat(){
        return  new DelayQueue<>();
    }
}

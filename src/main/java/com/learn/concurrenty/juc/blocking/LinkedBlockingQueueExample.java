package com.learn.concurrenty.juc.blocking;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @ClassName: LinkedBlockingQueueExample
 * @Description:
 * @Author: lin
 * @Date: 2020/4/17 14:41
 * History:
 * @<version> 1.0
 */
public class LinkedBlockingQueueExample {

    /**
     * 1.FIFO(first in first out)
     * 2.once created , the capacity cannot be changed.
     * @param size
     * @param <T>
     * @return
     */
    public <T> LinkedBlockingQueue<T> creat(int size){
        return  new LinkedBlockingQueue<>(size);
    }
}

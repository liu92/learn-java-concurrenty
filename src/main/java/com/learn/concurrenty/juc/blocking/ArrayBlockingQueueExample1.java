package com.learn.concurrenty.juc.blocking;

        import java.util.concurrent.ArrayBlockingQueue;

/**
 * @ClassName: ArrayBlockingQueueExample1
 * @Description:
 * @Author: lin
 * @Date: 2020/4/17 14:41
 * History:
 * @<version> 1.0
 */
public class ArrayBlockingQueueExample1 {

    /**
     * 1.FIFO(first in first out)
     * 2.once created , the capacity cannot be changed.
     * @param size
     * @param <T>
     * @return
     */
    public <T> ArrayBlockingQueue<T> creat(int size){
        return  new ArrayBlockingQueue<>(size);
    }
}

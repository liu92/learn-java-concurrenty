package com.learn.concurrenty.juc.blocking;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.LinkedTransferQueue;

/**
 * LinkedTransferQueue 担保 递出去的元素必须被消费
 *
 * @ClassName: LinkedTransferQueueExample
 * @Description:
 * @Author: lin
 * @Date: 2020/4/17 17:31
 * History:
 * @<version> 1.0
 */
public class LinkedTransferQueueExample {
    public static <T > LinkedTransferQueue<T> creat(){
        return  new LinkedTransferQueue<>();
    }
}

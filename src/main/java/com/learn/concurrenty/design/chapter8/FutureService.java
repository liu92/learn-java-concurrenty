package com.learn.concurrenty.design.chapter8;

import java.util.function.Consumer;

/**
 * 中间层，将Future和 FutureTask 连接起来
 * @ClassName: FutureService
 * @Description:
 * @Author: lin
 * @Date: 2020/3/27 14:52
 * History:
 * @<version> 1.0
 */
public class FutureService {

    public  <T> Future<T> submit(final  FutureTask<T> task){
        //异步调用
        AsyncFuture<T> asyncFuture = new AsyncFuture<>();
        //在调用get方法时候，怎么知道 我工作已经完成了呢？
        new Thread(() -> {
            // 启动线程 这个线程里面去调用call方法
                T result = task.call();
                //然后将结果异步通知
                asyncFuture.done(result);
        }).start();
        return  asyncFuture;
    }

    /**
     * 比如：当你的蛋糕做完之后, 你不想等,
     * 那么就给个电话或者地址, 让其送货上门
     * 这里的consumer就是 地址或者电话
     * @param task
     * @param consumer
     * @param <T>
     * @return
     */
    public  <T> Future<T> submit(final  FutureTask<T> task, Consumer<T> consumer){
        //异步调用
        AsyncFuture<T> asyncFuture = new AsyncFuture<>();
        //在调用get方法时候，怎么知道 我工作已经完成了呢？
        new Thread(() -> {
            // 启动线程 这个线程里面去调用call方法
            T result = task.call();
            //然后将结果异步通知
            asyncFuture.done(result);
            consumer.accept(result);
        }).start();
        return  asyncFuture;
    }

}

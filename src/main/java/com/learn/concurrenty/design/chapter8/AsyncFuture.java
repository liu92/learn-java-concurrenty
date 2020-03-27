package com.learn.concurrenty.design.chapter8;

/**
 * @ClassName: AsynFuture
 * @Description: 异步Future实现
 * @Author: lin
 * @Date: 2020/3/27 14:54
 * History:
 * @<version> 1.0
 */
public class AsyncFuture<T> implements  Future<T> {
    /**
     * 判断是否结束
     */
    private volatile  boolean done = false;

    private T result;

    public  void done(T result){
        synchronized (this){
            this.result = result;
            this.done = true;
            this.notifyAll();
        }
    }

    @Override
    public T get() throws InterruptedException {
        synchronized (this){
            while (!done){
                //如果还没有结果 ，那么只能让其wait
                this.wait();
            }
            //如果完成了 那么就返回结果
        }
        return result;
    }
}

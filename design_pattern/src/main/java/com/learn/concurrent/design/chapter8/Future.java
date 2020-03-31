package com.learn.concurrent.design.chapter8;

/**
 * 异步的方法拿取结果, 返回任意类型的结果
 * @ClassName: Future
 * @Description:
 * @Author: lin
 * @Date: 2020/3/27 14:47
 * History:
 * @<version> 1.0
 */
public interface Future<T>{
    /**
     * 通过get 真正得到结果
     * @return
     * @throws InterruptedException
     */
    T get()throws  InterruptedException;


}

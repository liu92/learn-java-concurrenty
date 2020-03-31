package com.learn.concurrent.design.chapter8;

/**
 *
 * @ClassName: FutureTask
 * @Description:
 * @Author: lin
 * @Date: 2020/3/27 14:49
 * @History:
 * @<version> 1.0
 */
public interface FutureTask<T> {
    /**
     * 去做事情
     * @return
     */
    T call();
}

package com.learn.concurrent.design.chapter4;

import java.util.Arrays;

/**
 * @ClassName: ThreadLifeCycleClient
 * @Description:
 * @Author: lin
 * @Date: 2020/3/26 16:52
 * History:
 * @<version> 1.0
 */
public class ThreadLifeCycleClient {
    public static void main(String[] args) {
        new ThreadLifeCycleObserver().concurrentQuery(Arrays.asList("1","2"));
    }
}

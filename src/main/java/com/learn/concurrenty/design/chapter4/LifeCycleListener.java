package com.learn.concurrenty.design.chapter4;

/**
 * @ClassName: LifeCyleListener
 * @Description:
 * @Author: lin
 * @Date: 2020/3/26 16:05
 * History:
 * @<version> 1.0
 */
public interface LifeCycleListener {

    void onEvent(AbstractObserverRunnable.RunnableEvent event);
}

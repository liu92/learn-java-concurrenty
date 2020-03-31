package com.learn.concurrent.design.chapter18;

/**
 * @ClassName: ActiveObjectFactory
 * @Description:
 * @Author: lin
 * @Date: 2020/3/30 16:57
 * History:
 * @<version> 1.0
 */
public final class ActiveObjectFactory {
    private ActiveObjectFactory(){

    }

    public static  ActiveObject creatActiveObject(){
        Servant servant = new Servant();
        ActivationQueue queue = new ActivationQueue();
        SchedulerThread schedulerThread = new SchedulerThread(queue);
        ActiveObjectProxy proxy = new ActiveObjectProxy(schedulerThread, servant);
        schedulerThread.start();
        return  proxy;
    }























}

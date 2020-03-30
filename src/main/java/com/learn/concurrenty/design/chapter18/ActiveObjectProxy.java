package com.learn.concurrenty.design.chapter18;

/**
 * @ClassName: ActiveObjectProxy
 * @Description:
 * @Author: lin
 * @Date: 2020/3/30 16:51
 * History:
 * @<version> 1.0
 */
 class ActiveObjectProxy implements ActiveObject {
    private final SchedulerThread schedulerThread;
    private final Servant servant;

    ActiveObjectProxy(SchedulerThread schedulerThread, Servant servant){
        this.schedulerThread = schedulerThread;
        this.servant = servant;
    }

    @Override
    public Result makeString(int count, char fillChar) {
        FutureResult future = new FutureResult();
        schedulerThread.invoke(new MakeStringRequest(servant, future, count, fillChar));
        return future;
    }

    @Override
    public void displayString(String text) {
       schedulerThread.invoke(new DisplayStringRequest(servant, text));
    }
}

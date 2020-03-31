package com.learn.concurrent.design.chapter18;

/**
 * 相当于执行异步方法的一个中间桥梁了
 * @ClassName: SchedulerThread
 * @Description:
 * @Author: lin
 * @Date: 2020/3/30 16:26
 * History:
 * @<version> 1.0
 */
public class SchedulerThread extends Thread {
    private  final ActivationQueue activationQueue;

    SchedulerThread(ActivationQueue activationQueue){
        this.activationQueue = activationQueue;
    }

    /**
     * 被另外一个线程放到 activationQueue里面去
     * @param request
     */
    void invoke(MethodRequest request){
        this.activationQueue.put(request);
    }

    @Override
    public void run() {
        while (true){
            // SchedulerThread本身也是个线程会不断的从activationQueue队列中拿取 执行
            activationQueue.take().execute();
        }
    }
}

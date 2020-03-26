package com.learn.concurrenty.design.chapter4;

import java.util.List;

/**
 * @ClassName: ThreadLifeCycleObserver
 * @Description:
 * @Author: lin
 * @Date: 2020/3/26 16:17
 * History:
 * @<version> 1.0
 */
public class ThreadLifeCycleObserver implements  LifeCycleListener {

    private final Object LOCK = new Object();

    /**
     * 批量查询
     * @param ids
     */
    public void concurrentQuery(List<String> ids){
        if(ids == null || ids.isEmpty()){
            return;
        }
        ids.stream().forEach(id -> new Thread(new AbstractObserverRunnable(this) {
            @Override
            public void run() {
                try {
                    notifyChange(new RunnableEvent(RunnableState.RUNNING,
                            Thread.currentThread(), null));
                    System.out.println("query for the id " + id);
                    Thread.sleep(1000L);
                    notifyChange(new RunnableEvent(RunnableState.DONE,
                            Thread.currentThread(), null));
                } catch (InterruptedException e) {
                    notifyChange(new RunnableEvent(RunnableState.ERROR,
                            Thread.currentThread(), null));
                }
            }
            //id相当于线程的名字了
         },id).start());
    }

    /**
     * 事件回调
     * @param event
     */
    @Override
    public void onEvent(AbstractObserverRunnable.RunnableEvent event) {
         synchronized (LOCK){
             System.out.println("The runnable [" + event.getThread().getName() +"] data changed and state is" );
             if(event.getCause() !=null){
                 System.out.println("The runnable ["+ event.getThread().getName() +"] process failed");
                 event.getCause().printStackTrace();
             }
         }
    }
}

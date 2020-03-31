package com.learn.concurrent.design.chapter4;

/**
 * @ClassName: AbstractObserverRunnable
 * @Description:
 * @Author: lin
 * @Date: 2020/3/26 16:04
 * History:
 * @<version> 1.0
 */
public abstract class AbstractObserverRunnable implements Runnable{
    final protected LifeCycleListener listener;

    public AbstractObserverRunnable(final  LifeCycleListener listener){
        this.listener = listener;
    }

    /**
     * 通知改变
     */
    protected void notifyChange(final RunnableEvent event){
        listener.onEvent(event);
    }

    public enum  RunnableState{
        /**
         * 运行
         */
        RUNNING,
        /**
         * 错误
         */
        ERROR ,DONE;
    }

    /**
     * 事件
     */
    public static  class RunnableEvent{
        private final RunnableState state;
        private final Thread thread;
        private final Throwable cause;

        public RunnableEvent(RunnableState state, Thread thread, Throwable cause) {
            this.state = state;
            this.thread = thread;
            this.cause = cause;
        }

        public RunnableState getState() {
            return state;
        }

        public Thread getThread() {
            return thread;
        }

        public Throwable getCause() {
            return cause;
        }
    }
}

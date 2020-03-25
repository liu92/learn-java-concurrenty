package com.learn.concurrenty.chapter13;

import javafx.concurrent.Worker;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @ClassName: SimpleThreadPool
 * @Description: 简单版 线程池
 * @Author: lin
 * @Date: 2020/3/25 10:42
 * History:
 * @<version> 1.0
 */
public class SimpleThreadPool {
    private  final int size;

    /**
     * 默认大小, 这个线程池 有10个线程
     */
    private static  final  int DEFAULT_SIZE = 10;

    /**
     * 定义线程的时候，让其自增
     */
    private static volatile int seq = 0;

    /**
     * 线程前缀
     */
    private static final  String THREAD_PREFIX ="SIMPLE_THREAD_POOL-";

    /**
     * 线程前缀
     */
    private static final  ThreadGroup GROUP = new ThreadGroup("Pool_Group");
    /**
     * 队列
     */
    private final  static LinkedList<Runnable> TASK_QUEUE = new LinkedList<>();

    /**
     * 工作任务
     */
    private final static List<WorkerTask>  THREAD_QUEUE = new ArrayList<>();

    public  SimpleThreadPool(){
       this(DEFAULT_SIZE);
    }

    public  SimpleThreadPool(int size){
        this.size = size;
        init();
    }

    /**
     * 初始化 的时候去 帮你创建线程
     */
    private  void init(){
        for (int i = 0; i <size ; i++) {
            createWorkTask();
        }
    }

    public void submit(Runnable runnable){
        //这里就是往TASK_QUEUE 添加任务
        // 因为有对其操作，而这读取操作是 同步的，所以写操作也是同步的
        synchronized (TASK_QUEUE){
            TASK_QUEUE.addLast(runnable);
            // 添加任务到队列中后，去通知那些等待的那些线程
            TASK_QUEUE.notifyAll();
        }
    }

    /**
     * 在提交任务前要先去构建
     */
    private void createWorkTask(){
      WorkerTask workerTask = new WorkerTask(GROUP, THREAD_PREFIX+(seq++));
       workerTask.start();
      //启动完成后，将其放到一个list中去
        THREAD_QUEUE.add(workerTask);
    }

    private enum  TaskState{
        /**
         * 什么都没有做
         */
        FREE,
        /**
         * 运行
         */
        RUNNING,
        /**
         * 阻塞
         */
        BLOCKED,
        /**
         * 死亡
         */
        DEAD,
    }

    /**
     * 这个定义为private 就是不想暴露给别人
     */
    private static class WorkerTask extends  Thread{

        private volatile TaskState taskState = TaskState.FREE;

        private WorkerTask(ThreadGroup threadGroup, String name){
            super(threadGroup, name);
        }

        public TaskState getTaskState(){
            return  this.taskState;
        }

        /**
         *  执行完任务不能让核心线程数挂掉
         *  如果挂掉了就需要重新起创建线程然后再启动， 这样线程池就没有意义了
         *  ,所以去继承下父类的构造函数，WorkerThread(ThreadGroup threadGroup, String name)
         *
         */
        @Override
        public void run() {
            OUT:
            // 在执行run方法时，我们需要判断这个线程池中的线程的状态
            while (this.taskState!= TaskState.DEAD){
                Runnable runnable;

                //如果线程不等于 DEAD，那么要去队列中取任务 来执行
                // 看看这个队列中有没有提交的任务
                synchronized (TASK_QUEUE){
                    // 将这个TASK_QUEUE 来作为锁，
                    // 这个每一个去这队列中取时候要等待 其他的线程释放锁
                    while (TASK_QUEUE.isEmpty()){
                        // 如果这个队列取出来是空的，也就是没有人提交任务
                         // 这是就让其等
                        try {
                            //等待是将其状态更新
                            taskState =TaskState.BLOCKED;
                            TASK_QUEUE.wait();
                        } catch (InterruptedException e) {
//                            e.printStackTrace();
                            //别人在停止这个线程时，要退出，这个时候退出到哪里
                            // 呢？ 所以这里要加一个标志
                            break  OUT;

                        }

                    }
                    //当被唤醒 后，要从队列中拿出任务
                     runnable = TASK_QUEUE.removeFirst();
                }

                if(runnable!=null){
                    //如果队列不为null,那就运行run,执行的时候将状态更新
                    taskState =TaskState.RUNNING;
                    runnable.run();
                    // 运行完后 状态再次更新
                    taskState =TaskState.FREE;
                }

            }

        }

        /**
         * 关闭线程
         */
        public void close(){
            this.taskState = TaskState.DEAD;
        }
    }

    public static void main(String[] args) {
        SimpleThreadPool simpleThreadPool = new SimpleThreadPool();
        //这里启动40个线程
        IntStream.rangeClosed(0, 40).forEach(i ->{
            simpleThreadPool.submit(()->{
                System.out.println("The runnable" + i + "be serviced by "+ Thread.currentThread()+" start");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("The runnable" + i + "be serviced by "+ Thread.currentThread()+".finished");
            });
        });
    }
}

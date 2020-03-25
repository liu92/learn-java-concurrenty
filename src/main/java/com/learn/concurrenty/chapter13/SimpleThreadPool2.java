package com.learn.concurrenty.chapter13;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.IntStream;

/**简单版 线程池 这个里面增加拒绝策略
 * @ClassName: SimpleThreadPool2
 * @Description:
 * @Author: lin
 * @Date: 2020/3/25 10:42
 * History:
 * @<version> 1.0
 */
public class SimpleThreadPool2 {
    private  final int size;


    private  final int queueSize ;
    /**
     * 默认大小, 这个线程池 有10个线程
     */
    private static  final  int DEFAULT_SIZE = 10;

    /**
     * 工作队列中最大数，超过这个数 就进行拒绝策略
     */
    private static final  int DEFAULT_TASK_QUEUE_SIZE = 2000;


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
     * 工作线程
     */
    private final static List<WorkerTask>  THREAD_QUEUE = new ArrayList<>();

    private final DiscardPolicy discardPolicy;

    public final  static DiscardPolicy  DEFAULT_DISCARD_POLICY = ()->{
        throw  new DiscardException("Discard This Task. ");
    };

    private volatile boolean destroy = false;

    public SimpleThreadPool2(){
       this(DEFAULT_SIZE, DEFAULT_TASK_QUEUE_SIZE, DEFAULT_DISCARD_POLICY);
    }

    public SimpleThreadPool2(int size, int queueSize, DiscardPolicy discardPolicy){
        this.size = size;
        this.queueSize = queueSize;
        this.discardPolicy = discardPolicy;
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
        //在提交任务的时候判断是否是destroy
        if(destroy){
            throw  new IllegalStateException("The thread pool already destroy and not allow submit task");
        }
        //这里就是往TASK_QUEUE 添加任务
        // 因为有对其操作，而这读取操作是 同步的，所以写操作也是同步的
        synchronized (TASK_QUEUE){
            //执行拒绝策略
            if(TASK_QUEUE.size() > queueSize){
                discardPolicy.rejected();
            }
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

    public void shutDown() throws InterruptedException {
        //现在停止线程 就是让 工作线程将工作做完,
        // 如果发现没有任何工作需要做，那么就停止
        while (!TASK_QUEUE.isEmpty()){
            // 如果这个队列 不是空的，那么稍微休息下
            Thread.sleep(50);
        }

         // 这里要去看看 这个 工作队列中还有多少个线程
        int initVal = THREAD_QUEUE.size();
        while (initVal > 0){
            for (WorkerTask task: THREAD_QUEUE) {
                if(task.getTaskState() == TaskState.BLOCKED){
                    //这里取打断一下 ，那么在WorkerTask中run方法会收到一个中断信号
                    // 当收到信号后 会从OUT这个标志中 退出，也就是它的生命周期就结束了
                    // 就是工作线程的生命周期就结束了
                    task.interrupt();
                    // 调用方法将其状态变更
                    task.close();
                    initVal --;
                }else{
                    Thread.sleep(10);
                }
            }
            //  在干掉线程时候，变更destroy
            this.destroy =true;
            System.out.println("The thread pool disposed.");
        }
    }

    public int getSize() {
        return size;
    }

    /**
     * 将这个开放出去，让外面可以知道 大小
     * @return
     */
    public int getQueueSize() {
        return queueSize;
    }

    public boolean isDestroy(){
        //查看状态，如果是某种状态那么就不在
        return this.destroy ;
    };

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
     * 定义拒绝策略异常
     */
    public static class DiscardException extends RuntimeException{
        public DiscardException(String message){
            super(message);
        }
    }

    public interface DiscardPolicy{
        /**
         * 拒绝策略
         * @throws DiscardException
         */
        void  rejected() throws DiscardException;
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
                            taskState = TaskState.BLOCKED;
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
                    taskState = TaskState.RUNNING;
                    runnable.run();
                    // 运行完后 状态再次更新
                    taskState = TaskState.FREE;
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

    public static void main(String[] args) throws InterruptedException {

        //这个设置 去测试拒绝策略
//        SimpleThreadPool2 threadPool2 = new SimpleThreadPool2(6,
//                10 , SimpleThreadPool2.DEFAULT_DISCARD_POLICY);
        //这个设置测试 关闭线程池
        SimpleThreadPool2 threadPool2 = new SimpleThreadPool2();
        //这里启动40个线程
        IntStream.rangeClosed(0, 40).forEach(i ->{
            threadPool2.submit(()->{
                System.out.println("The runnable be serviced by "+ Thread.currentThread()+" start");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("The runnable be serviced by "+ Thread.currentThread()+".finished");
            });
        });

        //等待任务提交，等待10s
        Thread.sleep(10000);
        //这个去关闭线程池
        threadPool2.shutDown();

        // 线程池停止后，再去提交任务
        // 就会报错 ,报错线程池已经销毁不能提交任务了
        threadPool2.submit(()-> System.out.println("==============="));
    }
}

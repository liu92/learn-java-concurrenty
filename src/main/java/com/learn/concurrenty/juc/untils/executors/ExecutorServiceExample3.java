package com.learn.concurrenty.juc.untils.executors;

import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * @ClassName: ExecutorServiceExample
 * @Description:
 * @Author: lin
 * @Date: 2020/4/13 14:01
 * {@link ExecutorService}
 * History:
 * @<version> 1.0
 */
public class ExecutorServiceExample3 {
    public static void main(String[] args) throws InterruptedException {
      /*test();*/
      /* testAllowCoreThreadTimeOut();*/
      /*testRemove();*/
       /*testPreStartCoreThread();*/
        testThreadPoolAdvice();
    }


    /**
     *
     * @throws InterruptedException
     */
    private static void test() throws InterruptedException {
        ThreadPoolExecutor executorService = (ThreadPoolExecutor)Executors.newFixedThreadPool(5);
        System.out.println(executorService.getActiveCount());
        executorService.execute(()->{
            try {
                // 这个任务运行结束，也不会结束整个操作，因为它里面已经有活动的线程。
                //
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        //休息20毫秒
        TimeUnit.MICROSECONDS.sleep(20);
        // 这个再来看看activeCount, 是一次性创建完 还是又创建了一个
        System.out.println(executorService.getActiveCount());
    }

    /**
     *  {@link ThreadPoolExecutor#allowCoreThreadTimeOut(boolean)}
     */
    private static void testAllowCoreThreadTimeOut(){
        // 比如执行五个任务， 超过五个任务 它就会有五个任务在里面，如果不进行释放它是永远也不会进行释放的
        // 虽然keepAliveTime 是0 ， 他需要去维持这个 coreSize的大小
        ThreadPoolExecutor executorService = (ThreadPoolExecutor)Executors.newFixedThreadPool(5);

        //设置keepAliveTime 时间
        executorService.setKeepAliveTime(10, TimeUnit.SECONDS);

        // 设置这个就会回收空闲线程吗？
        // 设置这个属性后，会出现 Exception in thread "main" java.lang.IllegalArgumentException: Core threads must have nonzero keep alive times，
        // 说明这个 这个keepAliveTime 不能为0, 所以在上面设置下时间,
        // 设置后 会自动回收，我们都可以不用去调用 shutdown了，
        // 设置了10s时间后 这个线程池里面的所以线程会被回收，并且这个线程池达到了terminated状态
        executorService.allowCoreThreadTimeOut(true);
        IntStream.range(0, 5).boxed().forEach(i ->{
            executorService.execute(()->{
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        });

    }

    /**
     * {@link ThreadPoolExecutor#remove(Runnable)}
     * @throws InterruptedException
     */
    private static void testRemove() throws InterruptedException {
        ThreadPoolExecutor executorService = (ThreadPoolExecutor)Executors.newFixedThreadPool(2);
        executorService.setKeepAliveTime(10, TimeUnit.SECONDS);
        executorService.allowCoreThreadTimeOut(true);
        IntStream.range(0, 5).boxed().forEach(i ->{
            // 这个是主线程 main 去调用
            // cpu 执行权， execute 会立即去创建线程 因为是异步的
            executorService.execute(()->{
                try {
                    TimeUnit.SECONDS.sleep(5);
                    System.out.println("===========i am finished========");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        });
        // 这里休眠 是为了确保 提交的任务在 它之前
        TimeUnit.MICROSECONDS.sleep(50);
        // 休眠就是让下面的线程不在 上面的线程执行的前面
        // 理论上下面的打印应该被执行，但是这个queue中任务被我们删除掉了
        Runnable runnable = ()->{
            System.out.println("i will never be executed!");
        };
        // 放入到queue 里面去
        executorService.execute(runnable);
        TimeUnit.MICROSECONDS.sleep(20);
        // 永远也不会被执行，因为被删掉了
    }

    /**
     * {@link ThreadPoolExecutor#prestartCoreThread()}
     */
    private static  void  testPreStartCoreThread(){
        // 初始化的时候 是0
        ThreadPoolExecutor executorService = (ThreadPoolExecutor)Executors.newFixedThreadPool(2);
        System.out.println(executorService.getActiveCount());

        System.out.println(executorService.prestartCoreThread());
        System.out.println(executorService.getActiveCount());

        // 不过调用多少次都不会超过coreSize的大小
        System.out.println(executorService.prestartCoreThread());
        System.out.println(executorService.getActiveCount());
        executorService.execute(()->
                {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        );

        executorService.execute(()->
                {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        );

        System.out.println(executorService.prestartCoreThread());
        System.out.println(executorService.getActiveCount());
    }


    /**
     * {@link ThreadPoolExecutor#prestartAllCoreThreads()}
     */
    private static  void  testPreStartAllCoreThread(){
        // 初始化的时候 是0
        ThreadPoolExecutor executorService = (ThreadPoolExecutor)Executors.newFixedThreadPool(2);
        executorService.setMaximumPoolSize(3);
        System.out.println(executorService.getActiveCount());
        // 预启动
        int num = executorService.prestartAllCoreThreads();
        System.out.println(num);
        // 启动结果还是 只有两个，并不没有到达max, 他只是按照coreSize来启动
        System.out.println(executorService.getActiveCount());

       /* System.out.println(executorService.prestartCoreThread());
        System.out.println(executorService.getActiveCount());

        System.out.println(executorService.prestartCoreThread());
        System.out.println(executorService.getActiveCount());
        executorService.execute(()->
                {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        );

        executorService.execute(()->
                {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        );

        System.out.println(executorService.prestartCoreThread());
        System.out.println(executorService.getActiveCount());*/
    }


    /**
     * 提交一批任务 有几个完成了，成不成功就不知道了
     */
    private static void testGetCompletedTaskCount(){}



    private static  void  testThreadPoolAdvice(){
        ExecutorService executorService = new MyThreadPoolExecutor(1,
                2, 30,
                TimeUnit.SECONDS, new ArrayBlockingQueue<>(1),
                r ->{
                    Thread t = new Thread(r);
                    return  t;
                }, new ThreadPoolExecutor.AbortPolicy());

        executorService.execute(new AbstractMyRunnable(1) {
            @Override
            public void run() {
                System.out.println("=======================");
            }
        });
    }


    private abstract static class AbstractMyRunnable implements Runnable{
        private final int no;

        protected AbstractMyRunnable(int no){
            this.no = no;
        }

        protected int getData(){
            return  this.no;
        }

    }



    private static class MyThreadPoolExecutor extends ThreadPoolExecutor{

        public MyThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        }

        @Override
        protected void beforeExecute(Thread t, Runnable r) {
            System.out.println("init the " + ((AbstractMyRunnable)r).getData());
        }

        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            if(null == t){
                System.out.println("successful " + ((AbstractMyRunnable)r).getData());
            }else {
                t.printStackTrace();
            }
        }
    }
}

package com.learn.concurrenty.juc.untils.executors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @ClassName: ExecutorsExample
 * @Description:
 * @Author: lin
 * @Date: 2020/4/13 9:26
 * History:
 * @<version> 1.0
 */
public class ExecutorsExample {
    public static void main(String[] args) throws InterruptedException {
      useCachedThreadPool();
    }

    /**
     * 这个不自动shutdown ,是因为始终有一个线程是 lived 的.
     *
     * @throws InterruptedException
     */
    private static void  useSinglePool() throws InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        // 这个转换会失败，因为ThreadPoolExecutor不是 newSingleThreadExecutor的实例
//        System.out.println(((ThreadPoolExecutor)executorService).getActiveCount());

        IntStream.range(0, 10).boxed().forEach(i -> executorService
                .execute(()->{
                    try {
                        TimeUnit.SECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName()+ " [" + i +"] ");
                }));

        TimeUnit.SECONDS.sleep(1);
        System.out.println(((ThreadPoolExecutor)executorService).getActiveCount());
    }


    /**
     * new ThreadPoolExecutor(nThreads, nThreads,
     *                                       0L, TimeUnit.MILLISECONDS,
     *                                       new LinkedBlockingQueue<Runnable>());
     * 虽然keepAliveTime=0，但是这个不回收，
     * 因为这个idle(空闲)线程 不大于core,所以不会进行回收
     *
     * @throws InterruptedException
     */
    private static void  useFixedSizePool() throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(10);
        System.out.println(((ThreadPoolExecutor)service).getActiveCount());

        IntStream.range(0, 10).boxed().forEach(i -> service
                .execute(()->{
                    try {
                        TimeUnit.SECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName()+ " [" + i +"] ");
                }));

        TimeUnit.SECONDS.sleep(1);
        System.out.println(((ThreadPoolExecutor)service).getActiveCount());
    }



    /**
     *   return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
     *                                       60L, TimeUnit.SECONDS,
     *                                       new SynchronousQueue<Runnable>());
     * BlockingQueue是一个  SynchronousQueue<Runnable>()
     * 这个阻塞队列只能放一个元素的BlockingQueue,
     * 所以他不会把任务暂存起来，只有等到1分钟后 它发现有空闲的，他才会去处理
     * 发现这些线程大于core线程个数，core线程个数是0，那么就会将其空闲的线程全部回收了
     *
     * @throws InterruptedException
     */
    private static void  useCachedThreadPool() throws InterruptedException {
        ExecutorService service = Executors.newCachedThreadPool();
        System.out.println(((ThreadPoolExecutor)service).getActiveCount());

        service.execute(()-> System.out.println("==============="));
        System.out.println(((ThreadPoolExecutor)service).getActiveCount());

        IntStream.range(0, 100).boxed().forEach(i -> service
                .execute(()->{
                    try {
                        TimeUnit.SECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName()+ " [" + i +"] ");
                }));

        TimeUnit.SECONDS.sleep(1);
        System.out.println(((ThreadPoolExecutor)service).getActiveCount());
    }
}

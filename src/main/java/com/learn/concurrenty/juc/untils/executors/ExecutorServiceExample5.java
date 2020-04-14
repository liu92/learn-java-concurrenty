package com.learn.concurrenty.juc.untils.executors;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @ClassName: ExecutorServiceExample5
 * @Description:
 * @Author: lin
 * @Date: 2020/4/13 14:01
 * {@link ExecutorService}
 * History:
 * @<version> 1.0
 */
public class ExecutorServiceExample5 {
    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
        ThreadPoolExecutor executorService = (ThreadPoolExecutor)Executors.newFixedThreadPool(2);
        // 返回一个queue, 那么能往这个queue加一个runnable 让其运行喃
        // 不通过submit的方式 或者invoke的方式，我们直接到queue里面去加任务 这样可不可以运行喃。

        // 从运行的结果来看, executorService 去执行submit, invoke ,execute 的时候
        //  它类似于 不仅仅是接收到了一个任务，还是收到了一个信号，这个信号会触发 这个在线程池里面的线程
        // 的创建， 但是这个直接加进去  线程池里面的线程 不知道是不是要去创建线程 去执行。
        // 因为大家都在wait，都在等待这个信号。
        executorService.getQueue().add(()-> System.out.print("I am added directly into the queue"));

    }


}

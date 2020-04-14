package com.learn.concurrenty.juc.untils.executors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * @ClassName: CompletionServiceExample1
 * @Description:
 * @Author: lin
 * @Date: 2020/4/14 17:23
 * History:
 * @<version> 1.0
 */
public class CompletionServiceExample2 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        futureDefect2();
    }

    /**
     * no callback
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private static  void futureDefect1() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<Integer> future = executorService.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 100;
        });
        System.out.println("===================");
        // 这里会阻塞，就会造成这一行代码下面的代码不能执行
        future.get();
    }

    /**
     * no callback
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private static  void futureDefect2() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        List<Callable<Integer>> callableList = Arrays.asList(
                () -> {
                    sleepSeconds(10);
                    System.out.println("The 10 finished.");
                    return 10;
                },
                () -> {
                    sleepSeconds(20);
                    System.out.println("The 20 finished.");
                    return 20;
                }
        );

        ExecutorCompletionService<Integer> completionService = new ExecutorCompletionService<>(executorService);
        List<Future<Integer>> futures = new ArrayList<>();
        callableList.stream().forEach(callable -> futures.add(completionService.submit(callable)));

        Future<Integer> future;
        // 拿取最快完成的那一个，take是一个阻塞方法
        while ((future = completionService.take()) != null){
            System.out.println(future.get());
        }

    }


    private static  void  sleepSeconds(long seconds){
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

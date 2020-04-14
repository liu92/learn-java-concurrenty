package com.learn.concurrenty.juc.untils.executors;

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
public class CompletionServiceExample1 {

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

        // 批量的处理
        List<Future<Integer>> futures = executorService.invokeAll(callableList);
        Integer v1 = futures.get(1).get();
        System.out.println(v1);
        Integer v2 = futures.get(0).get();
        System.out.println(v2);

    }


    private static  void  sleepSeconds(long seconds){
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

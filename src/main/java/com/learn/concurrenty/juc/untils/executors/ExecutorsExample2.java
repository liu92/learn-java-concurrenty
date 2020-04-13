package com.learn.concurrenty.juc.untils.executors;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @ClassName: ExecutorsExample
 * @Description:
 * @Author: lin
 * @Date: 2020/4/13 9:26
 * History:
 * @<version> 1.0
 */
public class ExecutorsExample2 {
    /**
     * 命令：dxdiag 这个是directx诊断工具命令
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        /**
         * 会自动结束，也就是说它 清光了所以的任务，
         * 包括queue里面的任务之后 它会全部结束
         */
        ExecutorService service = Executors.newWorkStealingPool();
        //查看cup核数
//        Optional.of(Runtime.getRuntime().availableProcessors())
//                .ifPresent(System.out::println);
        List<Callable<String>> callableList = IntStream.range(0, 20).boxed().map(i ->
                (Callable<String>) () -> {
                    System.out.println("Thread " + Thread.currentThread().getName());
                    sleepSeconds(2);
                    return "Task-" + i;
                }
        ).collect(Collectors.toList());

        // 这个返回是立即返回，但是结果要等到真正的线程池用完 启动了任务才会给你
        // 所以 这就叫future 设计模式，就是让你的程序不阻塞，可以进行下面的操作
        // 可以继续往下走
        //List<Future<String>> futures = service.invokeAll(callableList);
        service.invokeAll(callableList).stream().map(future ->{
            try {
               return future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }).forEach(System.out::println);
    }


    private  static  void sleepSeconds(long seconds){
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

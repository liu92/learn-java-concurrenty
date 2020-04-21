package com.learn.concurrenty.juc.untils.executors;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
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
public class ExecutorServiceExample4 {
    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
        /*testInvokeAny();*/
//        testInvokeAnyTimeOut();
    }

    /**
     * Question:
     *  when the result returned, other callable will be keep on process?
     * Answer:
     *  other callable will be canceled (if other not process finished).
     * Note:
     *  The invokeAny will be blocked caller;
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private static  void  testInvokeAny() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        List<Callable<Integer>> collectList = IntStream.range(0, 5).boxed().map(i ->
                (Callable<Integer>) () -> {

                    TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(20));
                    // 那么其中有一个返回之后，其他的callable还会不会继续执行？
                    System.out.println(Thread.currentThread().getName()+ " :" + i);
                    return i;
                }).collect(Collectors.toList());
        //那个先完成了就返回哪一个
        Integer value = executorService.invokeAny(collectList);
        System.out.println("===============finished=================");
        System.out.println(value);
    }


    /**
     * {@link ExecutorService#invokeAny(Collection, long, TimeUnit)}
     */
    private static  void  testInvokeAnyTimeOut() throws ExecutionException, InterruptedException, TimeoutException {
        ExecutorService service = Executors.newFixedThreadPool(10);

        Integer value = service.invokeAny(
                IntStream.range(0, 5).boxed().map(i ->
                (Callable<Integer>) () -> {
                    TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(20));
                    System.out.println(Thread.currentThread().getName() + " :" + i);
                    return i;
                }
              ).collect(Collectors.toList()), 1, TimeUnit.SECONDS);
        System.out.println("===============finished=================");
        System.out.println(value);
    }


    /**
     * {@link ExecutorService#invokeAll(Collection)}
     * @throws InterruptedException
     */
    private static  void  testInvokeAll() throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(10);
        service.invokeAll(
                IntStream.range(0, 5).boxed().map(i ->
                        (Callable<Integer>) () -> {
                    // 先执行----转换----再输出

                            // 这里有一个缺点，就是其中有一个执行的很慢，
                            // 他也要将第一阶段执行完后后 才会去执行第二阶段
                            // 第二阶段执行完后才会去执行第三阶段
                            TimeUnit.SECONDS.sleep(5);
                            System.out.println(Thread.currentThread().getName() + " :" + i);
                            return i;
                        }
                ).collect(Collectors.toList())
                // parallelStream()
        ).stream().map(future -> {
            try {
                // 将future中的结果拿出来。
              return   future.get();
            } catch (Exception e) {
                throw  new RuntimeException(e);
            }
        }).forEach(System.out::println);
        // callable 执行完后才会打印
        System.out.println("===============finished=================");
    }




    /**
     * {@link ExecutorService#invokeAll(Collection, long, TimeUnit)}
     * @throws InterruptedException
     */
    private static  void  testInvokeAllTimeOut() throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(10);
        service.invokeAll(
                IntStream.range(0, 5).boxed().map(i ->
                        (Callable<Integer>) () -> {

                            TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(10));
                            System.out.println(Thread.currentThread().getName() + " :" + i);
                            return i;
                        }
                ).collect(Collectors.toList()), 1, TimeUnit.SECONDS
                // parallelStream()
        ).stream().map(future -> {
            try {
                // 将future中的结果拿出来。
                return   future.get();
            } catch (Exception e) {
                throw  new RuntimeException(e);
            }
        }).forEach(System.out::println);
        // callable 执行完后才会打印
        System.out.println("===============finished=================");
    }


    /**
     * {@link ExecutorService#submit(Runnable)} 
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private static  void  testSubmitRunnable() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        // 是一个异步方法，立即返回， 返回的是future
        Future<?> future = executorService.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        // 如果有结果说明执行 对Runnable执行结束了
        future.get();
    }


    /**
     * {@link ExecutorService#submit(Runnable, Object)}
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private static  void  testSubmitRunnableWithResult() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        String result = "DONE";
        Future<String> future = executorService.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, result);

        System.out.println(future.get());
    }

}

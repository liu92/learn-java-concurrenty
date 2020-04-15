package com.learn.concurrenty.juc.untils.executors;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @ClassName: ComplexExample
 * @Description:
 * @Author: lin
 * @Date: 2020/4/15 15:34
 * History:
 * @<version> 1.0
 */
public class ComplexExample {
    public static void main(String[] args) throws InterruptedException {

        // CompletionService 相比 future 可以回调, 并且CompletionService 不是Executors 这个只是进行了保证
        // CompletionService 关注的是完成的任务,不会关注未完成的任务
        /*final ExecutorService service = Executors.newSingleThreadExecutor();
        List<Runnable> tasks = IntStream.range(0, 5).boxed().map(ComplexExample::toTask).collect(Collectors.toList());
        final CompletionService<Object> completionService = new ExecutorCompletionService<>(service);
        tasks.forEach(r -> completionService.submit(Executors.callable(r)));
        TimeUnit.SECONDS.sleep(12);
        // 这里取 shutdownNow 线程池，关注这个任务的执行是 ExecutorService,
        // 这个Runnable 返回的是未执行的, 那么在执行过程中被中断的任务 是不会放到这里面去的
        //
        List<Runnable> runnableList = service.shutdownNow();
        System.out.println(runnableList);*/


        //==========================================
        final ExecutorService service = Executors.newSingleThreadExecutor();
        List<Callable<Integer>> tasks = IntStream.range(0, 5).boxed().map(MyTask::new).collect(Collectors.toList());
        final CompletionService<Integer> completionService = new ExecutorCompletionService<>(service);

        tasks.forEach(completionService::submit);
        TimeUnit.SECONDS.sleep(12);
        // shutdownNow时候,返回的仅仅是队列里的,有的线程可能在执行过程中它被中断,或者出错了
        service.shutdownNow();
        // 这样可以拿出那些任务真正为完成
        tasks.stream().filter(callable -> !((MyTask)callable).isSuccess())
                .forEach(System.out::println);
    }

    private static class MyTask implements Callable<Integer>{
        private final Integer value;
        private boolean success = false;
        MyTask(Integer value){
            this.value = value;
        }


        @Override
        public Integer call() throws Exception {
            System.out.printf("The Task [%d] will be executed.\n", value);
            TimeUnit.SECONDS.sleep(value *5 + 10);
            System.out.printf("The Task [%d] execute done.\n", value);
            return value;
        }

        public boolean isSuccess(){
            return  success;
        }
    }

    private static Runnable toTask(int i ){
        return  ()->{
            try {
                System.out.printf("The Task [%d] will be executed.\n", i);
                TimeUnit.SECONDS.sleep(i * 5 + 10);
                System.out.printf("The Task [%d] execute done.\n", i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
    }
}

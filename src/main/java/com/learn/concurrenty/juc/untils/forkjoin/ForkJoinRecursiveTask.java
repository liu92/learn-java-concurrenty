package com.learn.concurrenty.juc.untils.forkjoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.IntStream;

/**
 * ForkJoin  简单使用
 * RecursiveTask
 * @ClassName: ForkJoinRecursiveTask
 * @Description:
 * @Author: lin
 * @Date: 2020/4/11 9:41
 * History:
 * @<version> 1.0
 */
public class ForkJoinRecursiveTask {

    private final  static  int MAX_THRESHOLD = 3;

    public static void main(String[] args) {
         final ForkJoinPool forkJoinPool = new ForkJoinPool();
         // 计算 从0~10
        ForkJoinTask<Integer> future = forkJoinPool.submit(new CalculatedRecursiveTask(0, 10));
        try {
            Integer result = future.get();
            System.out.println(result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static class  CalculatedRecursiveTask extends RecursiveTask<Integer>{
        private final int start;
        private final int end;

        CalculatedRecursiveTask(int start, int end){
            this.start = start;
            this.end = end;
        }
        /**
         * The main computation performed by this task.
         *
         * @return the result of the computation
         */
        @Override
        protected Integer compute() {
            if(end - start <= MAX_THRESHOLD){
                //如果这个 区间的数据相减 没有超过这个 MAX_THRESHOLD 范围，那么就不在进行任务拆分了
                return IntStream.rangeClosed(start, end).sum();
            }else {
                // 如果自己忙不过来，那么就将任务拆分 请其他的人帮忙
                int middle = (start + end) / 2;
                // 左边的任务
                CalculatedRecursiveTask leftTask = new CalculatedRecursiveTask(start, middle);
                //右边的任务 从middle +1 开始
                CalculatedRecursiveTask rightTask = new CalculatedRecursiveTask(middle + 1, end);

                leftTask.fork();
                rightTask.fork();
                return  leftTask.join() + rightTask.join();
            }
        }
    }
}

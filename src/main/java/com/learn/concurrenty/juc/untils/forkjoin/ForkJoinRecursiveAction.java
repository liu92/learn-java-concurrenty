package com.learn.concurrenty.juc.untils.forkjoin;

import java.util.Optional;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * 实现拿出最终的结果，那么必须有一个统一记录的地方
 * @ClassName: ForkJoinRecursiveAction
 * @Description:
 * @Author: lin
 * @Date: 2020/4/11 10:19
 * History:
 * @<version> 1.0
 */
public class ForkJoinRecursiveAction {
    private final  static  int MAX_THRESHOLD = 3;

    private final  static AtomicInteger SUM = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        final ForkJoinPool forkJoinPool = new ForkJoinPool();
        // 返回是拿不到值 ，因为是void
        forkJoinPool.submit(new CalculatedRecursiveAction(0, 10));

        forkJoinPool.awaitTermination(10, TimeUnit.SECONDS);
        Optional.of(SUM).ifPresent(System.out::println);
    }

    /**
     * RecursiveAction 的compute 方法是没有返回值的，
     * 所以想要有放回值那么就使用一个累加器来处理
     */
    private static class  CalculatedRecursiveAction extends RecursiveAction {
        private final int start;
        private final int end;

        CalculatedRecursiveAction(int start, int end){
            this.start = start;
            this.end = end;
        }
        /**
         * not return
         * The main computation performed by this task.
         */
        @Override
        protected void compute() {
            //将结果放到一个累加器中去
            if(end - start <= MAX_THRESHOLD){
                SUM.addAndGet(IntStream.rangeClosed(start, end).sum());
            }else {
                // 如果自己忙不过来，那么就将任务拆分 请其他的人帮忙
                int middle = (start + end) / 2;
                // 左边的任务
                CalculatedRecursiveAction leftActon = new CalculatedRecursiveAction(start, middle);
                //右边的任务 从middle +1 开始
                CalculatedRecursiveAction rightActon = new CalculatedRecursiveAction(middle + 1, end);

                leftActon.fork();
                rightActon.fork();
            }

        }
    }
}

package com.learn.concurrenty.juc.collections.concurrent;

import com.learn.concurrenty.DefaultThreadFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName: ConcurrentSkipListMapVsConcurrentHashMap
 * @Description:
 * @Author: lin
 * @Date: 2020/4/20 14:54
 * History:
 * @<version> 1.0
 */
public class ConcurrentSkipListMapVsConcurrentHashMap {

//    private static ExecutorService EXECUTOR_SERVICE = new ThreadPoolExecutor(10,
//            50, 200,
//            TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(20),
//            new DefaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());


    static class  Entry{
        // 多少个线程
        int threshold;
        // 平均值
        long ms;

        public  Entry(int threshold, long ms){
            this.threshold = threshold;
            this.ms = ms;
        }

        @Override
        public String toString() {
            return "Entry{" +
                    "threshold =" + threshold +
                    ", ms =" + ms +
                    '}';
        }
    }

    /**
     * 统一去放,这个就是去统计这两个
     */
    private final  static Map<Class<?>, List<Entry>> summary = new HashMap<Class<?>, List<Entry>>(){
        {
            put(ConcurrentHashMap.class, new ArrayList<>());
            put(ConcurrentSkipListMap.class, new ArrayList<>());
        }
    };


    public static void main(String[] args) throws InterruptedException {
        for (int i = 10; i <= 100 ; ) {
            pressureTest(new ConcurrentHashMap<>(), i);
            pressureTest(new ConcurrentSkipListMap<>(), i);
            //每次加10
            i+=10;
        }


        summary.forEach((k, v) ->{
            System.out.println(k.getSimpleName());
            v.forEach(System.out::println);
            System.out.println("================================");
        });
    }


    private static  void  pressureTest(final Map<String, Integer> map, int threshold) throws InterruptedException {
        System.out.println("Start pressure testing the map ["+ map.getClass()+ "] use the threshold ["+ threshold+"] ");
        long totalTime = 0L;
        final int MAX_THRESHOLD = 500000;
        int count = 5;
        // 循环5组 以此来分析 执行的时间
        for (int i = 0; i < count; i++) {
            final  AtomicInteger counter = new AtomicInteger(0);
            map.clear();
            long startTime = System.nanoTime();
            ExecutorService executorService = Executors.newFixedThreadPool(threshold);
            for (int j = 0; j < threshold ; j++) {
                executorService.execute(() -> {
                    for (int x = 0; x < MAX_THRESHOLD && counter.getAndIncrement() < MAX_THRESHOLD; x++) {
                        Integer randomNumber =(int) Math.ceil(Math.random() * 600000);
                        map.get(String.valueOf(randomNumber));
                        map.put(String.valueOf(randomNumber), randomNumber);
                    }
                });
            }

            executorService.shutdown();
            executorService.awaitTermination(2, TimeUnit.HOURS);
            long endTime = System.nanoTime();
            long period = (endTime - startTime) / 1000000L;
            System.out.println(threshold * MAX_THRESHOLD + " element inserted/retrieved in " + period + " ms");
            totalTime += period;
        }

        summary.get(map.getClass()).add(new Entry(threshold, (totalTime / 5)));
        System.out.println("For the map [" + map.getClass() + " ] thr average time is " + totalTime/5 + " ms");
    }
}

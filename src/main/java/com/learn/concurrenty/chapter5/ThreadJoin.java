package com.learn.concurrenty.chapter5;

import java.util.Optional;
import java.util.stream.IntStream;

/**
 * @ClassName: ThreadJoin
 * @Description: Thread中join使用
 * @Author: lin
 * @Date: 2020/3/21 23:04
 * History:
 * @<version> 1.0
 */
public class ThreadJoin {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() ->{
            IntStream.range(1,200)
                    .forEach(i-> System.out.println(Thread.currentThread().getName() + "->" +i));
        });

        Thread t2 = new Thread(() ->{
            IntStream.range(1,200)
                    .forEach(i-> System.out.println(Thread.currentThread().getName() + "->" +i));
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        Optional.of("All of task finish done.").ifPresent(System.out::println);
        IntStream.range(1,200)
                .forEach(i-> System.out.println(Thread.currentThread().getName() + "->" +i));

    }
}

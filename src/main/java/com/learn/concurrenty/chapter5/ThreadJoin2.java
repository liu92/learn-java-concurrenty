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
public class ThreadJoin2 {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() ->{
            try {
                System.out.println("t1 is running");
                Thread.sleep(10000);
                System.out.println("t1 is done.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });


        t1.start();
        //加入这个表示100毫秒没有执行，那么就执行下面的
        t1.join(100);

        Optional.of("All of task finish done.").ifPresent(System.out::println);
        IntStream.range(1,200)
                .forEach(i-> System.out.println(Thread.currentThread().getName() + "->" +i));

        //这里当前线程是main, main在等待自己结束，这个main线程一直在等待自己结束，所以这里会一直等待下去
//        Thread.currentThread().join();
    }
}

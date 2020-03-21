package com.learn.concurrenty.chapter4;

import java.util.Optional;

/**
 * @ClassName: ThreadSimpleApi
 * @Description: Thread简单API
 * @Author: lin
 * @Date: 2020/3/20 17:21
 * History:
 * @<version> 1.0
 */
public class ThreadSimpleApi {
    public static void main(String[] args) {
        Thread t = new Thread(() -> {
            Optional.of("Hello").ifPresent(System.out::println);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t1");
        t.start();
        Optional.of(t.getName()).ifPresent(System.out::println);
        Optional.of(t.getId()).ifPresent(System.out::println);
        Optional.of(t.getPriority()).ifPresent(System.out::println);
    }
}

package com.learn.concurrent.design.chapter16;

/**
 * @ClassName: CounterTest
 * @Description:
 * @Author: lin
 * @Date: 2020/3/28 22:35
 * History:
 * @<version> 1.0
 */
public class CounterTest {
    public static void main(String[] args) throws InterruptedException {
        CounterIncrement increment = new CounterIncrement();
        increment.start();

        Thread.sleep(10_000L);
        increment.close();
    }
}

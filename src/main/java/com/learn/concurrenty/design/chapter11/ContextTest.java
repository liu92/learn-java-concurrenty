package com.learn.concurrenty.design.chapter11;

import java.util.stream.IntStream;

/**
 * @ClassName: ContextTest
 * @Description:
 * @Author: lin
 * @Date: 2020/3/27 22:31
 * History:
 * @<version> 1.0
 */
public class ContextTest {
    public static void main(String[] args) {
        IntStream.range(1, 5).forEach(i ->{
           new Thread(new ExecutionTask()).start();
        });
    }
}

package com.learn.concurrent.design.chapter7;

import java.util.stream.IntStream;

/**
 * @ClassName: ImmutableClient
 * @Description:
 * @Author: lin
 * @Date: 2020/3/27 13:23
 * History:
 * @<version> 1.0
 */
public class ImmutableClient {
    public static void main(String[] args) {
        //share data
        Person person = new Person("lin","cd");

        //多个线程访问共享变量，看看有什么问题
        //
        IntStream.range(0, 5).forEach(i -> {
            new UsePersonThread(person).start();
        });

        //输入的结果，可以看到 这个不可变对象在多线程下，不会改变
        // 不可变对象 就是你不能改变他的任何状态
        //xxx
        //Thread-0 print Person{name='lin', address='cd'}
        //Thread-3 print Person{name='lin', address='cd'}
        //Thread-2 print Person{name='lin', address='cd'}
        //Thread-1 print Person{name='lin', address='cd'}
        //xxx

    }
}

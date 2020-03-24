package com.learn.concurrenty.chapter11;

import java.util.Arrays;
import java.util.Optional;

/**
 * @ClassName: Test1
 * @Description: 那个方法进行调用，并且在哪一行
 * @Author: lin
 * @Date: 2020/3/24 16:26
 * History:
 * @<version> 1.0
 */
public class Test2 {

    public void test2(){
      // 这里使用Thread 的一个 getStackTrace 记录下栈的信息
        //方法调用在哪里，然后多少行 都打印出来
        Arrays.asList(Thread.currentThread().getStackTrace())
           .stream()
           .filter(e-> !e.isNativeMethod())
           .forEach(e-> Optional.of(e.getClassName() + ":" + e.getMethodName()+ ":" +
            e.getLineNumber()).ifPresent(System.out::println));
        ;
    }
}

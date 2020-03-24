package com.learn.concurrenty.chapter11;

/**
 *
 * @ClassName: Test1
 * @Description: 那个方法进行调用，并且在哪一行
 * @Author: lin
 * @Date: 2020/3/24 16:26
 * History:
 * @<version> 1.0
 */
public class Test1 {
    private Test2 t2 =new Test2();
    public void test(){
        t2.test2();
    }
}

package com.learn.concurrenty.chapter11;

/**
 * 这里测试是 t2被谁调用了，在哪一行，然后 t1被谁调用了，也是在哪一行
 * @ClassName: ThreadException
 * @Description: 。
 * @Author: lin
 * @Date: 2020/3/24 16:13
 * History:
 * @<version> 1.0
 */
public class ThreadException2 {

    public static void main(String[] args) {
        Test1 t1 = new Test1();
        t1.test();
    }
}

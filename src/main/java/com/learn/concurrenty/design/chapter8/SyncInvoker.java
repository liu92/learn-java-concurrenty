package com.learn.concurrenty.design.chapter8;

/**
 * 多线程Future 设计模式
 * @ClassName: SyncInvoker
 * @Description:
 * @Author: lin
 * @Date: 2020/3/27 14:42
 * History:
 * @<version> 1.0
 */
public class SyncInvoker {
    public static void main(String[] args) throws InterruptedException {
        // 这个 方法的阻塞，导致了下面方法执行的阻塞
        // 那么有什么方法可以解决这种问题呢？
        // 有一种 就是在调用的时候 立即返回回来，返回回来的时候在想要结果的时候
        // 再去拿结果 ，就是异步的方式去拿取
        String result = get();
        System.out.println(result);
    }

    /**
     * 比较耗时的操作
     * @return
     * @throws InterruptedException
     */
    private static  String get() throws InterruptedException {
        Thread.sleep(100001);
        return  "FINISH";
    }
}

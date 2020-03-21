package com.learn.concurrenty.chapter4;

/**
 * @ClassName: DaemonThread
 * @Description: 守护线程
 * @Author: lin
 * @Date: 2020/3/20 16:14
 * History:
 * @<version> 1.0
 */
public class DaemonThread {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(){
            @Override
            public void run() {

                try {
                    System.out.println(Thread.currentThread().getName() + "running");
                    Thread.sleep(10000);
                    System.out.println(Thread.currentThread().getName() + "done");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        //设置为守护线程，那么这个 main线程结束 这个守护线程也跟着结束
        // 并且这个daemon设置 必须在线程活跃的时候去设置，
        // 如果线程不是存活的那么 就会报错， 如果将t.start()方法写在 t.setDaemon(true)之上
        // 就会出现IllegalThreadStateException异常
        t.setDaemon(true);

        t.start();

        //在main结束之后为什么 上面的Thead-0还没有推出喃，这是因为还有一些active的线程
        // 所以
        // 我们可以通过jconsole 去查看main线程 已经结束了但是 Thread-0线程还在
        // jdk 1.7之后写法
        Thread.sleep(10_0000);
        System.out.println(Thread.currentThread().getName());
    }
}

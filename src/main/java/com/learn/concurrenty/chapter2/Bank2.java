package com.learn.concurrenty.chapter2;

/**
 * 第二个版本，Runnable
 * @ClassName: Bank2
 * @Description:
 * @Author: lin
 * @Date: 2020/3/20 10:13
 * History:
 * @<version> 1.0
 */
public class Bank2 {
    public static void main(String[] args) {
        //这种方法 业务实例就只有一个，而不像第一个版本继承Thread的有多个实例
        //通过将业务抽取出来，就是你的业务逻辑和线程控制 是在不同的object里面
        final  TicketWindowRunnable tr = new TicketWindowRunnable();
        Thread w1 = new Thread(tr, "一号窗口");
        Thread w2 = new Thread(tr, "二号窗口");
        Thread w3 = new Thread(tr, "三号窗口");
        w1.start();
        w2.start();
        w3.start();
    }
}

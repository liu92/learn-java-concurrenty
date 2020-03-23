package com.learn.concurrenty.chapter7;

/**
 * 实现Runnable ，线程安全性
 * @ClassName: TicketRunnable
 * @Description:
 * @Author: lin
 * @Date: 2020/3/20 10:11
 * History:
 * @<version> 1.0
 */
public class TicketRunnable implements Runnable{

    private  int index = 1;

    private final  static  int  MAX =500;
    private final  Object MONITOR = new Object();

    /**
     * 通过将业务抽取出来，就是你的业务逻辑和线程控制 是在不同的object里面
     */
    @Override
    public void run() {
         while (true) {
             // 这里会出线程安全的问题， 因为在多线程情况下,
             // 线程A执行了判断，但是这个index值 比如为499 还没有增加，
             // 这个时候线程B 也进行判断的后，然后往下执行 当线程B执行到打印后
             // 这个index值已经更改了，index值为500
             // 所以这个时候线程A过来执行，再次执行index++ ，
             // 那么这个时候index值就变成了501了。
             // 那么如何解决这种问题呢？  使用同步方式，或者加上 volatile 关键字
             // 这里 使用 synchronized 进行同步
             synchronized (MONITOR) {
                 if (index > MAX) {
                     break;
                 }
                 try {
                     Thread.sleep(5);
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }
                 System.out.println(Thread.currentThread().getName() +
                         " 的号码是：" + (index++));
               }
         }
    }
}

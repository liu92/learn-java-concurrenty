package com.learn.concurrenty.chapter2;

/**
 * 实现Runnable
 * @ClassName: TicketWindowRunnable
 * @Description:
 * @Author: lin
 * @Date: 2020/3/20 10:11
 * History:
 * @<version> 1.0
 */
public class TicketWindowRunnable implements Runnable{

    private int index = 1;

    private final  static  int MAX =50;

    /**
     * 通过将业务抽取出来，就是你的业务逻辑和线程控制 是在不同的object里面
     */
    @Override
    public void run() {
         while (index<=MAX){
             System.out.println(Thread.currentThread().getName() +
                     " 的号码是：" + (index++));
             try {
                 Thread.sleep(100);
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
         }
    }
}

package com.learn.concurrenty.chapter2;

/**
 * @ClassName: Bank
 * @Description:
 * @Author: lin
 * @Date: 2020/3/20 9:54
 * History:
 * @<version> 1.0
 */
public class Bank {
    public static void main(String[] args) {
        TicketWindow ticket1 = new TicketWindow("一号柜台");
        ticket1.start();

        TicketWindow ticket2 = new TicketWindow("二号柜台");
        ticket2.start();

        TicketWindow ticket3 = new TicketWindow("三号柜台");
        ticket3.start();
        //这种情况 就可以知道TicketWindow中 index不是共享的变量，
        // 而是各玩各的 不是同一个，那么这里就需要将这个 变量变成共享的变量
        // 通常让其共享 那么急速让其实例化一次，也就是添加 static关键字。
        // 就是类变量，这种方式就可以使用同一个共享变量而不是 各自产生一个
        // 但是 static的生命周期比较长，有可能这个类销毁了，
        // 这个静态变量也不一定销毁，因为这静态变量有一份独立的空间。
        // 不过这种方式可以控制，但是叫号的顺序边乱了
    }
}

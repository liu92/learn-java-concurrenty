package com.learn.concurrenty.chapter2;

/**
 * @ClassName: TicketWindow
 * @Description: 模拟银行排队叫号
 * @Author: lin
 * @Date: 2020/3/20 9:52
 * History:
 * @<version> 1.0
 */
public class TicketWindow  extends  Thread{
    /**
     * 柜台名字
     */
    private final  String name;
    /**
     * 最大号数
     */
//   1、 private final  int MAX =50;
    private static final  int MAX =50;

//   1、 private int index =1;
    private static int index =1;

    public TicketWindow(String name) {
        this.name = name;
    }


    @Override
    public void run() {
        while (index <= MAX){
            System.out.println("柜台"+ name + "当前的号码是：" + (index++));
        }
    }
}

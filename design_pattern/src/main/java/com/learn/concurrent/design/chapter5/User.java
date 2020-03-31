package com.learn.concurrent.design.chapter5;

/**
 * @ClassName: User
 * @Description: user就相当一个线程
 * @Author: lin
 * @Date: 2020/3/26 17:07
 * History:
 * @<version> 1.0
 */
public class User extends  Thread{

    private final String myName;

    private final String myAddress;

    private final Gate gate;

    public User(String myName, String myAddress, Gate gate){
        this.myName = myName;
        this.myAddress = myAddress;
        this.gate = gate;
    }

    @Override
    public void run() {
        System.out.println(myName + "  BEGIN");
        // 线程启动后一直去 通过这门
        while (true){
            this.gate.pass(myName, myAddress);
        }
    }
}

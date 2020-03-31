package com.learn.concurrent.design.chapter7;

/**
 * @ClassName: UsePersonThread
 * @Description:
 * @Author: lin
 * @Date: 2020/3/27 13:21
 * History:
 * @<version> 1.0
 */
public class UsePersonThread extends  Thread{

    private Person person;

    public UsePersonThread(Person person){
        this.person = person;
    }

    @Override
    public void run() {
        for (int i = 0; i < 20 ; i++) {
            System.out.println(Thread.currentThread().getName() + " print "+
                    person.toString());
        }
    }
}

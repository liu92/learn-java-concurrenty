package com.learn.concurrenty.design.chapter4;

/**
 * @ClassName: ObserverClient
 * @Description:
 * @Author: lin
 * @Date: 2020/3/26 15:36
 * History:
 * @<version> 1.0
 */
public class ObserverClient {
    public static void main(String[] args) {
        final  Subject  subject = new Subject();
        AbstractObserver observer = new BinaryObserver(subject);
        AbstractObserver observer1 = new OctalObserver(subject);
        System.out.println("======================");
        subject.setState(10);
        System.out.println("======================");
        subject.setState(10);
        System.out.println("======================");
        subject.setState(12);
    }
}

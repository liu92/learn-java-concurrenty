package com.learn.concurrent.design.chapter4;

/**
 * 使用观察者模式观察线程的声明周期,
 * 其实就是加了listenler, 放到线程不同阶段去
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

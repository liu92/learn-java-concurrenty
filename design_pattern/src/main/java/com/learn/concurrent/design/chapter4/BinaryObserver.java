package com.learn.concurrent.design.chapter4;

/**
 * @ClassName: BinaryObserver
 * @Description: 二进制 server
 * @Author: lin
 * @Date: 2020/3/26 15:24
 * History:
 * @<version> 1.0
 */
public class BinaryObserver extends AbstractObserver {

    /**
     * 构造方法，这里需要 主题
     * @param subject
     */
    public  BinaryObserver(Subject subject){
        super(subject);
    }
    //如果在父类不加入 到attach 那么就需要用下面的方式
//    public  BinaryObserver(Subject subject){
//        this.subject = subject;
//    }

    @Override
    public void update() {
        System.out.println("Binary String:" + Integer.toBinaryString(subject.getState()));
    }
}

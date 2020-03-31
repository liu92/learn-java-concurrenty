package com.learn.concurrent.design.chapter1;

/**
 *使用静态内部类的方式
 * @ClassName: SingletonObject
 * @Description:
 * @Author: lin
 * @Date: 2020/3/25 17:18
 * History:
 * @<version> 1.0
 */
public class SingletonObject {
    /**
     * 私有化构造器，不给外部使用
     */
    private SingletonObject(){}

    /**
     * 静态内部类
     */
    private static class InstanceHolder{
        //这对像是类对象，所以只在jvm中只有一个
        private static final SingletonObject instance = new SingletonObject();
    }

    /**
     * 在调用整方法的时候，去初始化一个对象
     * @return
     */
    public static SingletonObject getInstance(){
        return InstanceHolder.instance;
    }
}

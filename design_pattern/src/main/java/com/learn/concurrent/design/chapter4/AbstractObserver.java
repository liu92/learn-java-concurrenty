package com.learn.concurrent.design.chapter4;

/**
 * 抽象类 ，这个观察者 里面 定义一些方法
 * 但是这些方法不去实现，让其子类实现
 * @ClassName: AbstractObserver
 * @Description:
 * @Author: lin
 * @Date: 2020/3/26 15:12
 * @History:
 * @<version> 1.0
 */
public abstract class AbstractObserver {


    /**
     * 组合, 这个让其子类可以访问
     */
    protected Subject subject;

    /**
     * 更新
     */
    public abstract void update();

    public AbstractObserver(Subject subject){
        this.subject = subject;
        //将observer加入到这个 集合中去，那么就不用到子类中每个都写一边
        this.subject.attach(this);
    }
}

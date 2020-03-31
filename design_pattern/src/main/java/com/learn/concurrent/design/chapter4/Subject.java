package com.learn.concurrent.design.chapter4;

import java.util.ArrayList;
import java.util.List;

/**
 * 在主题发生变化后 去通知所有的观察者
 * @ClassName: Subject
 * @Description: 观察者模式-主题
 * @Author: lin
 * @Date: 2020/3/26 15:09
 * History:
 * @<version> 1.0
 */
public class Subject {

    private List<AbstractObserver> observers = new ArrayList<>();

    private int state;


    /**
     * 状态变更
     * @param state
     */
    public void setState(int state){
        if(state == this.state){
            return;
        }
        this.state = state;
        //然后去通知 server
        notifyAllObserver();
    }

    public int getState(){
       return this.state ;
    }

    /**
     * 连接到每一个server
     */
    public void attach(AbstractObserver abstractObserver){
        observers.add(abstractObserver);
    }

    /**
     * 去通知所有的observer
     */
    private void notifyAllObserver(){
        //去更新
        observers.stream().forEach(AbstractObserver::update);
    }
}

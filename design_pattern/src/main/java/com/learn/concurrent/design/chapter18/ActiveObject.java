package com.learn.concurrent.design.chapter18;

/**
 * Active design
 * 1、首先有一个ActiveObject，是一个异步接收消息的主动对象，那要做一个ActiveObject设计模式
 * 首先要把的方法它转换成Request(对应不同的request)
 *
 *
 * 接收异步消息的主动对象
 * @ClassName: ActiveObject
 * @Description: 接收异步消息的主动方法
 * @Author: lin
 * @Date: 2020/3/30 15:30
 * History:
 * @<version> 1.0
 */
public interface ActiveObject {
    Result makeString(int count, char fillChar);

    void displayString(String text);
}

package com.learn.concurrent.design.chapter15;

/**
 *  Thead-per-message 设计， 这个就是在收到请求后 去开启一个线程来处理事情，
 *  这个一般使用线程池来代替，不过这里还是介绍下
 * @ClassName: Message
 * @Description:
 * @Author: lin
 * @Date: 2020/3/28 22:03
 * History:
 * @<version> 1.0
 */
public class Message {
    private final  String value;

    public Message(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

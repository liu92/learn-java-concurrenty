package com.learn.concurrent.design.chapter9;

/**
 * @ClassName: Request
 * @Description:
 * @Author: lin
 * @Date: 2020/3/27 16:13
 * History:
 * @<version> 1.0
 */
public class Request {
    final private String value;
    public Request(String value){
        this.value = value;
    }

    public String getValue(){
        return  value;
    }
}

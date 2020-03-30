package com.learn.concurrenty.design.chapter17;

/**
 * @ClassName: RequestCommodity
 * @Description:
 * @Author: lin
 * @Date: 2020/3/30 9:23
 * History:
 * @<version> 1.0
 */
public class RequestCommodity {
    private  final  String name;
    private  final  int number;

    RequestCommodity(final  String name, final  int number){
        this.name = name;
        this.number = number;
    }

    void execute(){
        System.out.println(Thread.currentThread().getName()
                + " execute " + this);
    }

    @Override
    public String toString() {
        return "Request => No." + number + " Name. " + name ;
    }
}

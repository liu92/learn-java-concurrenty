package com.learn.concurrenty.chapter3;

/**
 * @ClassName: CreateThread3
 * @Description:
 * @Author: lin
 * @Date: 2020/3/20 15:08
 * History:
 * @<version> 1.0
 */
public class CreateThread3 {

    private static  int counter =0;

    public static void main(String[] args) {
       // jvm stack, 创建一个操作压栈， 一直不停的往栈里面压入，那么会出现异常
        try {
            add(0);
        }catch (Error e){
           e.printStackTrace();
            System.out.println(counter);
        }

    }

    private static void add(int i){
        ++counter;
        add(i+1);
    }
}

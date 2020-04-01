package com.learn.concurrent.classloader.java.lang;

/**
 * @ClassName: String
 * @Description:
 * @Author: lin
 * @Date: 2020/4/1 14:56
 * History:
 * @<version> 1.0
 */
public class String {
    static {
        System.out.println("my customer string class ");
    }
    private static int i = 1;
    private int x;
    private Object object = new Object();

    String(){
        x = 10;
    }

    public int getValue(){
        this.object.hashCode();
        return 1;
    }
}


package com.learn.concurrent.classloader.chapter1;

/**
 * 加载类
 * @ClassName: LoaderClass
 * @Description:
 * @Author: lin
 * @Date: 2020/3/31 23:16
 * History:
 * @<version> 1.0
 */
public class LoaderClass {
    public static void main(String[] args) {
        MyObject myObject1 = new MyObject();
        MyObject myObject2 = new MyObject();
        MyObject myObject3 = new MyObject();
        MyObject myObject4 = new MyObject();

        System.out.println(myObject1.getClass() == myObject2.getClass());
        System.out.println(myObject1.getClass() == myObject3.getClass());
        System.out.println(myObject1.getClass() == myObject4.getClass());
    }
}


class MyObject{

}
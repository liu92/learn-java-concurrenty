package com.learn.concurrent.classloader.chapter5;

/**
 * @ClassName: NameSpace
 * @Description:
 * @Author: lin
 * @Date: 2020/4/2 10:02
 * History:
 * @<version> 1.0
 */
public class NameSpace {
    public static void main(String[] args) throws ClassNotFoundException {
        ClassLoader classLoader = NameSpace.class.getClassLoader();
        Class<?> aClass = classLoader.loadClass("java.lang.String");
        Class<?> aClass1 = classLoader.loadClass("java.lang.String");
        System.out.println(aClass.hashCode());
        System.out.println(aClass1.hashCode());

        // 只有一个class对象会被加载，也就是只有一个class对象分配在堆内存中
    }
}

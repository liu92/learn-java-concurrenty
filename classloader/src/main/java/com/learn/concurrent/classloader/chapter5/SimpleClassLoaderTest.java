package com.learn.concurrent.classloader.chapter5;

/**
 * @ClassName: SimpleClassLoaderTest
 * @Description:
 * @Author: lin
 * @Date: 2020/4/1 23:05
 * History:
 * @<version> 1.0
 */
public class SimpleClassLoaderTest {
    public static void main(String[] args) throws ClassNotFoundException {
        SimpleClassLoader simpleClassLoader = new SimpleClassLoader();
        Class<?> aClass = simpleClassLoader.loadClass("com.learn.concurrent.classloader.chapter5.SimpleObject");
        System.out.println(aClass.getClassLoader());
    }

}

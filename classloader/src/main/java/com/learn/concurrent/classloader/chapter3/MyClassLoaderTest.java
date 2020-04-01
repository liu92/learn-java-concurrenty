package com.learn.concurrent.classloader.chapter3;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @ClassName: MyClassLoaderTest
 * @Description:
 * @Author: lin
 * @Date: 2020/4/1 15:48
 * History:
 * @<version> 1.0
 */
public class MyClassLoaderTest {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        // 我们需要编译好的文件从com开拷贝到 那个我建立的 磁盘目录中去
        // D:\app\classloader\com\learn\concurrent\classloader\chapter3
        //MyClassLoader 是我们自定义的加载器， 类加载器加载的地方就是我们设置的磁盘目录
        // D:\app\classloader
        MyClassLoader classLoader = new MyClassLoader("MyClassLoader");
         // 这个MyObject 是要加载的类
        Class<?> aClass = classLoader.loadClass("com.learn.concurrent.classloader.chapter3.MyObject");
        System.out.println(aClass);
        // 这里如果那个MyObject 这个文件在这个项目中，那么这个加载就会是系统类加载
        // 原因是classPath中 有这个文件

        // 所以 我们把 项目中MyObject删除，因为在那个磁盘目录中我们已经将
        // 编译好的文件 MyObject拷贝到 磁盘目录中放好了
        // 注意: classLoader的时候 不会去初始化类，这个不在6个主动使用中
        // 在 newInstance的时候会去初始化类
        System.out.println(aClass.getClassLoader());
        //
        Object o = aClass.newInstance();
        Method method = aClass.getMethod("hello", new Class<?>[]{});
        Object result = method.invoke(o, new Object[]{});
        System.out.println(result);

        //下面是打印的结果
        //class com.learn.concurrent.classloader.chapter3.MyObject
        //com.learn.concurrent.classloader.chapter3.MyClassLoader@6d06d69c
        //My object static block.
        //Hello World
    }
}

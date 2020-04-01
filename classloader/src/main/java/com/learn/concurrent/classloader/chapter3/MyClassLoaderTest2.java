package com.learn.concurrent.classloader.chapter3;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 测试 ClassLoader 双亲委派机制
 * 1.类加载器的委托是优先交给父加载器先去尝试加载
 * 2.父加载器和子加载器其实是一种包装关系，或者 包含关系
 * 3.同一个classLoader(或者是父加载器的父加载器) 加载一个class 在加载完之后,它在堆去会有一个对象 且保持一份
 * 如果是两个不同的classLoader去加载,那么这里要注意命名空间 产生两个对象
 * @ClassName: MyClassLoaderTest2
 * @Description: 测试 ClassLoader 双亲委派机制
 * @Author: lin
 * @Date: 2020/4/1 15:48
 * History:
 * @<version> 1.0
 */
public class MyClassLoaderTest2 {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {

        MyClassLoader classLoader1 = new MyClassLoader("MyClassLoader-1");
        //这个加载类的父类加载器是 classLoader1，也就是说这两个是父子关系，
        // 但是不继承关系 他只一个包装的关系，也就classLoader1包含 classLoader2 但不是继承
        MyClassLoader classLoader2 = new MyClassLoader("MyClassLoader-2", classLoader1);
        // classLoader2加载的文件，这里重新设置一个地址，这目录下什么都没有，所以加载不了
        // 如果采取父委托机制，那么就会让classLoader2的父加载器 去加载, 这个父加载器会去找系统类加载器
        // 系统类加载器 去找扩展类加载器  ----> 根加载器，然后当根加载器看要加载的没有，那么就会告知
        // 扩展加载器去加载，如果扩展加载器也没有找到 就告知系统加载器---------------> 系统加载器去加载，
        // 当系统加载时 也没有，那么就会 去找自定义实现的加载器，当自定义加载器加载时有那么
        // 就将这个 给 当前请求的加载器， 如果都没有那么就是 报 我们定义的异常

        classLoader2.setDir("D:\\app\\classloader2");
        Class<?> aClass = classLoader2.loadClass("com.learn.concurrent.classloader.chapter3.MyObject");
        System.out.println(aClass);
        System.out.println(((MyClassLoader) aClass.getClassLoader()).getClassLoaderName());

        //打印结果 可以看出这个classLoader1去加载的
        //class com.learn.concurrent.classloader.chapter3.MyObject
        //MyClassLoader-1

        //如果classLoader2 没有定义父类加载器会出现什么呢？
        //出现的结果 就是我们定义的没有找到文件的异常
        //Exception in thread "main" java.lang.ClassNotFoundException: The class com.learn.concurrent.classloader.chapter3.MyObject not fount under


    }
}

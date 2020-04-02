package com.learn.concurrent.classloader.chapter5;

/**
 * @ClassName: RuntimePackage
 * @Description: 运行时包
 * @Author: lin
 * @Date: 2020/4/2 10:22
 * History:
 * @<version> 1.0
 */
public class RuntimePackage {
    //RuntimePackage 是由
    // 加载器的命名空间 加上你自己的类构成
    // 比如： 根加载器(可能参与也可能不参与) + 扩展加载器 (可能参与也可能不参与)
    // + 系统加载器(可能参与也可能不参与) + com.learn.concurrent.classloader.chapter5
    // 这个就是运行时期的包  这个作用也会起到保护作用
    //  com.learn.concurrent.classloader.chapter5
    // Boot.Ext.App.com.learn.concurrent.classloader.chapter5


    // 比如说用系统加载器 加载了一个类
    // 比如SimpleObject
    // Boot.Ext.App.com.learn.concurrent. classloader.chapter5.SimpleClassLoaderTest
    // 然后 自己的加载器， 我们在自己的SimpleClassLoaderTest里是看不到 SimpleClassLoader 加载的SimpleObject的
    // Boot.Ext.App.SimpleClassLoader.com.learn.concurrent. classloader.chapter5.RuntimePackage


    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        SimpleClassLoader simpleClassLoader = new SimpleClassLoader();
        Class<?> aClass = simpleClassLoader.loadClass("com.learn.concurrent.classloader.chapter5.SimpleObject");
        // 这里转换会 报错,但是这两个是一样，为什么会出现这样的问题
        // 这是因为 运行是的包 造成的 ， 命名空间不同
        SimpleObject instance= (SimpleObject)aClass.newInstance();

    }
}

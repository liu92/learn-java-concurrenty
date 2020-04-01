package com.learn.concurrent.classloader.chapter2;

/**
 * @ClassName: BootClassLoader
 * @Description:
 * @Author: lin
 * @Date: 2020/4/1 14:27
 * History:
 * @<version> 1.0
 */
public class BootClassLoader {
    public static void main(String[] args) throws ClassNotFoundException {
        System.out.println(System.getProperty("sun.boot.class.path"));
        System.out.println(System.getProperty("java.ext.dirs"));

        // 系统加载器 是 AppClassLoader
        Class<?> aClass = Class.forName("com.learn.concurrent.classloader.chapter2.SimpleObject");
        System.out.println(aClass.getClassLoader());
        // 系统加载器的父加载器 是扩展加载器 , ExtClassLoader
        System.out.println(aClass.getClassLoader().getParent());

        //扩展加载器的父类加载器是根加载器 BootstrapClassLoader,
        // 而这个根加载器是用c++ 语言写 的 所以 这里 打印出来是null
        System.out.println(aClass.getClassLoader().getParent().getParent());


        //父委托机制
        Class<?> clazz = Class.forName("java.lang.String");
        System.out.println(clazz);
        // 如果这个类 的父加载器是 系统加载器那么 ，那么我们添加这个包下面的String类中静态块应该
        // 打印出来， 如果不是那么 就是根加载器  打印出为 null

        // 这个打印结果是 null
        System.out.println(clazz.getClassLoader());
        // 所以这个父委托机制的一个安全机制，如果你写了一个Object类 优先级有比较高，所以的类都继承
        // Object 这样出问题，你就可以去修改别人的东西了。
    }
}

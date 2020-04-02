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
        // 我们要的效果就是 让我们自己定义加载器去加载这个类
        // 我们通过这种方式打破了双委托机制， 这个去加载的时候 首先将java.* 包的
        // 给他的父加载器加载，
        SimpleClassLoader simpleClassLoader = new SimpleClassLoader();
//        Class<?> aClass = simpleClassLoader.loadClass("com.learn.concurrent.classloader.chapter5.SimpleObject");
//        System.out.println(aClass.getClassLoader());


        // 注意:如果我们将 自己在目录中加一个java.lang.xxx 的包 然后里面写一个
        // String 的类,  我那个将其代码编译好了之后 将我们自己写的
        // java.lang.String 的class 放到和 "D:\\app\\revert" 这个同一目录下
        // 就是这样 D:\app\revert\java\lang\String.class
        // 然后 我将自己定义的SimpleClassLoader加载器 中 loadClass 取消对 java.lang.xxx
        // 加载的判断, 然后执行代码
        Class<?> aClass = simpleClassLoader.loadClass("java.lang.String");
        System.out.println(aClass.getClassLoader());
        // 这打印的结果是 java.lang.SecurityException: Prohibited package name: java.lang
        // 从这个错误来看 报了一个安全的异常错误, 禁止加载 java.lang的包，
        //
        // 一般面试就会问 可不可以打破 双委托机制: 这里我们实验了可以打破
        // 但是java里面对 java.lang.xxx 的加载 进行了安全的验证 和检查
        // 所以 如果我们自己写一个java.lang.xxx 的包然后 用自己的classLoader进行加载
        // 那么就会 出现安全异常
    }

}

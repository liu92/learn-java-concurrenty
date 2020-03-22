package com.learn.concurrenty;

/**
 * String.intern()返回引用的测试
 * @ClassName: RuntimeConstantPoolOom
 * @Description:
 * @Author: lin
 * @Date: 2020/3/22 12:45
 * History:
 * @<version> 1.0
 */
public class RuntimeConstantPoolOom {
    public static void main(String[] args) {
         String str1 = new StringBuilder("计算机").append("软件").toString();
         System.out.println(str1.intern() == str1);
         String str2 = new StringBuilder("ja").append("va").toString();
         System.out.println(str2.intern() == str2);

         //现在在jdk1.8.0_171 版本测试，测试结果是
         // true 和 false
        /**
         * 在jdk1.7 以前的版本 字符串常量 放在方法区中，这个方法区很多人更愿意将其称为“永久代”，本质上来说两者并不相当
         * 在jdk1.7的 HotSpot中， 已经将原本放在方法区中字符串常量池 移出。
         *
         * 那么上述的结果是怎么一回事呢？
         * 产生差异的原因是：在JDK 6中，intern()方法会把首次遇到的字符串实例复制到永久代的字符串常量池中存储，
         * 返回的也是永久代里面这个字符串实例的引用，而由StringBuilder创建的字符串实例在Java堆上，
         * 所以必然不是同一个引用，将返回false。
         *
         * 而JDK 1.7（以及部分其他虚拟机，例如JRocki）的intern()实现就不需要再拷贝字符串的实例到永久代了，
         * 既然字符串常量池已经移到了Java堆中，那只需要在常量池里记录一下首次出现的实例引用即可，
         * 因此intern()返回的引用和由StringBuilder创建的那个字符串实例就是同一个。
         *
         * 测试版本是jdk1.8.0_171
         *
         * 对str2比较返回false是因为“java”这个字符串在执行StringBuilder.toString()之前已经出现过，
         * 字符串常量池中已经有它的引用了，
         * 不符合intern()方法要求“首次出现”的原则，而“计算机软件”这个字符串则是首次出现的，因此返回true。
         * 为什么？
         *  从上面的 “java”这个字符串在执行StringBuilder.toString()之前已经出现过，这句话知道 “java”这个字符串已经出现过了？
         *  那么是在哪里出现过的呢？
         *  是在加载   sun.misc.Version 这个类的时候进入常量池，这个问题在知乎上面被R大提出来了，
         *  https://www.zhihu.com/question/51102308/answer/124441115
         *
         *  这个Version类中有一个静态变量 ，并且这个变量值就是 java
         *  private static final String launcher_name = "java";
         *  根据R大的回答我们知道，在System类中这个值加载是虚拟机自动调用的
         *  而其initializeSystemClass方法会调用sun.misc.Version.init()方法。
         *
         *  这里我们就可以知道了：
         *  Java标准库在JVM启动过程中会调用sun.misc.Version的init()方法。
         *  所以sun.misc.Version会进行类加载的操作，而类加载的初始化时，会对静态常量字段进行真正的赋值操作，
         *  但是，sun.misc.Version的launcher_name字段是final，引用的字符串“java”准备阶段就被intern到了字符串常量池里面了。
         */

    }
}

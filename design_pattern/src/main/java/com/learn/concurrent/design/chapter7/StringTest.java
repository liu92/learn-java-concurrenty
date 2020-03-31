package com.learn.concurrent.design.chapter7;

/**
 * @ClassName: StringTest
 * @Description:
 * @Author: lin
 * @Date: 2020/3/27 13:36
 * History:
 * @<version> 1.0
 */
public class StringTest {
    public static void main(String[] args) {
        // 不可变对象，
        String s = "hello";
        // 这里其实返回的是一个新的 对象地址，并且这个两个的hashcode都不想等
        String s2 = s.replace("l", "k");
        System.out.println(s.getClass() + " " + s.hashCode());
        System.out.println(s2.getClass() + " " + s2.hashCode());
        System.out.println(s.equals(s2));
    }
}

package com.learn.concurrenty.design.chapter7;

/**
 * 1.不可变对象一定是线程安全的（这里暂时不说反射）
 *
 * 2.可变对象不一定是不安全的
 *
 * @ClassName: Person
 * @Description:
 * @Author: lin
 * @Date: 2020/3/27 12:28
 * History:
 * @<version> 1.0
 */
public class Person {
    private final  String name;
    private final  String address;

    public Person(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}

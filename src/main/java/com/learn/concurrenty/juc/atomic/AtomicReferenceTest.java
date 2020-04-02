package com.learn.concurrenty.juc.atomic;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @ClassName: AtomicReferenceTest
 * @Description:
 * @Author: lin
 * @Date: 2020/4/2 16:26
 * History:
 * @<version> 1.0
 */
public class AtomicReferenceTest {
    public static void main(String[] args) {
        //4个字节的引用类型
        AtomicReference<Simple> atomicReference = new AtomicReference<Simple>(new Simple("lin",12));
        System.out.println(atomicReference.get());
        atomicReference.compareAndSet(new Simple("sdfs", 22),
                new Simple("sdfs",234));
    }


    static class Simple{
        private String name;
        private int age;
        Simple(String name, int age){
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}



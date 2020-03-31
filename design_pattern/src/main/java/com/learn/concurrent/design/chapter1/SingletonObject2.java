package com.learn.concurrent.design.chapter1;

import java.util.stream.IntStream;

/**
 *使用枚举的方式
 * @ClassName: SingletonObject
 * @Description:
 * @Author: lin
 * @Date: 2020/3/25 17:18
 * History:
 * @<version> 1.0
 */
public class SingletonObject2 {
    private SingletonObject2(){}

    public enum Singleton{

        /**
         *
         */
        INSTANCE;

        private final  SingletonObject2 instance;

        /**
         * 私有的构造器，在定义枚举的时候，这个构造函数已经创建了
         * 只会创建一个对象实例
         */
        Singleton(){
            instance = new SingletonObject2();
        }

        /**
         * 获取，然后初始化
         * @return
         */
        public SingletonObject2 getInstance(){
            return instance;
        }
    }

    /**
     * 通过枚举的方式，去初始化对象
     * @return
     */
    public static SingletonObject2 getInstance(){
        return Singleton.INSTANCE.getInstance();
    }

    public static void main(String[] args) {
        IntStream.rangeClosed(0,40).forEach(i -> new Thread(String.valueOf(i)){
            @Override
            public void run() {
                System.out.println(SingletonObject2.getInstance());
            }
        }.start());
    }
}

package com.learn.concurrenty.design.chapter3;

/**
 * VolatileTest2
 *
 * volatile 的使用, volatile 关键字的可见性
 * 如果不是volatile关键字来修饰这变量，那么其他线程不能感知这个共享变量的变更
 * @ClassName: VolatileTest
 * @Description: volatile 的使用
 * @Author: lin
 * @Date: 2020/3/26 9:01
 * History:
 * @<version> 1.0
 */
public class VolatileTest2 {
    /**
     * INIT_VALUE 变量都不加上 volatile 修饰都会造成缓存 不一致情况
     *
     * 当加上 volatile 关键字后 使其睡眠时间更短 一下那么就会出现 相同的数字
     * 这样 就没有保障原子性
     */
    private  static int INIT_VALUE = 0;

    private static  final int MAX_LIMIT = 5;

    /**
     * 比如当  INIT_VALUE =10 时
     * 下面代码 的 //  ++INIT_VALUE
     * 分为在jvm分为几个步骤
     * 1.从主内存中个读取INIT_VALUE ->10
     * 2. 将这个值加1 INIT_VALUE = 10 + 1（注意这里已经从主内存中读取出来了，所以不是INIT_VALUE = INIT_VALUE + 1 这样了）
     * 3. 然后将值赋值给INIT_VALUE =11
     * @param args
     */
    public static void main(String[] args) {
        new Thread(()->{
            while (INIT_VALUE < MAX_LIMIT){
                //而这里 ，这两个线程都会有对 INIT_VALUE 写的操作
                // 所以会去更新主内存
                //  ++INIT_VALUE
                System.out.println("T1->" +(++INIT_VALUE));
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"ADDER-1").start();


        new Thread(() ->{
            while (INIT_VALUE < MAX_LIMIT){
                try {
                    System.out.println("T2->" +(++INIT_VALUE));
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "ADDER-2").start();
    }
}

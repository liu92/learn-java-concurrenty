package com.learn.concurrent.design.chapter3;

/**volatile 的使用, volatile 关键字的可见性
 * 如果不是volatile关键字来修饰这变量，那么其他线程不能感知这个共享变量的变更
 * @ClassName: VolatileTest
 * @Description: volatile 的使用
 * @Author: lin
 * @Date: 2020/3/26 9:01
 * History:
 * @<version> 1.0
 */
public class VolatileTest {
    private volatile static int INIT_VALUE = 0;

    private static  final int MAX_LIMIT = 5;

    public static void main(String[] args) {
        new Thread(()->{
            int localValue = INIT_VALUE;
            while (localValue < MAX_LIMIT){
                //如果没有 volatile关键字 ，那么这里判断的值一直相当
                //因为这个 只是对INIT_VALUE的读取，
                // 那么就认为 不需要更新，也不会去主内存中读取数据，
                // 直接从 缓存中去拿取
                if(localValue != INIT_VALUE) {
                    System.out.printf("The value updated to [%d]\n", INIT_VALUE);
                    localValue = INIT_VALUE;
                }
            }
        },"READER").start();


        new Thread(() ->{
            int localValue = INIT_VALUE;
            while (INIT_VALUE < MAX_LIMIT){
                System.out.printf(" updated the value to [%d]\n", ++localValue);
                INIT_VALUE = localValue;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "UPDATER").start();
    }
}

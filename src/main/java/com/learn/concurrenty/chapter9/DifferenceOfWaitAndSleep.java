package com.learn.concurrenty.chapter9;

import java.util.stream.Stream;

/**
 * @ClassName: DifferenceOfWaitAndSleep
 * @Description: wait和sleep的区别
 * @Author: lin
 * @Date: 2020/3/24 9:03
 * History:
 * @<version> 1.0
 */
public class DifferenceOfWaitAndSleep {

    private static final  Object LOCK = new Object();

    public static void main(String[] args) {
        Stream.of("T1","T2").forEach(na -> {
//            new Thread(() -> m1());
            new Thread(() -> m2());
        });

    }

    public static  void  m1(){
        //这里如果加上synchronize 那么两个线程就是去争抢
        synchronized (LOCK) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public static  void  m2(){
        synchronized (LOCK) {
            try {
                //在调用wait方法时候 要结合 synchronize来使用，
                // 因为这个wait 必须要持有monitor, 那么这样使用的谁来做monitor喃
                // 这里使用的LOCK对象做的monitor。所以这里这里 加上synchronized(LOCK)
                System.out.println("The Thread " + Thread.currentThread().getName() + "enter.");
                LOCK.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

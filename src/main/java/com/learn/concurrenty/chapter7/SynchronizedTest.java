package com.learn.concurrenty.chapter7;

/**
 *
 * @ClassName: SynchronizedTest
 * @Description:
 * @Author: lin
 * @Date: 2020/3/23 13:35
 * History:
 * @<version> 1.0
 */
public class SynchronizedTest {
    private static  final  Object LOCK = new Object();

    public static void main(String[] args) {
         Runnable runnable = ()->{
           synchronized (LOCK){
               try {
                   Thread.sleep(100_000);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
         };
    }
}

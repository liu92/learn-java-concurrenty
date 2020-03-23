package com.learn.concurrenty.chapter6;

import javafx.concurrent.Worker;

/**
 *
 * @ClassName: ThreadCloseGraceful
 * @Description:
 * @Author: lin
 * @Date: 2020/3/23 9:22
 * History:
 * @<version> 1.0
 */
public class ThreadCloseGraceful4 {

    public static void main(String[] args) {
         ThreadService service = new ThreadService();
         long starTime = System.currentTimeMillis();
         service.execute(()->{
             // load a very heavy resource;
//             while (true){
//
//             }
             try {
                 // 有可能5秒就结束了
                 Thread.sleep(5000);
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
         });
         // 十秒
         service.shutDown(10000);
         long endTime = System.currentTimeMillis();
        System.out.println("时间消耗" + (endTime - starTime));
    }
}

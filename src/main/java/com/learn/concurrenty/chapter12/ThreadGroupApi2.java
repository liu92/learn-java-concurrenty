package com.learn.concurrenty.chapter12;

import java.util.Arrays;

/**
 * @ClassName: ThreadGroupApi
 * @Description: ThreadGroupApi 一些使用
 * @Author: lin
 * @Date: 2020/3/24 17:30
 * History:
 * @<version> 1.0
 */
public class ThreadGroupApi2 {
    public static void main(String[] args) throws InterruptedException {
        ThreadGroup group1 = new ThreadGroup("tGroup1");
        Thread t1 = new Thread(group1, "T1") {
            @Override
            public void run() {
                    try {
                        Thread.sleep(10_000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        // 这里取打断 ,就退出
                        // 那么t2 也会
                    }
            }
        };
//        tg1.setDaemon(true);
        t1.start();
        Thread.sleep(1_000);
        // 最后一个thread执行结束后，会将自动将group1回收，

        System.out.println(group1.isDestroyed());
        // 如果没有回收需要你 手动回收
        group1.destroy();
        System.out.println(group1.isDestroyed());
    }
}

package com.learn.concurrenty.chapter6;

/**
 * 优雅的去中断线程,这里使用打断的方式
 * @ClassName: ThreadCloseGraceful
 * @Description:
 * @Author: lin
 * @Date: 2020/3/23 9:22
 * History:
 * @<version> 1.0
 */
public class ThreadCloseGraceful3 {

    private static  class Worker extends Thread{
        private boolean flag =true;

        @Override
        public void run() {
            while (flag){
               //connection
                // 如果这里取通信的时候，这里BLOCK住了那么
                // 就没有办法去监听 这个Interrupted 信号了
                // 那么如何去中断呢？ 进入下面的ThreadService
            }


        }


    }

    public static void main(String[] args) {
        Worker worker = new Worker();
        worker.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //去打断
        worker.interrupt();
    }
}

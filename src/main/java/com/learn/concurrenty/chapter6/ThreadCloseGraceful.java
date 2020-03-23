package com.learn.concurrenty.chapter6;

/**
 * 优雅的去中断线程, 这里使用的flag去中断线程
 * @ClassName: ThreadCloseGraceful
 * @Description:
 * @Author: lin
 * @Date: 2020/3/23 9:22
 * History:
 * @<version> 1.0
 */
public class ThreadCloseGraceful {

    private static  class Worker extends Thread{
        private volatile boolean start =  true;

        @Override
        public void run() {
            while (start){
                //
            }
        }

        public void  shutDown(){
            this.start = false;
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

        worker.shutDown();
    }
}

package com.learn.concurrenty.juc.untils.phaser;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.IntStream;

/**
 * @ClassName: PhaserExample3
 * @Description:
 * @Author: lin
 * @Date: 2020/4/11 11:16
 * History:
 * @<version> 1.0
 */
public class PhaserExample6 {

    /**
     *
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
        // 动态的增加或者减少


//        final Phaser phaser = new Phaser(7);
        /*Thread thread = new Thread(()->{
            try {
                // 这里会一直等，只有别人调用了interrupt 将其打断才会退出
                phaser.awaitAdvanceInterruptibly(phaser.getPhase());
                System.out.println("***********************");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread.start();

        System.out.println("======================");
        TimeUnit.SECONDS.sleep(10);
        thread.interrupt();
        System.out.println("=======thread.interrupt===============");*/


        //=========================================
        /*
        final Phaser phaser = new Phaser(7);
        Thread thread = new Thread(()->{
            try {
                // 这里会一直等，只有别人调用了interrupt 将其打断才会退出
                phaser.awaitAdvanceInterruptibly(10);
                System.out.println("***********************");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread.start();

        System.out.println("======================");
        TimeUnit.SECONDS.sleep(10);
        thread.interrupt();
        System.out.println("=======thread.interrupt===============");*/


        final Phaser phaser = new Phaser(3);
        Thread thread = new Thread(()->{
            try {
                // 最多等待10秒钟，如果十秒钟没有完成，那么就不等了
                phaser.awaitAdvanceInterruptibly(0, 10 ,TimeUnit.SECONDS);
                System.out.println("***********************");
            } catch (InterruptedException | TimeoutException e) {
                e.printStackTrace();
            }
        });

        thread.start();

    }

    private static  class  AwaitAdvanceTask extends Thread{

        private final Phaser phaser;

        private  AwaitAdvanceTask(Phaser phaser){
            this.phaser = phaser;
        }

        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            phaser.arriveAndAwaitAdvance();
            System.out.println(getName() + " finished work.");
        }
    }




}

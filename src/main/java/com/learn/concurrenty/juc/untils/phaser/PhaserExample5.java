package com.learn.concurrenty.juc.untils.phaser;

import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @ClassName: PhaserExample3
 * @Description:
 * @Author: lin
 * @Date: 2020/4/11 11:16
 * History:
 * @<version> 1.0
 */
public class PhaserExample5 {

    /**
     *
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
        // 动态的增加或者减少
        final Phaser phaser = new Phaser(7);

        // 这里只有6个线程去做，但是 有7个所以 就会造成block
        IntStream.rangeClosed(0, 5).boxed().map(i->phaser).forEach(AwaitAdvanceTask::new);
        // 如果结束了就会从block中退出来，如果没有结束那么就会block住
        phaser.awaitAdvance(phaser.getPhase());
        System.out.println("======================");

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

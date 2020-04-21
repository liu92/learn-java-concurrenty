package com.learn.concurrenty.juc.untils.phaser;

import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: PhaserExample2
 * @Description:
 * @Author: lin
 * @Date: 2020/4/11 11:16
 * History:
 * @<version> 1.0
 */
public class PhaserExample2 {
    private final static Random RANDOM = new Random(System.currentTimeMillis());

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        final Phaser phaser = new Phaser(5);
        int count = 6;
        //从1号运动员开始
        for (int i = 1; i < count; i++) {
            new Athletes(i, phaser).start();
        }
    }

    /**
     * 比如运动员
     * 三项比赛
     */
    static  class Athletes extends  Thread{
        //几号运动员
        private final int  no;
        private final Phaser phaser;

        Athletes(int no, Phaser phaser){
            this.no = no;
            this.phaser = phaser;
        }

        @Override
        public void run() {
            try {
                System.out.println(no + ": start running....");
                TimeUnit.SECONDS.sleep(RANDOM.nextInt(5));
                System.out.println(no + ": end   running....");
                System.out.println("getPhase()==>" + phaser.getPhase());
                phaser.arriveAndAwaitAdvance();


                System.out.println(no + ": start bicycle....");
                TimeUnit.SECONDS.sleep(RANDOM.nextInt(5));
                System.out.println(no + ": end   bicycle....");
                System.out.println("getPhase()==>" + phaser.getPhase());
                phaser.arriveAndAwaitAdvance();

                System.out.println(no + ": start jump....");
                TimeUnit.SECONDS.sleep(RANDOM.nextInt(5));
                System.out.println(no + ": end   jump....");
                System.out.println("getPhase()==>" + phaser.getPhase());
                phaser.arriveAndAwaitAdvance();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

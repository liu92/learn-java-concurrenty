package com.learn.concurrenty.juc.untils.phaser;

import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: PhaserExample3
 * @Description:
 * @Author: lin
 * @Date: 2020/4/11 11:16
 * History:
 * @<version> 1.0
 */
public class PhaserExample4 {
    private final static Random RANDOM = new Random(System.currentTimeMillis());

    /**
     *
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
        // 动态的增加或者减少
//        final Phaser phaser = new Phaser(1);
        /*  System.out.println(phaser.getPhase());
        phaser.arriveAndAwaitAdvance();
        System.out.println(phaser.getPhase());

        phaser.arriveAndAwaitAdvance();
        System.out.println(phaser.getPhase());

        phaser.arriveAndAwaitAdvance();
        System.out.println(phaser.getPhase());*/

         /* System.out.println(phaser.getRegisteredParties());
          phaser.register();
          System.out.println(phaser.getRegisteredParties());
          phaser.register();
          System.out.println(phaser.getRegisteredParties());*/

         /*System.out.println(phaser.getArrivedParties());
         System.out.println(phaser.getUnarrivedParties());*/


         // 批量register
         /*phaser.bulkRegister(10);
         System.out.println(phaser.getRegisteredParties());
         System.out.println(phaser.getArrivedParties());
         System.out.println(phaser.getUnarrivedParties());
         new Thread(phaser::arriveAndAwaitAdvance).start();
         TimeUnit.SECONDS.sleep(1);
         System.out.println("======================");
         System.out.println(phaser.getRegisteredParties());
         System.out.println(phaser.getArrivedParties());
         System.out.println(phaser.getUnarrivedParties());
         */




        // 动态的增加或者减少
        final Phaser phaser = new Phaser(2){
            /**
             */
            @Override
            protected boolean onAdvance(int phase, int registeredParties) {
                return true;
            }
        };
        new OnAdvanceTask("Alex", phaser).start();
        new OnAdvanceTask("jack", phaser).start();
        TimeUnit.SECONDS.sleep(2);
        System.out.println(phaser.getUnarrivedParties());
        System.out.println(phaser.getArrivedParties());
    }

    /**
     * 比如运动员
     * 三项比赛
     */
    static  class OnAdvanceTask extends  Thread{
        private final Phaser phaser;

        OnAdvanceTask(String name, Phaser phaser){
            super(name);
            this.phaser = phaser;
        }

        @Override
        public void run() {
          System.out.println(getName() + " I am start and the phase" + phaser.getPhase());
          phaser.arriveAndAwaitAdvance();
          System.out.println(getName() + " I am end!");

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if("Alex".equals(getName())){
                System.out.println(getName() + " I am start and the phase" + phaser.getPhase());
                phaser.arriveAndAwaitAdvance();
                System.out.println(getName() + " I am end!");
            }
        }

    }





}

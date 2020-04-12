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
public class PhaserExample3 {
    private final static Random RANDOM = new Random(System.currentTimeMillis());

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        // 动态的增加或者减少
        final Phaser phaser = new Phaser(5);
        int count = 5;
        //从1号运动员开始
        for (int i = 1; i < count; i++) {
            new Athletes(i, phaser).start();
        }
        // 有点运动员在 运动中受伤，那么就不能继续比赛了
        new InjureAthletes(5, phaser).start();
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
                sport(phaser, no + ": start running....", no + ": end   running....");

                sport(phaser, no + ": start bicycle....", no + ": end   bicycle....");

                sport(phaser, no + ": start jump....", no + ": end   jump....");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * 运动员 有的在中途中受伤 不能继续比赛了
     */
    static  class InjureAthletes extends  Thread{
        //几号运动员
        private final int  no;
        private final Phaser phaser;

        InjureAthletes(int no, Phaser phaser){
            this.no = no;
            this.phaser = phaser;
        }

        @Override
        public void run() {
            try {
                sport(phaser, no + ": start running....", no + ": end   running....");

                sport(phaser, no + ": start bicycle....", no + ": end   bicycle....");

                System.out.println("oh shit, I am injured, i will be exited");
                // 退出注册
                phaser.arriveAndDeregister();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }


    private  static void sport(Phaser phaser, String x, String y) throws InterruptedException {
        System.out.println(x);
        TimeUnit.SECONDS.sleep(RANDOM.nextInt(5));
        System.out.println(y);
        phaser.arriveAndAwaitAdvance();
    }
}

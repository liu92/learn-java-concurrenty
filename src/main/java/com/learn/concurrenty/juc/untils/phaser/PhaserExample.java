package com.learn.concurrenty.juc.untils.phaser;

import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @ClassName: PhaserExample
 * @Description:
 * @Author: lin
 * @Date: 2020/4/11 11:16
 * History:
 * @<version> 1.0
 */
public class PhaserExample {
    private final static Random RANDOM = new Random(System.currentTimeMillis());

    public static void main(String[] args) {
        final Phaser phaser = new Phaser();
        IntStream.rangeClosed(1, 5).boxed().map(i -> phaser).forEach(Task::new);

        // 将main线程 也注册进去
        phaser.register();
        //让main线程也 到达等待前行
        phaser.arriveAndAwaitAdvance();
        System.out.println("all of worker finished the task.");
    }


    static  class Task extends  Thread{
        private final Phaser phaser;

        Task(Phaser phaser){
            this.phaser = phaser;
            //在运行的时候 动态的去加，这个就不像CyclicBarrier一样 构造时候必须放进去
            this.phaser.register();
            start();
        }

        @Override
        public void run() {
            System.out.println("The Worker ["+ getName() +"] is working....");

            try {
                TimeUnit.SECONDS.sleep(RANDOM.nextInt(5));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            phaser.arriveAndAwaitAdvance();
        }
    }
}

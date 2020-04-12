package com.learn.concurrenty.juc.untils.phaser;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName: PhaserExample3
 * @Description:
 * @Author: lin
 * @Date: 2020/4/11 11:16
 * History:
 * @<version> 1.0
 */
public class PhaserExample7 {

    /**
     *
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
        final Phaser phaser = new Phaser(3);
        new Thread(phaser::arriveAndAwaitAdvance).start();

        TimeUnit.SECONDS.sleep(3);
        System.out.println(phaser.isTerminated());

        // 销毁 不会再等待
        phaser.forceTermination();
        System.out.println(phaser.isTerminated());
    }






}

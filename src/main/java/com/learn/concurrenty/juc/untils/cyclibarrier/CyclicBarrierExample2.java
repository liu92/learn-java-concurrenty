package com.learn.concurrenty.juc.untils.cyclibarrier;

import com.learn.concurrenty.DefaultThreadFactory;

import java.util.concurrent.*;

/**
 * @ClassName: CylicBarrierExample1
 * @Description:
 * @Author: lin
 * @Date: 2020/4/9 22:18
 * History:
 * @<version> 1.0
 */
public class CyclicBarrierExample2 {


    public static void main(String[] args) throws InterruptedException {
        final CyclicBarrier cyclicBarrier = new CyclicBarrier(2 );

        new Thread(){
            @Override
            public void run() {
                try {
                    TimeUnit.MILLISECONDS.sleep(5);
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }.start();


        new Thread(){
            @Override
            public void run() {
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        TimeUnit.MILLISECONDS.sleep(6);
        System.out.println(cyclicBarrier.getNumberWaiting());
        System.out.println(cyclicBarrier.getParties());
        System.out.println(cyclicBarrier.isBroken());
        // reset 等价于 initial 等价于finished
        cyclicBarrier.reset();


        System.out.println(cyclicBarrier.getNumberWaiting());
        System.out.println(cyclicBarrier.getParties());
        System.out.println(cyclicBarrier.isBroken());
    }



}


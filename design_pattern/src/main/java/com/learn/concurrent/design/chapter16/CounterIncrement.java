package com.learn.concurrent.design.chapter16;

import java.util.Random;

/**
 * 两阶段结束 模式
 * Two-Phase-Termination design pattern
 * @ClassName: CounterIncrement
 * @Description:
 * @Author: lin
 * @Date: 2020/3/28 22:21
 * History:
 * @<version> 1.0
 */
public class CounterIncrement extends  Thread{
    private  volatile  boolean terminated = false;
    private  int counter = 0;
    private static  final Random RANDOM = new Random(System.currentTimeMillis());

    @Override
    public void run() {
         try {
             while (!terminated){
                 System.out.println(Thread.currentThread().getName() + " " + counter++);
                 Thread.sleep(RANDOM.nextInt(1000));
             }
         }catch (InterruptedException e){
             e.printStackTrace();
         }
         finally {
           this.clean();
         }
    }

    private  void  clean(){
        System.out.println("do some clean work for the second phase, current counter " + counter);
    }

    public void  close(){
        this.terminated = true;
        this.interrupt();
    }
}

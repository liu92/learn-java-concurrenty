package com.learn.concurrenty.juc.untils.condition;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName: ConditionExample
 * @Description:
 * @Author: lin
 * @Date: 2020/4/10 14:32
 * History:
 * @<version> 1.0
 */
public class ConditionExample {

    private final static ReentrantLock LOCK = new ReentrantLock();
    private final static Condition CONDITION = LOCK.newCondition();
    private static int data = 0;
    private static volatile boolean noUse = true;

    public static void main(String[] args) {
        new Thread(()->{
            for (; ;) {
              buildData();
            }
        }).start();

        new Thread(()->{
            for (; ;) {
                useData();
            }
        }).start();
    }


    public static void buildData(){
        try {
            LOCK.lock();    // synchronize key word  #monitor enter
            while (noUse){
                // 看看是否已使用，如果未使用那么就不能生产，就继续等待
                // 等着数据被消费
                CONDITION.await();   // monitor.wait()
            }
            // 如果消费了，那么这里就生产
            data++;
            Optional.of("Produce:" + data).ifPresent(System.out::println);
            // 休眠，模拟这个生产过程比较慢
            TimeUnit.SECONDS.sleep(1);
            // 生产了那么这里就是 没有使用
            noUse = true;
            // 生产好了之后进行通知
            CONDITION.signal();     // monitor.notify()
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            LOCK.unlock();      // synchronize end  #monitor exit
        }
    }

    public static void useData(){
        try {
            LOCK.lock();
            while (!noUse){
                CONDITION.await();
            }
            TimeUnit.SECONDS.sleep(1);
            Optional.of("Consumer:" + data).ifPresent(System.out::println);
            noUse = false;
            CONDITION.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
             LOCK.unlock();
        }
    }

}

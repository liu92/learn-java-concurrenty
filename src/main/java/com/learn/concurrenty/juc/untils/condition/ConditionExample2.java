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
public class ConditionExample2 {

    private final static ReentrantLock LOCK = new ReentrantLock(true);
    private final static Condition CONDITION = LOCK.newCondition();
    private static int data = 0;
    private static volatile boolean noUse = true;

    /**
     * 不能100%保证公平
     * 在上面公平锁测试情况暂时没有看出什么问题，但是在非公平锁下面就会有问题
     * 可能出现  生产者 一直生产 但是消费者没有消费
     *
     * 1. 不使用condition  只是使用lock 这种方式可以吗？  不能
     * 2. the producer get the lock but invoke await method and not jump out the lock statement block
     * why the consumer can get the lock still ?
     * 3. 不使用 lock 只使用 condition ?
     * @param args
     */
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
//            while (noUse){
//                // 看看是否已使用，如果未使用那么就不能生产，就继续等待
//                // 等着数据被消费
//                CONDITION.await();   // monitor.wait()
//            }
            // 如果消费了，那么这里就生产
            data++;
            Optional.of("Produce:" + data).ifPresent(System.out::println);
            // 休眠，模拟这个生产过程比较慢
            TimeUnit.SECONDS.sleep(1);
            // 生产了那么这里就是 没有使用
//            noUse = true;
            // 生产好了之后进行通知
//            CONDITION.signal();     // monitor.notify()
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            LOCK.unlock();      // synchronize end  #monitor exit
        }
    }

    public static void useData(){
        try {
            LOCK.lock();
//            while (!noUse){
//                CONDITION.await();
//            }
            TimeUnit.SECONDS.sleep(1);
            Optional.of("Consumer:" + data).ifPresent(System.out::println);
    //            noUse = false;
    //            CONDITION.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
             LOCK.unlock();
        }
    }

}

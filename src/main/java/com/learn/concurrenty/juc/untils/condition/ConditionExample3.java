package com.learn.concurrenty.juc.untils.condition;

import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

/**
 * @ClassName: ConditionExample
 * @Description:
 * @Author: lin
 * @Date: 2020/4/10 14:32
 * History:
 * @<version> 1.0
 */
public class ConditionExample3 {

    private final static ReentrantLock LOCK = new ReentrantLock();
    private final static Condition PRODUCE_COND = LOCK.newCondition();
    private final static Condition CONSUME_COND = LOCK.newCondition();
    private final static LinkedList<Long> TIMESTAMP_POOL = new LinkedList<>();
    private final static int MAX_CAPACITY = 100;

    /**
     * @param args
     */
    public static void main(String[] args) {
        IntStream.range(0, 6).boxed().forEach(ConditionExample3::beginProduce);
        IntStream.range(0, 13).boxed().forEach(ConditionExample3::beginConsume);
    }

    private static void beginProduce(int i){
        new Thread(()->{
            for (;;) {
                produce();
                sleep(2);
            }
        },"-P-" + i).start();
    }
    private static void beginConsume(int i){
        new Thread(()->{
            for (;;) {
                consumer();
                sleep(2);
            }
        },"-C-" + i).start();
    }


    private static void produce(){
        try {
           LOCK.lock();
           while (TIMESTAMP_POOL.size() >= MAX_CAPACITY){
               PRODUCE_COND.await();
           }
           long value = System.currentTimeMillis();
            System.out.println(Thread.currentThread().getName() + "-P-" + value);
            TIMESTAMP_POOL.addLast(value);
            CONSUME_COND.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            LOCK.unlock();
        }
    }

    private static void consumer(){
        try {
            LOCK.lock();
            while (TIMESTAMP_POOL.isEmpty()){
                CONSUME_COND.await();
            }
            //从头开始消费
            Long value = TIMESTAMP_POOL.removeFirst();
            System.out.println(Thread.currentThread().getName() + "-C-" + value);

            PRODUCE_COND.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            LOCK.unlock();
        }
    }

    private static  void sleep(long sec){
        try {
            TimeUnit.SECONDS.sleep(sec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



}

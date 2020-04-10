package com.learn.concurrenty.juc.untils.exchanger;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @ClassName: ExchangerExample1
 * @Description:
 * @Author: lin
 * @Date: 2020/4/10 8:43
 * History:
 * @<version> 1.0
 */
public class ExchangerExample2 {
    public static void main(String[] args) {
        final Exchanger<Integer> exchanger1 = new Exchanger<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                AtomicReference<Integer> value = new AtomicReference<>(1);
                try {
                    while (true){
                        value.set(exchanger1.exchange(value.get()));
                        System.out.println("Thread A has Value:"+value.get()+"");
                        TimeUnit.SECONDS.sleep(3);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " end.");
            }
        }, "=====A=====").start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                AtomicReference<Integer> value = new AtomicReference<>(2);
                try {
                    while (true){
                        value.set(exchanger1.exchange(value.get()));
                        System.out.println("Thread B has Value:"+value.get()+"");
                        TimeUnit.SECONDS.sleep(2);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " end.");
            }
        }, "=====A=====").start();
    }
}

package com.learn.concurrenty.chapter9;

import java.util.stream.Stream;

/**
 * 在多个生产者和消费者的情况下 ，使用while的方式
 * @ClassName: ProduceConsumerVersion
 * @Description:
 * @Author: lin
 * @Date: 2020/3/23 16:17
 * History:
 * @<version> 1.0
 */
public class ProduceConsumerVersion3 {
    private int i = 0;
    private  final Object lock = false;
    /**
     * 这里 让线程更新的值被另一个线程可见， 可见性
     */
    private volatile boolean isProduced = false;

    /**
     * 生产者
     */
    public void produce (){
        synchronized (lock){
            // 判断是否已经生产了数据
            while(isProduced){
                //如果生产了那么就让线程等待，让消费者去消费
                try {
                    // wait 会释放锁， 如果在多线程情况下，
                    // 这里不使用while循环进行再次判断，那么就会造成假死的情况
                    // 比如：有两个线程 当第一个线程执行完这个wait方法后 会释放锁
                    // 也就是唤醒
                    // 那 进入下面的代码逻辑 生产产品 ，isProduced被更新为true
                    // 那么其他的线程进入了之后 就会去判断，这个判断条件符合就会进入
                    // while循环 然后进行等待, 如果这个添加不符合就去生产 数据
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
                //如果没有生产那么就 生产
                i++;
                System.out.println("p ->" + i);
                // 生产好了之后 去通知消费者
                lock.notifyAll();
                isProduced = true;
        }
    }

    /**
     * 消费者
     */
    public void consume(){
        synchronized (lock){
            // 如果 没有生产数据那就等待
            while(!isProduced){
                //如果没有生产那么，就等待生成者生产
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("c ->" + i);
            //消费完了之后，通知生产者去生成
            lock.notifyAll();
            isProduced = false;

        }
    }

    public static void main(String[] args) {
        ProduceConsumerVersion3 p = new ProduceConsumerVersion3();
        Stream.of("p1","p2", "p3").forEach(n ->{
            Thread t1= new Thread(() -> {
                while (true) {
                    p.produce();
                }
            });
            t1.start();
        });


        Stream.of("c1","c2", "c3", "c4").forEach(n -> {
            Thread t2 = new Thread(() -> {
                while (true) {
                    p.consume();
                }
            });
            t2.start();
        });
    }

}

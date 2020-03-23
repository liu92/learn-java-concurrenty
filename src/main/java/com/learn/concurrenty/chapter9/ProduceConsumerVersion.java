package com.learn.concurrenty.chapter9;

/**
 * 线程间的通信
 * @ClassName: ProduceConsumerVersion
 * @Description:
 * @Author: lin
 * @Date: 2020/3/23 16:17
 * History:
 * @<version> 1.0
 */
public class ProduceConsumerVersion {
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
            if(isProduced){
                //如果生产了那么就让线程等待，让消费者去消费
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else{
                //如果没有生产那么就 生产
                i++;
                System.out.println("p ->" + i);
                // 生产好了之后 去通知消费者
                lock.notify();
                isProduced = true;
            }
        }
    }

    /**
     * 消费者
     */
    public void consume(){
        synchronized (lock){
            //首先判断是否生成了，如果生成了，那么消费者就消费
            if(isProduced){
                System.out.println("c ->" + i);
                //消费完了之后，通知生产者去生成
                lock.notify();
                isProduced = false;
            }else {
                //如果没有生产那么，就等待生成者生产
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        ProduceConsumerVersion p = new ProduceConsumerVersion();

        Thread t1= new Thread(){
            @Override
            public void run() {
                while (true) {
                    p.produce();
                }
            }
        };
        t1.start();

        Thread t2= new Thread(){
            @Override
            public void run() {
                while (true) {
                    p.consume();
                }
            }
        };
        t2.start();
    }
}

package com.learn.concurrent.design.chapter2;

/**
 * 多线程的休息室WaitSet
 * @ClassName: WaitSet
 * @Description:
 * @Author: lin
 * @Date: 2020/3/25 22:40
 * History:
 * @<version> 1.0
 */
public class WaitSet2 {

    private static  final Object LOCK = new Object();

    /**
     * 这里有个疑问就，在将这个线程 唤醒后，
     * 下面这个方法中 会不会去执行第一个打印语句？
     * 因为在这个synchronized同步 块中，当线程wait后被唤醒。这里的执行时怎么样的？
     *
     * 从执行的结果来看，这个会打印，但是这个 线程被唤醒之后 程序不会倒着执行，
     * 因为这个wait时候会记录一个输出地址，当被唤醒的时候 从这个记录的地址进行恢复
     *
     *
     */
    private  static  void  work(){
        synchronized (LOCK){
            System.out.println("begin...................");
            try {
                System.out.println("Thread will coming");
                // wait完之后 唤醒 ，必须要去抢锁，但是这个代码执行时有记录的
                // wait 输出的地址，还有本身执行的地址 会被记录下来，下次执行的时候 进行地址恢复
                // 然后继续往下走
                LOCK.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Thread will out");
        }

        //打印结果
//        begin...................
//        Thread will coming
//        Thread will out

    }

    /**
     *  wait set 在jvm中明确的规定
     * 1.所有的对象都会有一个wait set,用来存放调用了该对象wait方法之后进入block状态线程
     * 2.线程被notify之后，不一定立即得到执行
     * 3.线程从 wait set中被唤醒顺序不一定是 FIFO.
     * 4.线程被唤醒后，必须重新获取锁
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
        new Thread(()->{
            work();
        }).start();

        Thread.sleep(1000);
        synchronized (LOCK){
            LOCK.notify();
        }
  }
}

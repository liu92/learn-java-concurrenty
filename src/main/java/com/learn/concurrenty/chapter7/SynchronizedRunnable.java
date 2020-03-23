package com.learn.concurrenty.chapter7;

/**
 * 测试同步方法
 * @ClassName: SynchronizedRunnable
 * @Description:
 * @Author: lin
 * @Date: 2020/3/23 13:35
 * History:
 * @<version> 1.0
 */
public class SynchronizedRunnable implements Runnable{

    private  int index = 1;

    private final  static  int  MAX =500;
    private final  Object MONITOR = new Object();

    /**
     * 1、
     * 同步方法，这个锁就是 this ,而这个this就是SynchronizedRunnable的一个实例
     *
     *  //同步方法的形式， 这种方法是多个线程去争抢这个同步
     *   方法锁，那个先抢到了就去执行。 比如有三个线程
     *  那个线程抢到了锁 就执行，当一个线程进入这个同步块后 去执行下面的逻辑
     *  在执行完后，其他的线程 进入到这个判断，而这个时候 index 已经大于了 500
     *  所以就退出了
     */
//    @Override
//    public synchronized void run() {
//        while (true) {
//                if (index > MAX) {
//                    break;
//                }
//                try {
//                    Thread.sleep(5);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                System.out.println(Thread.currentThread().getName() +
//                        " 的号码是：" + (index++));
//            }
//    }


    /**
     * 2、
     * 下面方法
     */
    @Override
    public  void run() {
        while (true) {
            if(ticket()){
                break;
            }
        }
    }


    /**
     * 同步方法
     * @return
     */
    private synchronized boolean ticket(){
        if(index > MAX){
            return  true;
        }
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() +
                " 的号码是：" + (index++));
       return  false;
    }
}

package com.learn.concurrenty.chapter6;

/**
 * 优雅的去中断线程,这里使用打断的方式
 * @ClassName: ThreadCloseGraceful
 * @Description:
 * @Author: lin
 * @Date: 2020/3/23 9:22
 * History:
 * @<version> 1.0
 */
public class ThreadCloseGraceful2 {

    private static  class Worker extends Thread{

        @Override
        public void run() {
            while (true){
                try {
                    Thread.sleep(1);
                    //sleep 会去捕获的打断信号
                } catch (InterruptedException e) {
                    break;// return 的问题是这个 会直接退出去了
                    // 而break 在这个线程里面有可能 还会做一些其他事情，
                    // 它会有机会去做其他的事情，而使用 return 那么后面的逻辑有可能就执行不到了
                }
            }

            //2、
//            while (true){
//                //如果这里不去sleep，而是去判断，那么会退出吗?
//                // 可以看的这种 会去判断是不是被中断的，所以就直接退出去了
//                // 如果当前线程被打断了 那是true ,否则是false;
//                if(Thread.interrupted()){
//                    break;
//                }
//            }
        }


    }

    public static void main(String[] args) {
        Worker worker = new Worker();
        worker.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //去打断
        worker.interrupt();
    }
}

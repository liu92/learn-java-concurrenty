package com.learn.concurrenty.chapter4;

/**
 * @ClassName: DaemonThread
 * @Description: 守护线程
 * @Author: lin
 * @Date: 2020/3/20 16:14
 * History:
 * @<version> 1.0
 */
public class DaemonThread2 {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(() -> {
            Thread innerThread = new Thread(() ->{
                try {
                   while (true){
                       System.out.println("Do some thing for health check");
                       Thread.sleep(10000);
                   }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            //如果这里不设置为守护线程，我们可以看看这个线程是否退出，
            // 测试结果 是如果不设置为守护线程，
            // 那么在t线程结束后这个application不会退出
            // jvm 会判断里面还active的非daemon线程
            innerThread.setDaemon(true);
            //如果这里没有启动，没有线程当然退出了
            innerThread.start();

            //在t线程里面再创建一个线程，然后设置为daemon=true,
            // 如果这个t结束 后这个再次创建的线程会结束吗?
            try {
                Thread.sleep(1000);
                System.out.println("T thread finish done");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t.start();
        //打印结果如下, 可以看到 这个t线程结束后，里面的innerThread线程也结束了，
        //这就好比建立一个通讯连接的线程，里面就是给它创建一个心跳的检查类似。
        //如果连接失败了 会主动关闭，不需要手动的去关闭。
//        > Task :DaemonThread2.main()
//        T thread finish done

    }
}

package com.learn.concurrenty.juc.untils.semaphore;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 *
 * @ClassName: SemaphoreExample
 * @Description:
 * @Author: lin
 * @Date: 2020/4/10 9:30
 * History:
 * @<version> 1.0
 */
public class SemaphoreExample3 {
    public static void main(String[] args) throws InterruptedException {
         final  Semaphore semaphore = new Semaphore(1);
        Thread t1 = new Thread() {
            @Override
            public void run() {
                try {
                    semaphore.acquire();
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
                System.out.println("T1 finished");
            }
        };
        t1.start();

        TimeUnit.MICROSECONDS.sleep(50);

        // 因为只有一个 许可证, 被线程t1拿到了，所以去拿取拿不到
        // 如果拿取不到 那么想放弃去拿取 怎么办喃。
        Thread t2 = new Thread() {
            @Override
            public void run() {
                try {
                    semaphore.acquire();
                    // 这个方法没有中断，也是说你中断它，他也不会有什么提示
                    //semaphore.acquireUninterruptibly();
//                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
                System.out.println("T2 finished");
            }
        };
        t2.start();

        TimeUnit.MICROSECONDS.sleep(50);
        // 如果拿取不到那么就中断下
        t2.interrupt();

    }


}

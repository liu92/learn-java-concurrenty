package com.learn.concurrenty.chapter11;

/**
 * @ClassName: ThreadException
 * @Description: 线程里面异常，因为在run方法中不能抛出异常，那么怎么将其捕获。
 * @Author: lin
 * @Date: 2020/3/24 16:13
 * History:
 * @<version> 1.0
 */
public class ThreadException {
    private static  final  int A =10;
    private static  final  int B =0;
    public static void main(String[] args) {
        Thread t = new Thread(()->{
            try {
                Thread.sleep(1_000);
                //比如 这里会出现异常，线程死掉,run方法里面抛不出来这个异常
                int  result = A /B;
                System.out.println( result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        // 未捕获的异常处理
        t.setUncaughtExceptionHandler((thread, e)->{
            System.out.println(e);
            System.out.println(thread);
        });
        t.start();

        //运行结果
//        > Task :ThreadException.main()
//        java.lang.ArithmeticException: / by zero
//        Thread[Thread-0,5,main]

    }
}

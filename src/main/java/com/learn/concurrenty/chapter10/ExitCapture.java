package com.learn.concurrenty.chapter10;

/**
 * @ClassName: ExitCapture
 * @Description: 测试在linux中 退出是，进行通知
 * @Author: lin
 * @Date: 2020/3/24 15:29
 * History:
 * @<version> 1.0
 */
public class ExitCapture {
    public static void main(String[] args) {
        //给程序加入勾子, 注入 一个通知，让程序在结束的时候进行通知
      Runtime.getRuntime().addShutdownHook(new Thread(()->{
          System.out.println("The application will be exit.");
          notifyAndRelease();
      }));
      int i =0;
       while (true){
           try {
               Thread.sleep(1_000L);
               System.out.println("I  am working......");
           } catch (Throwable e) {
           }
           //下面的代码如果 写在 try里面，那么到执行if判断时候，就被捕获了
           i++;
           if(i > 20){
               throw  new RuntimeException("error");
           }
       }
    }

    private static  void notifyAndRelease(){
        System.out.println("notify to  the main");
        try {
            Thread.sleep(1_000);
        } catch (Throwable e) {

        }
        System.out.println("will release resource(socket, file, connect)");

        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(" release and notify done.");
    }
}

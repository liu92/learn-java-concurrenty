package com.learn.concurrenty.chapter1;

import com.learn.concurrenty.DefaultThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: TryConcurrent
 * @Description: 尝试使用线程
 * @Author: lin
 * @Date: 2020/3/19 13:36
 * History:
 * @<version> 1.0
 */
public class TryConcurrent {

    public static void main(String[] args) {
        //这种方式就会造成 一件 事情 一件事情的去处理，不会同时去执行
//        readFromData();
//        writeDateToFile();

//         Thread t1 =  new Thread(){
//             @Override
//             public void run(){
//                 for (int i = 0; i <100 ; i++) {
//                     println("Task 1=>" + i);
//                 }
//             }
//         };
//         t1.start();

         //main线程
//        for (int j = 0; j <100 ; j++) {
//            println("Task 2=>" + j);
//        }

        // 由cup去切换执行

        //启动start这个线程是 main线程


//        new Thread("READ-Thread"){
//            @Override
//            public void run() {
//                //执行run方法是 READ-Thread线程执行
//                readFromData();
//            }
//        }.start();
//
//
//        new Thread("WRITE-Thread"){
//            @Override
//            public void run() {
//                readFromData();
//            }
//        }.start();

        //在没有调用start之前不能称之为一个Thread,
        // 只有start之后才能称之为Thread. start这个方法是立即返回它并不会阻塞住，
        // 等到里面的方法都执行完了才会返回。它是立即返回


        //==================================================

        // 如果不使用start方法 而是用run方法那么这个当前线程就是main线程，而不会去重新启动一个线程
         Thread t1 = new Thread("READ-Thread"){
            @Override
            public void run() {
                println(Thread.currentThread().getName());
                readFromData();
            }
        };
        t1.run();
        //这里调用run方法 打印 ，可以看到当前线程就是main 线程 ，而不会去重新启动一个线程，
        // 也就是说 在new Thread()后 没有使用start()方法那么 就不会存在另外的一个线程
//        main
//        Begin read data to file.
//        Read data done and start handle it.
//        The readFromData handle finish and successfully.



        //============================================ 使用线程池创建

//        ExecutorService executorService = new ThreadPoolExecutor(2,
//                5, 200,
//                TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(6),
//                new DefaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
//        executorService.execute(new MyThreadReadFromData("READ-Thread"));
//        executorService.execute(new MyThreadWriteDateToFile("WRITE-Thread"));
//        executorService.shutdown();


    }

    public static  void  readFromData(){
        try {
            println("Begin read data to file.");
            Thread.sleep(1000 );
            println("Read data done and start handle it.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        println("The readFromData handle finish and successfully.");
    }

    public static void writeDateToFile(){
        try {
            println("Begin write data to file.");
            Thread.sleep(2000 );
            println("write data done and start handle it.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        println("The writeDateToFile handle finish and successfully.");
    }

    private static  void println(String message){
        System.out.println(message);
    }
}


class MyThreadReadFromData extends Thread{

    public MyThreadReadFromData(String name){
        super(name);
    }

    @Override
    public void run(){
        TryConcurrent.readFromData();
        System.out.println("MyThreadReadFromData执行当前run方法的线： " + Thread.currentThread().getName());
    }
}



class MyThreadWriteDateToFile extends Thread{

    public MyThreadWriteDateToFile(String name){
        super(name);
    }

    @Override
    public void run(){
        TryConcurrent.writeDateToFile();
        System.out.println("MyThreadWriteDateToFile执行当前run方法的线： " + Thread.currentThread().getName());
    }
}
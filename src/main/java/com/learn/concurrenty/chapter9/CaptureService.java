package com.learn.concurrenty.chapter9;

import javax.swing.text.html.Option;
import java.util.*;

/**
 * @ClassName: CaptureService
 * @Description: 综合案例测试
 * @Author: lin
 * @Date: 2020/3/24 10:09
 * History:
 * @<version> 1.0
 */
public class CaptureService {

    final static  private LinkedList<Control> CONTROLS = new LinkedList<>();

    private static final  int COUNT = 5;
    public static void main(String[] args) {

        List<Thread> worker = new ArrayList<>();
        Arrays.asList("M1", "M2", "M3", "M4", "M5", "M6", "M7", "M8", "M9", "M10")
                .stream()
                .map(CaptureService::createCaptureThread)
                .forEach(t -> {
                    // 不过这里 启动线程的时候 ，不应该启动10个， 这个要学习线程池里面的思路，
                    // 如果 当线程 不大于核心线程数的时候，就直接使用核心线程数里面的线程来启动
                    // 当大于核心线程的时候就将线程加入 工作线程中去
                    t.start();
                    //这里不能join ,因为join 后只有一个线程 去跑
                    // 等这个线程跑完之后再去启动另外的一个线程
                    // 所以 要把它存起来
                    worker.add(t);
                });

        worker.stream().forEach(t -> {
            try {
                //这里来join，并且捕获异常
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        //这个时候线程都启动了并且join了 ，那么这里在打印
        Optional.of("All of capture work finished").ifPresent(System.out::println);
    }

    private static  Thread  createCaptureThread(String name){

        return new Thread(()->{
            // 运行资格，允许需要多个少个线程，多余的线程就wait
            Optional.of("The worker ["+Thread.currentThread().getName() +"] begin capture data")
                    .ifPresent(System.out::println);

            // 控制线程执行个数，如果大于指定的个数 ，那么其他的线程就等待
            synchronized (CONTROLS){
                 while (CONTROLS.size() > COUNT){
                     try {
                         CONTROLS.wait();
                     } catch (InterruptedException e) {
                         e.printStackTrace();
                     }
                 }
                 //所以这个要放在synchronize中，进行同步控制
                CONTROLS.addLast(new Control());
             }

             //下面的执行是并行化的
            Optional.of("The worker [" + Thread.currentThread().getName()+"] is working...")
                    .ifPresent(System.out::println);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (CONTROLS){
                Optional.of("The worker [" + Thread.currentThread().getName()+"] END capture data ...")
                        .ifPresent(System.out::println);
                CONTROLS.removeFirst();
                CONTROLS.notifyAll();
            }
        }, name);
    }

    /**
     * 控制 线程个数
     */
    private static class Control{}
}

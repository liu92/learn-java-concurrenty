package com.learn.concurrenty.design.chapter17;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: Channel
 * @Description:
 * @Author: lin
 * @Date: 2020/3/30 9:20
 * History:
 * @<version> 1.0
 */
public class Channel {

    /**
     * 可以放东西的数量
     */
    private final  static  int MAX_REQUEST = 100;
    /**
     * 拥一个数组存放 商品
     */
    private final RequestCommodity[] requestQueue;

    private  int head;

    private  int tail;
    /**
     * 当前多少个
     */
    private  int count;

    /**
     * 流水线工人
     */
    private final  WorkerThread[] workerPool;

    /**
     * 分配多少个工人去做事情
     * @param workers
     */
    public Channel(int workers){
       this.requestQueue = new  RequestCommodity[MAX_REQUEST];
       this.head = 0;
       this.tail = 0;
       this.count = 0;
       this.workerPool = new WorkerThread[workers];
       init();
    }


   private  void init(){
       for (int i = 0; i < workerPool.length ; i++) {
           workerPool[i]  = new WorkerThread("Worker-" + i, this);
       }
   }

    /**
     * 启动这个传送带，然后流水线工人进行准备
     * push switch to start all of worker to work.
     */
   public  void startWorker(){
       Arrays.asList(workerPool).forEach(WorkerThread::start);
   }

   /**
     把商品 这个request放到 队列中去
    */
    public  synchronized  void put(RequestCommodity request){
        // 如果流水线上面放的东西大于了那个设置的 大小，那么就wait
        // 流水线上面的工人就需要等着了
        while (count >= requestQueue.length){
            try {
                //这个中断会产出一个中断异常
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // 如果没有满，那么就往tail(也就是尾部) 放
        this.requestQueue[tail] = request;
        //如果放满了100个那么 又从0开始, 那么就写一个简单的算法
        this.tail = (tail + 1 ) % requestQueue.length;
        this.count ++ ;
        this.notifyAll();
    }


    public synchronized  RequestCommodity take(){
        //这里 如果 判断只是小于0 ，那么就会造成 取数据的时候出现空指针
        while (count <= 0){
            //如果小于零 那么就wait住
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //如果不小于那么就 从头 开始拿取
        RequestCommodity request = this.requestQueue[head];
        //下一次拿取就是 (this.head + 1) % this.requestQueue.length
        this.head = (this.head + 1) % this.requestQueue.length;
//        startWorker(); 这个加上会报 线程多次start错误
        this.count --;
        this.notifyAll();
        return request;
    }







}

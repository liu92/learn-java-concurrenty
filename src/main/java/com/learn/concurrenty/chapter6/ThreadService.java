package com.learn.concurrenty.chapter6;

/**
 * @ClassName: ThreadService
 * @Description:
 * @Author: lin
 * @Date: 2020/3/23 9:52
 * History:
 * @<version> 1.0
 */
public class ThreadService {
    //可以将这个线程定义成守护线程，如果执行线程退出了那么守护线程也就退出了
    // 也就是说守护线程去做这件事，然后执行线程生命周期结束 你的守护线程也要结束

    /**
     * 定义执行线程
     */
    private Thread executeThread;

    private boolean finished = false;
    public void  execute(Runnable task){
         executeThread = new Thread(){
             @Override
             public void run() {
                //设置一个守护线程
                 Thread runner = new Thread(task);
                 runner.setDaemon(true);
                 // 执行线程 ，这个执行线程结束了，而那个守护线程可能还没有启动就结束了，
                 // 显然这样是不合理的，那么怎么处理呢？ 我们在守护线程里加入 join,
                 //让 runner 执行到死为止，然后在去执行执行线程中的一些逻辑
                 // 这里BLOCK 主，这个线程可能执行时间太长，永远不好吧finished变成true，
                 // 所声明周期会非常的长， 所以这样不好，那么就用别人去显示的调用
                 //
                 try {
                     runner.join();
                     finished =true;
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }
             }
         };

         executeThread.start();
    }

    /**
     * 最多等 多少秒，如果这段时间内没有处理完，那么就将其干掉
     * @param mills
     */
    public void shutDown(long mills){
       // 最多执行30毫秒, 如果线程在3毫秒时就结束了，
        // 我们不能一直等到30毫秒时才去结束
        long currentTime = System.currentTimeMillis();
        while (!finished){
           if(System.currentTimeMillis() - currentTime >= mills){
               System.out.println("任务超时，需要结束它！");
               //怎么去结束呢？ 这个上面的runner的join是当前线程 executeThread去join的
               // 那么通过executeThread 去打断他,在上面 就会被捕获到，
               // 执行线程生命周期结束后，那么守护线程 也自然而然的跟着结束
               executeThread.interrupt();
               break;
           }
            try {
//                executeThread.sleep(1);
                Thread.sleep(1);
            } catch (InterruptedException e) {
                System.out.println("执行线程被打断!");
                e.printStackTrace();
            }
        }
        finished = false;
    }
}

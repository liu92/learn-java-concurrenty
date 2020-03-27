package com.learn.concurrenty.design.chapter9;

import java.util.LinkedList;

/**
 * @ClassName: RequestQueue
 * @Description: 请求队列
 * @Author: lin
 * @Date: 2020/3/27 16:13
 * History:
 * @<version> 1.0
 */
public class RequestQueue {
   private final LinkedList<Request> queue = new LinkedList<>();

   public  Request getRequest(){
       synchronized (queue){
           while (queue.size() <= 0){
               try {
                   queue.wait();
               } catch (InterruptedException e) {
//                  break;
                   return  null;
               }
           }
           return  queue.removeFirst();
       }

   }

    public  void putRequest(Request request){
        synchronized (queue){
            queue.addLast(request);
            queue.notifyAll();
        }
    }

}

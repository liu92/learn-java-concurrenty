package com.learn.concurrenty.design.chapter18;

import java.util.LinkedList;

/**
 * 将方法封装成 methodRequest
 * @ClassName: ActivationQueue
 * @Description:
 * @Author: lin
 * @Date: 2020/3/30 16:16
 * History:
 * @<version> 1.0
 */
public class ActivationQueue {
  private final  static  int MAX_METHOD_REQUEST_QUEUE_SIZE = 100;

  private final LinkedList<MethodRequest> methodQueue;

  ActivationQueue(){
      methodQueue = new LinkedList<>();
  }

  public synchronized void put(MethodRequest request){
      while (methodQueue.size() > MAX_METHOD_REQUEST_QUEUE_SIZE){
          try {
              this.wait();
          } catch (InterruptedException e) {
              e.printStackTrace();
          }
      }
      this.methodQueue.addLast(request);
      this.notifyAll();
  }

  public synchronized MethodRequest take(){
      while (methodQueue.isEmpty()){
          try {
              this.wait();
          } catch (InterruptedException e) {
              e.printStackTrace();
          }
      }
      MethodRequest methodRequest = methodQueue.removeFirst();
      this.notifyAll();
      return methodRequest;
  }






}

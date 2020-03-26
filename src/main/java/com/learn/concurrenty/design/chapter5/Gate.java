package com.learn.concurrenty.design.chapter5;

/**
 * 单线程执行设计模式
 * @ClassName: Gate
 * @Description: 共享资源
 * @Author: lin
 * @Date: 2020/3/26 17:01
 * History:
 * @<version> 1.0
 */
public class Gate {
  private int counter =0;
  private String name = "hhh";
  private String address = "NowHere";

    /**
     * 一次只能一个人通过这个门
     * 这里加 synchronized 是让这个方法一次只有一个人通过
     * @param name
     * @param address
     */
  public synchronized void pass(String name, String address){
      this.counter++;
      this.name=name;
      this.address = address;

  }

  private void verify(){
      if(this.name.charAt(0)!=this.address.charAt(0)){
          System.out.println("**************BROKEN***************" + toString());
      }
  }

    @Override
    public String toString() {
        return "No." +counter+":" +name +"," + address;
    }
}

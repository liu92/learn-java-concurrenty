package com.learn.concurrent.design.chapter18;

/**
 * 将主动对象的每一个方法都抽 为一个命令
 * @ClassName: AbstractMethodRequest
 * @Description: 对应ActiveObject的每一个方法
 * @Author: lin
 * @Date: 2020/3/30 15:40
 * History:
 * @<version> 1.0
 */
public abstract class AbstractMethodRequest {
   public abstract  void execute();

}

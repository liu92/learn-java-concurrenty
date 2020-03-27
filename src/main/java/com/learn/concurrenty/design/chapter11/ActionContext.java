package com.learn.concurrenty.design.chapter11;

/**
 * 使用ThreadLocal实现一个上下文,
 * 通过这个和线程绑定的方式，就不需要传入参数了
 * @ClassName: ActionContext
 * @Description:
 * @Author: lin
 * @Date: 2020/3/27 22:44
 * History:
 * @<version> 1.0
 */
public class ActionContext {

    private static  final  ThreadLocal<Context> threadLocal = new ThreadLocal<Context>(){
        @Override
        protected Context initialValue() {
            return new Context();
        }
    };


    private static class  ContextHolder{
        private final  static ActionContext actionContext = new ActionContext();

    }
    public  static  ActionContext getActionContext(){
        return  ContextHolder.actionContext;
    }

    public Context getContext(){
       return  threadLocal.get();
    }








}

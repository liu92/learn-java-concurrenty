package com.learn.concurrent.design.chapter11;

/**
 * 多线程运行上下文切换
 * @ClassName: ExecutionTask
 * @Description:
 * @Author: lin
 * @Date: 2020/3/27 22:20
 * History:
 * @<version> 1.0
 */
public class ExecutionTask implements Runnable {
    private QueryFromDbAction action = new QueryFromDbAction();
    private QueryFromHttpAction httpAction = new QueryFromHttpAction();
    /**
     */
    @Override
    public void run() {
         Context context = ActionContext.getActionContext().getContext();
       action.execute();
       System.out.println("The name query successful");
       httpAction.execute();
       System.out.println("The card id query successful");
       System.out.println("The Name is " + context.getName() +" and CardId " + context.getCardId());
    }






}

package com.learn.concurrenty.design.chapter11;

/**
 * @ClassName: QueryFromDbAction
 * @Description:
 * @Author: lin
 * @Date: 2020/3/27 22:21
 * History:
 * @<version> 1.0
 */
public class QueryFromDbAction {

//    public  void  execute(Context context){
//        try {
//            Thread.sleep(1000L);
//            String name = "Alex " + Thread.currentThread().getName();
//            context.setName(name);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }

    public  void  execute(){
        try {
            Thread.sleep(1000L);
            String name = "Alex " + Thread.currentThread().getName();
            // 这个就直接去获取上下文，而不是传入进去
            ActionContext.getActionContext().getContext().setName(name);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

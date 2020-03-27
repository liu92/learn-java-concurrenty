package com.learn.concurrenty.design.chapter11;

/**
 * @ClassName: QueryFromHttpAction
 * @Description:
 * @Author: lin
 * @Date: 2020/3/27 22:25
 * History:
 * @<version> 1.0
 */
public class QueryFromHttpAction {

//    public  void  execute(Context context){
//        String name =  context.getName();
//        String cardId = getCardId(name);
//        context.setCardId(cardId);
//    }


    public  void  execute(){
        // 这里我们没有set 东西进去，因为 在 ActionContext有 初始的东西
        Context context = ActionContext.getActionContext().getContext();
        String name =  context.getName();
        String cardId = getCardId(name);
        context.setCardId(cardId);
    }


    /**
     * 通过名字 拿取身份证
     * @param name
     * @return
     */
    private  String getCardId(String name){
        try {

            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return  "234354" + Thread.currentThread().getId();
    }
}

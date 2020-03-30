package com.learn.concurrenty.design.chapter18;

/**
 * @ClassName: DisplayClientThread
 * @Description:
 * @Author: lin
 * @Date: 2020/3/30 17:04
 * History:
 * @<version> 1.0
 */
public class DisplayClientThread extends  Thread {
    private final  ActiveObject activeObject;

    public DisplayClientThread(String name, ActiveObject activeObject){
        super(name);
        this.activeObject = activeObject;
    }

    @Override
    public void run() {
        try {
            for (int i= 0; true; i++){
                String text = Thread.currentThread().getName() + "=>" + i;
                //这个就像调用 system.gc, 然后你去做其他的事情，
                // 而gc也不会立即将其卡住，为什么不卡住？ 是因为应用了
                // 一个原理 是 接收异步消息的主动对象
                activeObject.displayString(text);
                Thread.sleep(200);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

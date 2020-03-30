package com.learn.concurrenty.design.chapter18;

/**
 * @ClassName: MakeClientThread
 * @Description:
 * @Author: lin
 * @Date: 2020/3/30 17:09
 * History:
 * @<version> 1.0
 */
public class MakeClientThread extends Thread {
    private final  ActiveObject activeObject;

    private final  char fillChar;

    MakeClientThread(ActiveObject activeObject, String name){
        super(name);
        this.activeObject = activeObject;
        this.fillChar  = name.charAt(0);
    }

    @Override
    public void run() {
        try {
            for (int i = 0; true ; i++) {
                Result result = activeObject.makeString(i+1, fillChar);
                Thread.sleep(20);
                String value = (String)result.getResultValue();
                System.out.println(Thread.currentThread().getName() + ": value=" +value);
            }
        }catch (Exception e){

        }
    }
}

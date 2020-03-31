package com.learn.concurrent.design.chapter18;

/**
 * @ClassName: ActiveObjectClient
 * @Description:
 * @Author: lin
 * @Date: 2020/3/30 17:14
 * History:
 * @<version> 1.0
 */
public class ActiveObjectClient {
    public static void main(String[] args) {
        // system.gc
        ActiveObject activeObject = ActiveObjectFactory.creatActiveObject();
        new MakeClientThread(activeObject,"Alice").start();
        new MakeClientThread(activeObject,"Bo").start();
        // 这里对象 run方法中 就像调用 gc
        new DisplayClientThread("Chris" , activeObject).start();

        // 就是调用的时候不会卡住，而且不像future一样 只能调一次，这边实现是可以去调多次


    }
}

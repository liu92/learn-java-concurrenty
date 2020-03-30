package com.learn.concurrenty.design.chapter18;

/**
 * @ClassName: Servant
 * @Description: 真正做事情的类
 * @Author: lin
 * @Date: 2020/3/30 15:45
 * History:
 * @<version> 1.0
 */
 class Servant implements  ActiveObject{

    @Override
    public Result makeString(int count, char fillChar) {
        char[] buf = new char[count];
        for (int i = 0; i <count ; i++) {
            buf[i] = fillChar;
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return new RealResult(new String(buf));
    }

    @Override
    public void displayString(String text) {
        try {
            System.out.println("Display:" + text);
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

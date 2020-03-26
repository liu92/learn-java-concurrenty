package com.learn.concurrenty.design.chapter4;

/**
 * @ClassName: OctalObserver
 * @Description: 八进制 server
 * @Author: lin
 * @Date: 2020/3/26 15:24
 * History:
 * @<version> 1.0
 */
public class OctalObserver extends AbstractObserver {

    /**
     * 构造方法，这里需要 主题
     * @param subject
     */
    public OctalObserver(Subject subject){
        super(subject);
    }


    @Override
    public void update() {
        System.out.println("Octal String:" + Integer.toOctalString(subject.getState()));
    }
}

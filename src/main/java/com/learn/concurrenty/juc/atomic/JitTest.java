package com.learn.concurrenty.juc.atomic;

/**
 * @ClassName: JitTest
 * @Description:
 * @Author: lin
 * @Date: 2020/4/2 14:00
 * History:
 * @<version> 1.0
 */
public class JitTest {
    private static volatile   boolean init = false;
    public static void main(String[] args) {
        new Thread(){
            @Override
            public void run() {
                while (!init){
                    /**
                     * 这里有问题
                     * 就是在不同的jdk 下会有不同的情况
                     *
                     * 有的jdk情况下 这里会退出，而有的不会退出
                     * 造成这样的原因是jit 的进行了优化
                     *
                     * 就是在jdk1.8 下 如果这while循环体重没有其他的代码是
                     * 会优化成这样
                     * while(true){}
                     * 如果这个里面有其他的代码 就不会优化,比如这样
                     * while(!init){
                     *     System.out.println(".")
                     * }
                     *
                     * 那么解决这样的问题,就是变量要加上volatile,安装jdk规范来
                     * 避免不必要的争议
                     *
                     */
                }
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
               init = true;
                System.out.println("set init to true.");
            }
        }.start();
    }
}

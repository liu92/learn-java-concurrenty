package com.learn.concurrenty.design.chapter5;

/**
 * @ClassName: Client
 * @Description:
 * @Author: lin
 * @Date: 2020/3/26 17:12
 * History:
 * @<version> 1.0
 */
public class Client {
    public static void main(String[] args) {
        Gate gate = new Gate();
        User bj = new User("bao", "beijing", gate);
        User sh = new User("shang", "shanghai", gate);
        User gz = new User("Guang", "GuangZhou", gate);

        bj.start();
        sh.start();
        gz.start();
    }
}

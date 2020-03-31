package com.learn.concurrent.design.chapter6;

/**
 * 测试 读 写 锁
 * 读和写是冲突的，如果读抢到了锁，那么写 就不能写入
 * ,如果要解决这种问题 那么就在 ReadWriteLock中去修改
 *
 * ReadWriteLock design pattern
 * Reader-Writer design pattern
 * @ClassName: ReadWritLockClient
 * @Description:
 * @Author: lin
 * @Date: 2020/3/27 10:51
 * History:
 * @<version> 1.0
 */
public class ReadWritLockClient {
    public static void main(String[] args) {
        final  ShareData data = new ShareData(10);
        new ReadWorker(data).start();
        new ReadWorker(data).start();
        new ReadWorker(data).start();
        new ReadWorker(data).start();
        new ReadWorker(data).start();

        new WriteWorker(data, "qwertyuiopasdfg").start();
        new WriteWorker(data, "QWERTYUIOPASDFG").start();
    }
}

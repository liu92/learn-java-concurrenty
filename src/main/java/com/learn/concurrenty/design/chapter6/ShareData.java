package com.learn.concurrenty.design.chapter6;

/**
 * @ClassName: ShareData
 * @Description: 读和写的共享数据
 * @Author: lin
 * @Date: 2020/3/27 10:11
 * History:
 * @<version> 1.0
 */
public class ShareData {
    /**
     * 从这个buffer中读数据，
     * 和往这个buffer中写数据
     */
    private final  char[] buffer;

    private final ReadWriteLock lock = new ReadWriteLock();

    public ShareData(int size){
        // 声明一个char 数组
         buffer = new char[size];
        for (int i = 0; i <size ; i++) {
            this.buffer[i]= '*';
        }
    }

    public char[] read() throws  InterruptedException{
        try {
            lock.readLock();
            return this.doRead();
        }finally {
            lock.readUnLock();
        }
    }


    public void write(char c) throws InterruptedException {
        try {
            lock.writeLock();
            this.doWrite(c);
        }finally {
            lock.writeUnLock();
        }
    }

    /**
     * 往buffer中写
     * @param c
     */
    private void  doWrite(char c) {
        for (int i = 0; i <buffer.length ; i++) {
            buffer[i] = c;
            slowly(10);
        }
    }

    /**
     * 创建一个副本
     * @return
     */
    private char[] doRead(){
        // 因为是引用类型，所以重新定义一个，然后将值赋值给它
        char[] newBuffer = new char[buffer.length];
        for (int i = 0; i <buffer.length ; i++) {
            newBuffer[i] =buffer[i];
        }
        // 赋值完成后 进行短暂休眠下
        slowly(50);
        return  newBuffer;
    }

    private void slowly(int millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

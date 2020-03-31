package com.learn.concurrent.design.chapter6;

import java.util.Random;

/**
 * 写 工作者
 * @ClassName: WriteWorker
 * @Description:
 * @Author: lin
 * @Date: 2020/3/27 10:34
 * History:
 * @<version> 1.0
 */
public class WriteWorker  extends  Thread{
    private static final Random random = new Random(System.currentTimeMillis());

    private final  ShareData data;

    private final  String filter;

    private int index = 0;

    public WriteWorker(ShareData data, String filter){
        this.data = data;
        this.filter = filter;
    }

    @Override
    public void run() {
        try {
            // 这个捕获异常 不能在循环中，因为进行中断后 在循环中下一次还会进行
            //
            while (true){
               //从 filter拿数据，然后将其写入到 buffer中去
               char c = nextChar();
               //往 data里面写数据
               data.write(c);
               //写完一个让其简单休眠一下
               Thread.sleep(random.nextInt(1000));
            }
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private char nextChar(){
        char c = filter.charAt(index);
        index++;
        if(index >= filter.length()){
            index = 0;
        }
        return  c;
    }
}

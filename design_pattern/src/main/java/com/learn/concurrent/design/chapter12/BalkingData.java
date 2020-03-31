package com.learn.concurrent.design.chapter12;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * 这个就是 当一个人看到 文件已经被写入进去了，那么就不在 执行写的操作
 * @ClassName: BalkingData
 * @Description:
 * @Author: lin
 * @Date: 2020/3/27 23:05
 * History:
 * @<version> 1.0
 */
public class BalkingData {
    private final  String fileName;

    private String content;
    private boolean change;

    BalkingData(String fileName, String content){
        this.fileName = fileName;
        this.content = content;
        this.change = true;
    }

    /**
     * 一个线程会取改变它
     * @param newContent
     */
    public  synchronized  void change(String newContent){
        this.content = newContent;
        this.change = true;
    }

    /**
     * 巡视的方法，看看需不需要保存
     * @throws IOException
     */
    public synchronized  void  save() throws IOException {
        if(!change){
            return;
        }
        doSave();
        //述求已经被受理了
        this.change = false;
    }

    private void doSave() throws IOException {
        System.out.println(Thread.currentThread().getName() + "calls so save, content=" + content);
        try(Writer writer = new FileWriter(fileName,true)) {
            writer.write(content);
            writer.write("\n");
            writer.flush();
            // 最后还要去关掉它，所以就用try/catch的方式去做
        }
    }


}

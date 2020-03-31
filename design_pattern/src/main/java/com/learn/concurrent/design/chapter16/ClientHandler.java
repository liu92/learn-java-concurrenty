package com.learn.concurrenty.design.chapter16;

import java.io.*;
import java.net.Socket;

/**
 * ClientHandler 实现一个两阶段关闭
 * 两阶段关闭：就是客户端主动 的断开连接 然后做了些资源 清理
 * @ClassName: ClientHandler
 * @Description:
 * @Author: lin
 * @Date: 2020/3/28 22:55
 * History:
 * @<version> 1.0
 */
public class ClientHandler implements Runnable{
    private  final Socket socket;

    private volatile  boolean running = true;

    public ClientHandler(Socket socket){
        this.socket = socket;
    }

    /**
     * @see Thread#run()
     */
    @Override
    public void run() {
        try(InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            BufferedReader br = new  BufferedReader(new InputStreamReader(inputStream));
            PrintWriter printWriter = new PrintWriter(outputStream))
        {
          while (running){
              String message = br.readLine();
              if(message == null){
                  break;
              }
              System.out.println("Come from client > " + message);
              printWriter.write("echo " + message + "\n");
              printWriter.flush();
          }

        }catch (IOException e){
           this.running = false;
        }finally {
            this.stop();
        }

    }

    public  void stop(){
        //已经关闭， 如果没有关闭那么就退出
      if(!running){
          return;
      }
      this.running = false;
        try {
            this.socket.close();
        } catch (IOException e) {
            System.out.println("异常被吞掉了");
        }
    }
}

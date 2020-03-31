package com.learn.concurrenty.design.chapter16;

import com.learn.concurrenty.DefaultThreadFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 服务端
 * @ClassName: AppServer
 * @Description:
 * @Author: lin
 * @Date: 2020/3/28 22:40
 * History:
 * @<version> 1.0
 */
public class AppServer extends  Thread{
    private int port;

    private static  final  int DEFAULT_PORT = 12722;

    private boolean start = true;

    private List<ClientHandler> clientHandler = new ArrayList<>();

    private  final   ExecutorService executorService = Executors.newFixedThreadPool(10);

    private  ServerSocket server;

    AppServer(){
        this(DEFAULT_PORT);
    }

    public  AppServer(int port){
        this.port = port;
    }

    @Override
    public void run() {
        try {
             server = new ServerSocket(port);
            while (start){
                Socket client = server.accept();
                ClientHandler clientHandler = new ClientHandler(client);
                executorService.submit(clientHandler);
                this.clientHandler.add(clientHandler);
            }
        } catch (IOException e) {
           throw new  RuntimeException(e);
        }finally {
            this.dispose();
        }
    }

    private void dispose() {
        // 挨个关闭
        clientHandler.stream().forEach(ClientHandler::stop);
        // 销毁线程池
        this.executorService.shutdown();
    }


    public  void  shutDown() throws IOException {
        this.start = false;
        this.interrupt();
        this.server.close();
    }
}

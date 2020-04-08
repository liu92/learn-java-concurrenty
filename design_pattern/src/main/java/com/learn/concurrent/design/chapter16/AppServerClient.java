package com.learn.concurrent.design.chapter16;

import java.io.IOException;

/**
 * @ClassName: AppServerClient
 * @Description:
 * @Author: lin
 * @Date: 2020/3/28 23:16
 * History:
 * @<version> 1.0
 */
public class AppServerClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        AppServer server = new AppServer(13563);
        server.start();

        Thread.sleep(45_000L);
        server.shutDown();
    }
}

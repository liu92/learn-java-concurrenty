package com.learn.concurrenty.design.chapter15;

import java.util.stream.IntStream;

/**
 * @ClassName: PerThreadClient
 * @Description:
 * @Author: lin
 * @Date: 2020/3/28 22:09
 * History:
 * @<version> 1.0
 */
public class PerThreadClient {
    public static void main(String[] args) {
        final  MessageHandler handler = new MessageHandler();
        IntStream.rangeClosed(0, 10)
                .forEach(i ->
//                        handler.request(new Message(String.valueOf(i)))
                         handler.requestExecutor(new Message(String.valueOf(i)))
                );
        handler.shutDown();
    }

}

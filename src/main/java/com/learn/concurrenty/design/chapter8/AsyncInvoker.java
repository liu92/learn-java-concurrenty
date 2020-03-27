package com.learn.concurrenty.design.chapter8;

/**
 * Future          -----> 代表的是未来的一个凭据
 * FutureTask      -----> 将你的逻辑进行了隔离
 * FutureService   -----> 桥接 Future 和FutureTask
 *
 * 异步调用 多线程Future 设计模式
 * @ClassName: SyncInvoker
 * @Description:
 * @Author: lin
 * @Date: 2020/3/27 14:42
 * History:
 * @<version> 1.0
 */
public class AsyncInvoker {
    public static void main(String[] args) throws InterruptedException {
        FutureService futureService = new FutureService();
        Future<String> submit = futureService.submit(() -> {
            //模拟 时间比较长
            try {
                Thread.sleep(10001);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "FINISH";
        });
        //在调用后立即返回，然后最后去获取结果
        System.out.println("====================");
        System.out.println("do other thing.");
        //在做其他事情 也会花费时间
        Thread.sleep(1000);
        System.out.println("====================");

        //其他是做完了，这时想 其他的事情
        // 那么这个时候去获取 其他事情的结果
        System.out.println("获取结果"+ submit.get());

    }


}

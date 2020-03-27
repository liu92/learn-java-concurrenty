package com.learn.concurrenty.design.chapter7;

/**
 * @ClassName: ImmutablePerformance
 * @Description: 不可变对象 引用
 * @Author: lin
 * @Date: 2020/3/27 13:50
 * History:
 * @<version> 1.0
 */
public class ImmutablePerformance {
    public static void main(String[] args) throws InterruptedException {
        long startTimestamp = System.currentTimeMillis();
//        SyncObject syncObject = new SyncObject();
//        syncObject.setName("pp");

        //不可变对象
        ImmutableObject immutableObject = new ImmutableObject("ddd");
//        long t = 100000;
//        for (long i = 0L; i < t ; i++) {
//            System.out.println(immutableObject.toString());
//        }



        //多线程执行
        Thread t1 = new Thread() {

            @Override
            public void run() {
                long t = 100000;
                for (long i = 0L; i < t; i++) {
                    // syncObject.toString()
                    System.out.println(Thread.currentThread().getName()
                            +"=" + immutableObject.toString());
                }
            }
        };
        t1.start();


        Thread t2 = new Thread() {

            @Override
            public void run() {
                long t = 100000;
                for (long i = 0L; i < t; i++) {
                    // syncObject.toString()
                    System.out.println(Thread.currentThread().getName()
                            +"=" + immutableObject.toString());
                }
            }
        };
        t2.start();
        t1.join();
        t2.join();


        long endTimestamp = System.currentTimeMillis();
        System.out.println("Elapsed time " + (endTimestamp - startTimestamp));
    }
}

/**
 * 不可变对象 不能被继承和被修改 ，以及重写里面的方法
 */
final class  ImmutableObject{
    private final  String name;

    ImmutableObject(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return "ImmutableObject{" +
                "name='" + name + '\'' +
                '}';
    }
}

class SyncObject{
    private   String name;

    public synchronized  void  setName(String name){
        this.name = name;
    }

    @Override
    public  synchronized  String toString() {
        return "["   + name +  ']';
    }
}

package com.learn.concurrenty.juc.untils.locks;

import com.learn.concurrenty.DefaultThreadFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;
import java.util.stream.Collectors;

/**
 * @ClassName: StampedLockExample
 * @Description:
 * @Author: lin
 * @Date: 2020/4/10 16:43
 * History:
 * @<version> 1.0
 */
public class StampedLockExample {
    private static final StampedLock lock = new StampedLock();
    private final static List<Long> DATA = new ArrayList<>();
    private static ExecutorService executor =new ThreadPoolExecutor(5,
            20, 200,
            TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(120),
            new DefaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
    public static void main(String[] args) {

       Runnable readTask = ()->{
            for (;;){
                read();
            }
        };

        Runnable writeTask = ()->{
            for (;;){
                writed();
            }
        };

        executor.submit(readTask);
        executor.submit(readTask);
        executor.submit(readTask);
        executor.submit(readTask);
        executor.submit(readTask);
        executor.submit(readTask);
        executor.submit(readTask);
        executor.submit(readTask);
        executor.submit(writeTask);
    }


    /**
     * 悲观的去读
     */
    private static void read(){
        long stamped = -1;
        try {
            stamped = lock.readLock();
            Optional.of(
            DATA.stream().map(String::valueOf).
                    collect(Collectors.joining("#","R-",""))
            ).ifPresent(System.out::println);
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
           lock.unlockRead(stamped);
        }
    }

    private static void writed(){
        long stamped = -1;
        try {
            stamped = lock.writeLock();
             DATA.add(System.currentTimeMillis());
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlockWrite(stamped);
        }
    }





}

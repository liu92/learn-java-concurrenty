package com.learn.concurrenty.juc.atomic;

import com.learn.concurrenty.DefaultThreadFactory;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName: UnsafeTest
 * @Description:
 * @Author: lin
 * @Date: 2020/4/3 9:02
 * History:
 * @<version> 1.0
 */
public class UnsafeTest {
    public static void main(String[] args) throws InterruptedException {


        /**
         * StupidCounter:
         * counter result: 9271383
         * Time passed in ms: 24
         *
         * SyncCounter:
         * counter result: 10000000
         * Time passed in ms: 437
         *
         * LockCounter
         * counter result: 10000000
         * Time passed in ms: 302
         */
        ExecutorService service = new ThreadPoolExecutor(5,
                50, 200,
                TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(1024),
                new DefaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());

        Counter counter = new LockCounter();
        long start = System.currentTimeMillis();
        int count = 1000;
        for (int i = 0; i < count ; i++) {
            service.execute(new CounterRunnable(counter, 10000));
        }
        service.shutdown();
        service.awaitTermination(1, TimeUnit.HOURS);
        long end = System.currentTimeMillis();
        System.out.println("counter result: " + counter.getCounter());
        System.out.println("Time passed in ms: " + (end - start));
    }

    private static Unsafe getUnsafe() {
        try {
            //这个 名字必须是theUnsafe
            Field f  = Unsafe.class.getDeclaredField("theUnsafe");
            // 因为 这个属性是私有的，所以要访问私有的属性或者方法 那么这个值要设置为true
            f.setAccessible(true);
            return  (Unsafe)f.get(null);
        } catch (Exception e) {
           throw  new RuntimeException(e);
        }

    }


    interface  Counter{
        void increment();

        long getCounter();
    }

    static class StupidCounter implements Counter{

        private long counter =0;

        @Override
        public void increment() {
            counter ++ ;
        }

        @Override
        public long getCounter() {
            return counter;
        }
    }

    static class SyncCounter implements Counter{

        private long counter =0;

        @Override
        public synchronized void increment() {
            counter ++ ;
        }

        @Override
        public  long getCounter() {
            return counter;
        }
    }


    static class LockCounter implements Counter{

        private long counter =0;
        private final Lock lock = new ReentrantLock();

        @Override
        public  void increment() {
            lock.lock();
            try {
                counter ++ ;
            } finally {
                lock.unlock();
            }
        }

        @Override
        public  long getCounter() {
            return counter;
        }
    }



    static class AtomicCounter implements Counter{

        private AtomicLong counter = new AtomicLong();

        @Override
        public  void increment() {
            counter.getAndIncrement();
        }

        @Override
        public  long getCounter() {
            return counter.get();
        }
    }



    static class CasCounter implements Counter{

        private volatile long counter =0;
        private Unsafe unsafe;
        private long offset;

        CasCounter() throws NoSuchFieldException {
            unsafe = getUnsafe();
            offset = unsafe.objectFieldOffset(CasCounter.class.getDeclaredField("counter"));
        }

        @Override
        public void increment() {
            //当前值
            long current = counter;
             // 之前的值 current, update值 current+1;
             // 当不等于的时候 才去做
            while (!unsafe.compareAndSwapLong(this, offset, current, current+1)){
                current = counter;
            }
        }

        @Override
        public long getCounter() {
            return counter;
        }
    }

    static class CounterRunnable implements Runnable{
        private final Counter counter;
        private final int num;

        public CounterRunnable(Counter counter, int num) {
            this.counter = counter;
            this.num = num;
        }

        @Override
        public void run() {
            for (int i = 0; i <num ; i++) {
                counter.increment();
            }
        }
    }
}

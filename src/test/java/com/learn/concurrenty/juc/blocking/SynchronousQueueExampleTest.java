package com.learn.concurrenty.juc.blocking;

import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

public class SynchronousQueueExampleTest {

    @Test
    public  void testAdd() throws InterruptedException {
        SynchronousQueue<String> queue = SynchronousQueueExample.creat();
        Executors.newSingleThreadExecutor().submit(()->{
            try {
                assertThat(queue.take(), equalTo("SynchronousQueue"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        TimeUnit.MILLISECONDS.sleep(5);
        assertThat(queue.add("SynchronousQueue"), equalTo(true));
    }

}
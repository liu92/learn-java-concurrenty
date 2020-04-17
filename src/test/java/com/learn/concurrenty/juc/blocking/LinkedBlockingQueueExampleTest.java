package com.learn.concurrenty.juc.blocking;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.LinkedBlockingQueue;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

public class LinkedBlockingQueueExampleTest {
    private LinkedBlockingQueueExample example;


    @Before
    public void setUp(){
        example = new LinkedBlockingQueueExample();
    }

    @After
    public void  tearDown(){
        example = null;
    }


    @Test
    public void testInsertData(){
        LinkedBlockingQueue<String> queue = example.creat(2);
        assertThat(queue.offer("data1"), equalTo(true));
        assertThat(queue.offer("data1"), equalTo(true));
        assertThat(queue.offer("data1"), equalTo(false));
    }
}
package com.learn.concurrenty.juc.blocking;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.PriorityBlockingQueue;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

public class PriorityBlockingQueueExampleTest {
    private PriorityBlockingQueueExample example;

    @Before
    public void setUp(){
        example = new PriorityBlockingQueueExample();
    }

    @After
    public void  tearDown(){
        example = null;
    }

    @Test
    public  void  testAddNewElement(){
        PriorityBlockingQueue<String> queue = example.creat(5);
        assertThat(queue.add("Hello1"), equalTo(true));
        assertThat(queue.add("Hello2"), equalTo(true));
        assertThat(queue.add("Hello3"), equalTo(true));
        assertThat(queue.add("Hello4"), equalTo(true));
        assertThat(queue.add("Hello5"), equalTo(true));
        assertThat(queue.add("Hello6"), equalTo(true));
        assertThat(queue.size(), equalTo(6));
    }
}
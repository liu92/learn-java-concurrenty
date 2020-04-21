package com.learn.concurrenty.juc.blocking;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;


public class ArrayBlockingQueueExample1Test {

    private ArrayBlockingQueueExample1 example1;

    @Before
    public void setUp(){
        example1 = new ArrayBlockingQueueExample1();
    }

    @After
    public void  tearDown(){
        example1 = null;
    }


    /**
     * Inserts the specified element at the tail of this queue if it is
     * possible to do so immediately without exceeding the queue's capacity,
     * returning {@code true} upon success and throwing an
     * {@code IllegalStateException} if this queue is full.
     */
    @Test
    public void testAddMethodNotExceedCapacity(){
        ArrayBlockingQueue<String> queue = example1.creat(5);
        assertThat(queue.add("Hello1"), equalTo(true));
        assertThat(queue.add("Hello2"), equalTo(true));
        assertThat(queue.add("Hello3"), equalTo(true));
        assertThat(queue.add("Hello4"), equalTo(true));
        assertThat(queue.add("Hello5"), equalTo(true));
//        assertThat(queue.size(), equalTo(5));
    }


    /**
     * 超过这个队列的容量
     */
    @Test
    public void testAddMethodExceedExceedCapacity(){
        ArrayBlockingQueue<String> queue = example1.creat(5);
        assertThat(queue.add("Hello1"), equalTo(true));
        assertThat(queue.add("Hello2"), equalTo(true));
        assertThat(queue.add("Hello3"), equalTo(true));
        assertThat(queue.add("Hello4"), equalTo(true));
        assertThat(queue.add("Hello5"), equalTo(true));
        assertThat(queue.add("Hello6"), equalTo(true));
//        assertThat(queue.size(), equalTo(true));
    }


    @Test
    public void testPutMethodExceedExceedCapacity() throws InterruptedException {
        ArrayBlockingQueue<String> queue = example1.creat(5);
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.schedule(()->{
            try {
                assertThat(queue.take(), equalTo("Hello1"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },1 , TimeUnit.SECONDS);
        // 这里也应该等一下，因为在这里的时候任务都还没有执行就 shutdown了
        scheduledExecutorService.shutdown();

        // 使用put方法时会抛出异常，这个时候我们可以知道为啥会抛出异常
        // 因为当这个添加数量多于这个阻塞队列容量是，就会被阻塞
        queue.put("Hello1");
        queue.put("Hello2");
        queue.put("Hello3");
        queue.put("Hello4");
        queue.put("Hello5");
        queue.put("Hello6");
//        fail("should not process to here");

    }


    @Test
    public void testPoll(){
        ArrayBlockingQueue<String> queue = example1.creat(2);
        queue.add("Hello1");
        queue.add("Hello2");

        assertThat(queue.poll(), equalTo("Hello1"));
        assertThat(queue.poll(), equalTo("Hello2"));
        assertThat(queue.poll(), nullValue());
    }

}
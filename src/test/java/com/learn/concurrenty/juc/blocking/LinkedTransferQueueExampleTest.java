package com.learn.concurrenty.juc.blocking;

import org.junit.Test;

import java.util.concurrent.LinkedTransferQueue;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

public class LinkedTransferQueueExampleTest {

    /**
     * Transfers the element to a waiting consumer immediately, if possible.
     * Question
     * when return the false that means at this time no consumer waiting, how about the element ?
     * will store into the queue?
     *
     * Answer:
     *  without enqueuing the element.
     */
    @Test
    public void testTryTransfer()  {
        LinkedTransferQueue<String> queue = LinkedTransferQueueExample.creat();
        // 没有消费者 这个元素是不会放入进去的
        boolean result = queue.tryTransfer("Transfer");
        assertThat(result, equalTo(false));
        assertThat(queue.size(), equalTo(0));
    }

}
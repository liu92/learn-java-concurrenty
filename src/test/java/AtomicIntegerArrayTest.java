import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * @ClassName: AtomicIntegerArrayTest
 * @Description:
 * @Author: lin
 * @Date: 2020/4/2 16:38
 * History:
 * @<version> 1.0
 */
public class AtomicIntegerArrayTest {

    @Test
    public void testCreatAtomicIntegerArray(){
        AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(10);
        Assert.assertEquals(10, atomicIntegerArray.length());
    }

    @Test
    public void testGet(){
        AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(10);
        Assert.assertEquals(10, atomicIntegerArray.length());
        Assert.assertEquals(0, atomicIntegerArray.get(5));
    }

    @Test
    public void testSet(){
        AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(10);
        //第五个元素设置为5
        atomicIntegerArray.set(5, 5);
        Assert.assertEquals(10, atomicIntegerArray.length());
        Assert.assertEquals(5, atomicIntegerArray.get(5));
    }

    @Test
    public void testGetAndSet(){
        int[] originalArray = new int[10];
        originalArray[5] = 5;
        AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(originalArray);

        int v = atomicIntegerArray.getAndSet(5, 6);
        Assert.assertEquals(5, v);
        Assert.assertEquals(5, atomicIntegerArray.get(5));
    }
}

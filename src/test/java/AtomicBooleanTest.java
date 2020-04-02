import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 这个可以用在 优雅的停止线程
 * @ClassName: AtomicBooleanTest
 * @Description:
 * @Author: lin
 * @Date: 2020/4/2 15:44
 * History:
 * @<version> 1.0
 */
public class AtomicBooleanTest {
    @Test
    public void testCreatWithOutArguments(){
        AtomicBoolean atomicBoolean = new AtomicBoolean();
        Assert.assertFalse(atomicBoolean.get());
    }

    @Test
    public void testCreatWithOutArgument(){
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        Assert.assertTrue(atomicBoolean.get());
    }

    @Test
    public void testGetAndSet(){
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        boolean b = atomicBoolean.compareAndSet(true, false);
        Assert.assertTrue(b);
        Assert.assertFalse(atomicBoolean.get());
    }


    @Test
    public void testCompareAndSetFailed(){
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        boolean b = atomicBoolean.compareAndSet(false, true);
        Assert.assertFalse(b);
        Assert.assertTrue(atomicBoolean.get());
    }
}

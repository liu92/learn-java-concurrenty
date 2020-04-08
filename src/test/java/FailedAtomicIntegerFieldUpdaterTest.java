import org.junit.Test;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * @ClassName: FailedAtomicIntegerFieldUpdaterTest
 * @Description:
 * @Author: lin
 * @Date: 2020/4/2 17:17
 * History:
 * @<version> 1.0
 */
public class FailedAtomicIntegerFieldUpdaterTest {



//    @Test(expected = RuntimeException.class)
//    public void testPrivateFieldAccessError(){
//        AtomicIntegerFieldUpdater<TestMe> updater = AtomicIntegerFieldUpdater.newUpdater(TestMe.class,"i");
//        TestMe testMe = new TestMe();
//        updater.compareAndSet(testMe, 0 , 1);
//    }

//    @Test
//    public void testTargetObjectIsNull(){
//        AtomicIntegerFieldUpdater<TestMe> updater = AtomicIntegerFieldUpdater.newUpdater(TestMe.class,"i");
//        updater.compareAndSet(null, 0 , 1);
//    }

//    @Test
//    public void testFieldNameInvalid(){
//        AtomicIntegerFieldUpdater<TestMe> updater = AtomicIntegerFieldUpdater.newUpdater(TestMe.class,"i");
//        updater.compareAndSet(null, 0 , 1);
//    }

    /**
     * 反射 类似不对
     */
//    @Test
//    public void testFieldTypeInvalid(){
//        AtomicReferenceFieldUpdater<TestMe2, Integer> updater = AtomicReferenceFieldUpdater.newUpdater(TestMe2.class, Integer.class,"i");
//        TestMe2 me2 = new TestMe2();
//        updater.compareAndSet(me2, null , 1);
//    }


    /**
     * 反射 类似不对
     */
//    @Test
//    public void testFieldIsNotVolatile(){
//        AtomicReferenceFieldUpdater<TestMe2, Integer> updater = AtomicReferenceFieldUpdater.newUpdater(TestMe2.class, Integer.class,"i");
//        TestMe2 me2 = new TestMe2();
//        // 如果 TestMe2中 变量的 volatile 去掉会 类型错误
//        updater.compareAndSet(me2, null , 1);
//
//        // 错误 : Must be volatile type
//    }
//
//    static class TestMe2{
//         Integer i;
//    }
}

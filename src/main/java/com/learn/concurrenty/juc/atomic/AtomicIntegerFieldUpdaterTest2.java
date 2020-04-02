package com.learn.concurrenty.juc.atomic;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * 1、想让类的属性操作具备原子性
 *    1.1 volatile
 *    1.2 非private,protected(如果是当前类也可以是private ，protected)
 *    1.3 类型必须一致
 *    1.4 其他
 * 2.不想使用锁(包括显示锁或者重量级锁synchronized)
 * 3.大量需要原子类型修饰的对象，相对比较耗费内存
 *  比如使用AtomicReference来说修饰的时候，如果是一个Node ，那么这个node就是 占用16，再加上他本身AtomicReference<Node> 就会是32
 *  而在ConcurrentSkipListMap jdk1.6的版本中一般使用AtomicReferenceFieldUpdater来进行修饰，
 *  在这个jdk1.8 已经没有使用AtomicReferenceFieldUpdater来进行修饰了
 *
 * @ClassName: AtomicIntegerFieldUpdaterTest2
 * @Description:
 * @Author: lin
 * @Date: 2020/4/2 22:34
 * History:
 * @<version> 1.0
 */
public class AtomicIntegerFieldUpdaterTest2 {
    private volatile  int i;

    private AtomicInteger  j = new AtomicInteger();

    private AtomicIntegerFieldUpdater<AtomicIntegerFieldUpdaterTest2> updater = AtomicIntegerFieldUpdater.
            newUpdater(AtomicIntegerFieldUpdaterTest2.class, "i");

    public void update(int newValue){
        updater.compareAndSet(this, i , newValue);
    }

    public int get(){
        return  i;
    }

    public static void main(String[] args) {
        AtomicIntegerFieldUpdaterTest2 test2 = new AtomicIntegerFieldUpdaterTest2();
        test2.update(10);
        System.out.println(test2.get());
    }
}

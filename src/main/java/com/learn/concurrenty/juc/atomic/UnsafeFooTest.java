package com.learn.concurrenty.juc.atomic;

import sun.misc.Unsafe;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName: UnsafeFooTest
 * @Description: Unsafe趣味测试
 * @Author: lin
 * @Date: 2020/4/8 14:23
 * History:
 * @<version> 1.0
 */
public class UnsafeFooTest {


    public static void main(String[] args) throws IllegalAccessException, InstantiationException, ClassNotFoundException, NoSuchFieldException, IOException, NoSuchMethodException, InvocationTargetException {
        //Simple simple = new Simple();
        //System.out.println(simple.get());

        //Simple simple = Simple.class.newInstance();

        //Class.forName("com.learn.concurrenty.juc.atomic.UnsafeFooTest$Simple");

        Unsafe unsafe = getUnsafe();
        // 这种方式会绕过类的初始化,直接开辟内存
        //Simple simple =(Simple) unsafe.allocateInstance(Simple.class);
        //System.out.println("simple======"+simple);
        //可以拿到 这个类,和类加载器,但是不会类不会初始化
        //System.out.println("simple======"+simple.get());
        //System.out.println("simple======"+simple.getClass());
        //System.out.println("simple======"+simple.getClass().getClassLoader());

        //=========================================
        Guard guard = new Guard();
        guard.work();

//         通过Unsafe 绕过这个判断进行工作, 在Unsafe中大多数都是通过Field去操作的
        Field f = guard.getClass().getDeclaredField("ACCESS_ALLOWED");
//         设置偏移量, 42可以允许工作,那么就是设置为42
        unsafe.putInt(guard, unsafe.objectFieldOffset(f), 42);
        guard.work();

//        byte[] bytes = loadClassContent();
//        Class<?> aClass = unsafe.defineClass(null, bytes, 0, bytes.length, null, null);
//        int v  = (Integer) aClass.getMethod("get").invoke(aClass.newInstance());
//        System.out.println(v);


        //
        System.out.println(sizeOf(new Simple()));
    }




    private static byte[] loadClassContent() throws IOException  {
        File f = new File("D:\\app\\A.class");

        FileInputStream fis = new FileInputStream(f);
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        byte[] content = new byte[(int) f.length()];
        fis.read(content);
        fis.close();
        return  content;
    }


    /**
     * 获取一个长度, 可以去拿取Object的size大小
     * @param obj
     * @return
     */
    private static long sizeOf(Object obj){
        Unsafe unsafe = getUnsafe();
        Set<Field> fields = new HashSet<Field>();
        Class<?> c = obj.getClass();
        while (c != Object.class){
            Field[] declaredFields = c.getDeclaredFields();
            for (Field f : declaredFields) {
                //如果是静态的 等等不要, 非静态的就要,
                // 位运算
                if((f.getModifiers() & Modifier.STATIC) == 0) {
                    fields.add(f);
                }
            }
            //上面加进去了还没有完，还要获取父类
            c = c.getSuperclass();
        }

        long maxOffSet = 0;
        for (Field f : fields) {
            long offset = unsafe.objectFieldOffset(f);
            if(offset > maxOffSet){
                maxOffSet = offset;
            }
        }

        return ((maxOffSet/8)+1) * 8;
    }


    private static Unsafe getUnsafe() {
        try {
            //这个 名字必须是theUnsafe
            Field f  = Unsafe.class.getDeclaredField("theUnsafe");
            // 因为 这个属性是私有的，所以要访问私有的属性或者方法
            // 那么这个值要设置为true
            f.setAccessible(true);
            return  (Unsafe)f.get(null);
        } catch (Exception e) {
            throw  new RuntimeException(e);
        }
    }

    static class  Guard{
        private int ACCESS_ALLOWED = 1;
        private boolean allow(){
            // 一直不允许工作
            return  42 == ACCESS_ALLOWED;
        }

        public void work(){
            // 一直不允许工作，那么我们有什么办法越过这个操作喃
            if(allow()){
                System.out.println("I am working by allowed");
            }
        }

    }


    static class  Simple{
        private long l = 0L;

        Simple(){
            this.l = 1;
            System.out.println("===============");
        }

        public long get(){
            return  this.l;
        }
    }


}

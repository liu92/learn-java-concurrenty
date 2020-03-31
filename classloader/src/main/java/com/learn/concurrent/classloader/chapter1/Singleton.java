package com.learn.concurrent.classloader.chapter1;

/**
 * @ClassName: Singleton
 * @Description:
 * @Author: lin
 * @Date: 2020/3/31 17:13
 * History:
 * @<version> 1.0
 */
public class Singleton {
    /**
     * 第二种：就 这个instance 放在x, y静态变量的前面
     * 它的类加载过程是 ，
     *  这三点是 默认值
     * 1. instance = null;
     * 2. x = 0;
     * 3. y = 0;
     *
     * 下面是为静态变量赋予正确的初始值
     *  x++=>x=1
     *  y++=>y=1
     *  instance = new Singleton();
     *  当上面初始化，再对 x重新进行了赋值操作
     *  然后 x 被赋值为 0 ，
     *  而y 没有赋值 在初始的时候给了默认值，所以就是默认值，
     *  x = 0;
     *  y = 1;
     *
     *
     *
     */
    private static Singleton instance = new Singleton();

    public  static  int x = 0;

    public  static  int y ;

    /**
     * 第一种，就是这个instance放在这里
     * 当new的这个对象变量放在这里是, x 和 y的值
     * 是 1，1
     */
//    private static Singleton instance = new Singleton();
    /**
     * 这个要结合类加载的三个阶段类分析:
     *
     *  因为这样执行的步骤是
     *  int x 先放的是一个地址,开辟一个内存空间, 然后就是给一个默认值 0,
     *  这个默认值和上面的值不一样, 上面的是我们赋予的, 而这个0是 int的默认值
     *  1. int x = 0;
     *  y 也是给默认值
     *  2. int y = 0;
     *  引用类型 默认值
     *  3. instance = null;
     */

    private Singleton(){
        x ++;
        y++;
    }

    public static  Singleton getInstance(){
       return instance ;
    }

    public static void main(String[] args) {
        Singleton instance = Singleton.getInstance();
        System.out.println(instance);
        System.out.println(Singleton.x);
        System.out.println(Singleton.y);
    }














}

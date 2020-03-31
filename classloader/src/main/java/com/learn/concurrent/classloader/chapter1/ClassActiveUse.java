package com.learn.concurrent.classloader.chapter1;

import java.util.Random;

/**
 * @ClassName: ClassActiveUse
 * @Description:
 * @Author: lin
 * @Date: 2020/3/31 15:51
 * History:
 * @<version> 1.0
 */
public class ClassActiveUse {

    //6. 启动类初始化类
    static {
        System.out.println("==========ClassActiveUse============");
    }

    public static void main(String[] args) throws ClassNotFoundException {
       // 主动调用
       //new Obj();
       // 1.对接口中静态变量进行读取是也会初始类
       // System.out.println(Tes.count);

        //2. 在调用类中静态变量也会 先初始类
        //System.out.println(Obj.SALARY);
        //3. 调用静态方法 也会初始类
        //Obj.printSalary();

        //4.反射某个类,也会主动调用一个类 进行初始化
        //Class.forName("com.learn.concurrent.classloader.chapter1.Obj");

        //5.初始化一个子类,父类会被先初始
        //System.out.println(Child.at);


        // ============================
        //6.当子类调用父类的静态变量时,子类【不会】初始化
        //System.out.println(Child.SALARY);

        //7.数组引用是【不会】初始化类的
        //Obj[] arrays = new Obj[10];

        //8.如果变量是静态不可变的,那么会初始化类吗？
        // 这种不会初始类, 因为这个静态变量在使用final修饰后,就会有初始值了
        // 引用常量不会导致 类的一个初始化
        // 并且这个静态变量 在编译阶段就放到常量池中去了
        // System.out.println(Obj.CON);

        //9. 并且一个变量是静态不可变修饰的 但是是随机产生 会去初始化类吗?
        // 这个会去初始化类, 这个是因为在 编译阶段不能计算出值,
        // 只有在运行时才会去计算出值,所以这个会导致类的初始化
        System.out.println(Obj.t);


    }
}

class Obj{

    public  static long SALARY = 10000L;
    public static  final long CON = 88L;
    public static  final  int t = new Random().nextInt(100);

    static {
        System.out.println("Obj 被初始化");
    }

    public static void printSalary(){
        System.out.println("=========obj=====salary");
    }
}

class Child extends Obj{
    public static int at = 12;
    static {
        System.out.println("Child 被初始化");
    }
}

interface  Tes{
    //默认是静态修饰和不可变类型
    int count =0;
}

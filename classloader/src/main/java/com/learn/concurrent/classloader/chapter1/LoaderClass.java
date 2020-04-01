package com.learn.concurrent.classloader.chapter1;

/**
 * 加载类
 * @ClassName: LoaderClass
 * @Description:
 * @Author: lin
 * @Date: 2020/3/31 23:16
 * History:
 * @<version> 1.0
 */
public class    LoaderClass {
    public static void main(String[] args) {
//        MyObject myObject1 = new MyObject();
//        MyObject myObject2 = new MyObject();
//        MyObject myObject3 = new MyObject();
//        MyObject myObject4 = new MyObject();
//
//        System.out.println(myObject1.getClass() == myObject2.getClass());
//        System.out.println(myObject1.getClass() == myObject3.getClass());
//        System.out.println(myObject1.getClass() == myObject4.getClass());
//
//        System.out.println(MyObject.c);

        System.out.println(Sub.x);
    }
}


class MyObject{
   public static int c = 9;

   static {
       System.out.println(c);
       c = 9+1;

       //3.静态语句块只能访问到定义在静态语句块之前的变量，定义在他之后的变量
       //只能赋值，不能访问
       // 如下: 可以赋值操作
       //y = 23;
       // 但是 不能读 , 会报一个编译错误 Illegal forward reference
       //System.out.println(y);
   }

       //private static  int y = 0;
}


class Parent{
   static {
       System.out.println("parent");
   }
}

class Sub extends  Parent{
    public static  int x = 1;

    static {
        System.out.println("child");
    }
}

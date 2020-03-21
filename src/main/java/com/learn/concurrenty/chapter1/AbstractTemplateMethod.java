package com.learn.concurrenty.chapter1;

/**
 * 这个类 来体现Thread方法的巧妙之处
 * 在真正的模板方法里面 是需要把这个写成抽象的
 * @ClassName: AbstractTemplateMethod
 * @Description:
 * @Author: lin
 * @Date: 2020/3/19 15:45
 * History:
 * @<version> 1.0
 */
public abstract class AbstractTemplateMethod {

    /**
     * 这个必须定义成final类型，如果不定义final类型的那么子类
     * 就可以去复写这个方法
     * @param message
     */
    public final void print(String message){
        System.out.println("##################");
         //这里在message 的前后添加 其他的，
        // 这么没有把他定下来，针对不同的情况可能有不同的情况
         warpPrint(message);
        System.out.println("#################");
    }

    /**
     * 因为这个类 不想暴露给外不类，只想暴露给子类，
     * 这个方法可以是一个空的实现，也可以写成抽象方法。
     * 如果子类不实现 那么就是什么都不做，如果实现了那么就复写它。
     * Thread 里面就没有写成抽象的，所以这里可以不用写成抽象的
     *
     * @param message
     */
    protected  void warpPrint(String message){

    }


    public static void main(String[] args) {
        AbstractTemplateMethod tm = new AbstractTemplateMethod() {
            @Override
            protected void warpPrint(String message) {
                System.out.println("*" + message + "*");
            }
        };
        tm.print("Hello Thread");

        //这样就可以有不同的实现方式

        AbstractTemplateMethod t1 = new AbstractTemplateMethod() {
            @Override
            protected void warpPrint(String message) {
                System.out.println("+" + message + "+");
            }
        };
        t1.print("Hello Thread");
    }

    // 可以看到打印出不同的 东西，然后可以message前后中添加不同的 符合
    // 这里就可以看到Thread 设计的巧妙之处 ，
    // start方法 不知道具体的业务逻辑是怎么样的，
    // 只提供了一个主要的方法start,
    // 启动一个线程后 这个线程去做什么东西这个Thread不知道，
    // 那他就抽出来一个方法 叫做run,  run方法你可以实现也可以不实现，
    // 实现了之后你就可以写你自己的逻辑，就是上面的示例一样
}

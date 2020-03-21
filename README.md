# learn-java-concurrent
1、chapter1
不使用Thread类执行方法，那么方法必须要一个执行完后才能 执行下一个。
这种方式效率不高,下面的示例 也是有多线程的方式来执行方法。
```java
package com.learn.concurrenty.chapter1;

import com.learn.concurrenty.DefaultThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: TryConcurrent
 * @Description: 尝试使用线程
 * @Author: lin
 * @Date: 2020/3/19 13:36
 * History:
 * @<version> 1.0
 */
public class TryConcurrent {

    public static void main(String[] args) {
        //这种方式就会造成 一件 事情 一件事情的去处理，不会同时去执行
//        readFromData();
//        writeDateToFile();

//         Thread t1 =  new Thread(){
//             @Override
//             public void run(){
//                 for (int i = 0; i <100 ; i++) {
//                     println("Task 1=>" + i);
//                 }
//             }
//         };
//         t1.start();

         //main线程
//        for (int j = 0; j <100 ; j++) {
//            println("Task 2=>" + j);
//        }

        // 由cup去切换执行

        //启动start这个线程是 main线程


//        new Thread("READ-Thread"){
//            @Override
//            public void run() {
//                //执行run方法是 READ-Thread线程执行
//                readFromData();
//            }
//        }.start();
//
//
//        new Thread("WRITE-Thread"){
//            @Override
//            public void run() {
//                readFromData();
//            }
//        }.start();

        //在没有调用start之前不能称之为一个Thread,
        // 只有start之后才能称之为Thread. start这个方法是立即返回它并不会阻塞住，
        // 等到里面的方法都执行完了才会返回。它是立即返回


        //==================================================

        // 如果不使用start方法 而是用run方法那么这个当前线程就是main线程，而不会去重新启动一个线程
         Thread t1 = new Thread("READ-Thread"){
            @Override
            public void run() {
                println(Thread.currentThread().getName());
                readFromData();
            }
        };
        t1.run();
        //这里调用run方法 打印 ，可以看到当前线程就是main 线程 ，而不会去重新启动一个线程，
        // 也就是说 在new Thread()后 没有使用start()方法那么 就不会存在另外的一个线程
//        main
//        Begin read data to file.
//        Read data done and start handle it.
//        The readFromData handle finish and successfully.



        //============================================ 使用线程池创建

//        ExecutorService executorService = new ThreadPoolExecutor(2,
//                5, 200,
//                TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(6),
//                new DefaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
//        executorService.execute(new MyThreadReadFromData("READ-Thread"));
//        executorService.execute(new MyThreadWriteDateToFile("WRITE-Thread"));
//        executorService.shutdown();


    }

    public static  void  readFromData(){
        try {
            println("Begin read data to file.");
            Thread.sleep(1000 );
            println("Read data done and start handle it.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        println("The readFromData handle finish and successfully.");
    }

    public static void writeDateToFile(){
        try {
            println("Begin write data to file.");
            Thread.sleep(2000 );
            println("write data done and start handle it.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        println("The writeDateToFile handle finish and successfully.");
    }

    private static  void println(String message){
        System.out.println(message);
    }
}


class MyThreadReadFromData extends Thread{

    public MyThreadReadFromData(String name){
        super(name);
    }

    @Override
    public void run(){
        TryConcurrent.readFromData();
        System.out.println("MyThreadReadFromData执行当前run方法的线： " + Thread.currentThread().getName());
    }
}



class MyThreadWriteDateToFile extends Thread{

    public MyThreadWriteDateToFile(String name){
        super(name);
    }

    @Override
    public void run(){
        TryConcurrent.writeDateToFile();
        System.out.println("MyThreadWriteDateToFile执行当前run方法的线： " + Thread.currentThread().getName());
    }
}
```
1.1 分析Thread类里面的 start方法，这个方法的设计方式, 
从这里我们可学习其代码的设计，怎么更巧妙的是设计实现。
```java
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

```
1.2 总结
```
1、java应用程序的main 函数式是一个线程，是被jvm启动的时候调用的，
线程的名字叫做main.
2、实现一个线程，必须创建Thread示例， Override run方法，并且调用start方法
3、在jvm启动后，实际上有多个线程，但是至少有一个非守护线程
4、当调用一个线程start方法的时候，此时至少有两个线程，一个是调用你的线程，还有一个是执行run方法的线程
5、线程的生命周期分别为 NEW,RUNNABLE(RUNNING,READ)，BLOCK(在block时 必须先回到RUNNABLE才能进入RUNNING，
不肯能直接进入RUNNING, 在Block的过程中也可以中断),
 TIME_WAITING,TERMINATED
6、线程不能启动两次，启动两次会报错。
```

2、实现Runnable接口方式来创建线程, 示例如下
```java
package com.learn.concurrenty.chapter2;

/**
 * 实现Runnable
 * @ClassName: TicketWindowRunnable
 * @Description:
 * @Author: lin
 * @Date: 2020/3/20 10:11
 * History:
 * @<version> 1.0
 */
public class TicketWindowRunnable implements Runnable{

    private int index = 1;

    private final  static  int MAX =50;

    /**
     * 通过将业务抽取出来，就是你的业务逻辑和线程控制 是在不同的object里面
     */
    @Override
    public void run() {
         while (index<=MAX){
             System.out.println(Thread.currentThread().getName() +
                     " 的号码是：" + (index++));
             try {
                 Thread.sleep(100);
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
         }
    }
}

```

2.1 调用，这种方法 业务实例就只有一个，而不像第一个版本继承Thread的有多个实例
```java
package com.learn.concurrenty.chapter2;

/**
 * 第二个版本，Runnable
 * @ClassName: Bank2
 * @Description:
 * @Author: lin
 * @Date: 2020/3/20 10:13
 * History:
 * @<version> 1.0
 */
public class Bank2 {
    public static void main(String[] args) {
        //这种方法 业务实例就只有一个，而不像第一个版本继承Thread的有多个实例
        //通过将业务抽取出来，就是你的业务逻辑和线程控制 是在不同的object里面
        final  TicketWindowRunnable tr = new TicketWindowRunnable();
        Thread w1 = new Thread(tr, "一号窗口");
        Thread w2 = new Thread(tr, "二号窗口");
        Thread w3 = new Thread(tr, "三号窗口");
        w1.start();
        w2.start();
        w3.start();
    }
}

```
2.2 策略模式在Thread和Runnable的应用分析，先定义一个类，这里面定义要计算的方法这些
```java
package com.learn.concurrenty.chapter2;

/**
 * 计算
 * @ClassName: TaxCalculator
 * @Description: 策略模式在Thread和Runnable的应用分析
 * @Author: lin
 * @Date: 2020/3/20 10:34
 * History:
 * @<version> 1.0
 */
public class TaxCalculator {
    /**
     * 工资
     */
    private final  double salary;
    /**
     * 奖金
     */
    private final double bonus;

    /**
     * 这里使用组合的方式 接口，将参数传进去怎么计算不管，你自己处理
     */
    private  CalculatorStrategy calculatorStrategy;

    /**
     * 非jdk1.8的方式
     * @param salary
     * @param bonus
     */
//    public TaxCalculator(double salary, double bonus) {
//        this.salary = salary;
//        this.bonus = bonus;
//    }

    /**
     * 非jdk1.8的方式
     * @param calculatorStrategy
     */
//    public void setCalculatorStrategy(CalculatorStrategy calculatorStrategy) {
//        this.calculatorStrategy = calculatorStrategy;
//    }


    /**
     * jdk1.8的方式
     * @param salary
     * @param bonus
     */
    public TaxCalculator(double salary, double bonus, CalculatorStrategy calculatorStrategy) {
        this.salary = salary;
        this.bonus = bonus;
        this.calculatorStrategy = calculatorStrategy;
    }


    /**
     * 这里计算，根据定义的接口将传入的参数 进行计算
     * 子类去自行重写这个方法，然后计算自定义
     * @return
     */
    protected double calcTax(){
        return  calculatorStrategy.calculator(salary, bonus);
    }

    /**
     * 这样这种方式 就和Thread类 里面非常像了
     * @return
     */
    public  double calculate(){
        return  this.calcTax();
    }

    public double getSalary() {
        return salary;
    }

    public double getBonus() {
        return bonus;
    }
}

```
2.3、定义接口
```java
package com.learn.concurrenty.chapter2;

/**
 * 策略模式就是将算法封装起来，然后可以替换不同的算法
 * @ClassName: CalculatorStrategy
 * @Description:
 * @Author: lin
 * @Date: 2020/3/20 10:55
 * @History:
 * @<version> 1.0
 */
public interface CalculatorStrategy {

    /**
     * 定义一个计算方法，然后根据税率计算所交的税
     * @param salary
     * @param bonus
     * @return
     */
    double calculator(double salary, double bonus);
}


```
2.4、实现接口
```java
package com.learn.concurrenty.chapter2;

/**
 * 实现接口，这里面就将算法提出来了，不同的计算进行封装
 * @ClassName: SimpleCalculatorStrategy
 * @Description:
 * @Author: lin
 * @Date: 2020/3/20 11:03
 * History:
 * @<version> 1.0
 */
public class SimpleCalculatorStrategy implements  CalculatorStrategy {

    /**
     *常量性的要定义
     */
    private  static  final double  SALARY_RATE = 0.1;
    private  static  final double  BONUS_RATE = 0.15;

    @Override
    public double calculator(double salary, double bonus) {
        return salary*SALARY_RATE + bonus*BONUS_RATE;
    }
}

```
2.5、通过这种方法将计算抽取出来，如果有修改那么就添加不同的算法实现就可以了
```java
package com.learn.concurrenty.chapter2;

/**
 * @ClassName: TaxCalculatorMain
 * @Description:
 * @Author: lin
 * @Date: 2020/3/20 10:42
 * History:
 * @<version> 1.0
 */
public class TaxCalculatorMain {
    public static void main(String[] args) {
//        TaxCalculator  calculator = new TaxCalculator(10000d, 2000d){
//           // 如果我们要改变税率 ，那么势必要去改变这里的代码所以这种设计不好
//            // 代价就比较大， 所以这里抽取出一个计算税率的接口出来。
//            @Override
//            public double calcTax() {
//                return getSalary()*0.1 + getBonus()*0.15;
//            }
//
//        };
//        double tax = calculator.calculate();
//        //所交的税
//        System.out.println(tax);

        // 将上面的算法抽取出来后，我们就可以用实现算法的类来计算，不同计算用不同的类

//        TaxCalculator calculator = new TaxCalculator(10000d, 2000d);
//        //多态方式调用
//        CalculatorStrategy cal = new SimpleCalculatorStrategy();
//        calculator.setCalculatorStrategy(cal);
//        System.out.println(calculator.calculate());

        // 也可以改成jdk8的写法
        // 这种方式就是更加简洁
        TaxCalculator calculator = new TaxCalculator(10000d, 2000d, (s, b) -> (s*0.1 + b*0.15));
        System.out.println(calculator.calculate());
    }
}

```
3、Thread对象构建的介绍,线程的命名是通过  "Thread-" + nextThreadNum()的方式来命令的

```java
package com.learn.concurrenty.chapter3;

/**
 * @ClassName: CreateThread
 * @Description: 构造Thread对象不知道的内容介绍
 * @Author: lin
 * @Date: 2020/3/20 12:36
 * History:
 * @<version> 1.0
 */
public class CreateThread {
    public static void main(String[] args) {
        Thread t1 = new Thread();
        t1.start();
        System.out.println(t1.getName());
    }
}

```
3.1、命名方式, 通过定义threadInitNumber 变量在jvm启动后会被实例化一次，
从零开始 然后这个变量会累加。
```
     /* For autonumbering anonymous threads. */
    private static int threadInitNumber;
    private static synchronized int nextThreadNum() {
        return threadInitNumber++;
    }

```
3.2 
```java
package com.learn.concurrenty.chapter3;

import java.util.Arrays;

/**
 * @ClassName: CreateThread
 * @Description: 构造Thread对象不知道的内容介绍
 * @Author: lin
 * @Date: 2020/3/20 12:36
 * History:
 * @<version> 1.0
 */
public class CreateThread2 {
    public static void main(String[] args) {
        Thread t1 = new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t1.start();
        //如果不传入ThreadGroup那么就会去取 它父类的ThreadGroup()
//        System.out.println(t1.getThreadGroup());
//        System.out.println(Thread.currentThread().getName());

        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
//        System.out.println(threadGroup.getName());
        System.out.println(threadGroup.activeCount());

        Thread[] threads = new Thread[threadGroup.activeCount()];
        threadGroup.enumerate(threads);
//        for (Thread thread : threads) {
//            System.out.println(thread);
//        }
        Arrays.asList(threads).forEach(System.out::println);
    }
}

```
3.3 测试往栈里不停的压入,这种情况会出现StackOverflowError 异常
```java
package com.learn.concurrenty.chapter3;

/**
 * @ClassName: CreateThread3
 * @Description:
 * @Author: lin
 * @Date: 2020/3/20 15:08
 * History:
 * @<version> 1.0
 */
public class CreateThread3 {

    private static  int counter =0;

    public static void main(String[] args) {
       // jvm stack, 创建一个操作压栈， 一直不停的往栈里面压入，那么会出现异常
        try {
            add(0);
        }catch (Error e){
           e.printStackTrace();
            System.out.println(counter);
        }

    }

    private static void add(int i){
        ++counter;
        add(i+1);
    }
}
```
3.4、测试Thread构造方法的stackSize
```java
package com.learn.concurrenty.chapter3;

/**
 * @ClassName: CreateThread4
 * @Description:
 * @Author: lin
 * @Date: 2020/3/20 15:08
 * History:
 * @<version> 1.0
 */
public class CreateThread4 {

    private static  int counter =1;

    public static void main(String[] args) {
       Thread t1 = new Thread(new Runnable() {
           @Override
           public void run() {
               try {
                   add(1);
               }catch (Error e){
                   System.out.println(counter);
               }
           }
           private  void add(int i){
               ++counter;
               add(i + 1);
           }
       });
       t1.start();

    }

}

```


3.5 小结
```
1、创建线程对象，默认有一个线程名，以Thread-开头，从0开始计数
构造函数Thread()
Thread-0
Thread-1
2、如果在构造Thread是时候没有传递Runnable或者没有复写Thread的run方法，
该Thread将不会执行任何东西。如果传递了Ruannable接口的实例，或者复写了Thread的run方法，
则会执行该方法的逻辑代码
也就是说 构造方法Thread(Runnable target) 这里的target是 null，
 @Override
public void run() {
        if (target != null) {
            target.run();
        }
    }
3、如果构造线程对象时 未传入ThreadGroup,Thread会默认获取父线程的
ThreadGroup作为该线程的ThreadGroup,此时子线程和父线程将会在同一个threadgroup中

4、构造Thread的时候传入stackSize代表着该线程占用的stack大小，如果没有指定
stackSize的大小，默认是0,0代表着会忽略该参数，该参数会被JNI函数去使用，
需要注意：该参数在有些平台有效，在有些平台则无效。
5、Runnable是个接口 不是一个线程，它是一个任务执行单元 ，起到的作用是把控制部分和业务逻辑单元分开。
6、注意如果一个线程设置为守护线程，那么这个线程跟着主线程的声明周期结束。
比如在有些情况 不能设置setDaemon 为true，因为这种方式下 会随着主线程的声明周期结束而结束
这种情况下 可能我们的那个线程 里面的逻辑都没有执行，就结束了，有时要让线程
伴随jvm的结束而结束就可以了。
```
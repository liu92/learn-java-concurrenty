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
4、设置线程为守护线程 查看其结果 
```java
package com.learn.concurrenty.chapter4;

/**
 * @ClassName: DaemonThread
 * @Description: 守护线程
 * @Author: lin
 * @Date: 2020/3/20 16:14
 * History:
 * @<version> 1.0
 */
public class DaemonThread {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(){
            @Override
            public void run() {

                try {
                    System.out.println(Thread.currentThread().getName() + "running");
                    Thread.sleep(10000);
                    System.out.println(Thread.currentThread().getName() + "done");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        //设置为守护线程，那么这个 main线程结束 这个守护线程也跟着结束
        // 并且这个daemon设置 必须在线程活跃的时候去设置，
        // 如果线程不是存活的那么 就会报错， 如果将t.start()方法写在 t.setDaemon(true)之上
        // 就会出现IllegalThreadStateException异常
        t.setDaemon(true);

        t.start();

        //在main结束之后为什么 上面的Thead-0还没有推出喃，这是因为还有一些active的线程
        // 所以
        // 我们可以通过jconsole 去查看main线程 已经结束了但是 Thread-0线程还在
        // jdk 1.7之后写法
        Thread.sleep(10_0000);
        System.out.println(Thread.currentThread().getName());
    }
}

```
4.1、设置为守护线程，那么线程会跟着main线程结束吗？
```java
package com.learn.concurrenty.chapter4;

/**
 * @ClassName: DaemonThread
 * @Description: 守护线程
 * @Author: lin
 * @Date: 2020/3/20 16:14
 * History:
 * @<version> 1.0
 */
public class DaemonThread2 {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(() -> {
            Thread innerThread = new Thread(() ->{
                try {
                   while (true){
                       System.out.println("Do some thing for health check");
                       Thread.sleep(10000);
                   }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            //如果这里不设置为守护线程，我们可以看看这个线程是否退出，
            // 测试结果 是如果不设置为守护线程，
            // 那么在t线程结束后这个application不会退出
            // jvm 会判断里面还active的非daemon线程
            innerThread.setDaemon(true);
            //如果这里没有启动，没有线程当然退出了
            innerThread.start();

            //在t线程里面再创建一个线程，然后设置为daemon=true,
            // 如果这个t结束 后这个再次创建的线程会结束吗?
            try {
                Thread.sleep(1000);
                System.out.println("T thread finish done");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t.start();
        //打印结果如下, 可以看到 这个t线程结束后，里面的innerThread线程也结束了，
        //这就好比建立一个通讯连接的线程，里面就是给它创建一个心跳的检查类似。
        //如果连接失败了 会主动关闭，不需要手动的去关闭。
//        > Task :DaemonThread2.main()
//        T thread finish done

    }
}

```
4.2、Thread简单api使用
```java
package com.learn.concurrenty.chapter4;

import java.util.Optional;

/**
 * @ClassName: ThreadSimpleApi
 * @Description: Thread简单API
 * @Author: lin
 * @Date: 2020/3/20 17:21
 * History:
 * @<version> 1.0
 */
public class ThreadSimpleApi {
    public static void main(String[] args) {
        Thread t = new Thread(() -> {
            Optional.of("Hello").ifPresent(System.out::println);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t1");
        t.start();
        Optional.of(t.getName()).ifPresent(System.out::println);
        Optional.of(t.getId()).ifPresent(System.out::println);
        Optional.of(t.getPriority()).ifPresent(System.out::println);
    }
}

```
5、Thread中join使用
```java
package com.learn.concurrenty.chapter5;

import java.util.Optional;
import java.util.stream.IntStream;

/**
 * @ClassName: ThreadJoin
 * @Description: Thread中join使用
 * @Author: lin
 * @Date: 2020/3/21 23:04
 * History:
 * @<version> 1.0
 */
public class ThreadJoin {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() ->{
            IntStream.range(1,200)
                    .forEach(i-> System.out.println(Thread.currentThread().getName() + "->" +i));
        });

        Thread t2 = new Thread(() ->{
            IntStream.range(1,200)
                    .forEach(i-> System.out.println(Thread.currentThread().getName() + "->" +i));
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        Optional.of("All of task finish done.").ifPresent(System.out::println);
        IntStream.range(1,200)
                .forEach(i-> System.out.println(Thread.currentThread().getName() + "->" +i));

    }
}

```
5.1、Thread 类中join(Long)使用，表示一段时间没有其他的没有执行，那么其他就执行
```java
package com.learn.concurrenty.chapter5;

import java.util.Optional;
import java.util.stream.IntStream;

/**
 * @ClassName: ThreadJoin
 * @Description: Thread中join使用
 * @Author: lin
 * @Date: 2020/3/21 23:04
 * History:
 * @<version> 1.0
 */
public class ThreadJoin2 {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() ->{
            try {
                System.out.println("t1 is running");
                Thread.sleep(10000);
                System.out.println("t1 is done.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });


        t1.start();
        //加入这个表示100毫秒没有执行，那么就执行下面的
        t1.join(100);

        Optional.of("All of task finish done.").ifPresent(System.out::println);
        IntStream.range(1,200)
                .forEach(i-> System.out.println(Thread.currentThread().getName() + "->" +i));

        //这里当前线程是main, main在等待自己结束，这个main线程一直在等待自己结束，所以这里会一直等待下去
//        Thread.currentThread().join();
    }
}

```
5.2 数据采集，一个服务器 一个线程
```java
package com.learn.concurrenty.chapter5;

import java.util.Optional;
import java.util.stream.IntStream;

/**
 * 数据采集，一个服务器 一个线程
 *
 * @ClassName: ThreadJoin
 * @Description: Thread中join使用
 * @Author: lin
 * @Date: 2020/3/21 23:04
 * History:
 * @<version> 1.0
 */
public class ThreadJoin3 {
    public static void main(String[] args) throws InterruptedException {
        long startTimestamp = System.currentTimeMillis();

        Thread t1 = new Thread(new CaptureRunnable("M1", 10000L));
        Thread t2 = new Thread(new CaptureRunnable("M1", 10000L));
        Thread t3 = new Thread(new CaptureRunnable("M1", 10000L));


        t1.start();
        t2.start();
        t3.start();
        //如果不加使用 join,那么 就会出现 数据都还么有采集完，就报存了。
        // 所以 使用join 的目的就是 所有数据采集之后，再去保存
        t1.join();
        t2.join();
        t3.join();

        long endTimestamp = System.currentTimeMillis();
        System.out.printf("Save data begin timestamp is:%s, end timestamp is:%s\n", startTimestamp, endTimestamp);
        //如果不加入join，打印的结果是先打印
        // Save data begin timestamp.....
        // M1.....
        // M2....
        // M3

        //而使用了join，那么打印就是 这种
        // M1.....
        // M2....
        // M3
        //Save data begin timestamp.....
    }
}

class CaptureRunnable implements Runnable{

    private String machineName;

    private long spendTime;

    public CaptureRunnable(String machineName, long spendTime) {
        this.machineName = machineName;
        this.spendTime = spendTime;
    }

    /**
     */
    @Override
    public void run() {
        try {
            Thread.sleep(spendTime);
            System.out.printf(machineName  + "completed data capture at timestamp [%s] and successfully.\n", spendTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public  String getResult(){
        return  machineName + "finish.";
    }
}

```
6、线程的中断,使用什么方式来中断线程，那种方式最优雅的去中断
```java
package com.learn.concurrenty.chapter6;

import com.learn.concurrenty.DefaultThreadFactory;
import com.learn.concurrenty.chapter1.TryConcurrent;

import java.lang.annotation.Native;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程中断
 * @ClassName: ThreadInterrupt
 * @Description:
 * @Author: lin
 * @Date: 2020/3/22 20:40
 * History:
 * @<version> 1.0
 */
public class ThreadInterrupt {


    public static void main(String[] args) {
        ExecutorService executorService = new ThreadPoolExecutor(2,
                5, 200,
                TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(6),
                new DefaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        /**
         *  调用 execute方法时，线程池 从工作线程中选一个线程 进行 t.start();
         * 在调用t.start() 方法 后 会去执行 Worker 中的 run方法 ，run方法中会去调用runWorker()方法
         */
//        MyThreadInterrupt myThreadInterrupt = new MyThreadInterrupt();
//        System.out.println("线程中断状态。。。"+ myThreadInterrupt.isInterrupted());
//        executorService.execute(myThreadInterrupt);
//        System.out.println("线程中断状态。。。"+ myThreadInterrupt.isInterrupted());
        /**
         * 使用shutdownNow() 中断线程, 因为在shutdownNow()方法里 将状态设置为 STOP状态，
         * 这种状态在执行runWork方法时，会调用getTask()方法 进行状态的判断然后返回null,导致线程的退出。
         *
         * 我们知道，使用shutdownNow方法，可能会引起报错，使用shutdown方法可能会导致线程关闭不了。
         *
         * 所以当我们使用shutdownNow方法关闭线程池时，一定要对【任务里进行异常捕获】。
         *
         *  当我们使用shutdown方法关闭线程池时，一定要确保任务里不会有永久阻塞等待的逻辑，否则线程池就关闭不了。
         */

//        executorService.shutdownNow();
        //打印结果如下 , 这种方式会去打断这个线程
//        收到打断信号。 pool-1-thread1
//        java.lang.InterruptedException: sleep interrupted
//        at java.lang.Thread.sleep(Native Method)
//        at com.learn.concurrenty.chapter6.MyThreadInterrupt.run(ThreadInterrupt.java:42)
//        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
//        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
//        at java.lang.Thread.run(Thread.java:748)


         Thread t = new Thread(){
             @Override
             public void run() {
                 while (true){

                 }
             }
         };
          t.start();

          Thread mainThread = Thread.currentThread();
        Thread t2 = new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // t.interrupt()并没有打断线程。
                // t.interrupt();
                mainThread.interrupt();
                System.out.println("interrupt");
            }
        };

        t2.start();
        try {
            //t.join他join的是main线程，而不是当前线程，
            // join的是main线程，打断是t线程 所以不能打断，
            // 那么怎么将当前线程打断喃？ 我们将上面的   t.interrupt();
            // 改成 mainThread.interrupt(); 这样就可以打断线程了， 所以会进入下面
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("进入这里。。。。。。。。。");
        }

        // 打印结果
//        interrupt
//        java.lang.InterruptedException
//        at java.lang.Object.wait(Native Method)
//        at java.lang.Thread.join(Thread.java:1252)
//        at java.lang.Thread.join(Thread.java:1326)
//        进入这里。。。。。。。。。
//        at com.learn.concurrenty.chapter6.ThreadInterrupt.main(ThreadInterrupt.java:91)
    }
}

class  MyThreadInterrupt extends Thread{
 private static  final Object MONITOR = new Object();
//
//
//    @Override
//    public void run(){
//        try {
//            Thread.sleep(10);
//        } catch (InterruptedException e) {
//            System.out.println("收到打断信号。 " + Thread.currentThread().getName());
//            e.printStackTrace();
//        }
//
//    }



//    @Override
//    public void run(){
//        while (true) {
//            synchronized (ThreadInterrupt.class) {
//                try {
//                    wait(10);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }
//    }

    //这种运行的结果是
//    Exception in thread "pool-1-thread1" java.lang.IllegalMonitorStateException
//    at java.lang.Object.wait( Native Method)
//    at com.learn.concurrenty.chapter6.MyThreadInterrupt.run(ThreadInterrupt.java:77)
//    at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
//    at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
//    at java.lang.Thread.run(Thread.java:748)


//    @Override
//    public void run(){
//        while (true) {
//            synchronized (MONITOR) {
//                try {
//                    MONITOR.wait(10);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }
//    }

    /**
     * 收到了一个中断信号，这个中断信号也被捕获了
     *  但是这个线程 没有退出，只是设置了一个中断flag,
     *
     */
    // 那么为什么这个线程没有退出喃？ 因为这里是循环 任务没有完成 ，所以这个程序一直在执行

//> Task :ThreadInterrupt.main()
//    java.lang.InterruptedException
//    at java.lang.Object.wait(Native Method)
//    at com.learn.concurrenty.chapter6.MyThreadInterrupt.run(ThreadInterrupt.java:103)
//    at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
//    at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
//    at java.lang.Thread.run(Thread.java:748)
//    java.lang.InterruptedException
//    at java.lang.Object.wait(Native Method)
//    at com.learn.concurrenty.chapter6.MyThreadInterrupt.run(ThreadInterrupt.java:103)
//    at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
//    at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
//    at java.lang.Thread.run(Thread.java:748)

}

```
6.1 如何优雅的打断线程, 使用flag方式
```java
package com.learn.concurrenty.chapter6;

/**
 * 优雅的去中断线程, 这里使用的flag去中断线程
 * @ClassName: ThreadCloseGraceful
 * @Description:
 * @Author: lin
 * @Date: 2020/3/23 9:22
 * History:
 * @<version> 1.0
 */
public class ThreadCloseGraceful {

    private static  class Worker extends Thread{
        private volatile boolean start =  true;

        @Override
        public void run() {
            while (start){
                //
            }
        }

        public void  shutDown(){
            this.start = false;
        }
    }

    public static void main(String[] args) {
        Worker worker = new Worker();
        worker.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        worker.shutDown();
    }
}

```
6.2 使用interrupt方式
```java
package com.learn.concurrenty.chapter6;

/**
 * 优雅的去中断线程,这里使用打断的方式
 * @ClassName: ThreadCloseGraceful
 * @Description:
 * @Author: lin
 * @Date: 2020/3/23 9:22
 * History:
 * @<version> 1.0
 */
public class ThreadCloseGraceful2 {

    private static  class Worker extends Thread{

        @Override
        public void run() {
            while (true){
                try {
                    Thread.sleep(1);
                    //sleep 会去捕获的打断信号
                } catch (InterruptedException e) {
                    break;// return 的问题是这个 会直接退出去了
                    // 而break 在这个线程里面有可能 还会做一些其他事情，
                    // 它会有机会去做其他的事情，而使用 return 那么后面的逻辑有可能就执行不到了
                }
            }

            //2、
//            while (true){
//                //如果这里不去sleep，而是去判断，那么会退出吗?
//                // 可以看的这种 会去判断是不是被中断的，所以就直接退出去了
//                // 如果当前线程被打断了 那是true ,否则是false;
//                if(Thread.interrupted()){
//                    break;
//                }
//            }
        }


    }

    public static void main(String[] args) {
        Worker worker = new Worker();
        worker.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //去打断
        worker.interrupt();
    }
}

```
6.3 如果线程BLOCK时候如果去打断？
```java
package com.learn.concurrenty.chapter6;

/**
 * 优雅的去中断线程,这里使用打断的方式
 * @ClassName: ThreadCloseGraceful
 * @Description:
 * @Author: lin
 * @Date: 2020/3/23 9:22
 * History:
 * @<version> 1.0
 */
public class ThreadCloseGraceful3 {

    private static  class Worker extends Thread{
        private boolean flag =true;

        @Override
        public void run() {
            while (flag){
               //connection
                // 如果这里取通信的时候，这里BLOCK住了那么
                // 就没有办法去监听 这个Interrupted 信号了
                // 那么如何去中断呢？ 进入下面的ThreadService
            }


        }


    }

    public static void main(String[] args) {
        Worker worker = new Worker();
        worker.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //去打断
        worker.interrupt();
    }
}

```
6.4 如果线程BLOCK时候如果去打断？通过设置 守护线程 方式
```java
package com.learn.concurrenty.chapter6;

/**
 * @ClassName: ThreadService
 * @Description:
 * @Author: lin
 * @Date: 2020/3/23 9:52
 * History:
 * @<version> 1.0
 */
public class ThreadService {
    //可以将这个线程定义成守护线程，如果执行线程退出了那么守护线程也就退出了
    // 也就是说守护线程去做这件事，然后执行线程生命周期结束 你的守护线程也要结束

    /**
     * 定义执行线程
     */
    private Thread executeThread;

    private boolean finished = false;
    public void  execute(Runnable task){
         executeThread = new Thread(){
             @Override
             public void run() {
                //设置一个守护线程
                 Thread runner = new Thread(task);
                 runner.setDaemon(true);
                 // 执行线程 ，这个执行线程结束了，而那个守护线程可能还没有启动就结束了，
                 // 显然这样是不合理的，那么怎么处理呢？ 我们在守护线程里加入 join,
                 //让 runner 执行到死为止，然后在去执行执行线程中的一些逻辑
                 // 这里BLOCK 主，这个线程可能执行时间太长，永远不好吧finished变成true，
                 // 所声明周期会非常的长， 所以这样不好，那么就用别人去显示的调用
                 //
                 try {
                     runner.join();
                     finished =true;
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }
             }
         };

         executeThread.start();
    }

    /**
     * 最多等 多少秒，如果这段时间内没有处理完，那么就将其干掉
     * @param mills
     */
    public void shutDown(long mills){
       // 最多执行30毫秒, 如果线程在3毫秒时就结束了，
        // 我们不能一直等到30毫秒时才去结束
        long currentTime = System.currentTimeMillis();
        while (!finished){
           if(System.currentTimeMillis() - currentTime >= mills){
               System.out.println("任务超时，需要结束它！");
               //怎么去结束呢？ 这个上面的runner的join是当前线程 executeThread去join的
               // 那么通过executeThread 去打断他,在上面 就会被捕获到，
               // 执行线程生命周期结束后，那么守护线程 也自然而然的跟着结束
               executeThread.interrupt();
               break;
           }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                System.out.println("执行线程被打断!");
                e.printStackTrace();
            }
        }
        finished = false;
    }
}

```
6.5、调用
```java
package com.learn.concurrenty.chapter6;

import javafx.concurrent.Worker;

/**
 *
 * @ClassName: ThreadCloseGraceful
 * @Description:
 * @Author: lin
 * @Date: 2020/3/23 9:22
 * History:
 * @<version> 1.0
 */
public class ThreadCloseGraceful4 {

    public static void main(String[] args) {
         ThreadService service = new ThreadService();
         long starTime = System.currentTimeMillis();
         service.execute(()->{
             // load a very heavy resource;
             while (true){

             }
         });
         service.shutDown(10000);
         long endTime = System.currentTimeMillis();
        System.out.println("时间消耗" + (endTime - starTime));
    }
}

```
6.6 小结
```
1、使用falg 标志方式
2、使用interrupt方式
3、运用一个小技巧 使用一个执行线程，然后这个执行线程里面有一个runner线程
把runner线程设置为执行线程的守护线程，当这个执行线程结束是，那么这个runner 守护线程也跟着结束
但是如果这个线程在5s就结束了，我们还需要等到10s去结束吗？肯定不可以
```
7、线程安全，线程同步，synchronize(object)
```java
package com.learn.concurrenty.chapter7;

/**
 * 实现Runnable ，线程安全性
 * @ClassName: TicketRunnable
 * @Description:
 * @Author: lin
 * @Date: 2020/3/20 10:11
 * History:
 * @<version> 1.0
 */
public class TicketRunnable implements Runnable{

    private  int index = 1;

    private final  static  int  MAX =500;
    private final  Object MONITOR = new Object();

    /**
     * 通过将业务抽取出来，就是你的业务逻辑和线程控制 是在不同的object里面
     */
    @Override
    public void run() {
         while (true) {
             // 这里会出线程安全的问题， 因为在多线程情况下,
             // 线程A执行了判断，但是这个index值 比如为499 还没有增加，
             // 这个时候线程B 也进行判断的后，然后往下执行 当线程B执行到打印后
             // 这个index值已经更改了，index值为500
             // 所以这个时候线程A过来执行，再次执行index++ ，
             // 那么这个时候index值就变成了501了。
             // 那么如何解决这种问题呢？  使用同步方式，或者加上 volatile 关键字
             // 这里 使用 synchronized 进行同步
             synchronized (MONITOR) {
                 if (index > MAX) {
                     break;
                 }
                 try {
                     Thread.sleep(5);
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }
                 System.out.println(Thread.currentThread().getName() +
                         " 的号码是：" + (index++));
               }
         }
    }
}
```
7.1 测试synchronize(object)
```java
package com.learn.concurrenty.chapter7;

/**
 * @ClassName: BankVersion1
 * @Description:
 * @Author: lin
 * @Date: 2020/3/20 10:13
 * History:
 * @<version> 1.0
 */
public class BankVersion1 {
    public static void main(String[] args) {
        //这种方法 业务实例就只有一个，而不像第一个版本继承Thread的有多个实例
        //通过将业务抽取出来，就是你的业务逻辑和线程控制 是在不同的object里面
        final TicketRunnable tr = new TicketRunnable();
        Thread w1 = new Thread(tr, "一号窗口");
        Thread w2 = new Thread(tr, "二号窗口");
        Thread w3 = new Thread(tr, "三号窗口");
        w1.start();
        w2.start();
        w3.start();
    }
}

```
7.2 同步方法
```java
package com.learn.concurrenty.chapter7;

/**
 * 测试同步方法
 * @ClassName: SynchronizedRunnable
 * @Description:
 * @Author: lin
 * @Date: 2020/3/23 13:35
 * History:
 * @<version> 1.0
 */
public class SynchronizedRunnable implements Runnable{

    private  int index = 1;

    private final  static  int  MAX =500;
    private final  Object MONITOR = new Object();

    /**
     * 同步方法，这个锁就是 this ,而这个this就是SynchronizedRunnable的一个实例
     *
     *  //同步方法的形式， 这种方法是多个线程去争抢这个同步
     *   方法锁，那个先抢到了就去执行。 比如有三个线程
     *  那个线程抢到了锁 就执行，当一个线程进入这个同步块后 去执行下面的逻辑
     *  在执行完后，其他的线程 进入到这个判断，而这个时候 index 已经大于了 500
     *  所以就退出了
     */
//    @Override
//    public synchronized void run() {
//        while (true) {
//                if (index > MAX) {
//                    break;
//                }
//                try {
//                    Thread.sleep(5);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                System.out.println(Thread.currentThread().getName() +
//                        " 的号码是：" + (index++));
//            }
//    }


    /**
     * 下面方法
     */
    @Override
    public  void run() {
        while (true) {
            if(ticket()){
                break;
            }
        }
    }


    /**
     * 同步方法
     * @return
     */
    private synchronized boolean ticket(){
        if(index > MAX){
            return  true;
        }
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() +
                " 的号码是：" + (index++));
       return  false;
    }
}

```
7.3测试同步方法
```java
package com.learn.concurrenty.chapter7;

/**
 * 测试 synchronized 同步方法，这种方式和 同步块的区别
 * @ClassName: BankVersion1
 * @Description:
 * @Author: lin
 * @Date: 2020/3/20 10:13
 * History:
 * @<version> 1.0
 */
public class BankVersion2 {
    public static void main(String[] args) {

        // 同步方法的锁就是 this, 而这个this就是 SynchronizedRunnable的一个实例
        // 在下面的三个线程 中也是唯一的一个，所以三个线程去争抢这个实例
        final SynchronizedRunnable tr = new SynchronizedRunnable();

        Thread w1 = new Thread(tr, "一号窗口");
        Thread w2 = new Thread(tr, "二号窗口");
        Thread w3 = new Thread(tr, "三号窗口");
        w1.start();
        w2.start();
        w3.start();
    }
}

```
7.4 同步方法 相当于 synchronized(this)
````java
package com.learn.concurrenty.chapter7;

/**
 *  同步方法 和 synchronized(this)
 * @ClassName: SynchronizedTest
 * @Description:
 * @Author: lin
 * @Date: 2020/3/23 13:35
 * History:
 * @<version> 1.0
 */
public class SynchronizedThis {

    public static void main(String[] args) {
        ThisLock thisLock = new ThisLock();
        Thread thread = new Thread("t1") {
            @Override
            public void run() {
                thisLock.method();
            }
        };
        thread.start();

        Thread thread2 = new Thread("t2") {
            @Override
            public void run() {
                thisLock.method2();
            }
        };
        thread2.start();

        //首先测试，方法method2方法不加上synchronized， 那么这两个线程如何执行呢？
        // 打印结果如下，可以看到这个基本上都是同时执行，这说明 没有同步的方法可以去访问
        //t2没有加锁
        //t1抢占这个锁

        //如果这个方法method2方法加上synchronized， 那么这个两个线程在执行的时候会怎么样？
        //打印的结果是
        // 先 t1抢占这个锁 ，
        // 然后才是 t2抢占这个锁
        // 那么这就是说明了 这个锁是同一个，两个线程谁先争抢到，就先执行，
        // 线程1先抢到了这个锁，那么线程2 就要等待这个 线程1释放了这个锁才能获取。
        // 这里我们可以总结下 同步方法 这个锁就是this.所以多个线程就是抢这个this锁




    }
}

class ThisLock{

    private static  final Object LOCK = new Object();
    public synchronized  void method(){
        try {
            System.out.println(Thread.currentThread().getName() + "抢占这个锁");
            Thread.sleep(10_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized   void method2(){
        try {
            System.out.println(Thread.currentThread().getName() + "抢占这个锁");
            Thread.sleep(10_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 如果方法使用不同的 对象作为锁，那么在执行的时候基本上是同时执行
     * 这个 锁和 同步方法的锁 不一样。 这个显示的指定了对象锁
     */
    public void method3(){
        synchronized (LOCK) {
            try {
                System.out.println(Thread.currentThread().getName() + "抢占这个锁");
                Thread.sleep(10_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

````
7.5同步静态方法
```java
package com.learn.concurrenty.chapter7;

/**
 * 静态同步方法
 * @ClassName: SynchronizeStatic
 * @Description:
 * @Author: lin
 * @Date: 2020/3/23 15:04
 * History:
 * @<version> 1.0
 */
public class SynchronizeStatic {
    public synchronized static void method1(){
        System.out.println("m1 " + Thread.currentThread().getName());
        try {
            Thread.sleep(10_000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized static void method2(){
        System.out.println("m2 " + Thread.currentThread().getName());
        try {
            Thread.sleep(10_000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public  static void method3(){
        System.out.println("m3 " + Thread.currentThread().getName());
        try {
            Thread.sleep(10_000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

```
7.6测试同步静态方法
```java
package com.learn.concurrenty.chapter7;

/**
 * @ClassName: SynchronizeStaticTest
 * @Description: 测试 同步静态方法
 * @Author: lin
 * @Date: 2020/3/23 15:07
 * History:
 * @<version> 1.0
 */
public class SynchronizeStaticTest {
    public static void main(String[] args) {
        Thread t1 = new Thread("T1"){
            @Override
            public void run() {
                SynchronizeStatic.method1();
            }
        };
        t1.start();


        Thread t2 = new Thread("T2"){
            @Override
            public void run() {
                SynchronizeStatic.method2();
            }
        };
        t2.start();

        //两个线程去调用 同步静态方法，如果这个两个同步静态的锁补一样那么就会同时输出
        // 如果两个方的锁是同一个，那么这个锁被两个线程争抢，
        // 谁先抢到那么就执行，另外的一个线程就需要等待 锁的持有线程是否锁，才能获取到



        Thread t3 = new Thread("T3"){
            @Override
            public void run() {
                SynchronizeStatic.method3();
            }
        };
        t2.start();
    }
}

```

Tips
```
1、同步代码块synchronize(Object)，这种是多个线程去竞争锁，这个是显示去指定一个对象锁。和同步方法( synchronize(this    ))不同
2、同步方法的方式 和 synchronize(this)等价，这个this就是当前对象实例.
这个同步方法 在多个线程去竞争这个对象是实例的时候 就和造成其他的 线程一直等待
所以将在上面方法SynchronizedRunnable中 第二个方法将其 同步方法抽取出来，
这样 多个线程去竞争这个锁，然后同步去执行。而不是一个线程去执行。
this 锁是通过它的实例去作为一个锁。


```
8、死锁
```java
package com.learn.concurrenty.chapter8;

/**
 * 死锁
 * @ClassName: DeadLock
 * @Description:
 * @Author: lin
 * @Date: 2020/3/23 15:26
 * History:
 * @<version> 1.0
 */
public class DeadLock {
    private OtherService service ;

    public DeadLock(OtherService otherService){
        this.service = otherService;
    }
    private final Object lock = new Object();


    public void m1(){
        synchronized (lock){
            System.out.println("m1");
            service.s1();
        }
    }

    public void m2(){
        synchronized (lock){
            System.out.println("m2");
        }
    }
}

```
8.1 其他服务
```java
package com.learn.concurrenty.chapter8;

/**
 * 其他 的服务
 * @ClassName: OtherService
 * @Description:
 * @Author: lin
 * @Date: 2020/3/23 15:27
 * History:
 * @<version> 1.0
 */
public class OtherService {
    private final Object LOCK = new Object();

    private DeadLock  deadLock;

    public void s1(){
        synchronized (LOCK){
            System.out.println("s1=============");
        }
    }

    public void s2(){
        synchronized (LOCK){
            System.out.println("s2=============");
            deadLock.m2();
        }
    }



    public void setDeadLock(DeadLock deadLock) {
        this.deadLock = deadLock;
    }
}

```
8.2测试死锁
```java
package com.learn.concurrenty.chapter8;

import com.learn.concurrenty.chapter7.SynchronizeStatic;

/**
 * 死锁测试
 * @ClassName: DeadLock
 * @Description:
 * @Author: lin
 * @Date: 2020/3/23 15:26
 * History:
 * @<version> 1.0
 */
public class DeadLockTest {
    public static void main(String[] args) {
        OtherService otherService = new OtherService();
        DeadLock deadLock = new DeadLock(otherService);
        otherService.setDeadLock(deadLock);

        Thread t1 = new Thread(){
            @Override
            public void run() {
                while (true){
                    deadLock.m1();
                }
            }
        };
        t1.start();

        Thread t2= new Thread(){
            @Override
            public void run() {
                while (true) {
                    otherService.s2();
                }
            }
        };
        t2.start();

    }
}

```
tips
```
1、死锁造成的原因，比如两个线程， 这个两个线程持有对方的锁，而这个线程都要等待地方先释放锁，
但是这两个线程都不先释放对方的锁，所以就造成了死循环。一直执行下去等待对方先释放锁。
2、造成死锁后 使用jps 去查看这个进程的pid
3、使用jstack pid 去分析死锁

```
9、线程间的通信,生产消费者，这个是一个生产者和一个消费者的情况下，在多线程情况下会有问题
```java
package com.learn.concurrenty.chapter9;

/**
 * 线程间的通信
 * @ClassName: ProduceConsumerVersion
 * @Description:
 * @Author: lin
 * @Date: 2020/3/23 16:17
 * History:
 * @<version> 1.0
 */
public class ProduceConsumerVersion {
    private int i = 0;
    private  final Object lock = false;
    /**
     * 这里 让线程更新的值被另一个线程可见， 可见性
     */
    private volatile boolean isProduced = false;

    /**
     * 生产者
     */
    public void produce (){
        synchronized (lock){
            // 判断是否已经生产了数据，并且这个判断有问题,
            // 这里 在线wait后 结束，要再次进行循环判断，判断这包含条件是否成立
            if(isProduced){
                //如果生产了那么就让线程等待，让消费者去消费
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else{
                //如果没有生产那么就 生产
                i++;
                System.out.println("p ->" + i);
                // 生产好了之后 去通知消费者
                lock.notify();
                isProduced = true;
            }
        }
    }

    /**
     * 消费者
     */
    public void consume(){
        synchronized (lock){
            //首先判断是否生成了，如果生成了，那么消费者就消费
            if(isProduced){
                System.out.println("c ->" + i);
                //消费完了之后，通知生产者去生成
                lock.notify();
                isProduced = false;
            }else {
                //如果没有生产那么，就等待生成者生产
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        ProduceConsumerVersion p = new ProduceConsumerVersion();

        Thread t1= new Thread(){
            @Override
            public void run() {
                while (true) {
                    p.produce();
                }
            }
        };
        t1.start();

        Thread t2= new Thread(){
            @Override
            public void run() {
                while (true) {
                    p.consume();
                }
            }
        };
        t2.start();
    }
}

```
9.2 多线程 有多个生产者和消费者情况下
```java
package com.learn.concurrenty.chapter9;

import java.util.stream.Stream;

/**
 * 在多个生产者和消费者的情况下 ，使用while的方式
 * @ClassName: ProduceConsumerVersion
 * @Description:
 * @Author: lin
 * @Date: 2020/3/23 16:17
 * History:
 * @<version> 1.0
 */
public class ProduceConsumerVersion3 {
    private int i = 0;
    private  final Object lock = false;
    /**
     * 这里 让线程更新的值被另一个线程可见， 可见性
     */
    private volatile boolean isProduced = false;

    /**
     * 生产者
     */
    public void produce (){
        synchronized (lock){
            // 判断是否已经生产了数据
            while(isProduced){
                //如果生产了那么就让线程等待，让消费者去消费
                try {
                    // wait 会释放锁， 如果在多线程情况下，
                    // 这里不使用while循环进行再次判断，那么就会造成假死的情况
                    // 比如：有两个线程 当第一个线程执行完这个wait方法后 会释放锁
                    // 也就是唤醒
                    // 那 进入下面的代码逻辑 生产产品 ，isProduced被更新为true
                    // 那么其他的线程进入了之后 就会去判断，这个判断条件符合就会进入
                    // while循环 然后进行等待, 如果这个添加不符合就去生产 数据
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
                //如果没有生产那么就 生产
                i++;
                System.out.println("p ->" + i);
                // 生产好了之后 去通知消费者
                lock.notifyAll();
                isProduced = true;
        }
    }

    /**
     * 消费者
     */
    public void consume(){
        synchronized (lock){
            // 如果 没有生产数据那就等待
            while(!isProduced){
                //如果没有生产那么，就等待生成者生产
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("c ->" + i);
            //消费完了之后，通知生产者去生成
            lock.notifyAll();
            isProduced = false;

        }
    }

    public static void main(String[] args) {
        ProduceConsumerVersion3 p = new ProduceConsumerVersion3();
        Stream.of("p1","p2", "p3").forEach(n ->{
            Thread t1= new Thread(() -> {
                while (true) {
                    p.produce();
                }
            });
            t1.start();
        });


        Stream.of("c1","c2", "c3", "c4").forEach(n -> {
            Thread t2 = new Thread(() -> {
                while (true) {
                    p.consume();
                }
            });
            t2.start();
        });
    }

}

```
9.3、sleep 和 wait的本质区别
```java
package com.learn.concurrenty.chapter9;

import java.util.stream.Stream;

/**
 * @ClassName: DifferenceOfWaitAndSleep
 * @Description: wait和sleep的区别
 * @Author: lin
 * @Date: 2020/3/24 9:03
 * History:
 * @<version> 1.0
 */
public class DifferenceOfWaitAndSleep {

    private static final  Object LOCK = new Object();

    public static void main(String[] args) {
        Stream.of("T1","T2").forEach(na -> {
//            new Thread(() -> m1());
            new Thread(() -> m2());
        });

    }

    public static  void  m1(){
        //这里如果加上synchronize 那么两个线程就是去争抢
        synchronized (LOCK) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public static  void  m2(){
        synchronized (LOCK) {
            try {
                //在调用wait方法时候 要结合 synchronize来使用，
                // 因为这个wait 必须要持有monitor, 那么这样使用的谁来做monitor喃
                // 这里使用的LOCK对象做的monitor。所以这里这里 加上synchronized(LOCK)
                System.out.println("The Thread " + Thread.currentThread().getName() + "enter.");
                LOCK.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

```
9.4 测试
```java
package com.learn.concurrenty.chapter9;

import javax.swing.text.html.Option;
import java.util.*;

/**
 * @ClassName: CaptureService
 * @Description: 综合案例测试
 * @Author: lin
 * @Date: 2020/3/24 10:09
 * History:
 * @<version> 1.0
 */
public class CaptureService {

    final static  private LinkedList<Control> CONTROLS = new LinkedList<>();

    private static final  int COUNT = 5;
    public static void main(String[] args) {

        List<Thread> worker = new ArrayList<>();
        Arrays.asList("M1", "M2", "M3", "M4", "M5", "M6", "M7", "M8", "M9", "M10")
                .stream()
                .map(CaptureService::createCaptureThread)
                .forEach(t -> {
                    // 不过这里 启动线程的时候 ，不应该启动10个， 这个要学习线程池里面的思路，
                    // 如果 当线程 不大于核心线程数的时候，就直接使用核心线程数里面的线程来启动
                    // 当大于核心线程的时候就将线程加入 工作线程中去
                    t.start();
                    //这里不能join ,因为join 后只有一个线程 去跑
                    // 等这个线程跑完之后再去启动另外的一个线程
                    // 所以 要把它存起来
                    worker.add(t);
                });

        worker.stream().forEach(t -> {
            try {
                //这里来join，并且捕获异常
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        //这个时候线程都启动了并且join了 ，那么这里在打印
        Optional.of("All of capture work finished").ifPresent(System.out::println);
    }

    private static  Thread  createCaptureThread(String name){

        return new Thread(()->{
            // 运行资格，允许需要多个少个线程，多余的线程就wait
            Optional.of("The worker ["+Thread.currentThread().getName() +"] begin capture data")
                    .ifPresent(System.out::println);

            // 控制线程执行个数，如果大于指定的个数 ，那么其他的线程就等待
            synchronized (CONTROLS){
                 while (CONTROLS.size() > COUNT){
                     try {
                         CONTROLS.wait();
                     } catch (InterruptedException e) {
                         e.printStackTrace();
                     }
                 }
                 //所以这个要放在synchronize中，进行同步控制
                CONTROLS.addLast(new Control());
             }

             //下面的执行是并行化的
            Optional.of("The worker [" + Thread.currentThread().getName()+"] is working...")
                    .ifPresent(System.out::println);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (CONTROLS){
                Optional.of("The worker [" + Thread.currentThread().getName()+"] END capture data ...")
                        .ifPresent(System.out::println);
                CONTROLS.removeFirst();
                CONTROLS.notifyAll();
            }
        }, name);
    }

    /**
     * 控制 线程个数
     */
    private static class Control{}
}

```
9.5、 sleep 和 wait的本质区别总结
``` 
1、sleep是Thread的方法 ，wati是Object的方法(所有object都会有这个方法)
2、sleep 不是释放锁， 而wait释放锁
3、wait 必须写要和synchronize配合使用，而sleep 并不需要等于一个synchronized
4、使用sleep 方法不需要唤醒，但是wait需要唤醒

```
10、自定义显示的锁,定义接口
```java
package com.learn.concurrenty.chapter10;

import java.util.Collection;
import java.util.concurrent.TimeoutException;

/**
 * 自定义Lock锁
 * @ClassName: CustomizeLock
 * @Description:
 * @Author: lin
 * @Date: 2020/3/24 11:11
 * History:
 * @<version> 1.0
 */
public interface CustomizeLock {

    class  TimeOutException extends Exception{
        public  TimeOutException(String message){
            super(message);
        }
    }

    /**
     * 定义 锁方法， 允许中断 （synchronized 不允许中断）
     * @throws InterruptedException
     */
    void lock() throws InterruptedException;

    /**
     * 在规定的时间没有获取到锁
     * @param mills
     * @throws InterruptedException
     * @throws TimeoutException
     */
    void lock(long mills) throws  InterruptedException, TimeoutException;

    /**
     * 释放锁
     */
    void  unLock();

    /**
     * 获取阻塞住的线程
     * @return
     */
    Collection<Thread> getBlockThread();

    /**
     * 获取阻塞线程数量
     * @return
     */
    int getBLockSize();
}

```
10.1 实现这个接口类
```java
package com.learn.concurrenty.chapter10;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

/**
 * 实现自定义 锁
 * @ClassName: BooleanLock
 * @Description:
 * @Author: lin
 * @Date: 2020/3/24 11:18
 * History:
 * @<version> 1.0
 */
public class BooleanLock implements  CustomizeLock{
    /**
     * 初始值
     */
    private boolean initValue;
    private Collection<Thread> blockedThreadCollection = new ArrayList<>();

    /**
     * 这个 表示 那个线程，作用是那个线程加的锁，就是它自己去释放这个锁
     * 不能让其他的线程释放 加锁的这个线程
     */
    private Thread currentThread;

    /**
     * 在构造函数中去初始化
     */
    public BooleanLock() {
        this.initValue = false;
    }

    @Override
    public synchronized void lock() throws InterruptedException {
        // 如果这个锁 是true, 那么说明被一个线程抢到了，
        // 其他的线程等待，然后将这个线程加入到这个集合中去
        while (initValue){
            blockedThreadCollection.add(Thread.currentThread());
            this.wait();
        }
        blockedThreadCollection.remove(Thread.currentThread());
        // 如果这锁没有被 抢到那么 就设置
        this.initValue = true;
        currentThread = Thread.currentThread();
    }

    @Override
    public void lock(long mills) throws InterruptedException, TimeoutException {

    }

    @Override
    public synchronized void unLock() {
        // 这个判断是 那个线程加的锁，就让那个线程自己释放这个锁，
        // 如果不判断 那么其他线程就可能来释放 这个被加锁的线程，
//        if(Thread.currentThread() == currentThread) {
            // 这个锁是 initValue;
            this.initValue = false;
            Optional.of(Thread.currentThread().getName() + " release the lock monitor.")
                    .ifPresent(System.out::println);
            this.notifyAll();
//        }
    }

    @Override
    public Collection<Thread> getBlockThread() {
        // 这里 可能存在修改 blockedThreadCollection, 所以这里不能被修改

        return Collections.unmodifiableCollection(blockedThreadCollection);
    }

    @Override
    public int getBLockSize() {
        return blockedThreadCollection.size();
    }
}

```
10.2 测试这个显示锁
```java
package com.learn.concurrenty.chapter10;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * 测试自定义的锁, 这种方式存在问题，就是其他线程会争抢到锁
 * @ClassName: LockTest
 * @Description:
 * @Author: lin
 * @Date: 2020/3/24 12:14
 * History:
 * @<version> 1.0
 */
public class LockTest {
    public static void main(String[] args) throws InterruptedException {
        final  BooleanLock booleanLock = new BooleanLock();
        Stream.of("T1", "T2", "T3", "T4")
                .forEach(name ->
                        new  Thread(()->{
                            try {
                                booleanLock.lock();
                                Optional.of(Thread.currentThread().getName() +
                                        " have the lock monitor").ifPresent(System.out::println);
                                work();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }finally {
                                booleanLock.unLock();
                            }
                        },name).start()
                );

        // 这里取非法操作
        Thread.sleep(100);
        // 这里取非法操作,去释放锁，那么这种情况
        // 就会造成 一个线程抢到锁之后 还没有释放了锁，其他的线程也抢到了锁
        // 所以 只有谁加了锁，谁才去释放这个锁
        booleanLock.unLock();
// 

    }

    private static  void work() throws InterruptedException {
        Optional.of(Thread.currentThread().getName() + " is Working....")
        .ifPresent(System.out::println);
        Thread.sleep(10_000);
    }
}

```
10.3、等待锁释放超时方法实现
```java
package com.learn.concurrenty.chapter10;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

/**
 * 实现自定义 锁
 * @ClassName: BooleanLock
 * @Description:
 * @Author: lin
 * @Date: 2020/3/24 11:18
 * History:
 * @<version> 1.0
 */
public class BooleanLock implements  CustomizeLock{
    /**
     * 初始值
     */
    private boolean initValue;
    private Collection<Thread> blockedThreadCollection = new ArrayList<>();

    /**
     * 这个 表示 那个线程，作用是那个线程加的锁，就是它自己去释放这个锁
     * 不能让其他的线程释放 加锁的这个线程
     */
    private Thread currentThread;

    /**
     * 在构造函数中去初始化
     */
    public BooleanLock() {
        this.initValue = false;
    }

    @Override
    public synchronized void lock() throws InterruptedException {
        // 如果这个锁 是true, 那么说明被一个线程抢到了，
        // 其他的线程等待，然后将这个线程加入到这个集合中去
        while (initValue){
            blockedThreadCollection.add(Thread.currentThread());
            this.wait();
        }
        blockedThreadCollection.remove(Thread.currentThread());
        // 如果这锁没有被 抢到那么 就设置
        this.initValue = true;
        currentThread = Thread.currentThread();
    }

    @Override
    public synchronized void lock(long mills) throws InterruptedException, TimeoutException {
          if(mills <= 0){
              lock();
          }
          long hasRemaining = mills;
          long endTime = System.currentTimeMillis() + mills;
          while (initValue){
              if(hasRemaining <= 0){
                  // 等待锁释放 超时
                  throw  new TimeoutException(" Time out");
              }
              //如果这个 锁被其他的线程抢到了 ，那么就将其他的线程加入到 等待队里中去
              blockedThreadCollection.add(Thread.currentThread());
              //让其他线程等待
              this.wait(mills);
              hasRemaining = endTime - System.currentTimeMillis();
          }
          //这里 一个线程抢到锁 将其值改为true
          this.initValue = true;
          this.currentThread = Thread.currentThread();
    }

    @Override
    public synchronized void unLock() {
        // 这个判断是 那个线程加的锁，就让那个线程自己释放这个锁，
        // 如果不判断 那么其他线程就可能来释放 这个被加锁的线程，
        if(Thread.currentThread() == currentThread) {
            // 这个锁是 initValue;
            this.initValue = false;
            Optional.of(Thread.currentThread().getName() + " release the lock monitor.")
                    .ifPresent(System.out::println);
            this.notifyAll();
        }
    }

    @Override
    public Collection<Thread> getBlockThread() {
        // 这里 可能存在修改 blockedThreadCollection, 所以这里不能被修改

        return Collections.unmodifiableCollection(blockedThreadCollection);
    }

    @Override
    public int getBLockSize() {
        return blockedThreadCollection.size();
    }
}

```
10.3.1、测试等待锁释放超时
```java
package com.learn.concurrenty.chapter10;

import java.util.Optional;
import java.util.concurrent.TimeoutException;
import java.util.stream.Stream;

/**
 * 测试自定义的锁, 等待锁 释放超时的情况
 * @ClassName: LockTest
 * @Description:
 * @Author: lin
 * @Date: 2020/3/24 12:14
 * History:
 * @<version> 1.0
 */
public class LockTest2 {
    public static void main(String[] args) throws InterruptedException {

        final  BooleanLock bk = new BooleanLock();

        Stream.of("T1", "T2", "T3", "T4")
                .forEach(name ->
                        new  Thread(()->{
                            try {
                                bk.lock(10L);
                                Optional.of(Thread.currentThread().getName() +
                                        " have the lock monitor").ifPresent(System.out::println);
                                work();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (TimeoutException e) {
                                Optional.of(Thread.currentThread().getName()+" time out")
                                        .ifPresent(System.out::println);
                            } finally {
                                bk.unLock();
                            }
                        },name).start()
                );

        //打印结果
//        T1 have the lock monitor
//        T1 is Working....
//        T2 time out
//        T4 time out
//        T3 time out
//        T1 release the lock monitor.

    }

    private static  void work() throws InterruptedException {
        Optional.of(Thread.currentThread().getName() + " is Working....")
        .ifPresent(System.out::println);
        Thread.sleep(10_000);
    }
}

```
10、4 给程序注入勾子,将这个代码放在linux中测试
```java
package com.learn.concurrenty.chapter10;

/**
 * @ClassName: ExitCapture
 * @Description: 测试在linux中 退出是，进行通知
 * @Author: lin
 * @Date: 2020/3/24 15:29
 * History:
 * @<version> 1.0
 */
public class ExitCapture {
    public static void main(String[] args) {
        //给程序加入勾子, 注入 一个通知，让程序在结束的时候进行通知
      Runtime.getRuntime().addShutdownHook(new Thread(()->{
          System.out.println("The application will be exit.");
          notifyAndRelease();
      }));
      int i =0;
       while (true){
           try {
               Thread.sleep(1_000L);
               System.out.println("I  am working......");
           } catch (Throwable e) {
           }
           //下面的代码如果 写在 try里面，那么到执行if判断时候，就被捕获了
           i++;
           if(i > 20){
               throw  new RuntimeException("error");
           }
       }
    }

    private static  void notifyAndRelease(){
        System.out.println("notify to  the main");
        try {
            Thread.sleep(1_000);
        } catch (Throwable e) {

        }
        System.out.println("will release resource(socket, file, connect)");

        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(" release and notify done.");
    }
}

```


10.5 小结
```
1、如上面的测试代码，如果这个代码里面 没有进行非法操作那么他们能进行正常的执行
一个线程抢到锁之后，其他的线程要等到这个线程释放锁之后 去争抢这个锁，抢到了才会执行
2、如果进行了非法的操作，比如线程A 获取这个锁，但是其他的线程B 来释放这个锁，
那么这个时候 A线程都还没有 释放锁， 而线程都可以获取到这个锁了，所以这种方式不对
 // 测试结果
//T1 have the lock monitor
//T1 is Working....
//main release the lock monitor.
//T4 have the lock monitor
//T4 is Working....

3、那个线程加的锁，那么就让那个线程去释放这个锁，所以进行非法操作时
在释放锁的时候 需要判断是不是 加锁的线程，如果是才进行释放锁
  

4、一个程序在退出的时候 尽量不要使用 kill -9  这种是强制性的杀掉进程
一般使用kill, 在linux中使用。应用程序在出问题的情况下或者说是被人为的干掉时候 去捕获到他的信号
并且有机会去通知管理员 去写入一些记录。
```

11、线程里面异常，因为在run方法中不能抛出异常，那么怎么将其捕获。
```java
package com.learn.concurrenty.chapter11;

/**
 * @ClassName: ThreadException
 * @Description: 线程里面异常，因为在run方法中不能抛出异常，那么怎么将其捕获。
 * @Author: lin
 * @Date: 2020/3/24 16:13
 * History:
 * @<version> 1.0
 */
public class ThreadException {
    private static  final  int A =10;
    private static  final  int B =0;
    public static void main(String[] args) {
        Thread t = new Thread(()->{
            try {
                Thread.sleep(1_000);
                //比如 这里会出现异常，线程死掉,run方法里面抛不出来这个异常
                int  result = A /B;
                System.out.println( result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        // 未捕获的异常处理
        t.setUncaughtExceptionHandler((thread, e)->{
            System.out.println(e);
            System.out.println(thread);
        });
        t.start();

        //运行结果
//        > Task :ThreadException.main()
//        java.lang.ArithmeticException: / by zero
//        Thread[Thread-0,5,main]

    }
}

```
12、 Thread能访问ThreadGroup的那些方法
```java
package com.learn.concurrenty.chapter12;

import java.util.Arrays;

/**
 * @ClassName: ThreadGroupCreate
 * @Description: Thread能访问ThreadGroup的那些方法
 * @Author: lin
 * @Date: 2020/3/24 16:55
 * History:
 * @<version> 1.0
 */
public class ThreadGroupCreate {

    public static void main(String[] args) {
        ThreadGroup threadGroup = new ThreadGroup("TGroup1");
        Thread t1 = new Thread(threadGroup, "t1"){
            @Override
            public void run() {
               while (true){
                   try {
//                       System.out.println(getThreadGroup().getName());
//                       System.out.println(getThreadGroup().getParent());
                       // sleep不会释放锁
                       Thread.sleep(10_000);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }
            }
        };
        t1.start();


        ThreadGroup threadGroup2 = new ThreadGroup("TGroup2");
        Thread  t2 = new Thread(threadGroup2, "T2"){
            @Override
            public void run() {
                System.out.println("========="+threadGroup.getName());
                Thread[] threads = new Thread[threadGroup.activeCount()];
                threadGroup.enumerate(threads);

                Arrays.asList(threads).forEach(System.out::println);
            }
        };
        t2.start();


        /**
         *  sleep是线程方法，wait是object方法；看区别，主要是看CPU的运行机制：
         *  它们的区别主要考虑两点：1.cpu是否继续执行、2.锁是否释放掉。
         *
         * 对于这两点，首先解释下cpu是否继续执行的含义：cpu为每个线程划分时间片去执行，
         * 每个时间片时间都很短，cpu不停地切换不同的线程，
         * 以看似他们好像同时执行的效果。
         *
         * 其次解释下锁是否释放的含义：锁如果被占用，
         * 那么这个执行代码片段是同步执行的，如果锁释放掉，
         * 就允许其它的线程继续执行此代码块了。
         *
         * sleep ，释放cpu资源，不释放锁资源，如果线程进入sleep的话，
         * 释放cpu资源，如果外层包有Synchronize，那么此锁并没有释放掉。
         *
         * wait，释放cpu资源，也释放锁资源，一般用于锁机制中 肯定是要释放掉锁的，
         * 因为notify并不会立即调起此线程，因此cpu是不会为其分配时间片的，
         * 也就是说wait 线程进入等待池，cpu不分时间片给它，锁释放掉。
         *
         * sleep：Thread类的方法，必须带一个时间参数。
         * 会让当前线程休眠进入阻塞状态并释放CPU（阿里面试题 Sleep释放CPU，
         * wait 也会释放cpu，因为cpu资源太宝贵了，只有在线程running的时候，
         * 才会获取cpu片段），提供其他线程运行的机会且不考虑优先级，
         * 但如果有同步锁则sleep不会释放锁即其他线程无法获得同步锁
         * 可通过调用interrupt()方法来唤醒休眠线程。
         */

    }
}

```
12.1  ThreadGroupApi 一些使用ThreadGroupApi
```java
package com.learn.concurrenty.chapter12;

import java.util.Arrays;

/**
 * @ClassName: ThreadGroupCreate
 * @Description: Thread能访问ThreadGroup的那些方法
 * @Author: lin
 * @Date: 2020/3/24 16:55
 * History:
 * @<version> 1.0
 */
public class ThreadGroupCreate {

    public static void main(String[] args) {
        ThreadGroup threadGroup = new ThreadGroup("TGroup1");
        Thread t1 = new Thread(threadGroup, "t1"){
            @Override
            public void run() {
               while (true){
                   try {
//                       System.out.println(getThreadGroup().getName());
//                       System.out.println(getThreadGroup().getParent());
                       // sleep不会释放锁
                       Thread.sleep(10_000);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }
            }
        };
        t1.start();


        ThreadGroup threadGroup2 = new ThreadGroup("TGroup2");
        Thread  t2 = new Thread(threadGroup2, "T2"){
            @Override
            public void run() {
                System.out.println("========="+threadGroup.getName());
                Thread[] threads = new Thread[threadGroup.activeCount()];
                threadGroup.enumerate(threads);

                Arrays.asList(threads).forEach(System.out::println);
            }
        };
        t2.start();


        /**
         *  sleep是线程方法，wait是object方法；看区别，主要是看CPU的运行机制：
         *  它们的区别主要考虑两点：1.cpu是否继续执行、2.锁是否释放掉。
         *
         * 对于这两点，首先解释下cpu是否继续执行的含义：cpu为每个线程划分时间片去执行，
         * 每个时间片时间都很短，cpu不停地切换不同的线程，
         * 以看似他们好像同时执行的效果。
         *
         * 其次解释下锁是否释放的含义：锁如果被占用，
         * 那么这个执行代码片段是同步执行的，如果锁释放掉，
         * 就允许其它的线程继续执行此代码块了。
         *
         * sleep ，释放cpu资源，不释放锁资源，如果线程进入sleep的话，
         * 释放cpu资源，如果外层包有Synchronize，那么此锁并没有释放掉。
         *
         * wait，释放cpu资源，也释放锁资源，一般用于锁机制中 肯定是要释放掉锁的，
         * 因为notify并不会立即调起此线程，因此cpu是不会为其分配时间片的，
         * 也就是说wait 线程进入等待池，cpu不分时间片给它，锁释放掉。
         *
         * sleep：Thread类的方法，必须带一个时间参数。
         * 会让当前线程休眠进入阻塞状态并释放CPU（阿里面试题 Sleep释放CPU，
         * wait 也会释放cpu，因为cpu资源太宝贵了，只有在线程running的时候，
         * 才会获取cpu片段），提供其他线程运行的机会且不考虑优先级，
         * 但如果有同步锁则sleep不会释放锁即其他线程无法获得同步锁
         * 可通过调用interrupt()方法来唤醒休眠线程。
         */

    }
}

```
13.简单的线程池
```java
package com.learn.concurrenty.chapter13;

import javafx.concurrent.Worker;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @ClassName: SimpleThreadPool
 * @Description: 简单版 线程池
 * @Author: lin
 * @Date: 2020/3/25 10:42
 * History:
 * @<version> 1.0
 */
public class SimpleThreadPool {
    private  final int size;

    /**
     * 默认大小, 这个线程池 有10个线程
     */
    private static  final  int DEFAULT_SIZE = 10;

    /**
     * 定义线程的时候，让其自增
     */
    private static volatile int seq = 0;

    /**
     * 线程前缀
     */
    private static final  String THREAD_PREFIX ="SIMPLE_THREAD_POOL-";

    /**
     * 线程前缀
     */
    private static final  ThreadGroup GROUP = new ThreadGroup("Pool_Group");
    /**
     * 队列
     */
    private final  static LinkedList<Runnable> TASK_QUEUE = new LinkedList<>();

    /**
     * 工作任务
     */
    private final static List<WorkerTask>  THREAD_QUEUE = new ArrayList<>();

    public  SimpleThreadPool(){
       this(DEFAULT_SIZE);
    }

    public  SimpleThreadPool(int size){
        this.size = size;
        init();
    }

    /**
     * 初始化 的时候去 帮你创建线程
     */
    private  void init(){
        for (int i = 0; i <size ; i++) {
            createWorkTask();
        }
    }

    public void submit(Runnable runnable){
        //这里就是往TASK_QUEUE 添加任务
        // 因为有对其操作，而这读取操作是 同步的，所以写操作也是同步的
        synchronized (TASK_QUEUE){
            TASK_QUEUE.addLast(runnable);
            // 添加任务到队列中后，去通知那些等待的那些线程
            TASK_QUEUE.notifyAll();
        }
    }

    /**
     * 在提交任务前要先去构建
     */
    private void createWorkTask(){
      WorkerTask workerTask = new WorkerTask(GROUP, THREAD_PREFIX+(seq++));
       workerTask.start();
      //启动完成后，将其放到一个list中去
        THREAD_QUEUE.add(workerTask);
    }

    private enum  TaskState{
        /**
         * 什么都没有做
         */
        FREE,
        /**
         * 运行
         */
        RUNNING,
        /**
         * 阻塞
         */
        BLOCKED,
        /**
         * 死亡
         */
        DEAD,
    }

    /**
     * 这个定义为private 就是不想暴露给别人
     */
    private static class WorkerTask extends  Thread{

        private volatile TaskState taskState = TaskState.FREE;

        private WorkerTask(ThreadGroup threadGroup, String name){
            super(threadGroup, name);
        }

        public TaskState getTaskState(){
            return  this.taskState;
        }

        /**
         *  执行完任务不能让核心线程数挂掉
         *  如果挂掉了就需要重新起创建线程然后再启动， 这样线程池就没有意义了
         *  ,所以去继承下父类的构造函数，WorkerThread(ThreadGroup threadGroup, String name)
         *
         */
        @Override
        public void run() {
            OUT:
            // 在执行run方法时，我们需要判断这个线程池中的线程的状态
            while (this.taskState!= TaskState.DEAD){
                Runnable runnable;

                //如果线程不等于 DEAD，那么要去队列中取任务 来执行
                // 看看这个队列中有没有提交的任务
                synchronized (TASK_QUEUE){
                    // 将这个TASK_QUEUE 来作为锁，
                    // 这个每一个去这队列中取时候要等待 其他的线程释放锁
                    while (TASK_QUEUE.isEmpty()){
                        // 如果这个队列取出来是空的，也就是没有人提交任务
                         // 这是就让其等
                        try {
                            //等待是将其状态更新
                            taskState =TaskState.BLOCKED;
                            TASK_QUEUE.wait();
                        } catch (InterruptedException e) {
//                            e.printStackTrace();
                            //别人在停止这个线程时，要退出，这个时候退出到哪里
                            // 呢？ 所以这里要加一个标志
                            break  OUT;

                        }

                    }
                    //当被唤醒 后，要从队列中拿出任务
                     runnable = TASK_QUEUE.removeFirst();
                }

                if(runnable!=null){
                    //如果队列不为null,那就运行run,执行的时候将状态更新
                    taskState =TaskState.RUNNING;
                    runnable.run();
                    // 运行完后 状态再次更新
                    taskState =TaskState.FREE;
                }

            }

        }

        /**
         * 关闭线程
         */
        public void close(){
            this.taskState = TaskState.DEAD;
        }
    }

    public static void main(String[] args) {
        SimpleThreadPool simpleThreadPool = new SimpleThreadPool();
        //这里启动40个线程
        IntStream.rangeClosed(0, 40).forEach(i ->{
            simpleThreadPool.submit(()->{
                System.out.println("The runnable" + i + "be serviced by "+ Thread.currentThread()+" start");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("The runnable" + i + "be serviced by "+ Thread.currentThread()+".finished");
            });
        });
    }
}

```
13.1 线程拒绝策略和关闭线程池
```java
package com.learn.concurrenty.chapter13;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.IntStream;

/**简单版 线程池 这个里面增加拒绝策略
 * @ClassName: SimpleThreadPool2
 * @Description:
 * @Author: lin
 * @Date: 2020/3/25 10:42
 * History:
 * @<version> 1.0
 */
public class SimpleThreadPool2 {
    private  final int size;


    private  final int queueSize ;
    /**
     * 默认大小, 这个线程池 有10个线程
     */
    private static  final  int DEFAULT_SIZE = 10;

    /**
     * 工作队列中最大数，超过这个数 就进行拒绝策略
     */
    private static final  int DEFAULT_TASK_QUEUE_SIZE = 2000;


    /**
     * 定义线程的时候，让其自增
     */
    private static volatile int seq = 0;

    /**
     * 线程前缀
     */
    private static final  String THREAD_PREFIX ="SIMPLE_THREAD_POOL-";

    /**
     * 线程前缀
     */
    private static final  ThreadGroup GROUP = new ThreadGroup("Pool_Group");
    /**
     * 队列
     */
    private final  static LinkedList<Runnable> TASK_QUEUE = new LinkedList<>();

    /**
     * 工作线程
     */
    private final static List<WorkerTask>  THREAD_QUEUE = new ArrayList<>();

    private final DiscardPolicy discardPolicy;

    public final  static DiscardPolicy  DEFAULT_DISCARD_POLICY = ()->{
        throw  new DiscardException("Discard This Task. ");
    };

    private volatile boolean destroy = false;

    public SimpleThreadPool2(){
       this(DEFAULT_SIZE, DEFAULT_TASK_QUEUE_SIZE, DEFAULT_DISCARD_POLICY);
    }

    public SimpleThreadPool2(int size, int queueSize, DiscardPolicy discardPolicy){
        this.size = size;
        this.queueSize = queueSize;
        this.discardPolicy = discardPolicy;
        init();
    }

    /**
     * 初始化 的时候去 帮你创建线程
     */
    private  void init(){
        for (int i = 0; i <size ; i++) {
            createWorkTask();
        }
    }

    public void submit(Runnable runnable){
        //在提交任务的时候判断是否是destroy
        if(destroy){
            throw  new IllegalStateException("The thread pool already destroy and not allow submit task");
        }
        //这里就是往TASK_QUEUE 添加任务
        // 因为有对其操作，而这读取操作是 同步的，所以写操作也是同步的
        synchronized (TASK_QUEUE){
            //执行拒绝策略
            if(TASK_QUEUE.size() > queueSize){
                discardPolicy.rejected();
            }
            TASK_QUEUE.addLast(runnable);
            // 添加任务到队列中后，去通知那些等待的那些线程
            TASK_QUEUE.notifyAll();
        }
    }

    /**
     * 在提交任务前要先去构建
     */
    private void createWorkTask(){
      WorkerTask workerTask = new WorkerTask(GROUP, THREAD_PREFIX+(seq++));
       workerTask.start();
      //启动完成后，将其放到一个list中去
        THREAD_QUEUE.add(workerTask);
    }

    public void shutDown() throws InterruptedException {
        //现在停止线程 就是让 工作线程将工作做完,
        // 如果发现没有任何工作需要做，那么就停止
        while (!TASK_QUEUE.isEmpty()){
            // 如果这个队列 不是空的，那么稍微休息下
            Thread.sleep(50);
        }

         // 这里要去看看 这个 工作队列中还有多少个线程
        int initVal = THREAD_QUEUE.size();
        while (initVal > 0){
            for (WorkerTask task: THREAD_QUEUE) {
                if(task.getTaskState() == TaskState.BLOCKED){
                    //这里取打断一下 ，那么在WorkerTask中run方法会收到一个中断信号
                    // 当收到信号后 会从OUT这个标志中 退出，也就是它的生命周期就结束了
                    // 就是工作线程的生命周期就结束了
                    task.interrupt();
                    // 调用方法将其状态变更
                    task.close();
                    initVal --;
                }else{
                    Thread.sleep(10);
                }
            }
            //  在干掉线程时候，变更destroy
            this.destroy =true;
            System.out.println("The thread pool disposed.");
        }
    }

    public int getSize() {
        return size;
    }

    /**
     * 将这个开放出去，让外面可以知道 大小
     * @return
     */
    public int getQueueSize() {
        return queueSize;
    }

    public boolean isDestroy(){
        //查看状态，如果是某种状态那么就不在
        return this.destroy ;
    };

    private enum  TaskState{
        /**
         * 什么都没有做
         */
        FREE,
        /**
         * 运行
         */
        RUNNING,
        /**
         * 阻塞
         */
        BLOCKED,
        /**
         * 死亡
         */
        DEAD,
    }

    /**
     * 定义拒绝策略异常
     */
    public static class DiscardException extends RuntimeException{
        public DiscardException(String message){
            super(message);
        }
    }

    public interface DiscardPolicy{
        /**
         * 拒绝策略
         * @throws DiscardException
         */
        void  rejected() throws DiscardException;
    }



    /**
     * 这个定义为private 就是不想暴露给别人
     */
    private static class WorkerTask extends  Thread{

        private volatile TaskState taskState = TaskState.FREE;

        private WorkerTask(ThreadGroup threadGroup, String name){
            super(threadGroup, name);
        }

        public TaskState getTaskState(){
            return  this.taskState;
        }

        /**
         *  执行完任务不能让核心线程数挂掉
         *  如果挂掉了就需要重新起创建线程然后再启动， 这样线程池就没有意义了
         *  ,所以去继承下父类的构造函数，WorkerThread(ThreadGroup threadGroup, String name)
         *
         */
        @Override
        public void run() {
            OUT:
            // 在执行run方法时，我们需要判断这个线程池中的线程的状态
            while (this.taskState!= TaskState.DEAD){
                Runnable runnable;

                //如果线程不等于 DEAD，那么要去队列中取任务 来执行
                // 看看这个队列中有没有提交的任务
                synchronized (TASK_QUEUE){
                    // 将这个TASK_QUEUE 来作为锁，
                    // 这个每一个去这队列中取时候要等待 其他的线程释放锁
                    while (TASK_QUEUE.isEmpty()){
                        // 如果这个队列取出来是空的，也就是没有人提交任务
                         // 这是就让其等
                        try {
                            //等待是将其状态更新
                            taskState = TaskState.BLOCKED;
                            TASK_QUEUE.wait();
                        } catch (InterruptedException e) {
//                            e.printStackTrace();
                            //别人在停止这个线程时，要退出，这个时候退出到哪里
                            // 呢？ 所以这里要加一个标志
                            break  OUT;

                        }

                    }
                    //当被唤醒 后，要从队列中拿出任务
                     runnable = TASK_QUEUE.removeFirst();
                }

                if(runnable!=null){
                    //如果队列不为null,那就运行run,执行的时候将状态更新
                    taskState = TaskState.RUNNING;
                    runnable.run();
                    // 运行完后 状态再次更新
                    taskState = TaskState.FREE;
                }
            }
        }

        /**
         * 关闭线程
         */
        public void close(){
            this.taskState = TaskState.DEAD;
        }
    }

    public static void main(String[] args) throws InterruptedException {

        //这个设置 去测试拒绝策略
//        SimpleThreadPool2 threadPool2 = new SimpleThreadPool2(6,
//                10 , SimpleThreadPool2.DEFAULT_DISCARD_POLICY);
        //这个设置测试 关闭线程池
        SimpleThreadPool2 threadPool2 = new SimpleThreadPool2();
        //这里启动40个线程
        IntStream.rangeClosed(0, 40).forEach(i ->{
            threadPool2.submit(()->{
                System.out.println("The runnable be serviced by "+ Thread.currentThread()+" start");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("The runnable be serviced by "+ Thread.currentThread()+".finished");
            });
        });
        //等待任务提交，等待10s
        Thread.sleep(10000);
        //这个去关闭线程池
        threadPool2.shutDown();
    }
}

```
14、design 单例模式
```java
package com.learn.concurrent.design.design.design.chapter1;

/**
 *使用静态内部类的方式
 * @ClassName: SingletonObject
 * @Description:
 * @Author: lin
 * @Date: 2020/3/25 17:18
 * History:
 * @<version> 1.0
 */
public class SingletonObject {
    /**
     * 私有化构造器，不给外部使用
     */
    private SingletonObject(){}

    /**
     * 静态内部类
     */
    private static class InstanceHolder{
        //这对像是类对象，所以只在jvm中只有一个
        private static final SingletonObject instance = new SingletonObject();
    }

    /**
     * 在调用整方法的时候，去初始化一个对象
     * @return
     */
    public static SingletonObject getInstance(){
        return InstanceHolder.instance;
    }
}

```
14.1 使用枚举方式实现单例
```java
package com.learn.concurrent.design.design.design.chapter1;

import java.util.stream.IntStream;

/**
 *使用枚举的方式
 * @ClassName: SingletonObject
 * @Description:
 * @Author: lin
 * @Date: 2020/3/25 17:18
 * History:
 * @<version> 1.0
 */
public class SingletonObject2 {
    private SingletonObject2(){}

    public enum Singleton{

        /**
         *
         */
        INSTANCE;

        private final  SingletonObject2 instance;

        /**
         * 私有的构造器，在定义枚举的时候，这个构造函数已经创建了
         * 只会创建一个对象实例
         */
        Singleton(){
            instance = new SingletonObject2();
        }

        /**
         * 获取，然后初始化
         * @return
         */
        public SingletonObject2 getInstance(){
            return instance;
        }
    }

    /**
     * 通过枚举的方式，去初始化对象
     * @return
     */
    public static SingletonObject2 getInstance(){
        return Singleton.INSTANCE.getInstance();
    }

    public static void main(String[] args) {
        IntStream.rangeClosed(0,40).forEach(i -> new Thread(String.valueOf(i)){
            @Override
            public void run() {
                System.out.println(SingletonObject2.getInstance());
            }
        }.start());
    }
}

```
15、多线程的休息室wait set 
```java
package com.learn.concurrent.design.design.design.chapter2;

import java.util.Optional;
import java.util.stream.IntStream;

/**
 * 多线程的休息室WaitSet
 * @ClassName: WaitSet
 * @Description:
 * @Author: lin
 * @Date: 2020/3/25 22:40
 * History:
 * @<version> 1.0
 */
public class WaitSet {

    private static  final Object LOCK = new Object();


    /**
     *  wait set 在jvm中明确的规定
     * 1.所有的对象都会有一个wait set,用来存放调用了该对象wait方法之后进入block状态线程
     * 2.线程被notify之后，不一定立即得到执行
     * 3.线程从 wait set中被唤醒顺序不一定是 FIFO.
     * @param args
     */
    public static void main(String[] args) {
        IntStream.rangeClosed(1, 10).forEach(i ->
                new Thread(String.valueOf(i)){
                    @Override
                    public void run() {
                        synchronized (LOCK){

                            try {
                                Optional.of(Thread.currentThread().getName() + " will come to wait set.").ifPresent(System.out::println);
                                LOCK.wait();
                                Optional.of(Thread.currentThread().getName() + " will leave to wait set.").ifPresent(System.out::println);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }.start()
        );


        IntStream.rangeClosed(1, 10).forEach(i ->{
            synchronized (LOCK){
                LOCK.notify();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

```
15.1 测试在在wait 后唤醒，会倒回去执行吗？
```java
package com.learn.concurrent.design.design.design.chapter2;

/**
 * 多线程的休息室WaitSet
 * @ClassName: WaitSet
 * @Description:
 * @Author: lin
 * @Date: 2020/3/25 22:40
 * History:
 * @<version> 1.0
 */
public class WaitSet2 {

    private static  final Object LOCK = new Object();

    /**
     * 这里有个疑问就，在将这个线程 唤醒后，
     * 下面这个方法中 会不会去执行第一个打印语句？
     * 因为在这个synchronized同步 块中，当线程wait后被唤醒。这里的执行时怎么样的？
     *
     * 从执行的结果来看，这个会打印，但是这个 线程被唤醒之后 程序不会倒着执行，
     * 因为这个wait时候会记录一个输出地址，当被唤醒的时候 从这个记录的地址进行恢复
     *
     *
     */
    private  static  void  work(){
        synchronized (LOCK){
            System.out.println("begin...................");
            try {
                System.out.println("Thread will coming");
                // wait完之后 唤醒 ，必须要去抢锁，但是这个代码执行时有记录的
                // wait 输出的地址，还有本身执行的地址 会被记录下来，下次执行的时候 进行地址恢复
                // 然后继续往下走
                LOCK.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Thread will out");
        }

        //打印结果
//        begin...................
//        Thread will coming
//        Thread will out

    }

    /**
     *  wait set 在jvm中明确的规定
     * 1.所有的对象都会有一个wait set,用来存放调用了该对象wait方法之后进入block状态线程
     * 2.线程被notify之后，不一定立即得到执行
     * 3.线程从 wait set中被唤醒顺序不一定是 FIFO.
     * 4.线程被唤醒后，必须重新获取锁
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
        new Thread(()->{
            work();
        }).start();

        Thread.sleep(1000);
        synchronized (LOCK){
            LOCK.notify();
        }
  }
}

```
16、volatile 的使用, volatile 关键字的可见性
```java
package com.learn.concurrent.design.design.design.chapter3;

/**volatile 的使用, volatile 关键字的可见性
 * 如果不是volatile关键字来修饰这变量，那么其他线程不能感知这个共享变量的变更
 * @ClassName: VolatileTest
 * @Description: volatile 的使用
 * @Author: lin
 * @Date: 2020/3/26 9:01
 * History:
 * @<version> 1.0
 */
public class VolatileTest {
    private volatile static int INIT_VALUE = 0;

    private static  final int MAX_LIMIT = 5;

    public static void main(String[] args) {
        new Thread(()->{
            int localValue = INIT_VALUE;
            while (localValue < MAX_LIMIT){
                if(localValue != INIT_VALUE) {
                    System.out.printf("The value updated to [%d]\n", INIT_VALUE);
                    localValue = INIT_VALUE;
                }
            }
        },"READER").start();


        new Thread(() ->{
            int localValue = INIT_VALUE;
            while (INIT_VALUE < MAX_LIMIT){
                System.out.printf(" updated the value to [%d]\n", ++localValue);
                INIT_VALUE = localValue;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "UPDATER").start();
    }
}

```
16.1
```java
package com.learn.concurrent.design.design.design.chapter3;

/**
 * VolatileTest2
 *
 * volatile 的使用, volatile 关键字的可见性
 * 如果不是volatile关键字来修饰这变量，那么其他线程不能感知这个共享变量的变更
 * @ClassName: VolatileTest
 * @Description: volatile 的使用
 * @Author: lin
 * @Date: 2020/3/26 9:01
 * History:
 * @<version> 1.0
 */
public class VolatileTest2 {
    /**
     * INIT_VALUE 变量都不加上 volatile 修饰都会造成缓存 不一致情况
     *
     * 当加上 volatile 关键字后 使其睡眠时间更短 一下那么就会出现 相同的数字
     * 这样 就没有保障原子性
     */
    private  static int INIT_VALUE = 0;

    private static  final int MAX_LIMIT = 5;

    /**
     * 比如当  INIT_VALUE =10 时
     * 下面代码 的 //  ++INIT_VALUE
     * 分为在jvm分为几个步骤
     * 1.从主内存中个读取INIT_VALUE ->10
     * 2. 将这个值加1 INIT_VALUE = 10 + 1（注意这里已经从主内存中读取出来了，所以不是INIT_VALUE = INIT_VALUE + 1 这样了）
     * 3. 然后将值赋值给INIT_VALUE =11
     * @param args
     */
    public static void main(String[] args) {
        new Thread(()->{
            while (INIT_VALUE < MAX_LIMIT){
                //而这里 ，这两个线程都会有对 INIT_VALUE 写的操作
                // 所以会去更新主内存
                //  ++INIT_VALUE
                System.out.println("T1->" +(++INIT_VALUE));
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"ADDER-1").start();


        new Thread(() ->{
            while (INIT_VALUE < MAX_LIMIT){
                try {
                    System.out.println("T2->" +(++INIT_VALUE));
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "ADDER-2").start();
    }
}

```

16.2 cpu架构和  高速缓存一致性协议
```
解决缓存不一致的方法
1、给数据总线加锁 
        总线(数据总线，地址总线，控制总线)
2、CPU高速缓存一致性协议
      Inter提出 MESI
核心思想
1、当cpu写入数据的时候，如果发现该变量被共享(也就是锁，在其他cpu中存在该变量的副本)
会发出一个信号，通知其他cup变量的缓存无效
2、当其他cpu访问该变量时候，重新到主内存进行获取
```
16.3  原子性、可见性、有序性
```
1、原子性：对基本数据类型的变量读取和赋值是保证原子性的，要么成功，要么失败，这个操作不可中断
2、可见性：对共享变量的更新 对其它线程可见
3、有序性： 重排序只要求最终一致性
    happens-before relationship
3.1 代码的执行顺序，编写在前面的发生在编写后面的（只是在单线程情况下）
3.2 unlock必须发生在lock之后
3.3 volatile 修饰的变量，对一个变量的写操作先于对该变量的读操作
3.4 传递规则 ，操作A先于B，B先于C,那么A肯定先于C
3.5 线程启动规则，start 方法 先于线程run
3.6 线程中断规则，interrupt 这个动作，必须发生在捕获动作之前（也就是线程中断才能捕获，不能说是捕获了才去中断）
3.7 对象销毁规则，一个对象的初始化 必须发生在finalize 之前
3.8 线程终结规则，所以的操作必须发生在线程死亡之前

```
17、观察者模式
```java
package com.learn.concurrent.design.design.design.chapter4;

import java.util.ArrayList;
import java.util.List;

/**
 * 在主题发生变化后 去通知所有的观察者
 * @ClassName: Subject
 * @Description: 观察者模式-主题
 * @Author: lin
 * @Date: 2020/3/26 15:09
 * History:
 * @<version> 1.0
 */
public class Subject {

    private List<AbstractObserver> observers = new ArrayList<>();

    private int state;


    /**
     * 状态变更
     * @param state
     */
    public void setState(int state){
        if(state == this.state){
            return;
        }
        this.state = state;
        //然后去通知 server
        notifyAllObserver();
    }

    public int getState(){
       return this.state ;
    }

    /**
     * 连接到每一个server
     */
    public void attach(AbstractObserver abstractObserver){
        observers.add(abstractObserver);
    }

    /**
     * 去通知所有的observer
     */
    private void notifyAllObserver(){
        //去更新
        observers.stream().forEach(AbstractObserver::update);
    }
}

```
17.1、观察者
```java
package com.learn.concurrent.design.design.design.chapter4;

/**
 * 抽象类 ，这个观察者 里面 定义一些方法
 * 但是这些方法不去实现，让其子类实现
 * @ClassName: AbstractObserver
 * @Description:
 * @Author: lin
 * @Date: 2020/3/26 15:12
 * @History:
 * @<version> 1.0
 */
public abstract class AbstractObserver {


    /**
     * 组合, 这个让其子类可以访问
     */
    protected Subject subject;

    /**
     * 更新
     */
    public abstract void update();

    public AbstractObserver(Subject subject){
        this.subject = subject;
        //将observer加入到这个 集合中去，那么就不用到子类中每个都写一边
        this.subject.attach(this);
    }
}

```
17.2、测试
```java
package com.learn.concurrent.design.design.design.chapter4;

/**
 * @ClassName: ObserverClient
 * @Description:
 * @Author: lin
 * @Date: 2020/3/26 15:36
 * History:
 * @<version> 1.0
 */
public class ObserverClient {
    public static void main(String[] args) {
        final  Subject  subject = new Subject();
        AbstractObserver observer = new BinaryObserver(subject);
        AbstractObserver observer1 = new OctalObserver(subject);
        System.out.println("======================");
        subject.setState(10);
        System.out.println("======================");
        subject.setState(10);
        System.out.println("======================");
        subject.setState(12);
    }
}

```
17.3 将其观察者继承 抽象类
```java
package com.learn.concurrent.design.design.design.chapter4;

/**
 * @ClassName: BinaryObserver
 * @Description: 二进制 server
 * @Author: lin
 * @Date: 2020/3/26 15:24
 * History:
 * @<version> 1.0
 */
public class BinaryObserver extends AbstractObserver {

    /**
     * 构造方法，这里需要 主题
     * @param subject
     */
    public  BinaryObserver(Subject subject){
        super(subject);
    }
    //如果在父类不加入 到attach 那么就需要用下面的方式
//    public  BinaryObserver(Subject subject){
//        this.subject = subject;
//    }

    @Override
    public void update() {
        System.out.println("Binary String:" + Integer.toBinaryString(subject.getState()));
    }
}

```
17.4 不同的观察者
```java
package com.learn.concurrent.design.design.design.chapter4;

/**
 * @ClassName: OctalObserver
 * @Description: 八进制 server
 * @Author: lin
 * @Date: 2020/3/26 15:24
 * History:
 * @<version> 1.0
 */
public class OctalObserver extends AbstractObserver {

    /**
     * 构造方法，这里需要 主题
     * @param subject
     */
    public OctalObserver(Subject subject){
        super(subject);
    }


    @Override
    public void update() {
        System.out.println("Octal String:" + Integer.toOctalString(subject.getState()));
    }
}

```
17.5 监听
```java
package com.learn.concurrent.design.design.design.chapter4;

/**
 * @ClassName: LifeCyleListener
 * @Description:
 * @Author: lin
 * @Date: 2020/3/26 16:05
 * History:
 * @<version> 1.0
 */
public interface LifeCycleListener {

    void onEvent(AbstractObserverRunnable.RunnableEvent event);
}

```
17.6
```java
package com.learn.concurrent.design.design.design.chapter4;

import java.util.List;

/**
 * @ClassName: ThreadLifeCycleObserver
 * @Description:
 * @Author: lin
 * @Date: 2020/3/26 16:17
 * History:
 * @<version> 1.0
 */
public class ThreadLifeCycleObserver implements  LifeCycleListener {

    private final Object LOCK = new Object();

    /**
     * 批量查询
     * @param ids
     */
    public void concurrentQuery(List<String> ids){
        if(ids == null || ids.isEmpty()){
            return;
        }
        ids.stream().forEach(id -> new Thread(new AbstractObserverRunnable(this) {
            @Override
            public void run() {
                try {
                    notifyChange(new RunnableEvent(RunnableState.RUNNING,
                            Thread.currentThread(), null));
                    System.out.println("query for the id " + id);
                    Thread.sleep(1000L);
                    notifyChange(new RunnableEvent(RunnableState.DONE,
                            Thread.currentThread(), null));
                } catch (InterruptedException e) {
                    notifyChange(new RunnableEvent(RunnableState.ERROR,
                            Thread.currentThread(), null));
                }
            }
            //id相当于线程的名字了
         },id).start());
    }

    /**
     * 事件回调
     * @param event
     */
    @Override
    public void onEvent(AbstractObserverRunnable.RunnableEvent event) {
         synchronized (LOCK){
             System.out.println("The runnable [" + event.getThread().getName() +"] data changed and state is" );
             if(event.getCause() !=null){
                 System.out.println("The runnable ["+ event.getThread().getName() +"] process failed");
                 event.getCause().printStackTrace();
             }
         }
    }
}

```
17.7测试
```java
package com.learn.concurrent.design.design.design.chapter4;

import java.util.Arrays;

/**
 * @ClassName: ThreadLifeCycleClient
 * @Description:
 * @Author: lin
 * @Date: 2020/3/26 16:52
 * History:
 * @<version> 1.0
 */
public class ThreadLifeCycleClient {
    public static void main(String[] args) {
        new ThreadLifeCycleObserver().concurrentQuery(Arrays.asList("1","2"));
    }
}

```
18.单线程执行设计模式
```java
package com.learn.concurrent.design.design.design.chapter5;

/**
 * 单线程执行设计模式
 * @ClassName: Gate
 * @Description: 共享资源
 * @Author: lin
 * @Date: 2020/3/26 17:01
 * History:
 * @<version> 1.0
 */
public class Gate {
  private int counter =0;
  private String name = "hhh";
  private String address = "NowHere";

    /**
     * 一次只能一个人通过这个门
     * 这里加 synchronized 是让这个方法一次只有一个人通过
     * @param name
     * @param address
     */
  public synchronized void pass(String name, String address){
      this.counter++;
      this.name=name;
      this.address = address;

  }

  private void verify(){
      if(this.name.charAt(0)!=this.address.charAt(0)){
          System.out.println("**************BROKEN***************" + toString());
      }
  }

    @Override
    public String toString() {
        return "No." +counter+":" +name +"," + address;
    }
}

```
18.1 相当于线程的user
```java
package com.learn.concurrent.design.design.design.chapter5;

/**
 * @ClassName: User
 * @Description: user就相当一个线程
 * @Author: lin
 * @Date: 2020/3/26 17:07
 * History:
 * @<version> 1.0
 */
public class User extends  Thread{

    private final String myName;

    private final String myAddress;

    private final Gate gate;

    public User(String myName, String myAddress, Gate gate){
        this.myName = myName;
        this.myAddress = myAddress;
        this.gate = gate;
    }

    @Override
    public void run() {
        System.out.println(myName + "  BEGIN");
        // 线程启动后一直去 通过这门
        while (true){
            this.gate.pass(myName, myAddress);
        }
    }
}

```
18.2 测试
```java
package com.learn.concurrent.design.design.design.chapter5;

/**
 * @ClassName: Client
 * @Description:
 * @Author: lin
 * @Date: 2020/3/26 17:12
 * History:
 * @<version> 1.0
 */
public class Client {
    public static void main(String[] args) {
        Gate gate = new Gate();
        User bj = new User("bao", "beijing", gate);
        User sh = new User("shang", "shanghai", gate);
        User gz = new User("Guang", "GuangZhou", gate);

        bj.start();
        sh.start();
        gz.start();
    }
}

```
19. 读写锁 
```java
package com.learn.concurrent.design.design.design.chapter6;

/**
 * @ClassName: ReadWriteLock
 * @Description: 读写锁分离设计
 * @Author: lin
 * @Date: 2020/3/27 9:53
 * History:
 * @<version> 1.0
 */
public class ReadWriteLock {
    /**
     * 读线程多少个
     */
    private int readingReaders = 0;
    /**
     * 等待读 的线程 多少个
     */
    private int waitingReaders = 0;
    /**
     * 写线程 多少个
     */
    private int writingWriters = 0;
    /**
     * 等待写 的线程 多少个
     */
    private int waitingWriters = 0;

    /**
     * 这里 设置更偏向于 Writer
     */
    private boolean preferWriter = true;

    public ReadWriteLock(){
        this(true);
    }

    public ReadWriteLock(boolean preferWriter){
        this.preferWriter = preferWriter;
    }

    /**
     * 读 锁
     */
    public synchronized  void readLock()throws  InterruptedException{
        // 等待读的线程++
        this.waitingReaders++;
        try {
            // 判断，如果写线程 大于0， 那么就等待
            // 在读取的时候 如果更偏向于 writer那么 就是
            // 添加判断,如个 读的时候 preferWriter =true 并且 等待的 大于0
            // 那么就进行等待 ，这样在使用的时候就会更公平些，writer些的时候就会和read 一样
            while (writingWriters > 0 || (preferWriter && waitingWriters > 0)) {
                this.wait();
            }
            // 如果没有，那么读线程就 ++
            this.readingReaders ++;
        } finally {
            // 等待读的线程 减减
            this.waitingReaders --;
        }
    }

    /**
     * 释放 读 锁
     */
    public synchronized  void readUnLock(){
        //释放锁
        this.readingReaders--;
        this.notifyAll();
    }

    /**
     *  写 锁
     */
    public synchronized void writeLock() throws InterruptedException {
        // 写等待线程 ++
       this.waitingWriters ++;
       try {
           // 当有读的 或者 写的 大于 零，那么就不能写， 让其等待
           while (readingReaders > 0 || writingWriters > 0){
               this.wait();
           }
           this.writingWriters++;
       }finally {
           // 写等待线程 --
           this.waitingWriters--;
       }
    }

    public synchronized void writeUnLock(){
        this.writingWriters --;
        this.notifyAll();
    }
}


```
19.1 读工作者
```java
package com.learn.concurrent.design.design.design.chapter6;

/**
 * @ClassName: ReadWorker
 * @Description:
 * @Author: lin
 * @Date: 2020/3/27 10:35
 * History:
 * @<version> 1.0
 */
public class ReadWorker extends Thread {
    private final  ShareData data;

    public  ReadWorker(ShareData data){
        this.data = data;
    }

    @Override
    public void run() {
        try {
            while (true) {
                char[] readBuffer = data.read();
                System.out.println(Thread.currentThread().getName()
                        + " reads " + String.valueOf(readBuffer));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

```
19.2 写工作者
```java
package com.learn.concurrent.design.design.design.chapter6;

import java.util.Random;

/**
 * 写 工作者
 * @ClassName: WriteWorker
 * @Description:
 * @Author: lin
 * @Date: 2020/3/27 10:34
 * History:
 * @<version> 1.0
 */
public class WriteWorker  extends  Thread{
    private static final Random random = new Random(System.currentTimeMillis());

    private final  ShareData data;

    private final  String filter;

    private int index = 0;

    public WriteWorker(ShareData data, String filter){
        this.data = data;
        this.filter = filter;
    }

    @Override
    public void run() {
        try {
            // 这个捕获异常 不能在循环中，因为进行中断后 在循环中下一次还会进行
            //
            while (true){
               //从 filter拿数据，然后将其写入到 buffer中去
               char c = nextChar();
               //往 data里面写数据
               data.write(c);
               //写完一个让其简单休眠一下
               Thread.sleep(random.nextInt(1000));
            }
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private char nextChar(){
        char c = filter.charAt(index);
        index++;
        if(index >= filter.length()){
            index = 0;
        }
        return  c;
    }
}

```
19.3 共享数据
```java
package com.learn.concurrent.design.design.design.chapter6;

/**
 * @ClassName: ShareData
 * @Description: 读和写的共享数据
 * @Author: lin
 * @Date: 2020/3/27 10:11
 * History:
 * @<version> 1.0
 */
public class ShareData {
    /**
     * 从这个buffer中读数据，
     * 和往这个buffer中写数据
     */
    private final  char[] buffer;

    private final ReadWriteLock lock = new ReadWriteLock();

    public ShareData(int size){
        // 声明一个char 数组
         buffer = new char[size];
        for (int i = 0; i <size ; i++) {
            this.buffer[i]= '*';
        }
    }

    public char[] read() throws  InterruptedException{
        try {
            lock.readLock();
            return this.doRead();
        }finally {
            lock.readUnLock();
        }
    }


    public void write(char c) throws InterruptedException {
        try {
            lock.writeLock();
            this.doWrite(c);
        }finally {
            lock.writeUnLock();
        }
    }

    /**
     * 往buffer中写
     * @param c
     */
    private void  doWrite(char c) {
        for (int i = 0; i <buffer.length ; i++) {
            buffer[i] = c;
            slowly(10);
        }
    }

    /**
     * 创建一个副本
     * @return
     */
    private char[] doRead(){
        // 因为是引用类型，所以重新定义一个，然后将值赋值给它
        char[] newBuffer = new char[buffer.length];
        for (int i = 0; i <buffer.length ; i++) {
            newBuffer[i] =buffer[i];
        }
        // 赋值完成后 进行短暂休眠下
        slowly(50);
        return  newBuffer;
    }

    private void slowly(int millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

```
19.4 测试
```java
package com.learn.concurrent.design.design.design.chapter6;

/**
 * 测试 读 写 锁
 * 读和写是冲突的，如果读抢到了锁，那么写 就不能写入
 * ,如果要解决这种问题 那么就在 ReadWriteLock中去修改
 *
 * ReadWriteLock design pattern
 * Reader-Writer design pattern
 * @ClassName: ReadWritLockClient
 * @Description:
 * @Author: lin
 * @Date: 2020/3/27 10:51
 * History:
 * @<version> 1.0
 */
public class ReadWritLockClient {
    public static void main(String[] args) {
        final  ShareData data = new ShareData(10);
        new ReadWorker(data).start();
        new ReadWorker(data).start();
        new ReadWorker(data).start();
        new ReadWorker(data).start();
        new ReadWorker(data).start();

        new WriteWorker(data, "qwertyuiopasdfg").start();
        new WriteWorker(data, "QWERTYUIOPASDFG").start();
    }
}

```
20.不可变对象
```java
package com.learn.concurrent.design.design.design.chapter7;

/**
 * 1.不可变对象一定是线程安全的（这里暂时不说反射）
 *
 * 2.可变对象不一定是不安全的
 *
 * @ClassName: Person
 * @Description:
 * @Author: lin
 * @Date: 2020/3/27 12:28
 * History:
 * @<version> 1.0
 */
public class Person {
    private final  String name;
    private final  String address;

    public Person(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}

```
20.1 不可变
```java
package com.learn.concurrent.design.design.design.chapter7;

/**
 * @ClassName: UsePersonThread
 * @Description:
 * @Author: lin
 * @Date: 2020/3/27 13:21
 * History:
 * @<version> 1.0
 */
public class UsePersonThread extends  Thread{

    private Person person;

    public UsePersonThread(Person person){
        this.person = person;
    }

    @Override
    public void run() {
        for (int i = 0; i < 20 ; i++) {
            System.out.println(Thread.currentThread().getName() + " print "+
                    person.toString());
        }
    }
}

```
20.2 测试
```java
package com.learn.concurrent.design.design.design.chapter7;

import java.util.stream.IntStream;

/**
 * @ClassName: ImmutableClient
 * @Description:
 * @Author: lin
 * @Date: 2020/3/27 13:23
 * History:
 * @<version> 1.0
 */
public class ImmutableClient {
    public static void main(String[] args) {
        //share data
        Person person = new Person("lin","cd");

        //多个线程访问共享变量，看看有什么问题
        //
        IntStream.range(0, 5).forEach(i -> {
            new UsePersonThread(person).start();
        });

        //输入的结果，可以看到 这个不可变对象在多线程下，不会改变
        // 不可变对象 就是你不能改变他的任何状态
        //xxx
        //Thread-0 print Person{name='lin', address='cd'}
        //Thread-3 print Person{name='lin', address='cd'}
        //Thread-2 print Person{name='lin', address='cd'}
        //Thread-1 print Person{name='lin', address='cd'}
        //xxx

    }
}

```
21. 多线程异步调用
先测试 调用阻塞，必须等到其他事情执行完才继续往上执行，这种方式不会，需别人等到
```java
package com.learn.concurrent.design.design.design.chapter8;

/**
 * 多线程Future 设计模式
 * @ClassName: SyncInvoker
 * @Description:
 * @Author: lin
 * @Date: 2020/3/27 14:42
 * History:
 * @<version> 1.0
 */
public class SyncInvoker {
    public static void main(String[] args) throws InterruptedException {
        // 这个 方法的阻塞，导致了下面方法执行的阻塞
        // 那么有什么方法可以解决这种问题呢？
        // 有一种 就是在调用的时候 立即返回回来，返回回来的时候在想要结果的时候
        // 再去拿结果 ，就是异步的方式去拿取
        String result = get();
        System.out.println(result);
    }

    /**
     * 比较耗时的操作
     * @return
     * @throws InterruptedException
     */
    private static  String get() throws InterruptedException {
        Thread.sleep(100001);
        return  "FINISH";
    }
}
```
21.1 异步
```java
package com.learn.concurrent.design.design.design.chapter8;

/**
 * 异步的方法拿取结果, 返回任意类型的结果
 * @ClassName: Future
 * @Description:
 * @Author: lin
 * @Date: 2020/3/27 14:47
 * History:
 * @<version> 1.0
 */
public interface Future<T>{
    /**
     * 通过get 真正得到结果
     * @return
     * @throws InterruptedException
     */
    T get()throws  InterruptedException;
}

```
21.2 异步任务
```java
package com.learn.concurrent.design.design.design.chapter8;

/**
 *
 * @ClassName: FutureTask
 * @Description:
 * @Author: lin
 * @Date: 2020/3/27 14:49
 * @History:
 * @<version> 1.0
 */
public interface FutureTask<T> {
    /**
     * 去做事情
     * @return
     */
    T call();
}

```
21.2 中间层，起到桥接
```java
package com.learn.concurrent.design.design.design.chapter8;

import java.util.function.Consumer;

/**
 * 中间层，将Future和 FutureTask 连接起来
 * @ClassName: FutureService
 * @Description:
 * @Author: lin
 * @Date: 2020/3/27 14:52
 * History:
 * @<version> 1.0
 */
public class FutureService {

    public  <T> Future<T> submit(final  FutureTask<T> task){
        //异步调用
        AsyncFuture<T> asyncFuture = new AsyncFuture<>();
        //在调用get方法时候，怎么知道 我工作已经完成了呢？
        new Thread(() -> {
            // 启动线程 这个线程里面去调用call方法
                T result = task.call();
                //然后将结果异步通知
                asyncFuture.done(result);
        }).start();
        return  asyncFuture;
    }

    /**
     * 比如：当你的蛋糕做完之后, 你不想等,
     * 那么就给个电话或者地址, 让其送货上门
     * 这里的consumer就是 地址或者电话
     * @param task
     * @param consumer
     * @param <T>
     * @return
     */
    public  <T> Future<T> submit(final  FutureTask<T> task, Consumer<T> consumer){
        //异步调用
        AsyncFuture<T> asyncFuture = new AsyncFuture<>();
        //在调用get方法时候，怎么知道 我工作已经完成了呢？
        new Thread(() -> {
            // 启动线程 这个线程里面去调用call方法
            T result = task.call();
            //然后将结果异步通知
            asyncFuture.done(result);
            consumer.accept(result);
        }).start();
        return  asyncFuture;
    }

}


```
21.3异步 Future
```java
package com.learn.concurrent.design.design.design.chapter8;

/**
 * @ClassName: AsynFuture
 * @Description: 异步Future实现
 * @Author: lin
 * @Date: 2020/3/27 14:54
 * History:
 * @<version> 1.0
 */
public class AsyncFuture<T> implements  Future<T> {
    /**
     * 判断是否结束
     */
    private volatile  boolean done = false;

    private T result;

    public  void done(T result){
        synchronized (this){
            this.result = result;
            this.done = true;
            this.notifyAll();
        }
    }

    @Override
    public T get() throws InterruptedException {
        synchronized (this){
            while (!done){
                //如果还没有结果 ，那么只能让其wait
                this.wait();
            }
            //如果完成了 那么就返回结果
        }
        return result;
    }
}

```
21.4 测试
```java
package com.learn.concurrent.design.design.design.chapter8;

/**
 * Future          -----> 代表的是未来的一个凭据
 * FutureTask      -----> 将你的逻辑进行了隔离
 * FutureService   -----> 桥接 Future 和FutureTask
 *
 * 异步调用 多线程Future 设计模式
 * @ClassName: SyncInvoker
 * @Description:
 * @Author: lin
 * @Date: 2020/3/27 14:42
 * History:
 * @<version> 1.0
 */
public class AsyncInvoker {
    public static void main(String[] args) throws InterruptedException {
        FutureService futureService = new FutureService();
        Future<String> submit = futureService.submit(() -> {
            //模拟 时间比较长
            try {
                Thread.sleep(10001);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "FINISH";
        });
        //在调用后立即返回，然后最后去获取结果
        System.out.println("====================");
        System.out.println("do other thing.");
        //在做其他事情 也会花费时间
        Thread.sleep(1000);
        System.out.println("====================");

        //其他是做完了，这时想 其他的事情
        // 那么这个时候去获取 其他事情的结果
        System.out.println("获取结果"+ submit.get());

    }


}

```
22. 线程挂起，等忙完了再去做其他的事情
```java
package com.learn.concurrent.design.design.design.chapter9;

/**
 * @ClassName: Request
 * @Description:
 * @Author: lin
 * @Date: 2020/3/27 16:13
 * History:
 * @<version> 1.0
 */
public class Request {
    final private String value;
    public Request(String value){
        this.value = value;
    }

    public String getValue(){
        return  value;
    }
}

```  
22.1 请求队列
```java
package com.learn.concurrent.design.design.design.chapter9;

import java.util.LinkedList;

/**
 * @ClassName: RequestQueue
 * @Description: 请求队列
 * @Author: lin
 * @Date: 2020/3/27 16:13
 * History:
 * @<version> 1.0
 */
public class RequestQueue {
   private final LinkedList<Request> queue = new LinkedList<>();

   public  Request getRequest(){
       synchronized (queue){
           while (queue.size() <= 0){
               try {
                   queue.wait();
               } catch (InterruptedException e) {
//                  break;
                   return  null;
               }
           }
           return  queue.removeFirst();
       }

   }

    public  void putRequest(Request request){
        synchronized (queue){
            queue.addLast(request);
            queue.notifyAll();
        }
    }

}

```
22.2 客户端线程
```java
package com.learn.concurrent.design.design.design.chapter9;

import java.util.Random;

/**
 * @ClassName: ClientThread
 * @Description:
 * @Author: lin
 * @Date: 2020/3/27 16:19
 * History:
 * @<version> 1.0
 */
public class ClientThread extends Thread {
    private final RequestQueue queue;

    private final Random random;

    private final  String sendValue;

    public  ClientThread(RequestQueue queue, String sendValue){
        this.queue = queue;
        this.sendValue = sendValue;
        random = new Random(System.currentTimeMillis());
    }

    @Override
    public void run() {
        int t =10;
        for (int i = 0; i < t; i++) {
            System.out.println("Client -> request " + sendValue);
            queue.putRequest(new Request(sendValue));
            try {
                Thread.sleep(random.nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

```
22.3服务端
```java
package com.learn.concurrent.design.design.design.chapter9;

import java.util.Random;

/**
 * @ClassName: ServerThread
 * @Description:
 * @Author: lin
 * @Date: 2020/3/27 16:23
 * History:
 * @<version> 1.0
 */
public class ServerThread extends  Thread {
    private final RequestQueue queue;

    private final Random random;

    private volatile  boolean close = false;

    ServerThread(RequestQueue queue){
        this.queue = queue;
        random = new Random(System.currentTimeMillis());
    }

    @Override
    public void run() {
        while (!close){
            //1、当flag = false 时，有可能在getRequest 已经wait住了 ，
            // 是判断不到这个值的,
            Request request = queue.getRequest();
            if(null == request){
                ///2、sleep的过程中，进行了打断，那么catch中收到了中断信号
                // 这个时候就退出出去了
                System.out.println("Received the empty request");
                continue;
            }
            System.out.println("server -> " + request.getValue());
            try {
                Thread.sleep(random.nextInt(1000));
            } catch (InterruptedException e) {
                return;
            }
            // 3、本身在休眠的过程中  然后让其中断
        }
    }

    public  void close(){
        this.close = true;
        this.interrupt();
    }
}

```
22.4 测试
```java
package com.learn.concurrent.design.design.design.chapter9;

/**
 *  线程挂起
 * 比如：你在做饭，但是你的快递到了，这个时候没有办法去拿，那么只有叫快递员等待一会儿
 *
 * 手上的事情还没有做完，等忙完了再去做其他的事情
 * @ClassName: SuspensionClient
 * @Description:
 * @Author: lin
 * @Date: 2020/3/27 16:40
 * History:
 * @<version> 1.0
 */
public class SuspensionClient {
    public static void main(String[] args) throws InterruptedException {
        final  RequestQueue queue = new RequestQueue();
        new ClientThread(queue, "Alex").start();
        ServerThread serverThread = new ServerThread(queue);
        serverThread.start();
//        serverThread.join();

        //如果这个地方休眠时间太少，基本上看不到 request参数为空的情况
        Thread.sleep(10000);
        serverThread.close();
    }
}

```
23. ThreadLocal使用
```java
package com.learn.concurrent.design.design.design.chapter10;

import java.util.Random;

/**
 * ThreadLocal使用
 * @ClassName: ThreadLocalComplexTest
 * @Description:
 * @Author: lin
 * @Date: 2020/3/27 17:27
 * History:
 * @<version> 1.0
 */
public class ThreadLocalComplexTest {

    /**
     * threadLocal
     */
    private static  ThreadLocal<String> threadLocal = new ThreadLocal<>();

    /**
     * 随机因子
     */
    private static final Random random = new Random(System.currentTimeMillis());


    /**
     * JVM start main thread
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            threadLocal.set("Thread-T1");
            try {
                Thread.sleep(random.nextInt(100));
                System.out.println(Thread.currentThread().getName() + " " + threadLocal.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });


        Thread t2 = new Thread(() -> {
            threadLocal.set("Thread-T2");
            try {
                Thread.sleep(random.nextInt(100));
                System.out.println(Thread.currentThread().getName() + " " + threadLocal.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("===================");
        System.out.println(Thread.currentThread().getName() + " " + threadLocal.get());

    }
}
```
24.上下文
```java
package com.learn.concurrent.design.design.design.chapter11;

/**
 * @ClassName: Context
 * @Description:
 * @Author: lin
 * @Date: 2020/3/27 22:21
 * History:
 * @<version> 1.0
 */
public class Context {

    private  String name;

    private String cardId;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCardId() {
        return cardId;
    }
}

```
24.1 使用ThreadLocal实现一个上下文
```java
package com.learn.concurrent.design.design.design.chapter11;

/**
 * 使用ThreadLocal实现一个上下文,
 * 通过这个和线程绑定的方式，就不需要传入参数了
 * @ClassName: ActionContext
 * @Description:
 * @Author: lin
 * @Date: 2020/3/27 22:44
 * History:
 * @<version> 1.0
 */
public class ActionContext {

    private static  final  ThreadLocal<Context> threadLocal = new ThreadLocal<Context>(){
        @Override
        protected Context initialValue() {
            return new Context();
        }
    };


    private static class  ContextHolder{
        private final  static ActionContext actionContext = new ActionContext();

    }
    public  static  ActionContext getActionContext(){
        return  ContextHolder.actionContext;
    }

    public Context getContext(){
       return  threadLocal.get();
    }

}

```
24.2 查询db
```java
package com.learn.concurrent.design.design.design.chapter11;

/**
 * @ClassName: QueryFromDbAction
 * @Description:
 * @Author: lin
 * @Date: 2020/3/27 22:21
 * History:
 * @<version> 1.0
 */
public class QueryFromDbAction {

//    public  void  execute(Context context){
//        try {
//            Thread.sleep(1000L);
//            String name = "Alex " + Thread.currentThread().getName();
//            context.setName(name);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }

    public  void  execute(){
        try {
            Thread.sleep(1000L);
            String name = "Alex " + Thread.currentThread().getName();
            // 这个就直接去获取上下文，而不是传入进去
            ActionContext.getActionContext().getContext().setName(name);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

```
24.3 使用http
```java
package com.learn.concurrent.design.design.design.chapter11;

/**
 * @ClassName: QueryFromHttpAction
 * @Description:
 * @Author: lin
 * @Date: 2020/3/27 22:25
 * History:
 * @<version> 1.0
 */
public class QueryFromHttpAction {

//    public  void  execute(Context context){
//        String name =  context.getName();
//        String cardId = getCardId(name);
//        context.setCardId(cardId);
//    }


    public  void  execute(){
        // 这里我们没有set 东西进去，因为 在 ActionContext有 初始的东西
        Context context = ActionContext.getActionContext().getContext();
        String name =  context.getName();
        String cardId = getCardId(name);
        context.setCardId(cardId);
    }


    /**
     * 通过名字 拿取身份证
     * @param name
     * @return
     */
    private  String getCardId(String name){
        try {

            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return  "234354" + Thread.currentThread().getId();
    }
}

```
24.4 多线程上下文切换
```java
package com.learn.concurrent.design.design.design.chapter11;

/**
 * 多线程运行上下文切换
 * @ClassName: ExecutionTask
 * @Description:
 * @Author: lin
 * @Date: 2020/3/27 22:20
 * History:
 * @<version> 1.0
 */
public class ExecutionTask implements Runnable {
    private QueryFromDbAction action = new QueryFromDbAction();
    private QueryFromHttpAction httpAction = new QueryFromHttpAction();
    /**
     */
    @Override
    public void run() {
         Context context = ActionContext.getActionContext().getContext();
       action.execute();
       System.out.println("The name query successful");
       httpAction.execute();
       System.out.println("The card id query successful");
       System.out.println("The Name is " + context.getName() +" and CardId " + context.getCardId());
    }
}
```
24.5测试
```java
package com.learn.concurrent.design.design.design.chapter11;

import java.util.stream.IntStream;

/**
 * @ClassName: ContextTest
 * @Description:
 * @Author: lin
 * @Date: 2020/3/27 22:31
 * History:
 * @<version> 1.0
 */
public class ContextTest {
    public static void main(String[] args) {
        IntStream.range(1, 5).forEach(i ->{
           new Thread(new ExecutionTask()).start();
        });
    }
}

```

28、相关多线程和设计模式
```
1. singleton design pattern
2. WaitSet in synchronzied monitor
3. Cpu&Cpu cache& Main Memory & Data Bus & Cache Line
4. The volatile key word in deep
5. Java Class Loader
6. Observer to monitor The Thread lifecycle
7. Single Thread Execution design pattern
8. Immutable design pattern
9. Guarded Suspension design pattern
10.Balking design pattern
11.Producer-Consumer
12.Read-Write Lock design pattern
13.Thread-Per-Message Design Patten
14.Worker Thread Design Pattern
15.Future Design Pattern
16.Two-Phase Termination Design Pattern
17.The Thread-Specific Storage
18.Active Objects
19.Count Down Design Pattern
20.JMM-java Memory Model
```

30、ClassLoader类加载
1、类加载的三个阶段
```
1.加载:查找并且加载类的二进制数据
2.链接
   -验证:确保被加载类的正确性
   -准备:为类的静态变量分配内存,并将其初始化为默认值
   -解析:把类中的符号引用转换为直接引用
3.初始化:为类的静态变量赋予正确的初始值
```
2、主动使用
```
1. new 直接使用
2. 访问某个类或者接口的静态变量,或者对该静态变量进行赋值操作
   a.对某个类的静态变量进行读写  ---->初始化class
   b.对接口中静态变量进行读取  -------> interface
3. 调用静态方法
4. 反射某个类
5. 初始一个子类
6. 启动类,比如 java HelloWorld
除了上面这几种,其余的都是被动引用,不会导致类的初始化
```

30.1 下面是一个简单的测试
```java
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

```
30.2 测试
```java
package com.learn.concurrent.classloader.chapter1;

/**
 * 一个例子 来分析 类会不会被加载
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

```
30.3
```
1.方法区 存放了每一个类对象的结构、运行时常量、字段 、方法数据和构造函数代码
包括用于类和实例初始化以及接口初始化的特殊方法
而堆里面存放的是类对象的真实数据，这个真实的数据的结构就是存放在堆里
当我们在访问的时候 通过堆中有个对象类型数据的指针 然后去访问 方法区，
因为数据有了 但是要组织起来，这个数据的类型是存放在方法区中的 所以 通过 
对象数据类型指针来指定到 方法区 然后方法区将这个对象的类类型组织起来。

```
30.4 链接阶段
```
.在加载阶段完成后，虚拟机外部的二进制数据量就会按照虚拟机所需要的格式存储在
方法区中(数据结构)，然后在堆中创建一个class对象，这个对象作为程序访问方法区中
这些数据结构的外部接口
.加载阶段与连接阶段的部分内容可以是交叉进行的，比如一部分代码加载完成就可以进行验证
提供效率 

验证阶段: 验证住院的目的是确保class文件中的字节流中包含的信息符合虚拟机的要求，
并且不会损害到jvm自身的安全
.VerifyError
.文件格式验证
  .魔术因子是否正确，0xCAFEBABE
  .主从版本号是否符合当前虚拟机
  .常量池中常量类型是不是不支持
  .etc
. 元数据验证
  . 是否有父类
  . 父类是不是 允许继承
  . 是否实现了抽象方法
  . 是否覆盖了父类的final字段
  . 其他的语义检查
. 字节码验证
  . 主要进行数据和控制流的分析，不会出现这样的情况，在操作栈中放置一个int类型
    但是却给了一个long行的数据
. 符合应用验证
  . 调用了一个不存在方法，字段等等
    符号引用验证的目的是确保解析动作能正常执行，如果无法通过符号引用验证，将会抛出
  一个java.lang.IncompatibleClassChangeError异常的子类，如java.lang.IllegalAccessError,
   java.lang.NosuchFieldError 等

.准备阶段: 就是给类变量分配初始值(就是一些默认值)

.解析阶段: 
   . 类或者接口的解析
   . 字段解析
   . 类方法解析
   . 接口方法解析

```
30.5 初始化阶段 类加载的最后一步
```
1.初始化阶段是执行构造函数<clinit>()方法的过程
2.<clinit>()方法是有编译器自动收集类中的所有变量的赋值动作和静态语句
块中的语句合并产生的
3.静态语句块只能访问到定义在静态语句块之前的变量，定义在他之后的变量
只能赋值，不能访问
4.<clinit>()方法与类的构造函数有点区别，他不需要显示的调用父类的构造函数,
虚拟机会保证子类的<clinit>执行之前，先执行父类的<clinit>,因此在虚拟机
中首先被执行的是Object的<clinit>()方法
5.由于父类的<clinit>()方法要先执行，也就是意味着父类中定义的静态语句块，
要优先于子类
6.<clinit>()方法对于一个类来说并不是必须的
7.接口中照样存在<clinit>()方法
8.虚拟机有义务保证<clinit>()方法的线程安全
```
31.JVM类加载器
```
1、根(Bootstrap)类加载器:该加载器没有父类加载器。它负责加载虚拟机的核心类库，
如java.lang.*等。 java.lang.Object就是由根类加载器加载的。根类加载器从系统属性
sun.boot.class.path所指定的目录中加载类库。根类加载器的实现依赖于底层操作系统，
属于虚拟机的实现一部分，它并没有继承java.lang.ClassLoader类
2、扩展(Extension)类加载器:它的父类加载器为根类加载器。它从java.ext.dirs系统属性
所指定的目录中加载类库，或者从jdk的安装目录的jre\lib\ext子目录(扩展目录)下加载类库
如果把用户创建的JAR文件放在这个目录下，也会自动由扩展类加载器加载。扩展类加载器
是纯Java类，是java.lang.ClassLoader类的子类
3、系统(System)类加载器: 也称为应用加载器，它的父加载器为扩展类加载器。它从
环境变量classpath或者系统属性java.class.path所指定的目录中加载类，它是用户
自定义的类加载器的默认父加载器。系统类加载器是纯java类，
是java.lang.ClassLoader类的子类, 系统加载器的父加载器是扩展类加载
```
31.1 自定义一个类加载 ,去加载我们指定的类
```java
package com.learn.concurrent.classloader.chapter3;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 我们自己定的的ClassLoader, 我们打算从文件中去拿取
 * 也就是去磁盘定义一个目录
 * @ClassName: MyClassLoader
 * @Description:
 * @Author: lin
 * @Date: 2020/4/1 15:20
 * History:
 * @<version> 1.0
 */
public class MyClassLoader extends  ClassLoader{
    /**
     * 磁盘定义一个目录
     */
    private static  final String DEFAULT_DIR = "D:\\app\\classloader";

    /**
     * 定义一个dir, 让别人可以传入,如果不传入那么就是默认值
     */
    private String dir = DEFAULT_DIR;

    /**
     * classLoader的名字
     */
    private String classLoaderName;

    /**
     * 默认构造函数
     */
    MyClassLoader(){
        super();
    }

    /**
     * 这个设置 classLoader的名字
     * @param classLoaderName
     */
    MyClassLoader(String classLoaderName){
        super();
        this.classLoaderName = classLoaderName;
    }

    /**
     * 将去 父类加载器传入进去, 如果不传入那么就是 系统类加载默认值
     * @param classLoaderName
     * @param parent
     */
    public  MyClassLoader(String classLoaderName, ClassLoader parent){
        super(parent);
        this.classLoaderName = classLoaderName;
    }

    /**
     * 重写这个 方法，通过这个方法去查找父类加载器
     * 这个 name传入 的是 xxx.xxx.xxx.AA 等
     * 就是去读取文件, 然后将其转换为符合加载的格式
     * xxx/xxx/xxx/xxx/AAA.class
     * @param name
     * @return
     * @throws ClassNotFoundException
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String classPath = name.replace(".", "/");
        // 文件是 classPath 然后加上 .class 才是class文件
        // dir是父目录  D:\\app\\classloader + com/learn/concurrent/classloader/chapter3/MyObject.class;
        File classFile = new File(dir , classPath + ".class");
        // D:\app\classloader\com\learn\concurrent\classloader\chapter3\MyObject.class
        if(!classFile.exists()){
            throw  new ClassNotFoundException("The class " + name + " not fount under");
        }
        //将文件读成一个字节数组
        byte[] classBytes = loadClassBytes(classFile);
        if(null == classBytes && classBytes.length == 0){
            throw  new ClassNotFoundException("load the class " + name + " failed");
        }

        //返回 这定义的类 ，从0开始读取， 可以读取多个class, 从0~1000 是A class
        // 从1000~200是B class
        return this.defineClass(name, classBytes, 0, classBytes.length);
    }

    /**
     * 将一个文件变成一个数组
     * @param classFile
     * @return
     */
    private byte[] loadClassBytes(File classFile) {
        //写成这样的好处就是 不需要你自己去释放流，它会自己去释放
        // 把文件输入流，转换成内存输出流
        try(ByteArrayOutputStream b = new ByteArrayOutputStream();
            FileInputStream fis = new FileInputStream(classFile)) {
           // 用byte来缓冲 ,一次最多读多少个
            byte[] buffer = new byte[1024];
            // 读了多少个 ，如果是负1那么就没有了
            int len ;
            // 这个将文件中数据读到 byte数组中
            while ((len = fis.read(buffer))!=-1){
                // byte数组 写，这个将buffer的写到 byte数组中
                  b.write(buffer, 0, len);
            }
            //写完之后 flush
            b.flush();
            return  b.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return  null;
        }

    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }
}

```
31.2 我们需要加载的类
```java
package com.learn.concurrent.classloader.chapter3;

/**
 * @ClassName: MyObject
 * @Description:
 * @Author: lin
 * @Date: 2020/4/1 15:46
 * History:
 * @<version> 1.0
 */
public class MyObject {
    static {
        System.out.println("My object static block. ");
    }

    public String hello(){
        return "Hello World";
    }
}

```
31.3 测试自定义的类加载器
```java
package com.learn.concurrent.classloader.chapter3;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @ClassName: MyClassLoaderTest
 * @Description:
 * @Author: lin
 * @Date: 2020/4/1 15:48
 * History:
 * @<version> 1.0
 */
public class MyClassLoaderTest {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        // 我们需要编译好的文件从com开拷贝到 那个我建立的 磁盘目录中去
        // D:\app\classloader\com\learn\concurrent\classloader\chapter3
        //MyClassLoader 是我们自定义的加载器， 类加载器加载的地方就是我们设置的磁盘目录
        // D:\app\classloader
        MyClassLoader classLoader = new MyClassLoader("MyClassLoader");
         // 这个MyObject 是要加载的类
        Class<?> aClass = classLoader.loadClass("com.learn.concurrent.classloader.chapter3.MyObject");
        System.out.println(aClass);
        // 这里如果那个MyObject 这个文件在这个项目中，那么这个加载就会是系统类加载
        // 原因是classPath中 有这个文件

        // 所以 我们把 项目中MyObject删除，因为在那个磁盘目录中我们已经将
        // 编译好的文件 MyObject拷贝到 磁盘目录中放好了
        // 注意: classLoader的时候 不会去初始化类，这个不在6个主动使用中
        // 在 newInstance的时候会去初始化类
        System.out.println(aClass.getClassLoader());
        // 静态代码块会打印出来来
        Object o = aClass.newInstance();
        Method method = aClass.getMethod("hello", new Class<?>[]{});
        Object result = method.invoke(o, new Object[]{});
        System.out.println(result);

        //下面是打印的结果
        //class com.learn.concurrent.classloader.chapter3.MyObject
        //com.learn.concurrent.classloader.chapter3.MyClassLoader@6d06d69c
        //My object static block.
        //Hello World
    }
}

```
31.4、ClassLoader的父委托机制
```java

package com.learn.concurrent.classloader.chapter3;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 测试 ClassLoader 双亲委派机制
 * 1.类加载器的委托是优先交给父加载器先去尝试加载
 * 2.父加载器和子加载器其实是一种包装关系，或者 包含关系
 * 3.同一个classLoader(或者是父加载器的父加载器) 加载一个class 在加载完之后,它在堆去会有一个对象 且保持一份
 * 如果是两个不同的classLoader去加载,那么这里要注意命名空间 产生两个对象
 * @ClassName: MyClassLoaderTest2
 * @Description: 测试 ClassLoader 双亲委派机制
 * @Author: lin
 * @Date: 2020/4/1 15:48
 * History:
 * @<version> 1.0
 */
public class MyClassLoaderTest2 {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {

        MyClassLoader classLoader1 = new MyClassLoader("MyClassLoader-1");
        //这个加载类的父类加载器是 classLoader1，也就是说这两个是父子关系，
        // 但是不继承关系 他只一个包装的关系，也就classLoader1包含 classLoader2 但不是继承
        MyClassLoader classLoader2 = new MyClassLoader("MyClassLoader-2", classLoader1);
        // classLoader2加载的文件，这里重新设置一个地址，这目录下什么都没有，所以加载不了
        // 如果采取父委托机制，那么就会让classLoader2的父加载器 去加载, 这个父加载器会去找系统类加载器
        // 系统类加载器 去找扩展类加载器  ----> 根加载器，然后当根加载器看要加载的没有，那么就会告知
        // 扩展加载器去加载，如果扩展加载器也没有找到 就告知系统加载器---------------> 系统加载器去加载，
        // 当系统加载时 也没有，那么就会 去找自定义实现的加载器，当自定义加载器加载时有那么
        // 就将这个 给 当前请求的加载器， 如果都没有那么就是 报 我们定义的异常

        classLoader2.setDir("D:\\app\\classloader2");
        Class<?> aClass = classLoader2.loadClass("com.learn.concurrent.classloader.chapter3.MyObject");
        System.out.println(aClass);
        System.out.println(((MyClassLoader) aClass.getClassLoader()).getClassLoaderName());

        //打印结果 可以看出这个classLoader1去加载的
        //class com.learn.concurrent.classloader.chapter3.MyObject
        //MyClassLoader-1

        //如果classLoader2 没有定义父类加载器会出现什么呢？
        //出现的结果 就是我们定义的没有找到文件的异常
        //Exception in thread "main" java.lang.ClassNotFoundException: The class com.learn.concurrent.classloader.chapter3.MyObject not fount under


    }
}

```
31.5 简单的加密解密
```java

package com.learn.concurrent.classloader.chapter4;


/**
 * @ClassName: SimpleEncrypt
 * @Description: 简单加密
 * @Author: lin
 * @Date: 2020/4/1 22:08
 * History:
 * @<version> 1.0
 */
public class SimpleEncrypt {
   private static  final  String PLAIN = "Hello ClassLoader";

   private static  final byte ENCRYPT_FACTOR = (byte) 0xff;

    public static void main(String[] args) {
       byte[] bytes = PLAIN.getBytes();
       byte[] encrypt = new byte[bytes.length];
        for (int i = 0; i < bytes.length ; i++) {
            //通过位运算来进行简单加密
             encrypt[i] = (byte) ( bytes[i] ^ ENCRYPT_FACTOR);
        }
        System.out.println(new String(encrypt));

        byte[] decrypt = new byte[encrypt.length];
        for (int i = 0; i <encrypt.length ; i++) {
             decrypt[i] =(byte) (encrypt[i] ^ ENCRYPT_FACTOR);
        }
        System.out.println(new String(decrypt));


    }
}

```
31.6 打破双亲委派
```java
package com.learn.concurrent.classloader.chapter5;

import com.learn.concurrent.classloader.chapter1.LoaderClass;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 我们自己定的的ClassLoader, 我们打算从文件中去拿取
 * 也就是去磁盘定义一个目录
 * @ClassName: MyClassLoader
 * @Description:
 * @Author: lin
 * @Date: 2020/4/1 15:20
 * History:
 * @<version> 1.0
 */
public class SimpleClassLoader extends  ClassLoader{
    /**
     * 磁盘定义一个目录
     */
    private static  final String DEFAULT_DIR = "D:\\app\\revert";

    /**
     * 定义一个dir, 让别人可以传入,如果不传入那么就是默认值
     */
    private String dir = DEFAULT_DIR;

    /**
     * classLoader的名字
     */
    private String classLoaderName;

    /**
     * 默认构造函数
     */
    SimpleClassLoader(){
        super();
    }

    /**
     * 这个设置 classLoader的名字
     * @param classLoaderName
     */
    SimpleClassLoader(String classLoaderName){
        super();
        this.classLoaderName = classLoaderName;
    }

    /**
     * 将去 父类加载器传入进去, 如果不传入那么就是 系统类加载默认值
     * @param classLoaderName
     * @param parent
     */
    public SimpleClassLoader(String classLoaderName, ClassLoader parent){
        super(parent);
        this.classLoaderName = classLoaderName;
    }


    /**
     * 重写 loadClass 去覆盖父类的方法
     * @param name
     * @param resolve
     * @return
     * @throws ClassNotFoundException
     */
    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> clazz = null;
        //java 的这部分还是交给 其父加载器 去查找
        // 这么去将java中 包 进行处理
//        if(name.startsWith("java.")){
//            try {
//                //系统类加载器
//                ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
//                clazz = systemClassLoader.loadClass(name);
//                if(clazz != null){
//                    if (resolve){
//                        //解析
//                        resolveClass(clazz);
//                    }
//                    return  clazz;
//                }
//            }catch (Exception e){
//
//            }
//        }

        try {
            clazz = findClass(name);
        }catch (Exception e){
            e.printStackTrace();
        }

        if(clazz == null && getParent()!=null){
            getParent().loadClass(name);
        }

        return clazz;
    }

    /**
     * 重写这个 方法，通过这个方法去查找父类加载器
     * 这个 name传入 的是 xxx.xxx.xxx.AA 等
     * 就是去读取文件, 然后将其转换为符合加载的格式
     * xxx/xxx/xxx/xxx/AAA.class
     * @param name
     * @return
     * @throws ClassNotFoundException
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String classPath = name.replace(".", "/");
        // 文件是 classPath 然后加上 .class 才是class文件
        // dir是父目录  D:\\app\\classloader + com/learn/concurrent/classloader/chapter3/MyObject.class;
        File classFile = new File(dir , classPath + ".class");
        // D:\app\classloader\com\learn\concurrent\classloader\chapter3\MyObject.class
        if(!classFile.exists()){
            throw  new ClassNotFoundException("The class " + name + " not fount under");
        }
        //将文件读成一个字节数组
        byte[] classBytes = loadClassBytes(classFile);
        if(null == classBytes || classBytes.length == 0){
            throw  new ClassNotFoundException("load the class " + name + " failed");
        }

        //返回 这定义的类 ，从0开始读取， 可以读取多个class, 从0~1000 是A class
        // 从1000~200是B class
        return this.defineClass(name, classBytes, 0, classBytes.length);
    }




    /**
     * 将一个文件变成一个数组
     * @param classFile
     * @return
     */
    private byte[] loadClassBytes(File classFile) {
        //写成这样的好处就是 不需要你自己去释放流，它会自己去释放
        // 把文件输入流，转换成内存输出流
        try(ByteArrayOutputStream b = new ByteArrayOutputStream();
            FileInputStream fis = new FileInputStream(classFile)) {
           // 用byte来缓冲 ,一次最多读多少个
            byte[] buffer = new byte[1024];
            // 读了多少个 ，如果是负1那么就没有了
            int len ;
            // 这个将文件中数据读到 byte数组中
            while ((len = fis.read(buffer))!=-1){
                // byte数组 写，这个将buffer的写到 byte数组中
                  b.write(buffer, 0, len);
            }
            //写完之后 flush
            b.flush();
            return  b.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return  null;
        }

    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getClassLoaderName() {
        return classLoaderName;
    }
}

```
31.7 需要加载的类
```java
package com.learn.concurrent.classloader.chapter5;

/**
 * @ClassName: SimpleObject
 * @Description:
 * @Author: lin
 * @Date: 2020/4/1 23:06
 * History:
 * @<version> 1.0
 */
public class SimpleObject {
}

```
31.8 测试打破双亲委派
```java
package com.learn.concurrent.classloader.chapter5;

/**
 * @ClassName: SimpleClassLoaderTest
 * @Description:
 * @Author: lin
 * @Date: 2020/4/1 23:05
 * History:
 * @<version> 1.0
 */
public class SimpleClassLoaderTest {
    public static void main(String[] args) throws ClassNotFoundException {
        // 我们要的效果就是 让我们自己定义加载器去加载这个类
        // 我们通过这种方式打破了双委托机制， 这个去加载的时候 首先将java.* 包的
        // 给他的父加载器加载，
        SimpleClassLoader simpleClassLoader = new SimpleClassLoader();
//        Class<?> aClass = simpleClassLoader.loadClass("com.learn.concurrent.classloader.chapter5.SimpleObject");
//        System.out.println(aClass.getClassLoader());


        // 注意:如果我们将 自己在目录中加一个java.lang.xxx 的包 然后里面写一个
        // String 的类,  我那个将其代码编译好了之后 将我们自己写的
        // java.lang.String 的class 放到和 "D:\\app\\revert" 这个同一目录下
        // 就是这样 D:\app\revert\java\lang\String.class
        // 然后 我将自己定义的SimpleClassLoader加载器 中 loadClass 取消对 java.lang.xxx
        // 加载的判断, 然后执行代码
        Class<?> aClass = simpleClassLoader.loadClass("java.lang.String");
        System.out.println(aClass.getClassLoader());
        // 这打印的结果是 java.lang.SecurityException: Prohibited package name: java.lang
        // 从这个错误来看 报了一个安全的异常错误, 禁止加载 java.lang的包，
        //
        // 一般面试就会问 可不可以打破 双委托机制: 这里我们实验了可以打破
        // 但是java里面对 java.lang.xxx 的加载 进行了安全的验证 和检查
        // 所以 如果我们自己写一个java.lang.xxx 的包然后 用自己的classLoader进行加载
        // 那么就会 出现安全异常
    }
}

```
32、classLoader命名空间,运行时包
``` 
类加载器的命名空间:
  .每个类的加载器都有子的命名空间,命名空间由该加载器及其所有父加载器所加载的类组成
  .在同一个命名空间中，不会出现完整的名字

运行时包: 
   .父类加载器看不到子类加载器加载的类
   .不同命名空间下的类加载之间的类互相不可访问
```
33、类的卸载以及classLoader的卸载
```
JVM的class只有满足以下三个条件，才能被GC回收，也就是该class被卸载:
 .该类所有的实例都已经被GC。
 .加载类的ClassLoader实例已经被GC
 .该类的java.lang.Class对象没有在任何地方被引用
 .GC的时机我们是不可控制的，那么同样的我们对于Class的卸载也是不可控的。
```
34.Unsafe 的趣味使用
```java
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

```


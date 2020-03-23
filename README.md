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
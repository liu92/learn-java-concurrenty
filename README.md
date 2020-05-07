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
比如在有些情况 不能设置setDaemon 为true，因为这种方式下 会随着主线程的生命周期结束而结束
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
4、一般来说，Java 中的线程可以分为两种：守护线程和普通线程。在 JVM 刚启动时，它创建的所有线程，
除了主线程（main thread）外，其他的线程都是守护线程（比如：垃圾收集器、以及其他执行辅助操作的线程）。
当创建一个新线程时，新线程将会继承它线程的守护状态，默认情况下，
主线程创建的所有线程都是普通线程。

什么情况下会需要守护线程呢？一般情况下是，当我们希望创建一个线程来执
行一些辅助的工作，但是又不希望这个线程阻碍 JVM 的关闭，在这种情况下，
我们就需要使用守护线程了。

5、守护线程的作用
守护线程与普通线程唯一的区别是：当线程退出时，JVM 会检查其他正在运行的线程，
如果这些线程都是守护线程，那么 JVM 会正常退出操作，但是如果有普通线程还在运行，
JVM 是不会执行退出操作的。当 JVM 退出时，所有仍然存在的守护线程都将被抛弃，既不会执行 finally 部分的代码，
也不会执行 stack unwound 操作，JVM 会直接退出。

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
1. singleton design pattern (单例模式)
2. WaitSet in synchronzied monitor
3. Cpu&Cpu cache& Main Memory & Data Bus & Cache Line (cpu缓存，主内存，数据线，缓存级别)
4. The volatile key word in deep （volatile 关键字深入理解）
5. Java Class Loader (类加载)
6. Observer to monitor The Thread lifecycle (线程生命周期)
7. Single Thread Execution design pattern (单线程执行模式)
8. Immutable design pattern (不可变线程设计模式)
9. Guarded Suspension design pattern : 等待唤醒机制的规范实现 ，直译为保护性暂停
10.Balking design pattern
11.Producer-Consumer (生产---消费)
12.Read-Write Lock design pattern （读写锁模式）
13.Thread-Per-Message Design Patten ：这种委托他人办理的方式，在并发编程领域被总结为一种设计模式，叫做 Thread-Per-Message 模式，简言之就是为每个任务分配一个独立的线程。
这是一种最简单的分工方法，实现起来也非常简单。
Thread-Per-Message 模式的一个最经典的应用场景是网络编程里服务端的实现，服务端为每个客户端请求创建一个独立的线程，当线程处理完请求后，
自动销毁，这是一种最简单的并发处理网络请求的方法。

14.Worker Thread Design Pattern
15.Future Design Pattern  （Future设计模式）
16.Two-Phase Termination Design Pattern （两阶段提交）
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
包括用于类和实例初始化以及接口初始化的特殊方法。
而堆里面存放的是类对象的真实数据，这个真实的数据的结构就是存放在堆里
当我们在访问的时候 通过堆中有个对象类型数据的指针 然后去访问 方法区，
因为数据有了 但是要组织起来，这个数据的类型是存放在方法区中的 所以 通过 
对象数据类型指针来指定到 方法区 然后方法区将这个对象的类类型组织起来。

```
30.4 链接阶段
```
1、在加载阶段完成后，虚拟机外部的二进制数据量就会按照虚拟机所需要的格式存储在
方法区中(数据结构)，然后在堆中创建一个class对象，这个对象作为程序访问方法区中
这些数据结构的外部接口
2、加载阶段与连接阶段的部分内容可以是交叉进行的，比如一部分代码加载完成就可以进行验证
提供效率 

3、验证阶段: 验证阶段的目的是确保class文件中的字节流中包含的信息符合虚拟机的要求，
并且不会损害到jvm自身的安全
   VerifyError
3.1、文件格式验证
  .魔术因子是否正确，0xCAFEBABE
  .主从版本号是否符合当前虚拟机
  .常量池中常量类型是不是不支持
  .etc
3.2、 元数据验证
  . 是否有父类
  . 父类是不是 允许继承
  . 是否实现了抽象方法
  . 是否覆盖了父类的final字段
  . 其他的语义检查
3.3、 字节码验证
  . 主要进行数据和控制流的分析，不会出现这样的情况，在操作栈中放置一个int类型
    但是却给了一个long行的数据
3.4、 符合应用验证
  . 调用了一个不存在方法，字段等等
    符号引用验证的目的是确保解析动作能正常执行，如果无法通过符号引用验证，将会抛出
  一个java.lang.IncompatibleClassChangeError异常的子类，如java.lang.IllegalAccessError,
   java.lang.NosuchFieldError 等

4 准备阶段: 就是给类变量分配初始值(就是一些默认值)

5、解析阶段: 
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
31.9 线程上下文以及数据库驱动
```java
package com.learn.concurrent.classloader.chapter6;

import com.learn.concurrent.classloader.chapter3.MyClassLoader;

/**
 * 线程上下文以及数据库驱动
 *
 * @ClassName: ThreadContextClassLoader
 * @Description:
 * @Author: lin
 * @Date: 2020/4/2 11:16
 * History:
 * @<version> 1.0
 */
public class ThreadContextClassLoader {
    public static void main(String[] args) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        System.out.println(classLoader);
        Thread.currentThread().setContextClassLoader(new MyClassLoader());
        // 这种线程上下文，就相当于jdk开了一个 后门 去破坏父委托机制
        System.out.println(Thread.currentThread().getContextClassLoader());

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
35. AtomicInteger原子类的使用
```java
package com.learn.concurrenty.juc.atomic;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName: AtomicIntegerTest
 * @Description:
 * @Author: lin
 * @Date: 2020/4/2 12:12
 * History:
 * @<version> 1.0
 */
public class AtomicIntegerTest {
    private static  final Set<Integer> SET = Collections.synchronizedSet(new HashSet<>());
    public static void main(String[] args) throws InterruptedException {

        final AtomicInteger value = new AtomicInteger();
        Thread t1 = new Thread(() -> {
            int x = 0;
            int count = 500;
            while (x < count){
                int v = value.getAndIncrement();
                SET.add(v);
                System.out.println(Thread.currentThread().getName() + ":" + v);
                x++;
            }
        });

        Thread t2 = new Thread(() -> {
            int x = 0;
            int count = 500;
            while (x < count){
                int v = value.getAndIncrement();
                SET.add(v);
                System.out.println(Thread.currentThread().getName() + ":" + v);
                x++;
            }
        });

        Thread t3 = new Thread(()->{
                int x = 0;
                int count = 500;
                while (x < count){
                    int v = value.getAndIncrement();
                    SET.add(v);
                    System.out.println(Thread.currentThread().getName() + ":" + v);
                    x++;
                }
           });

        t1.start();
        t2.start();
        t3.start();
        t1.join();
        t2.join();
        t3.join();
        System.out.println(SET.size());
    }
}

```
35.1 AtomicIntegerDetailsTest 测试
```java
package com.learn.concurrenty.juc.atomic;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName: AtomicIntegerDetailsTest
 * @Description:
 * @Author: lin
 * @Date: 2020/4/2 14:18
 * History:
 * @<version> 1.0
 */
public class AtomicIntegerDetailsTest {
    public static void main(String[] args) {
        //get and set
//        AtomicInteger  getAndSet = new AtomicInteger(10);
//        int result = getAndSet.getAndAdd(10);
//        System.out.println(result);
//        System.out.println(getAndSet.get());


        AtomicInteger  atomicInteger = new AtomicInteger(10);
        // 将 期望值12和 当前值10 进行比较，在AtomicInteger中 value表示当前值
        // 最快失败
        atomicInteger.compareAndSet(12, 20);
    }

}

```
35.2  通过 compareAndSet 尝试去 获取锁,如果获取不到锁就去做其他的
```java
package com.learn.concurrenty.juc.atomic;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName: AtomicIntegerDetailsTest
 * @Description:
 * @Author: lin
 * @Date: 2020/4/2 14:18
 * History:
 * @<version> 1.0
 */
public class AtomicIntegerDetailsTest2 {
    private final  static  CompareAndSetLock LOCK = new CompareAndSetLock();
    public static void main(String[] args) {
        for (int i = 0; i <2 ; i++) {
            new Thread(){
                @Override
                public void run() {
                    try {
//                        doSomething();
                        doSomething2();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    private static void doSomething() throws InterruptedException {
        synchronized (AtomicIntegerDetailsTest2.class){
            System.out.println(Thread.currentThread().getName()+" ");
            Thread.sleep(100000);
        }
    }

    /**
     * 通过 compareAndSet 尝试去 获取锁,如果获取不到锁就去做其他的
     * @throws InterruptedException
     */
    private static void doSomething2() throws InterruptedException {
        try {
            LOCK.tryLock();
            System.out.println(Thread.currentThread().getName()+" get the lock");
            Thread.sleep(100000);
        } catch (GetLockException e) {
            e.printStackTrace();
        }finally {
            // 这个地方有问题，就是有的线程没有抢到锁，但是会去释放锁，
            // 使用这个 compareAndSet(1,0) 去进行比较替换处理，其他线程抢到锁之后
            // 已经将其更新，而这时没有抢到锁的线程 就通过 其他线程更新的 value
            // 再来比较 compareAndSet(1,0) 而这时 去比较 期望值和当前值相等，就会将值更新为0
            // 这样就会造成 锁被多个线程拿到， 所以谁拿到锁就是谁去释放
            LOCK.unLock();
        }

    }


}

```
35.2.1
```java
package com.learn.concurrenty.juc.atomic;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName: CompareAndSetLock
 * @Description:
 * @Author: lin
 * @Date: 2020/4/2 15:10
 * History:
 * @<version> 1.0
 */
public class CompareAndSetLock {
    private final AtomicInteger value = new AtomicInteger(0);

    private Thread lockedThread;

    public void tryLock() throws GetLockException {
        boolean success = value.compareAndSet(0, 1);
        if(!success){
            throw  new GetLockException("Get the Lock failed");
        }else{
            // 那个线程获得了锁，那么就是那个线程去释放
            lockedThread = Thread.currentThread();
        }
    }

    public void unLock(){
        //这个锁已经被释放了
        if(0 == value.get() || lockedThread ==null){
            return;
        }
        if(lockedThread == Thread.currentThread()){
            value.compareAndSet(1, 0);
        }

    }
}

```

35.3 AtomicReference 使用
```java
package com.learn.concurrenty.juc.atomic;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @ClassName: AtomicReferenceTest
 * @Description:
 * @Author: lin
 * @Date: 2020/4/2 16:26
 * History:
 * @<version> 1.0
 */
public class AtomicReferenceTest {
    public static void main(String[] args) {
        //4个字节的引用类型
        AtomicReference<Simple> atomicReference = new AtomicReference<Simple>(new Simple("lin",12));
        System.out.println(atomicReference.get());
        atomicReference.compareAndSet(new Simple("sdfs", 22),
                new Simple("sdfs",234));
    }


    static class Simple{
        private String name;
        private int age;
        Simple(String name, int age){
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}

```
35.4 AtomicIntegerFieldUpdater 使用
```java
package com.learn.concurrenty.juc.atomic;


import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * @ClassName: AtomicIntegerFieldUpdaterTest
 * @Description:
 * @Author: lin
 * @Date: 2020/4/2 16:59
 * History:
 * @<version> 1.0
 */
public class AtomicIntegerFieldUpdaterTest {
    public static void main(String[] args) {
        final AtomicIntegerFieldUpdater<TestMe> updater = AtomicIntegerFieldUpdater.
                newUpdater(TestMe.class, "i");
        TestMe testMe = new TestMe();
        int t = 2 ;
        for (int i = 0; i < t; i++) {
            new Thread(){
                @Override
                public void run() {
                    final  int MAX = 20 ;
                    for (int i = 0; i < MAX; i++) {
                        int v = updater.getAndIncrement(testMe);
                        System.out.println(Thread.currentThread().getName() + "=>"+ v );
                    }
                } 
            };
        }
    }

    static class TestMe{
        volatile int i;
    }
}
```
35.5 AtomicIntegerFieldUpdater 测试2
```java
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

```

35.6 AtomicBooleanFlag原子类的使用
```java
package com.learn.concurrenty.juc.atomic;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @ClassName: AtomicBooleanFlag
 * @Description:
 * @Author: lin
 * @Date: 2020/4/2 16:06
 * History:
 * @<version> 1.0
 */
public class AtomicBooleanFlag {
    /**
     * 这个可以替代 volatile 和 boolean 作为一个flag的标志
     */
    private final  static AtomicBoolean flag = new AtomicBoolean(true);
    public static void main(String[] args) throws InterruptedException {
        new Thread(){
            @Override
            public void run() {
                while (flag.get()){
                    try {
                        Thread.sleep(1000);
                        System.out.println("I am working");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("I am finished.");
            }
        }.start();

        Thread.sleep(5000);
        flag.set(false);
    }
}

```
35.7 JitTest
```java
package com.learn.concurrenty.juc.atomic;

/**
 * @ClassName: JitTest
 * @Description:
 * @Author: lin
 * @Date: 2020/4/2 14:00
 * History:
 * @<version> 1.0
 */
public class JitTest {
    private static volatile   boolean init = false;
    public static void main(String[] args) {
        new Thread(){
            @Override
            public void run() {
                while (!init){
                    /**
                     * 这里有问题
                     * 就是在不同的jdk 下会有不同的情况
                     *
                     * 有的jdk情况下 这里会退出，而有的不会退出
                     * 造成这样的原因是jit 的进行了优化
                     *
                     * 就是在jdk1.8 下 如果这while循环体重没有其他的代码是
                     * 会优化成这样
                     * while(true){}
                     * 如果这个里面有其他的代码 就不会优化,比如这样
                     * while(!init){
                     *     System.out.println(".")
                     * }
                     *
                     * 那么解决这样的问题,就是变量要加上volatile,安装jdk规范来
                     * 避免不必要的争议
                     *
                     */
                }
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
               init = true;
                System.out.println("set init to true.");
            }
        }.start();
    }
}

```
36. ArrayBlockingQueue阻塞队列学习
```java
package com.learn.concurrenty.juc.blocking;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @ClassName: ArrayBlockingQueueExample1
 * @Description:
 * @Author: lin
 * @Date: 2020/4/17 14:41
 * History:
 * @<version> 1.0
 */
public class ArrayBlockingQueueExample1 {

    /**
     * 1.FIFO(first in first out)
     * 2.once created , the capacity cannot be changed.
     * @param size
     * @param <T>
     * @return
     */
    public <T> ArrayBlockingQueue<T> creat(int size){
        return  new ArrayBlockingQueue<>(size);
    }
}

```
36.1 junit 测试ArrayBlockingQueue
```java
package com.learn.concurrenty.juc.blocking;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;


public class ArrayBlockingQueueExample1Test {

    private ArrayBlockingQueueExample1 example1;

    @Before
    public void setUp(){
        example1 = new ArrayBlockingQueueExample1();
    }

    @After
    public void  tearDown(){
        example1 = null;
    }


    /**
     * Inserts the specified element at the tail of this queue if it is
     * possible to do so immediately without exceeding the queue's capacity,
     * returning {@code true} upon success and throwing an
     * {@code IllegalStateException} if this queue is full.
     */
    @Test
    public void testAddMethodNotExceedCapacity(){
        ArrayBlockingQueue<String> queue = example1.creat(5);
        assertThat(queue.add("Hello1"), equalTo(true));
        assertThat(queue.add("Hello2"), equalTo(true));
        assertThat(queue.add("Hello3"), equalTo(true));
        assertThat(queue.add("Hello4"), equalTo(true));
        assertThat(queue.add("Hello5"), equalTo(true));
//        assertThat(queue.size(), equalTo(5));
    }


    /**
     * 超过这个队列的容量
     */
    @Test
    public void testAddMethodExceedExceedCapacity(){
        ArrayBlockingQueue<String> queue = example1.creat(5);
        assertThat(queue.add("Hello1"), equalTo(true));
        assertThat(queue.add("Hello2"), equalTo(true));
        assertThat(queue.add("Hello3"), equalTo(true));
        assertThat(queue.add("Hello4"), equalTo(true));
        assertThat(queue.add("Hello5"), equalTo(true));
        assertThat(queue.add("Hello6"), equalTo(true));
//        assertThat(queue.size(), equalTo(true));
    }


    @Test
    public void testPutMethodExceedExceedCapacity() throws InterruptedException {
        ArrayBlockingQueue<String> queue = example1.creat(5);
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.schedule(()->{
            try {
                assertThat(queue.take(), equalTo("Hello1"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },1 , TimeUnit.SECONDS);
        // 这里也应该等一下，因为在这里的时候任务都还没有执行就 shutdown了
        scheduledExecutorService.shutdown();

        // 使用put方法时会抛出异常，这个时候我们可以知道为啥会抛出异常
        // 因为当这个添加数量多于这个阻塞队列容量是，就会被阻塞
        queue.put("Hello1");
        queue.put("Hello2");
        queue.put("Hello3");
        queue.put("Hello4");
        queue.put("Hello5");
        queue.put("Hello6");
//        fail("should not process to here");

    }

    @Test
    public void testPoll(){
        ArrayBlockingQueue<String> queue = example1.creat(2);
        queue.add("Hello1");
        queue.add("Hello2");

        assertThat(queue.poll(), equalTo("Hello1"));
        assertThat(queue.poll(), equalTo("Hello2"));
        assertThat(queue.poll(), nullValue());
    }

}
```
36.2 DelayQueue 队列
```java
package com.learn.concurrenty.juc.blocking;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.SynchronousQueue;

/**
 *
 * 1.The delay queue will ordered by expired time? yes
 * 2.When poll the empty delay queue will return null? use take?
 * 3.when less  the expire time will return quickly?  yes
 * 4.Even though unexpired elements cannot be remove using {@code take} or {@code poll}
 * 5.This queue does not permit null elements.
 * 6.Use iterator can return quickly?
 *
 * NOTICE: The DelayQueue elements must implement the {@link java.util.concurrent.Delayed}
 * The DelayQueue is a unbounded queue.
 * @ClassName: DelayQueueExample
 * @Description:
 * @Author: lin
 * @Date: 2020/4/17 17:31
 * History:
 * @<version> 1.0
 */
public class DelayQueueExample {
    public static <T extends Delayed> DelayQueue<T> creat(){
        return  new DelayQueue<>();
    }
}

```
36.2.1 DelayQueue队列 junit测试
```java
package com.learn.concurrenty.juc.blocking;

import org.junit.Test;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class DelayQueueExampleTest {

    @Test
    public  void testAdd(){
        DelayQueue<DelayElement<String>> delayQueue = DelayQueueExample.creat();
        DelayElement<String> delayed1 = DelayElement.of("Delayed1", 1000);
        delayQueue.add(delayed1);
        assertThat(delayQueue.size(), equalTo(1));
        assertThat(delayQueue.peek(), is(delayed1));
    }


    static class  DelayElement<E> implements Delayed{

        private final E e;

        private final long expireTime;

        DelayElement(E e, long delay){
            this.e = e;
            this.expireTime = System.currentTimeMillis() + delay;
        }

        static <T> DelayElement<T> of(T t, long delay){
            return  new DelayElement<>(t, delay);
        }


        /**
         * Returns the remaining delay associated with this object, in the
         * given time unit.
         *
         * @param unit the time unit
         * @return the remaining delay; zero or negative values indicate
         * that the delay has already elapsed
         */
        @Override
        public long getDelay(TimeUnit unit) {
            long diff = expireTime - System.currentTimeMillis();
            return unit.convert(diff, TimeUnit.MILLISECONDS);
        }

        /**
         * Compares this object with the specified object for order.  Returns a
         * negative integer, zero, or a positive integer as this object is less
         * than, equal to, or greater than the specified object.
         *
         * <p>The implementor must ensure <tt>sgn(x.compareTo(y)) ==
         * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
         * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
         * <tt>y.compareTo(x)</tt> throws an exception.)
         *
         * <p>The implementor must also ensure that the relation is transitive:
         * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
         * <tt>x.compareTo(z)&gt;0</tt>.
         *
         * <p>Finally, the implementor must ensure that <tt>x.compareTo(y)==0</tt>
         * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
         * all <tt>z</tt>.
         *
         * <p>It is strongly recommended, but <i>not</i> strictly required that
         * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
         * class that implements the <tt>Comparable</tt> interface and violates
         * this condition should clearly indicate this fact.  The recommended
         * language is "Note: this class has a natural ordering that is
         * inconsistent with equals."
         *
         * <p>In the foregoing description, the notation
         * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
         * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
         * <tt>0</tt>, or <tt>1</tt> according to whether the value of
         * <i>expression</i> is negative, zero or positive.
         *
         * @param delayedObject the object to be compared.
         * @return a negative integer, zero, or a positive integer as this object
         * is less than, equal to, or greater than the specified object.
         * @throws NullPointerException if the specified object is null
         * @throws ClassCastException   if the specified object's type prevents it
         *                              from being compared to this object.
         */
        @Override
        public int compareTo(Delayed delayedObject) {
            DelayElement delayElement =(DelayElement) delayedObject;
            if(this.expireTime < delayElement.getExpireTime()){
                return  -1;
            }
            else  if(this.expireTime > delayElement.getExpireTime()) {
                return 1;
            }else {
                return 0;
            }
        }


        public E getData(){
            return e;
        }

        public long getExpireTime(){
            return expireTime;
        }
    }
}
```
36.3 LinkedBlockingQueue队列
```java
package com.learn.concurrenty.juc.blocking;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @ClassName: LinkedBlockingQueueExample
 * @Description:
 * @Author: lin
 * @Date: 2020/4/17 14:41
 * History:
 * @<version> 1.0
 */
public class LinkedBlockingQueueExample {

    /**
     * 1.FIFO(first in first out)
     * 2.once created , the capacity cannot be changed.
     * @param size
     * @param <T>
     * @return
     */
    public <T> LinkedBlockingQueue<T> creat(int size){
        return  new LinkedBlockingQueue<>(size);
    }
}

```
36.3.1 LinkedBlockingQueue 单元例测试
```java
package com.learn.concurrenty.juc.blocking;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.LinkedBlockingQueue;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

public class LinkedBlockingQueueExampleTest {
    private LinkedBlockingQueueExample example;


    @Before
    public void setUp(){
        example = new LinkedBlockingQueueExample();
    }

    @After
    public void  tearDown(){
        example = null;
    }


    @Test
    public void testInsertData(){
        LinkedBlockingQueue<String> queue = example.creat(2);
        assertThat(queue.offer("data1"), equalTo(true));
        assertThat(queue.offer("data1"), equalTo(true));
        assertThat(queue.offer("data1"), equalTo(false));
    }
}
```
36.4 LinkedTransferQueue 队列学习
```java
package com.learn.concurrenty.juc.blocking;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.LinkedTransferQueue;

/**
 * LinkedTransferQueue 担保 递出去的元素必须被消费
 *
 * @ClassName: LinkedTransferQueueExample
 * @Description:
 * @Author: lin
 * @Date: 2020/4/17 17:31
 * History:
 * @<version> 1.0
 */
public class LinkedTransferQueueExample {
    public static <T > LinkedTransferQueue<T> creat(){
        return  new LinkedTransferQueue<>();
    }
}

```
36.4.1 LinkedTransferQueue 队列junit 测试
```java
package com.learn.concurrenty.juc.blocking;

import org.junit.Test;

import java.util.concurrent.LinkedTransferQueue;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

public class LinkedTransferQueueExampleTest {

    /**
     * Transfers the element to a waiting consumer immediately, if possible.
     * Question
     * when return the false that means at this time no consumer waiting, how about the element ?
     * will store into the queue?
     *
     * Answer:
     *  without enqueuing the element.
     */
    @Test
    public void testTryTransfer()  {
        LinkedTransferQueue<String> queue = LinkedTransferQueueExample.creat();
        // 没有消费者 这个元素是不会放入进去的
        boolean result = queue.tryTransfer("Transfer");
        assertThat(result, equalTo(false));
        assertThat(queue.size(), equalTo(0));
    }

}
```
36.5 PriorityBlockingQueue 队列
```java
package com.learn.concurrenty.juc.blocking;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * @ClassName: ArrayBlockingQueueExample1
 * @Description:
 * @Author: lin
 * @Date: 2020/4/17 14:41
 * History:
 * @<version> 1.0
 */
public class PriorityBlockingQueueExample {

    /**
     * 1.FIFO(first in first out)
     * 2.once created , the capacity cannot be changed.
     * @param size
     * @param <T>
     * @return
     */
    public <T> PriorityBlockingQueue<T> creat(int size){
        return  new PriorityBlockingQueue<>(size);
    }
}

```
36.5.1 PriorityBlockingQueueTest 单元例测试
```java
package com.learn.concurrenty.juc.blocking;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.PriorityBlockingQueue;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

public class PriorityBlockingQueueExampleTest {
    private PriorityBlockingQueueExample example;

    @Before
    public void setUp(){
        example = new PriorityBlockingQueueExample();
    }

    @After
    public void  tearDown(){
        example = null;
    }

    @Test
    public  void  testAddNewElement(){
        PriorityBlockingQueue<String> queue = example.creat(5);
        assertThat(queue.add("Hello1"), equalTo(true));
        assertThat(queue.add("Hello2"), equalTo(true));
        assertThat(queue.add("Hello3"), equalTo(true));
        assertThat(queue.add("Hello4"), equalTo(true));
        assertThat(queue.add("Hello5"), equalTo(true));
        assertThat(queue.add("Hello6"), equalTo(true));
        assertThat(queue.size(), equalTo(6));
    }
}
```
36.6 SynchronousQueueExample
```java
package com.learn.concurrenty.juc.blocking;

import java.util.concurrent.SynchronousQueue;

/**
 * @ClassName: SynchronousQueueExample
 * @Description:
 * @Author: lin
 * @Date: 2020/4/17 17:31
 * History:
 * @<version> 1.0
 */
public class SynchronousQueueExample {
    public static <T> SynchronousQueue<T> creat(){
        return  new SynchronousQueue<>();
    }
}

```
36.6.1 SynchronousQueueExampleTest junit测试
```java
package com.learn.concurrenty.juc.blocking;

import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

public class SynchronousQueueExampleTest {

    @Test
    public  void testAdd() throws InterruptedException {
        SynchronousQueue<String> queue = SynchronousQueueExample.creat();
        Executors.newSingleThreadExecutor().submit(()->{
            try {
                assertThat(queue.take(), equalTo("SynchronousQueue"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        TimeUnit.MILLISECONDS.sleep(5);
        assertThat(queue.add("SynchronousQueue"), equalTo(true));
    }

}
```
37. 学习 java.util.concurrent 包下面api
```java
package com.learn.concurrenty.juc.untils.condition;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName: ConditionExample
 * @Description:
 * @Author: lin
 * @Date: 2020/4/10 14:32
 * History:
 * @<version> 1.0
 */
public class ConditionExample {

    private final static ReentrantLock LOCK = new ReentrantLock();
    private final static Condition CONDITION = LOCK.newCondition();
    private static int data = 0;
    private static volatile boolean noUse = true;

    public static void main(String[] args) {
        new Thread(()->{
            for (; ;) {
              buildData();
            }
        }).start();

        new Thread(()->{
            for (; ;) {
                useData();
            }
        }).start();
    }


    public static void buildData(){
        try {
            LOCK.lock();    // synchronize key word  #monitor enter
            while (noUse){
                // 看看是否已使用，如果未使用那么就不能生产，就继续等待
                // 等着数据被消费
                CONDITION.await();   // monitor.wait()
            }
            // 如果消费了，那么这里就生产
            data++;
            Optional.of("Produce:" + data).ifPresent(System.out::println);
            // 休眠，模拟这个生产过程比较慢
            TimeUnit.SECONDS.sleep(1);
            // 生产了那么这里就是 没有使用
            noUse = true;
            // 生产好了之后进行通知
            CONDITION.signal();     // monitor.notify()
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            LOCK.unlock();      // synchronize end  #monitor exit
        }
    }

    public static void useData(){
        try {
            LOCK.lock();
            while (!noUse){
                CONDITION.await();
            }
            TimeUnit.SECONDS.sleep(1);
            Optional.of("Consumer:" + data).ifPresent(System.out::println);
            noUse = false;
            CONDITION.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
             LOCK.unlock();
        }
    }

}

``` 
37.2 Condition2 测试
```java
package com.learn.concurrenty.juc.untils.condition;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName: ConditionExample
 * @Description:
 * @Author: lin
 * @Date: 2020/4/10 14:32
 * History:
 * @<version> 1.0
 */
public class ConditionExample2 {

    private final static ReentrantLock LOCK = new ReentrantLock(true);
    private final static Condition CONDITION = LOCK.newCondition();
    private static int data = 0;
    private static volatile boolean noUse = true;

    /**
     * 不能100%保证公平
     * 在上面公平锁测试情况暂时没有看出什么问题，但是在非公平锁下面就会有问题
     * 可能出现  生产者 一直生产 但是消费者没有消费
     *
     * 1. 不使用condition  只是使用lock 这种方式可以吗？  不能
     * 2. the producer get the lock but invoke await method and not jump out the lock statement block
     * why the consumer can get the lock still ?
     * 3. 不使用 lock 只使用 condition ?
     * @param args
     */
    public static void main(String[] args) {
        new Thread(()->{
            for (; ;) {
              buildData();
            }
        }).start();

        new Thread(()->{
            for (; ;) {
                useData();
            }
        }).start();
    }


    public static void buildData(){
        try {
            LOCK.lock();    // synchronize key word  #monitor enter
//            while (noUse){
//                // 看看是否已使用，如果未使用那么就不能生产，就继续等待
//                // 等着数据被消费
//                CONDITION.await();   // monitor.wait()
//            }
            // 如果消费了，那么这里就生产
            data++;
            Optional.of("Produce:" + data).ifPresent(System.out::println);
            // 休眠，模拟这个生产过程比较慢
            TimeUnit.SECONDS.sleep(1);
            // 生产了那么这里就是 没有使用
//            noUse = true;
            // 生产好了之后进行通知
//            CONDITION.signal();     // monitor.notify()
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            LOCK.unlock();      // synchronize end  #monitor exit
        }
    }

    public static void useData(){
        try {
            LOCK.lock();
//            while (!noUse){
//                CONDITION.await();
//            }
            TimeUnit.SECONDS.sleep(1);
            Optional.of("Consumer:" + data).ifPresent(System.out::println);
    //            noUse = false;
    //            CONDITION.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
             LOCK.unlock();
        }
    }

}

```
37.3 Condition3 测试
```java
package com.learn.concurrenty.juc.untils.condition;

import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

/**
 * @ClassName: ConditionExample
 * @Description:
 * @Author: lin
 * @Date: 2020/4/10 14:32
 * History:
 * @<version> 1.0
 */
public class ConditionExample3 {

    private final static ReentrantLock LOCK = new ReentrantLock();
    private final static Condition PRODUCE_COND = LOCK.newCondition();
    private final static Condition CONSUME_COND = LOCK.newCondition();
    private final static LinkedList<Long> TIMESTAMP_POOL = new LinkedList<>();
    private final static int MAX_CAPACITY = 100;

    /**
     * @param args
     */
    public static void main(String[] args) {
        IntStream.range(0, 6).boxed().forEach(ConditionExample3::beginProduce);
        IntStream.range(0, 13).boxed().forEach(ConditionExample3::beginConsume);
    }

    private static void beginProduce(int i){
        new Thread(()->{
            for (;;) {
                produce();
                sleep(2);
            }
        },"-P-" + i).start();
    }
    private static void beginConsume(int i){
        new Thread(()->{
            for (;;) {
                consumer();
                sleep(2);
            }
        },"-C-" + i).start();
    }


    private static void produce(){
        try {
           LOCK.lock();
           while (TIMESTAMP_POOL.size() >= MAX_CAPACITY){
               PRODUCE_COND.await();
           }
           long value = System.currentTimeMillis();
            System.out.println(Thread.currentThread().getName() + "-P-" + value);
            TIMESTAMP_POOL.addLast(value);
            CONSUME_COND.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            LOCK.unlock();
        }
    }

    private static void consumer(){
        try {
            LOCK.lock();
            while (TIMESTAMP_POOL.isEmpty()){
                CONSUME_COND.await();
            }
            //从头开始消费
            Long value = TIMESTAMP_POOL.removeFirst();
            System.out.println(Thread.currentThread().getName() + "-C-" + value);

            PRODUCE_COND.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            LOCK.unlock();
        }
    }

    private static  void sleep(long sec){
        try {
            TimeUnit.SECONDS.sleep(sec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

```
37.4 CountDownLatchExample1 测试
```java
package com.learn.concurrenty.juc.untils.countdownlatch;

import com.learn.concurrenty.DefaultThreadFactory;

import java.util.Random;
import java.util.concurrent.*;

/**
 * @ClassName: CountDownLatchExample1
 * @Description:
 * @Author: lin
 * @Date: 2020/4/8 16:57
 * History:
 * @<version> 1.0
 */
public class CountDownLatchExample1 {
   private static Random random = new Random(System.currentTimeMillis());
   private static CountDownLatch countDownLatch = new CountDownLatch(10);
   private static ExecutorService EXECUTOR_SERVICE = new ThreadPoolExecutor(2,
            2, 200,
            TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(6),
            new DefaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());

    private static ExecutorService EXECUTOR_SERVICE_FIXED = Executors.newFixedThreadPool(2);

    public static void main(String[] args) throws InterruptedException {
        //(1)
        int[] data = query();
        //(2)
        for (int i = 0; i <data.length; i++) {
            //异步执行
            // 什么时候countDownLatch减为零, 就是这个线程工作完了 就可以了
            EXECUTOR_SERVICE_FIXED.execute(new SimpleRunnable(data, i, countDownLatch));
        }
        //(3) 这一步应该等所有动作完成才执行

        // 可以使用shoutDown();但是这个只是打一个标志,
        // 等到所有的线程都空闲了才结束
        //EXECUTOR_SERVICE.shutdown();
        // 也可以使用这个来配合关闭
        //EXECUTOR_SERVICE.awaitTermination(1, TimeUnit.HOURS);

        //使用CountDownLatch, 阻塞,当CountDownLatch里面的计算器数减为0 时就结束
        countDownLatch.await();
        System.out.println("all of work finish done.");
        EXECUTOR_SERVICE_FIXED.shutdown();
    }


    static  class SimpleRunnable implements Runnable{

        private final int[] data;

        private final int index;
        private final  CountDownLatch countDownLatch;

        SimpleRunnable(int[] data, int index, CountDownLatch countDownLatch){
            this.data = data;
            this.index = index;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(random.nextInt(2000));

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int value = data[index];
            if(value % 2 == 0){
                data[index] = value * 2;
            }else{
                data[index] = value * 10;
            }
            System.out.println(Thread.currentThread().getName()+" finished.");
            //线程执行结束
            countDownLatch.countDown();
        }
    }

    private static  int[] query(){
        return  new int[]{1, 2 ,3, 4, 5, 6, 7, 8, 9, 10};
    }
}

```
37.5 CountDownLatchExample4 测试
```java
package com.learn.concurrenty.juc.untils.countdownlatch;

import com.learn.concurrenty.DefaultThreadFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * @ClassName: CountDownLatchExample1
 * @Description:
 * @Author: lin
 * @Date: 2020/4/8 16:57
 * History:
 * @<version> 1.0
 */
public class CountDownLatchExample4 {
   private static Random random = new Random(System.currentTimeMillis());


   private static ExecutorService EXECUTOR_SERVICE = new ThreadPoolExecutor(5,
            20, 200,
            TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(20),
            new DefaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());

    private static ExecutorService EXECUTOR_SERVICE_FIXED = Executors.newFixedThreadPool(5);

    public static void main(String[] args)  {
         Event[] events = {new Event(1), new Event(2)};
        for (Event event : events) {
            List<Table> tables = capture(event);
            TaskGroup taskGroup = new TaskGroup(tables.size(), event);
            for (Table table : tables) {
                TaskBatch taskBatch = new TaskBatch(taskGroup,2);
                TrustSourceColumns columnsRunnable = new TrustSourceColumns(table, taskBatch);
                TrustSourceRecordCount recordCountRunnable = new TrustSourceRecordCount(table,
                        taskBatch);
                // 每个table 执行结束后,而且所有的记录都在 sourceColumnSchema里面去做了
                // 并且update 只是update了一次
                EXECUTOR_SERVICE.execute(columnsRunnable);
                EXECUTOR_SERVICE.execute(recordCountRunnable);
            }
        }
    }


    static  class Event {
        int id = 0;
        Event(int id){
            this.id = id;
        }

    }

    interface Watcher{
        /**
         * 开始监听
         */
//        void startWatch();

        /**
         * 已经完成
         * @param table
         */
        void done(Table table);
    }

    static class TaskBatch implements Watcher{

        private  CountDownLatch countDownLatch;
        //要知道是那个event ,那么 这里加上 TaskGroup
        private TaskGroup taskGroup;

        public  TaskBatch(TaskGroup taskGroup, int size){
           this.taskGroup = taskGroup;
           this.countDownLatch = new CountDownLatch(size);
        }

//        @Override
//        public void startWatch() {
//
//        }

        @Override
        public void done(Table table) {
          //如果完成一次 计算器就减一次
          countDownLatch.countDown();
          if(countDownLatch.getCount() == 0){
              System.out.println("The table " + table.tableName + " finished work, " +
                      "[" + table+"]");
              //当event做完了,那么就是调用下 group来 看看是那个event
              taskGroup.done(table);
          }
        }
    }


    static class TaskGroup implements Watcher{

        private  CountDownLatch countDownLatch;

        private Event event;

        public  TaskGroup(int size, Event e){
            this.event = e;
            this.countDownLatch = new CountDownLatch(size);
        }

        @Override
        public void done(Table table) {
            countDownLatch.countDown();
            if(countDownLatch.getCount() == 0){
                System.out.println("===========All of  table done in event: " + event.id );
            }
        }
    }


    static  class Table {
        String tableName;
        long sourceRecordCount = 10;
        long targetCount;
        String sourceColumnSchema = "<table name='a'><column name='col1' type='varchar2'/></table>";
        String targetColumnSchema = "";

       public Table(String tableName, long sourceRecordCount){
            this.tableName = tableName;
            this.sourceRecordCount = sourceRecordCount;
        }

        @Override
        public String toString() {
            return "Table{" +
                    "tableName='" + tableName + '\'' +
                    ", sourceRecordCount=" + sourceRecordCount +
                    ", targetCount=" + targetCount +
                    ", sourceColumnSchema='" + sourceColumnSchema + '\'' +
                    ", targetColumnSchema='" + targetColumnSchema + '\'' +
                    '}';
        }
    }

     private static List<Table> capture(Event event){
        List<Table> list = new ArrayList<>();
        int count = 10;
         for (int i = 0; i <count ; i++) {
             list.add(new Table("table-"+event.id+"-" + i, i*1000));
         }
         return list;
     }


    static class  TrustSourceRecordCount implements Runnable{
        private final  Table table;
        private final TaskBatch taskBatch;

        public TrustSourceRecordCount(Table table,TaskBatch taskBatch) {
            this.table = table;
            this.taskBatch = taskBatch;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(random.nextInt(10000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            table.targetCount = table.sourceRecordCount;
            //当任务执行完,那么就调用 TaskBatch中done方法
            // 然后就减一下 计数器, 每一个都要减,
            // 那么就将这个方法放到 TaskBatch中去 进行封装
            taskBatch.done(table);
//            System.out.println("The table " + table.tableName +
//                    " target record count capture done and update the data.");
        }
    }


     static class  TrustSourceColumns implements Runnable{
        private final  Table table;
        private final TaskBatch taskBatch;

         public TrustSourceColumns(Table table, TaskBatch taskBatch) {
             this.table = table;
             this.taskBatch = taskBatch;
         }

         @Override
         public void run() {
             try {
                 Thread.sleep(random.nextInt(10000));
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
             table.targetColumnSchema = table.sourceColumnSchema;
             //当任务执行完,那么就调用 TaskBatch中done方法
             // 然后就减一下 计数器, 每一个都要减,
             // 那么就将这个方法放到 TaskBatch中去 进行封装
             taskBatch.done(table);
//             System.out.println("The table " + table.tableName +
//                     " target columns capture done and update the data.");
         }
     }
}

```
37.6 CyclicBarrierExample1 测试
```java
package com.learn.concurrenty.juc.untils.cyclibarrier;

import com.learn.concurrenty.DefaultThreadFactory;

import java.util.concurrent.*;

/**
 * @ClassName: CylicBarrierExample1
 * @Description:
 * @Author: lin
 * @Date: 2020/4/9 22:18
 * History:
 * @<version> 1.0
 */
public class CyclicBarrierExample1 {
    private static ExecutorService EXECUTOR_SERVICE = new ThreadPoolExecutor(5,
            20, 200,
            TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(20),
            new DefaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());

    public static void main(String[] args) {
        //可以进行回调
        final CyclicBarrier cyclicBarrier = new CyclicBarrier(2, () -> System.out.println("all of finished"));

        EXECUTOR_SERVICE.execute(new MyThread("T1", cyclicBarrier));
        EXECUTOR_SERVICE.execute(new MyThread("T2", cyclicBarrier));
        EXECUTOR_SERVICE.shutdown();

    }


    static class MyThread extends Thread{
        private String name;
        private CyclicBarrier cyclicBarrier;

        MyThread(String name, CyclicBarrier cyclicBarrier){
            this.name = name;
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(20);
                System.out.println("T1 finished");
                cyclicBarrier.await();
                System.out.println("T1 the other thread finished too.");
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

}

```
37.7 CyclicBarrierExample2 测试
```java
package com.learn.concurrenty.juc.untils.cyclibarrier;

import com.learn.concurrenty.DefaultThreadFactory;

import java.util.concurrent.*;

/**
 * @ClassName: CylicBarrierExample1
 * @Description:
 * @Author: lin
 * @Date: 2020/4/9 22:18
 * History:
 * @<version> 1.0
 */
public class CyclicBarrierExample2 {


    public static void main(String[] args) throws InterruptedException {
        final CyclicBarrier cyclicBarrier = new CyclicBarrier(2 );

        new Thread(){
            @Override
            public void run() {
                try {
                    TimeUnit.MILLISECONDS.sleep(5);
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }.start();


        new Thread(){
            @Override
            public void run() {
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        TimeUnit.MILLISECONDS.sleep(6);
        System.out.println(cyclicBarrier.getNumberWaiting());
        System.out.println(cyclicBarrier.getParties());
        System.out.println(cyclicBarrier.isBroken());
        // reset 等价于 initial 等价于finished
        cyclicBarrier.reset();


        System.out.println(cyclicBarrier.getNumberWaiting());
        System.out.println(cyclicBarrier.getParties());
        System.out.println(cyclicBarrier.isBroken());
    }

}


```
37.8 CyclicBarrierExample3 测试
```java
package com.learn.concurrenty.juc.untils.cyclibarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

/**
 * CountDownLatch VS CyclicBarrier
 * 1、CountDownLatch不能rest, 而CyclicBarrier是可以循环使用的
 * 2、CountDownLatch工作线程之间相互不关心， CyclicBarrier工作线程必须等到同一个共同的点才去执行某个动作
 *
 * @ClassName: CyclicBarrierExample1
 * @Description:
 * @Author: lin
 * @Date: 2020/4/9 22:18
 * History:
 * @<version> 1.0
 */
public class CyclicBarrierExample3 {


   static  class MyCountDownLatch extends CountDownLatch {
       private final  Runnable runnable;

       /**
        * Constructs a {@code CountDownLatch} initialized with the given count.
        *
        * @param count the number of times {@link #countDown} must be invoked
        *              before threads can pass through {@link #await}
        * @throws IllegalArgumentException if {@code count} is negative
        */
       public MyCountDownLatch(int count ,Runnable runnable) {
           super(count);
           this.runnable = runnable;
       }

       @Override
       public void countDown() {
           super.countDown();
           if(getCount() == 0){
               this.runnable.run();
           }
       }
   }

    public static void main(String[] args) {
        final  MyCountDownLatch myCountDownLatch = new MyCountDownLatch(2, new Runnable() {
            @Override
            public void run() {
                System.out.println("all of work finish done.");
            }
        });

        new Thread(){
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                myCountDownLatch.countDown();
                System.out.println(Thread.currentThread()+getName() + " finished work");
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                myCountDownLatch.countDown();
                System.out.println(Thread.currentThread()+getName() + " finished work");
            }
        }.start();

    }

}


```
37.9 ExchangerExample1 测试
```java
package com.learn.concurrenty.juc.untils.exchanger;

import java.util.concurrent.Exchanger;

/**
 * @ClassName: ExchangerExample1
 * @Description:
 * @Author: lin
 * @Date: 2020/4/10 8:43
 * History:
 * @<version> 1.0
 */
public class ExchangerExample1 {
    public static void main(String[] args) {
        final Exchanger<String> exchanger = new Exchanger<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " start.");
                try {
                    String result = exchanger.exchange("I am come from T-A");
                    System.out.println(Thread.currentThread().getName() + " Get value [" +result + "]");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " end.");
            }
        }, "=====A=====").start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " start.");
                try {
                    String result = exchanger.exchange("I am come from T-B");
                    System.out.println(Thread.currentThread().getName() + " Get value [" +result + "]");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " end.");
            }
        }, "=====B=====").start();
    }
}

```
37.10 ExchangerExample2
```java
package com.learn.concurrenty.juc.untils.exchanger;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @ClassName: ExchangerExample2
 * @Description:
 * @Author: lin
 * @Date: 2020/4/10 8:43
 * History:
 * @<version> 1.0
 */
public class ExchangerExample2 {
    public static void main(String[] args) {
        final Exchanger<Integer> exchanger1 = new Exchanger<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                AtomicReference<Integer> value = new AtomicReference<>(1);
                try {
                    while (true){
                        value.set(exchanger1.exchange(value.get()));
                        System.out.println("Thread A has Value:"+value.get()+"");
                        TimeUnit.SECONDS.sleep(3);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " end.");
            }
        }, "=====A=====").start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                AtomicReference<Integer> value = new AtomicReference<>(2);
                try {
                    while (true){
                        value.set(exchanger1.exchange(value.get()));
                        System.out.println("Thread B has Value:"+value.get()+"");
                        TimeUnit.SECONDS.sleep(2);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " end.");
            }
        }, "=====A=====").start();
    }
}

```
37.11 ForkJoinTask 使用
```java
package com.learn.concurrenty.juc.untils.forkjoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.IntStream;

/**
 * ForkJoin  简单使用
 * RecursiveTask
 * @ClassName: ForkJoinRecursiveTask
 * @Description:
 * @Author: lin
 * @Date: 2020/4/11 9:41
 * History:
 * @<version> 1.0
 */
public class ForkJoinRecursiveTask {

    private final  static  int MAX_THRESHOLD = 3;

    public static void main(String[] args) {
         final ForkJoinPool forkJoinPool = new ForkJoinPool();
         // 计算 从0~10
        ForkJoinTask<Integer> future = forkJoinPool.submit(new CalculatedRecursiveTask(0, 10));
        try {
            Integer result = future.get();
            System.out.println(result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static class  CalculatedRecursiveTask extends RecursiveTask<Integer>{
        private final int start;
        private final int end;

        CalculatedRecursiveTask(int start, int end){
            this.start = start;
            this.end = end;
        }
        /**
         * The main computation performed by this task.
         *
         * @return the result of the computation
         */
        @Override
        protected Integer compute() {
            if(end - start <= MAX_THRESHOLD){
                //如果这个 区间的数据相减 没有超过这个 MAX_THRESHOLD 范围，那么就不在进行任务拆分了
                return IntStream.rangeClosed(start, end).sum();
            }else {
                // 如果自己忙不过来，那么就将任务拆分 请其他的人帮忙
                int middle = (start + end) / 2;
                // 左边的任务
                CalculatedRecursiveTask leftTask = new CalculatedRecursiveTask(start, middle);
                //右边的任务 从middle +1 开始
                CalculatedRecursiveTask rightTask = new CalculatedRecursiveTask(middle + 1, end);

                leftTask.fork();
                rightTask.fork();
                return  leftTask.join() + rightTask.join();
            }
        }
    }
}

```
37.12 CalculatedRecursiveAction 使用
```java
package com.learn.concurrenty.juc.untils.forkjoin;

import java.util.Optional;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * 实现拿出最终的结果，那么必须有一个统一记录的地方
 * @ClassName: ForkJoinRecursiveAction
 * @Description:
 * @Author: lin
 * @Date: 2020/4/11 10:19
 * History:
 * @<version> 1.0
 */
public class ForkJoinRecursiveAction {
    private final  static  int MAX_THRESHOLD = 3;

    private final  static AtomicInteger SUM = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        final ForkJoinPool forkJoinPool = new ForkJoinPool();
        // 返回是拿不到值 ，因为是void
        forkJoinPool.submit(new CalculatedRecursiveAction(0, 10));

        forkJoinPool.awaitTermination(10, TimeUnit.SECONDS);
        Optional.of(SUM).ifPresent(System.out::println);
    }

    /**
     * RecursiveAction 的compute 方法是没有返回值的，
     * 所以想要有放回值那么就使用一个累加器来处理
     */
    private static class  CalculatedRecursiveAction extends RecursiveAction {
        private final int start;
        private final int end;

        CalculatedRecursiveAction(int start, int end){
            this.start = start;
            this.end = end;
        }
        /**
         * not return
         * The main computation performed by this task.
         */
        @Override
        protected void compute() {
            //将结果放到一个累加器中去
            if(end - start <= MAX_THRESHOLD){
                SUM.addAndGet(IntStream.rangeClosed(start, end).sum());
            }else {
                // 如果自己忙不过来，那么就将任务拆分 请其他的人帮忙
                int middle = (start + end) / 2;
                // 左边的任务
                CalculatedRecursiveAction leftActon = new CalculatedRecursiveAction(start, middle);
                //右边的任务 从middle +1 开始
                CalculatedRecursiveAction rightActon = new CalculatedRecursiveAction(middle + 1, end);

                leftActon.fork();
                rightActon.fork();
            }

        }
    }
}

```
37.13 ReadWriteLockExample
```java
package com.learn.concurrenty.juc.untils.locks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 读写锁
 * @ClassName: ReadWriteLockExample
 * @Description:
 * @Author: lin
 * @Date: 2020/4/10 14:12
 * History:
 * @<version> 1.0
 */
public class ReadWriteLockExample {
    private final static ReentrantLock LOCK = new ReentrantLock(true);
    private final static List<Long> data = new ArrayList<>();

    /**
     * Write Write  x
     * Write Read   x
     * Read  Write  x
     * Read  Read   ok
     *
     * 读写分离锁
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(ReadWriteLockExample::write);
        t1.start();
        TimeUnit.SECONDS.sleep(1);

        Thread t2 = new Thread(ReadWriteLockExample::read);
        t2.start();
        TimeUnit.SECONDS.sleep(1);
    }

    public static void  write(){
        try {
            LOCK.lock();
            data.add(System.currentTimeMillis());
            //为了模拟写数据比较耗时，那么就让其睡 几秒
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            LOCK.unlock();
        }
    }

    public static void  read(){
        try {
            LOCK.lock();
            data.forEach(System.out::println);
            //为了模拟写数据比较耗时，那么就让其睡 几秒
            // 同样读数据也是一样，让其睡几秒
            TimeUnit.SECONDS.sleep(5);
            System.out.println(Thread.currentThread().getName() + "============");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            LOCK.unlock();
        }
    }

}

```
37.14 ReadWriteLockExample1
```java
package com.learn.concurrenty.juc.untils.locks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁
 * @ClassName: ReadWriteLockExample
 * @Description:
 * @Author: lin
 * @Date: 2020/4/10 14:12
 * History:
 * @<version> 1.0
 */
public class ReadWriteLockExample1 {
    private final static ReentrantReadWriteLock READ_WRITE_LOCK = new ReentrantReadWriteLock(true);
    private final static Lock READ_LOCK = READ_WRITE_LOCK.readLock();
    private final static Lock WRITE_LOCK = READ_WRITE_LOCK.writeLock();
    private final static List<Long> data = new ArrayList<>();

    /**
     * Write Write  x
     * Write Read   x
     * Read  Write  x
     * Read  Read   ok
     *
     * @param args
     */
    public static void main(String[] args) {

    }

    public static void  write(){
        try {
            WRITE_LOCK.lock();
            data.add(System.currentTimeMillis());
            //为了模拟写数据比较耗时，那么就让其睡 几秒
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            WRITE_LOCK.unlock();
        }
    }

    public static void  read(){
        try {
            READ_LOCK.lock();
            data.forEach(System.out::println);
            //为了模拟写数据比较耗时，那么就让其睡 几秒
            // 同样读数据也是一样，让其睡几秒
            TimeUnit.SECONDS.sleep(5);
            System.out.println(Thread.currentThread().getName() + "============");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            READ_LOCK.unlock();
        }
    }

}

```
37.14 ReentrantLockExample 使用
```java
package com.learn.concurrenty.juc.untils.locks;


import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName: ReentrantLockExample
 * @Description:
 * @Author: lin
 * @Date: 2020/4/10 11:12
 * History:
 * @<version> 1.0
 */
public class ReentrantLockExample {
    private static final ReentrantLock lock = new ReentrantLock();
    public static void main(String[] args) throws InterruptedException {
//        IntStream.range(0, 2).forEach(i ->
//                new Thread(ReentrantLockExample::needLock).start());

//        Thread t1 = new Thread(ReentrantLockExample::testUnlockInterrupt);
//        t1.start();
//        TimeUnit.SECONDS.sleep(1);
//
//        Thread t2 = new Thread(ReentrantLockExample::testUnlockInterrupt);
//        t2.start();
//        TimeUnit.SECONDS.sleep(1);
//
//        t2.interrupt();
//        System.out.println("==================");

//        Thread t1 = new Thread(ReentrantLockExample::testTryLock);
//        t1.start();
//        TimeUnit.SECONDS.sleep(1);
//
//        Thread t2 = new Thread(ReentrantLockExample::testTryLock);
//        t2.start();

         Thread t1 = new Thread(ReentrantLockExample::testUnlockInterrupt);
         t1.start();
         TimeUnit.SECONDS.sleep(1);

         Thread t2 = new Thread(ReentrantLockExample::testUnlockInterrupt);
         t2.start();
         TimeUnit.SECONDS.sleep(1);
         Optional.of(lock.getQueueLength()).ifPresent(System.out::println);
         Optional.of(lock.hasQueuedThreads()).ifPresent(System.out::println);
         //这个方法表示 那个线程被放到了 阻塞队列中去
         Optional.of(lock.hasQueuedThread(t2)).ifPresent(System.out::println);
         Optional.of(lock.hasQueuedThread(t1)).ifPresent(System.out::println);
    }


    public static void testTryLock(){
        //尝试获取锁，如果没有获取到锁，那么就退出锁的竞争了
        if(lock.tryLock()){
            try {
                Optional.of("The thread-" + Thread.currentThread().getName()
                        + " get lock and will do working")
                        .ifPresent(System.out::println);
                while (true){}
            }finally {
                lock.unlock();
            }
        }
        else {
            Optional.of("The thread-" + Thread.currentThread().getName()
                    + " not get lock. ")
                    .ifPresent(System.out::println);
        }

    }


    public static void testUnlockInterrupt(){
        try {
            // 抢锁，如果抢不到锁那么其他线程可以被其他线程打断。
            lock.lockInterruptibly();
            Optional.of(Thread.currentThread().getName() + ":" + lock.getHoldCount())
            .ifPresent(System.out::println);
            Optional.of("The thread-" + Thread.currentThread().getName()
                    + " get lock and will do working")
                    .ifPresent(System.out::println);
           while (true){
           }
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }


    public  static  void needLock(){
        lock.lock();
        try {
            Optional.of("The thread-" + Thread.currentThread().getName() + " get lock and will do working")
            .ifPresent(System.out::println);
            TimeUnit.SECONDS.sleep(10);
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    public static void  needLockBySync(){
        synchronized (ReentrantLockExample.class){
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

```
37.15 StampedLockExample 使用
```java
package com.learn.concurrenty.juc.untils.locks;

import com.learn.concurrenty.DefaultThreadFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.locks.StampedLock;
import java.util.stream.Collectors;

/**
 * @ClassName: StampedLockExample
 * @Description:
 * @Author: lin
 * @Date: 2020/4/10 16:43
 * History:
 * @<version> 1.0
 */
public class StampedLockExample {
    private static final StampedLock lock = new StampedLock();
    private final static List<Long> DATA = new ArrayList<>();
    private static ExecutorService executor =new ThreadPoolExecutor(5,
            20, 200,
            TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(120),
            new DefaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
    public static void main(String[] args) {

       Runnable readTask = ()->{
            for (;;){
                read();
            }
        };

        Runnable writeTask = ()->{
            for (;;){
                writed();
            }
        };

        executor.submit(readTask);
        executor.submit(readTask);
        executor.submit(readTask);
        executor.submit(readTask);
        executor.submit(readTask);
        executor.submit(readTask);
        executor.submit(readTask);
        executor.submit(readTask);
        executor.submit(writeTask);
    }


    /**
     * 悲观的去读
     */
    private static void read(){
        long stamped = -1;
        try {
            stamped = lock.readLock();
            Optional.of(
            DATA.stream().map(String::valueOf).
                    collect(Collectors.joining("#","R-",""))
            ).ifPresent(System.out::println);
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
           lock.unlockRead(stamped);
        }
    }

    private static void writed(){
        long stamped = -1;
        try {
            stamped = lock.writeLock();
             DATA.add(System.currentTimeMillis());
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlockWrite(stamped);
        }
    }

}

```
37.16 StampedLockExample2 使用
```java
package com.learn.concurrenty.juc.untils.locks;

import com.learn.concurrenty.DefaultThreadFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;
import java.util.stream.Collectors;

/**
 * @ClassName: StampedLockExample
 * @Description:
 * @Author: lin
 * @Date: 2020/4/10 16:43
 * History:
 * @<version> 1.0
 */
public class StampedLockExample2 {
    private static final StampedLock lock = new StampedLock();
    private final static List<Long> DATA = new ArrayList<>();
    private static ExecutorService executor =new ThreadPoolExecutor(5,
            20, 200,
            TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(120),
            new DefaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
    public static void main(String[] args) {

       Runnable readTask = ()->{
            for (;;){
                read();
            }
        };

        Runnable writeTask = ()->{
            for (;;){
                writed();
            }
        };

        executor.submit(readTask);
        executor.submit(readTask);
        executor.submit(readTask);
        executor.submit(readTask);
        executor.submit(readTask);
        executor.submit(readTask);
        executor.submit(readTask);
        executor.submit(readTask);
        executor.submit(writeTask);
    }


    /**
     * 乐观的去读
     */
    private static void read(){
        // 这是一个乐观锁，他这个拿的时候是不会被阻塞住的。
        // 这个是一个戳，如果有改变那么就会判断
        long stamped = lock.tryOptimisticRead();
        // 检查是否在修改
        if(lock.validate(stamped)){
            try {
                stamped = lock.readLock();
                System.out.println(stamped);
                Optional.of(
                        DATA.stream().map(String::valueOf).collect(Collectors.joining("#","R-",""))
                ).ifPresent(System.out::println);
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlockRead(stamped);
            }
        }

    }

    private static void writed(){
        long stamped = -1;
        try {
            stamped = lock.writeLock();
             DATA.add(System.currentTimeMillis());
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlockWrite(stamped);
        }
    }
}

```
38.1 Phaser 使用
```java
package com.learn.concurrenty.juc.untils.phaser;

import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @ClassName: PhaserExample
 * @Description:
 * @Author: lin
 * @Date: 2020/4/11 11:16
 * History:
 * @<version> 1.0
 */
public class PhaserExample {
    private final static Random RANDOM = new Random(System.currentTimeMillis());

    public static void main(String[] args) {
        final Phaser phaser = new Phaser();
        IntStream.rangeClosed(1, 5).boxed().map(i -> phaser).forEach(Task::new);

        // 将main线程 也注册进去
        phaser.register();
        //让main线程也 到达等待前行
        phaser.arriveAndAwaitAdvance();
        System.out.println("all of worker finished the task.");
    }


    static  class Task extends  Thread{
        private final Phaser phaser;

        Task(Phaser phaser){
            this.phaser = phaser;
            //在运行的时候 动态的去加，这个就不像CyclicBarrier一样 构造时候必须放进去
            this.phaser.register();
            start();
        }

        @Override
        public void run() {
            System.out.println("The Worker ["+ getName() +"] is working....");

            try {
                TimeUnit.SECONDS.sleep(RANDOM.nextInt(5));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            phaser.arriveAndAwaitAdvance();
        }
    }
}

```
38.2 PhaserExample2 使用
```java
package com.learn.concurrenty.juc.untils.phaser;

import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: PhaserExample2
 * @Description:
 * @Author: lin
 * @Date: 2020/4/11 11:16
 * History:
 * @<version> 1.0
 */
public class PhaserExample2 {
    private final static Random RANDOM = new Random(System.currentTimeMillis());

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        final Phaser phaser = new Phaser(5);
        int count = 6;
        //从1号运动员开始
        for (int i = 1; i < count; i++) {
            new Athletes(i, phaser).start();
        }
    }

    /**
     * 比如运动员
     * 三项比赛
     */
    static  class Athletes extends  Thread{
        //几号运动员
        private final int  no;
        private final Phaser phaser;

        Athletes(int no, Phaser phaser){
            this.no = no;
            this.phaser = phaser;
        }

        @Override
        public void run() {
            try {
                System.out.println(no + ": start running....");
                TimeUnit.SECONDS.sleep(RANDOM.nextInt(5));
                System.out.println(no + ": end   running....");
                System.out.println("getPhase()==>" + phaser.getPhase());
                phaser.arriveAndAwaitAdvance();


                System.out.println(no + ": start bicycle....");
                TimeUnit.SECONDS.sleep(RANDOM.nextInt(5));
                System.out.println(no + ": end   bicycle....");
                System.out.println("getPhase()==>" + phaser.getPhase());
                phaser.arriveAndAwaitAdvance();

                System.out.println(no + ": start jump....");
                TimeUnit.SECONDS.sleep(RANDOM.nextInt(5));
                System.out.println(no + ": end   jump....");
                System.out.println("getPhase()==>" + phaser.getPhase());
                phaser.arriveAndAwaitAdvance();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

```
38.3 PhaserExample3 使用
```java
package com.learn.concurrenty.juc.untils.phaser;

import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: PhaserExample3
 * @Description:
 * @Author: lin
 * @Date: 2020/4/11 11:16
 * History:
 * @<version> 1.0
 */
public class PhaserExample3 {
    private final static Random RANDOM = new Random(System.currentTimeMillis());

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        // 动态的增加或者减少
        final Phaser phaser = new Phaser(5);
        int count = 5;
        //从1号运动员开始
        for (int i = 1; i < count; i++) {
            new Athletes(i, phaser).start();
        }
        // 有点运动员在 运动中受伤，那么就不能继续比赛了
        new InjureAthletes(5, phaser).start();
    }

    /**
     * 比如运动员
     * 三项比赛
     */
    static  class Athletes extends  Thread{
        //几号运动员
        private final int  no;
        private final Phaser phaser;

        Athletes(int no, Phaser phaser){
            this.no = no;
            this.phaser = phaser;
        }

        @Override
        public void run() {
            try {
                sport(phaser, no + ": start running....", no + ": end   running....");

                sport(phaser, no + ": start bicycle....", no + ": end   bicycle....");

                sport(phaser, no + ": start jump....", no + ": end   jump....");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * 运动员 有的在中途中受伤 不能继续比赛了
     */
    static  class InjureAthletes extends  Thread{
        //几号运动员
        private final int  no;
        private final Phaser phaser;

        InjureAthletes(int no, Phaser phaser){
            this.no = no;
            this.phaser = phaser;
        }

        @Override
        public void run() {
            try {
                sport(phaser, no + ": start running....", no + ": end   running....");

                sport(phaser, no + ": start bicycle....", no + ": end   bicycle....");

                System.out.println("oh shit, I am injured, i will be exited");
                // 退出注册
                phaser.arriveAndDeregister();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private  static void sport(Phaser phaser, String x, String y) throws InterruptedException {
        System.out.println(x);
        TimeUnit.SECONDS.sleep(RANDOM.nextInt(5));
        System.out.println(y);
        phaser.arriveAndAwaitAdvance();
    }
}

```
38.4 PhaserExample4 使用
```java
package com.learn.concurrenty.juc.untils.phaser;

import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: PhaserExample3
 * @Description:
 * @Author: lin
 * @Date: 2020/4/11 11:16
 * History:
 * @<version> 1.0
 */
public class PhaserExample4 {
    private final static Random RANDOM = new Random(System.currentTimeMillis());

    /**
     *
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
        // 动态的增加或者减少
//        final Phaser phaser = new Phaser(1);
        /*  System.out.println(phaser.getPhase());
        phaser.arriveAndAwaitAdvance();
        System.out.println(phaser.getPhase());

        phaser.arriveAndAwaitAdvance();
        System.out.println(phaser.getPhase());

        phaser.arriveAndAwaitAdvance();
        System.out.println(phaser.getPhase());*/

         /* System.out.println(phaser.getRegisteredParties());
          phaser.register();
          System.out.println(phaser.getRegisteredParties());
          phaser.register();
          System.out.println(phaser.getRegisteredParties());*/

         /*System.out.println(phaser.getArrivedParties());
         System.out.println(phaser.getUnarrivedParties());*/


         // 批量register
         /*phaser.bulkRegister(10);
         System.out.println(phaser.getRegisteredParties());
         System.out.println(phaser.getArrivedParties());
         System.out.println(phaser.getUnarrivedParties());
         new Thread(phaser::arriveAndAwaitAdvance).start();
         TimeUnit.SECONDS.sleep(1);
         System.out.println("======================");
         System.out.println(phaser.getRegisteredParties());
         System.out.println(phaser.getArrivedParties());
         System.out.println(phaser.getUnarrivedParties());
         */




        // 动态的增加或者减少
        final Phaser phaser = new Phaser(2){
            /**
             */
            @Override
            protected boolean onAdvance(int phase, int registeredParties) {
                return true;
            }
        };
        new OnAdvanceTask("Alex", phaser).start();
        new OnAdvanceTask("jack", phaser).start();
        TimeUnit.SECONDS.sleep(2);
        System.out.println(phaser.getUnarrivedParties());
        System.out.println(phaser.getArrivedParties());
    }

    /**
     * 比如运动员
     * 三项比赛
     */
    static  class OnAdvanceTask extends  Thread{
        private final Phaser phaser;

        OnAdvanceTask(String name, Phaser phaser){
            super(name);
            this.phaser = phaser;
        }

        @Override
        public void run() {
          System.out.println(getName() + " I am start and the phase" + phaser.getPhase());
          phaser.arriveAndAwaitAdvance();
          System.out.println(getName() + " I am end!");

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if("Alex".equals(getName())){
                System.out.println(getName() + " I am start and the phase" + phaser.getPhase());
                phaser.arriveAndAwaitAdvance();
                System.out.println(getName() + " I am end!");
            }
        }

    }
}

```
38.5 PhaserExample5 
```java
package com.learn.concurrenty.juc.untils.phaser;

import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @ClassName: PhaserExample5
 * @Description:
 * @Author: lin
 * @Date: 2020/4/11 11:16
 * History:
 * @<version> 1.0
 */
public class PhaserExample5 {

    /**
     *
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
        // 动态的增加或者减少
        final Phaser phaser = new Phaser(7);

        // 这里只有6个线程去做，但是 有7个所以 就会造成block
        IntStream.rangeClosed(0, 5).boxed().map(i->phaser).forEach(AwaitAdvanceTask::new);
        // 如果结束了就会从block中退出来，如果没有结束那么就会block住
        phaser.awaitAdvance(phaser.getPhase());
        System.out.println("======================");

    }

    private static  class  AwaitAdvanceTask extends Thread{

        private final Phaser phaser;

        private  AwaitAdvanceTask(Phaser phaser){
            this.phaser = phaser;
        }

        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            phaser.arriveAndAwaitAdvance();
            System.out.println(getName() + " finished work.");
        }
    }

}

```
38.6 PhaserExample6 测试
```java
package com.learn.concurrenty.juc.untils.phaser;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName: PhaserExample6
 * @Description:
 * @Author: lin
 * @Date: 2020/4/11 11:16
 * History:
 * @<version> 1.0
 */
public class PhaserExample6 {

    /**
     *
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
        // 动态的增加或者减少


//        final Phaser phaser = new Phaser(7);
        /*Thread thread = new Thread(()->{
            try {
                // 这里会一直等，只有别人调用了interrupt 将其打断才会退出
                phaser.awaitAdvanceInterruptibly(phaser.getPhase());
                System.out.println("***********************");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread.start();

        System.out.println("======================");
        TimeUnit.SECONDS.sleep(10);
        thread.interrupt();
        System.out.println("=======thread.interrupt===============");*/


        //=========================================
        /*
        final Phaser phaser = new Phaser(7);
        Thread thread = new Thread(()->{
            try {
                // 这里会一直等，只有别人调用了interrupt 将其打断才会退出
                phaser.awaitAdvanceInterruptibly(10);
                System.out.println("***********************");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread.start();

        System.out.println("======================");
        TimeUnit.SECONDS.sleep(10);
        thread.interrupt();
        System.out.println("=======thread.interrupt===============");*/


        final Phaser phaser = new Phaser(3);
        Thread thread = new Thread(()->{
            try {
                // 最多等待10秒钟，如果十秒钟没有完成，那么就不等了
                phaser.awaitAdvanceInterruptibly(0, 10 ,TimeUnit.SECONDS);
                System.out.println("***********************");
            } catch (InterruptedException | TimeoutException e) {
                e.printStackTrace();
            }
        });

        thread.start();

    }

    private static  class  AwaitAdvanceTask extends Thread{

        private final Phaser phaser;

        private  AwaitAdvanceTask(Phaser phaser){
            this.phaser = phaser;
        }

        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            phaser.arriveAndAwaitAdvance();
            System.out.println(getName() + " finished work.");
        }
    }

}

```
38.7 PhaserExample7 测试
```java
package com.learn.concurrenty.juc.untils.phaser;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName: PhaserExample3
 * @Description:
 * @Author: lin
 * @Date: 2020/4/11 11:16
 * History:
 * @<version> 1.0
 */
public class PhaserExample7 {

    /**
     *
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
        final Phaser phaser = new Phaser(3);
        new Thread(phaser::arriveAndAwaitAdvance).start();

        TimeUnit.SECONDS.sleep(3);
        System.out.println(phaser.isTerminated());

        // 销毁 不会再等待
        phaser.forceTermination();
        System.out.println(phaser.isTerminated());
    }

}

```
39.1 SemaphoreExample 测试
```java
package com.learn.concurrenty.juc.untils.semaphore;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: SemaphoreExample
 * @Description:
 * @Author: lin
 * @Date: 2020/4/10 9:30
 * History:
 * @<version> 1.0
 */
public class SemaphoreExample {
    public static void main(String[] args) {
         final  SemaphoreLock lock = new SemaphoreLock();
         new Thread(){
             @Override
             public void run() {
                 try {
                     System.out.println(Thread.currentThread().getName()+" is running");
                     lock.lock();
                     System.out.println(Thread.currentThread().getName()+" get the #SemaphoreLock");
                     TimeUnit.SECONDS.sleep(10);
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }finally {
                     lock.unlock();
                 }
                 System.out.println(Thread.currentThread().getName()+" Released #SemaphoreLock");
             }
         }.start();
    }

    static class  SemaphoreLock{
        private final Semaphore semaphore = new Semaphore(1);

        public  void lock() throws InterruptedException {
            //申请一个许可证
            semaphore.acquire();
        }

        public void unlock(){
            semaphore.release();
        }
    }
}

```
39.2 SemaphoreExample2
```java
package com.learn.concurrenty.juc.untils.semaphore;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: SemaphoreExample
 * @Description:
 * @Author: lin
 * @Date: 2020/4/10 9:30
 * History:
 * @<version> 1.0
 */
public class SemaphoreExample2 {
    public static void main(String[] args) {
         final  Semaphore semaphore = new Semaphore(1);
         int count =2;
        for (int i = 0; i <count ; i++) {
            new Thread(){
                @Override
                public void run() {
                    try {
                        System.out.println(Thread.currentThread().getName()+" in");
                        semaphore.acquire();
                        System.out.println(Thread.currentThread().getName()+" Get the #Semaphore");
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }finally {
                        semaphore.release();
                    }
                    System.out.println(Thread.currentThread().getName()+" out");
                }
            }.start();
        }
    }

}

```
39.3 SemaphoreExample3
```java
package com.learn.concurrenty.juc.untils.semaphore;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 *
 * @ClassName: SemaphoreExample
 * @Description:
 * @Author: lin
 * @Date: 2020/4/10 9:30
 * History:
 * @<version> 1.0
 */
public class SemaphoreExample3 {
    public static void main(String[] args) throws InterruptedException {
         final  Semaphore semaphore = new Semaphore(1);
        Thread t1 = new Thread() {
            @Override
            public void run() {
                try {
                    semaphore.acquire();
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
                System.out.println("T1 finished");
            }
        };
        t1.start();

        TimeUnit.MICROSECONDS.sleep(50);

        // 因为只有一个 许可证, 被线程t1拿到了，所以去拿取拿不到
        // 如果拿取不到 那么想放弃去拿取 怎么办喃。
        Thread t2 = new Thread() {
            @Override
            public void run() {
                try {
                    semaphore.acquire();
                    // 这个方法没有中断，也是说你中断它，他也不会有什么提示
                    //semaphore.acquireUninterruptibly();
//                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
                System.out.println("T2 finished");
            }
        };
        t2.start();

        TimeUnit.MICROSECONDS.sleep(50);
        // 如果拿取不到那么就中断下
        t2.interrupt();

    }
}

```
39.4 SemaphoreExample4
```java
package com.learn.concurrenty.juc.untils.semaphore;

import java.util.Collection;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 *
 * @ClassName: SemaphoreExample
 * @Description:
 * @Author: lin
 * @Date: 2020/4/10 9:30
 * History:
 * @<version> 1.0
 */
public class SemaphoreExample4 {
    public static void main(String[] args) throws InterruptedException {
//        final  Semaphore semaphore = new Semaphore(5);
        final  MySemaphore semaphore = new MySemaphore(5);
        Thread t1 = new Thread() {
            @Override
            public void run() {
                try {
                    // 这个方法的意思 是获取完许可证
                    semaphore.drainPermits();
                    System.out.println(semaphore.availablePermits());
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release(5);
                }
                System.out.println("T1 finished");
            }
        };
        t1.start();

        TimeUnit.MICROSECONDS.sleep(1);


        Thread t2 = new Thread() {
            @Override
            public void run() {
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
                System.out.println("T2 finished");
            }
        };
        t2.start();

        TimeUnit.SECONDS.sleep(1);
        System.out.println("===" +semaphore.hasQueuedThreads());
        Collection<Thread> waitingThread = semaphore.getWaitingThreads();
        for (Thread t : waitingThread) {
            System.out.println("waitingThread========="+t);
        }
    }


    static class MySemaphore extends Semaphore{

        public MySemaphore(int permits) {
            super(permits);
        }

        public MySemaphore(int permits, boolean fair) {
            super(permits, fair);
        }

        public Collection<Thread> getWaitingThreads(){
            return super.getQueuedThreads();
        }
    }

}

```
40. ThreadPoolExecutorBuild
```java
package com.learn.concurrenty.juc.untils.threadpool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: ThreadPoolExecutorBuild
 * @Description:
 * @Author: lin
 * @Date: 2020/4/12 22:30
 * History:
 * @<version> 1.0
 */
public class ThreadPoolExecutorBuild {
    public static void main(String[] args) {
        ThreadPoolExecutor executorService = (ThreadPoolExecutor) buildThreadPoolExecutor();
        int activeCount = -1;
        int queueSize = -1;
        while (true){
            if(activeCount != executorService.getActiveCount() || queueSize!=executorService.getQueue().size()) {
                System.out.println(executorService.getActiveCount());
                System.out.println(executorService.getCorePoolSize());
                System.out.println(executorService.getQueue());
                System.out.println(executorService.getMaximumPoolSize());
                activeCount = executorService.getActiveCount();
                queueSize = executorService.getQueue().size();
                System.out.println("==================================");
            }
        }
    }

    /**
     * Test point.
     *
     * 1.coreSize=1, MaxSize=2 blockingQueue is empty. what happen when submit 3 task?
     * 2.coreSize=1, MaxSize=2 blockingQueue size=5 what happen when submit 7 task?
     * 3.coreSize=1, MaxSize=2 blockingQueue size=5 what happen when submit 8 task?
     *
     * int corePoolSize,
     * int maximumPoolSize,
     * long keepAliveTime,
     * TimeUnit unit,
     * BlockingQueue<Runnable> workQueue,
     * ThreadFactory threadFactory,
     * RejectedExecutionHandler handler
     */
    private  static  ExecutorService  buildThreadPoolExecutor(){
        ExecutorService executorService = new ThreadPoolExecutor(1, 2, 30, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1), r->{
            Thread t = new Thread(r);
            return t;
        }, new ThreadPoolExecutor.AbortPolicy());
        System.out.println("The ThreadPoolExecutor create done.");

        executorService.execute(()->sleepSeconds(100));
        executorService.execute(()->sleepSeconds(10));
        executorService.execute(()->sleepSeconds(100));
        return  executorService;
    }

    private  static  void sleepSeconds(long seconds){
        try {
            System.out.println("* " + Thread.currentThread().getName() + " *");
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

```
40.1 ThreadPoolExecutorTask
```java
package com.learn.concurrenty.juc.untils.threadpool;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @ClassName: ThreadPoolExecutorTask
 * @Description:
 * @Author: lin
 * @Date: 2020/4/12 22:30
 * History:
 * @<version> 1.0
 */
public class ThreadPoolExecutorTask {
    public static void main(String[] args) {
        ExecutorService executorService = new ThreadPoolExecutor(10, 20, 30, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(10), r->{
            Thread t = new Thread(r);
            return t;
        }, new ThreadPoolExecutor.AbortPolicy());

        IntStream.range(0, 20).boxed().forEach(i -> executorService.execute(()->{
            try {
                TimeUnit.SECONDS.sleep(10);
                System.out.println(Thread.currentThread().getName() + " [" + i + "] finish done.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));

        List<Runnable> runnableList = null;
        try {
            runnableList = executorService.shutdownNow();
            System.out.println("===================over======================");
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("======================over==========================");
        System.out.println(runnableList);
        System.out.println(runnableList.size());

        /**
         * shutdown
         * 20 threads
         *    10 threads work
         *    10 idle(空闲)
         *
         * showdown invoked
         * 1. 10 waiting to finished the work
         * 2. 10 interrupt the idle works
         * 3. 20 idle threads will exist
         *
         * shutdownNow
         * 10  thread queue elements 10
         * 10  running
         * 10  stored in the blocking queue
         *
         * 1.return list<Runnable> remain 10 un handle runnable
         *
         * 2.interrupted all of threads in the pool
         */
    }

}

```
41.1 线程池相关 CompletableFutureExample1
```java
package com.learn.concurrenty.juc.untils.executors;

import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * @ClassName: CompletableFutureExample1
 * @Description:
 * @Author: lin
 * @Date: 2020/4/15 16:15
 * History:
 * @<version> 1.0
 */
public class CompletableFutureExample1 {
    public static void main(String[] args) throws InterruptedException {

        // 因为这里写在了main方法里， 并且CompletableFuture
        // 里面会将其设置为守护线程.
       /* CompletableFuture.runAsync(()->{
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 执行完返回结果
            // 注册一个callback
        }).whenComplete((v, t) -> System.out.println("DONE"));
        System.out.println("=========i am not blocked=============");*/



        ExecutorService executorService = Executors.newFixedThreadPool(10);
        // 一个阶段 一个阶段的执行
        // 定义了一组callable
       /* List<Callable<Integer>> tasks = IntStream.range(0, 10).boxed()
                .map(i -> (Callable<Integer>) () -> get()).collect(Collectors.toList());
        //executorService 提交了一批的 callable 然后将结果拿出来
        executorService.invokeAll(tasks).stream().map(future ->{
            try {
                // future 分别将值提取出来
               return future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw  new RuntimeException(e);
            }
        }).parallel().forEach(CompletableFutureExample1::display);*/


        /**
         * 使用CompletableFuture
         * CompletableFuture 是future和 ExecutorService的结合
         *
         */
        IntStream.range(0, 10).boxed()
                // 执行是交替执行, 不想上面的一批执行完后再执行下一批
                // 并行的执行
                .forEach(i -> CompletableFuture.supplyAsync(CompletableFutureExample1::get)
                .thenAccept(CompletableFutureExample1::display)
                //执行结束之后
                .whenComplete((v, t) -> System.out.println(i + " DONE")));
        Thread.currentThread().join();
    }

    private static  void display(int data ){
        int value = ThreadLocalRandom.current().nextInt(20);
        try {
            System.out.println(Thread.currentThread().getName() + " display will be sleep " + value);
            TimeUnit.SECONDS.sleep(value);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() +
                " display execute done " + data);
    }



    private static  int get(){
        int value = ThreadLocalRandom.current().nextInt(20);
        try {
            System.out.println(Thread.currentThread().getName() + " will be sleep " + value);
            TimeUnit.SECONDS.sleep(value);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " execute done " + value);
        return  value;
    }

}

```
41.2  CompletableFutureExample2 学习
```java
package com.learn.concurrenty.juc.untils.executors;

import java.util.concurrent.*;
import java.util.function.Supplier;
import java.util.stream.IntStream;

/**
 * @ClassName: CompletableFutureExample1
 * @Description:
 * @Author: lin
 * @Date: 2020/4/15 16:15
 * History:
 * @<version> 1.0
 */
public class CompletableFutureExample2 {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
       //supplyAsync();
       // hello 没有输出 是因为 线程是守护线程 , 所以使用join来等待期线程执行后
       //Thread.currentThread().join();

        //=====
        //Future<?> future = runAsync();
        //future.get();

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>"+anyOf().get());
        Thread.currentThread().join();
    }




    /**
     * {@link CompletableFuture#allOf(CompletableFuture[])}
     * @param
     * @return
     */
    private static  void allOf(){
         CompletableFuture.allOf(CompletableFuture.runAsync(()->{
                    try {
                        System.out.println("1======Start");
                        TimeUnit.SECONDS.sleep(5);
                        System.out.println("1=========end");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).whenComplete((v, t) -> System.out.println("===============over===============")),
                CompletableFuture.supplyAsync(()->{
                    try {
                        System.out.println("2======Start");
                        TimeUnit.SECONDS.sleep(4);
                        System.out.println("2=========end");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return  "Hello";
                }).whenComplete((v, t) -> System.out.println(v+ "===============over==============="))

        );
    }


    /**
     * {@link CompletableFuture#anyOf(CompletableFuture[])}
     * @param
     * @return
     */
    private static  Future<?> anyOf(){
         return CompletableFuture.anyOf(CompletableFuture.runAsync(()->{
            try {
                System.out.println("1======Start");
                TimeUnit.SECONDS.sleep(5);
                System.out.println("1=========end");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).whenComplete((v, t) -> System.out.println("===============over===============")),
          CompletableFuture.supplyAsync(()->{
              try {
                  System.out.println("2======Start");
                  TimeUnit.SECONDS.sleep(4);
                  System.out.println("2=========end");
              } catch (InterruptedException e) {
                  e.printStackTrace();
              }
              return  "Hello";
          }).whenComplete((v, t) -> System.out.println(v+ "===============over==============="))

          );
    }


    /**
     * {@link CompletableFuture#completedFuture(Object)}
     * @param data
     * @return
     */
    private static  Future<Void> completed(String data){
        return  CompletableFuture.completedFuture(data).thenAccept(System.out::println);
    }


    /**
     * {@link CompletableFuture#supplyAsync(Supplier)}
     * @return
     */
    private static  Future<?> runAsync(){
      return  CompletableFuture.supplyAsync(Object::new)
            .thenAccept(o -> {
                try {
                    System.out.println("Obj======Start");
                    TimeUnit.SECONDS.sleep(5);
                    System.out.println("Obj========="+o);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).whenComplete((v, t) -> System.out.println("===============over==============="));
    }


    /**
     * {@link CompletableFuture#supplyAsync(Supplier)}
     */
    private static  void supplyAsync(){
        CompletableFuture.supplyAsync(Object::new)
                .thenAccept(o -> {
                    try {
                        System.out.println("Obj======Start");
                        TimeUnit.SECONDS.sleep(5);
                        System.out.println("Obj========="+o);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // 输出完了之后 返回的是void类型
                }).runAfterBoth(CompletableFuture.supplyAsync(()->"Hello")
                .thenAccept(s->{
                    try {
                        System.out.println("String======Start");
                        TimeUnit.SECONDS.sleep(3);
                        System.out.println("String========="+s);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }),()->System.out.print("=======finished======")
        );
    }
}

```
41.3  CompletableFutureExample3 学习
```java
package com.learn.concurrenty.juc.untils.executors;

import java.util.concurrent.*;
import java.util.function.*;

/**
 * @ClassName: CompletableFutureExample1
 * @Description:
 * @Author: lin
 * @Date: 2020/4/15 16:15
 * History:
 * @<version> 1.0
 */
public class CompletableFutureExample3 {
    public static void main(String[] args) throws InterruptedException, ExecutionException {

    }




    /**
     * {@link CompletableFuture#thenCompose(Function)}
     */
    private static  void thenCompose(){
        CompletableFuture.supplyAsync(()->{
            try {
                System.out.println("start the compose1");
                TimeUnit.SECONDS.sleep(5);
                System.out.println("end the compose1");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return  "compose-1";
            // 上一个的输出作为下一个的输入
        }).thenCompose(s-> CompletableFuture.supplyAsync(()->{
            try {
                System.out.println("start the compose2");
                TimeUnit.SECONDS.sleep(3);
                System.out.println("end the compose2");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return  s.length();
        }));

    }



    /**
     * {@link CompletableFuture#thenCombine(CompletionStage, BiFunction)}
     */
    private static  void thenCombine(){
        CompletableFuture.supplyAsync(()->{
            try {
                System.out.println("start the combine1");
                TimeUnit.SECONDS.sleep(5);
                System.out.println("end the combine1");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return  "combine-1";
            // 组合
        }).thenCombine(CompletableFuture.supplyAsync(()->{
            try {
                System.out.println("start the combine2");
                TimeUnit.SECONDS.sleep(3);
                System.out.println("end the combine2");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return  100;
        }),(s, i) -> s.length() > i).whenComplete((v, t) -> System.out.println(v));

    }



    /**
     * {@link CompletableFuture#acceptEither(CompletionStage, Consumer)}
     */
    private static  void acceptEither(){
        CompletableFuture.supplyAsync(()->{
            try {
                System.out.println("start the acceptEither1");
                TimeUnit.SECONDS.sleep(5);
                System.out.println("end the acceptEither1");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return  "acceptEither-1";
            // 组合
        }).acceptEither(CompletableFuture.supplyAsync(()->{
            try {
                System.out.println("start the acceptEither2");
                TimeUnit.SECONDS.sleep(3);
                System.out.println("end the acceptEither2");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return  "acceptEither-2";
        }),System.out::println);

    }


    /**
     * {@link CompletableFuture#thenAcceptBoth(CompletionStage, BiConsumer)}
     */
    private static  void thenAcceptBoth(){
        CompletableFuture.supplyAsync(()->{
                    try {
                        System.out.println("start the supplyAsync");
                        TimeUnit.SECONDS.sleep(5);
                        System.out.println("end the supplyAsync");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return  "thenAcceptBoth";
                    // 组合
                }).thenAcceptBoth(CompletableFuture.supplyAsync(()->{
                    try {
                        System.out.println("start the thenAcceptBoth");
                        TimeUnit.SECONDS.sleep(3);
                        System.out.println("end the thenAcceptBoth");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return  100;
                }),(s, i)->System.out.print(s + "----" + i));

    }

}

```
41.4  CompletableFutureExample4 学习
```java
package com.learn.concurrenty.juc.untils.executors;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @ClassName: CompletableFutureExample1
 * @Description:
 * @Author: lin
 * @Date: 2020/4/15 16:15
 * History:
 * @<version> 1.0
 */
public class CompletableFutureExample4 {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        completeExceptionally();
    }


    /**
     * {@link CompletableFuture#completeExceptionally(Throwable)}
     */
    private static  void completeExceptionally() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            sleepSeconds(5);
            System.out.println("===========================i will be still process...");
            return "HELLo";
        });
        // 因为是异步的，所以在这里进行休眠，让其CompletableFuture 执行一定发生在 下面代码 的前面
        sleepSeconds(1);
        // 立即返回的机制
        future.completeExceptionally(new RuntimeException());
        System.out.println(future.get());
    }


    /**
     * {@link CompletableFuture#join()}
     */
    private static  void testJoin() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            sleepSeconds(5);
            System.out.println("===========================i will be still process...");
            return "HELLo";
        });
        String result = future.join();
        System.out.println(result);
    }


    /**
     * {@link CompletableFuture#complete(Object)}
     */
    private static  void complete() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            sleepSeconds(5);
            System.out.println("===========================i will be still process...");
            return "HELLo";
        });
        // 已经转换了 就是true
        boolean world = future.complete("WORLD");
        System.out.println(world);
        System.out.println(future.get());

    }

    private static  void  getNow() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            sleepSeconds(5);
            return "HELLo";
        });
        String result = future.getNow("WORLD");
        System.out.println(result);
        System.out.println(future.get());

    }

    
    private static  void  sleepSeconds(long seconds){
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

```
41.5  CompletionServiceExample1 学习
```java
package com.learn.concurrenty.juc.untils.executors;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * @ClassName: CompletionServiceExample1
 * @Description:
 * @Author: lin
 * @Date: 2020/4/14 17:23
 * History:
 * @<version> 1.0
 */
public class CompletionServiceExample1 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        futureDefect2();
    }

    /**
     * no callback
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private static  void futureDefect1() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<Integer> future = executorService.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 100;
        });
        System.out.println("===================");
        // 这里会阻塞，就会造成这一行代码下面的代码不能执行
        future.get();
    }

    /**
     * no callback
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private static  void futureDefect2() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        List<Callable<Integer>> callableList = Arrays.asList(
                () -> {
                    sleepSeconds(10);
                    System.out.println("The 10 finished.");
                    return 10;
                },
                () -> {
                    sleepSeconds(20);
                    System.out.println("The 20 finished.");
                    return 20;
                }
        );

        // 批量的处理
        List<Future<Integer>> futures = executorService.invokeAll(callableList);
        Integer v1 = futures.get(1).get();
        System.out.println(v1);
        Integer v2 = futures.get(0).get();
        System.out.println(v2);

    }


    private static  void  sleepSeconds(long seconds){
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

```
41.6  CompletionServiceExample2 学习
```java
package com.learn.concurrenty.juc.untils.executors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * @ClassName: CompletionServiceExample1
 * @Description:
 * @Author: lin
 * @Date: 2020/4/14 17:23
 * History:
 * @<version> 1.0
 */
public class CompletionServiceExample2 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        futureDefect2();
    }

    /**
     * no callback
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private static  void futureDefect1() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<Integer> future = executorService.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 100;
        });
        System.out.println("===================");
        // 这里会阻塞，就会造成这一行代码下面的代码不能执行
        future.get();
    }

    /**
     * no callback
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private static  void futureDefect2() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        List<Callable<Integer>> callableList = Arrays.asList(
                () -> {
                    sleepSeconds(10);
                    System.out.println("The 10 finished.");
                    return 10;
                },
                () -> {
                    sleepSeconds(20);
                    System.out.println("The 20 finished.");
                    return 20;
                }
        );

        ExecutorCompletionService<Integer> completionService = new ExecutorCompletionService<>(executorService);
        List<Future<Integer>> futures = new ArrayList<>();
        callableList.stream().forEach(callable -> futures.add(completionService.submit(callable)));

        Future<Integer> future;
        // 拿取最快完成的那一个，take是一个阻塞方法
        while ((future = completionService.take()) != null){
            System.out.println(future.get());
        }

    }


    private static  void  sleepSeconds(long seconds){
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

```
41.7 ComplexExample
```java
package com.learn.concurrenty.juc.untils.executors;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @ClassName: ComplexExample
 * @Description:
 * @Author: lin
 * @Date: 2020/4/15 15:34
 * History:
 * @<version> 1.0
 */
public class ComplexExample {
    public static void main(String[] args) throws InterruptedException {

        // CompletionService 相比 future 可以回调, 并且CompletionService 不是Executors 这个只是进行了保证
        // CompletionService 关注的是完成的任务,不会关注未完成的任务
        /*final ExecutorService service = Executors.newSingleThreadExecutor();
        List<Runnable> tasks = IntStream.range(0, 5).boxed().map(ComplexExample::toTask).collect(Collectors.toList());
        final CompletionService<Object> completionService = new ExecutorCompletionService<>(service);
        tasks.forEach(r -> completionService.submit(Executors.callable(r)));
        TimeUnit.SECONDS.sleep(12);
        // 这里取 shutdownNow 线程池，关注这个任务的执行是 ExecutorService,
        // 这个Runnable 返回的是未执行的, 那么在执行过程中被中断的任务 是不会放到这里面去的
        //
        List<Runnable> runnableList = service.shutdownNow();
        System.out.println(runnableList);*/


        //==========================================
        final ExecutorService service = Executors.newSingleThreadExecutor();
        List<Callable<Integer>> tasks = IntStream.range(0, 5).boxed().map(MyTask::new).collect(Collectors.toList());
        final CompletionService<Integer> completionService = new ExecutorCompletionService<>(service);

        tasks.forEach(completionService::submit);
        TimeUnit.SECONDS.sleep(12);
        // shutdownNow时候,返回的仅仅是队列里的,有的线程可能在执行过程中它被中断,或者出错了
        service.shutdownNow();
        // 这样可以拿出那些任务真正为完成
        tasks.stream().filter(callable -> !((MyTask)callable).isSuccess())
                .forEach(System.out::println);
    }

    private static class MyTask implements Callable<Integer>{
        private final Integer value;
        private boolean success = false;
        MyTask(Integer value){
            this.value = value;
        }


        @Override
        public Integer call() throws Exception {
            System.out.printf("The Task [%d] will be executed.\n", value);
            TimeUnit.SECONDS.sleep(value *5 + 10);
            System.out.printf("The Task [%d] execute done.\n", value);
            return value;
        }

        public boolean isSuccess(){
            return  success;
        }
    }

    private static Runnable toTask(int i ){
        return  ()->{
            try {
                System.out.printf("The Task [%d] will be executed.\n", i);
                TimeUnit.SECONDS.sleep(i * 5 + 10);
                System.out.printf("The Task [%d] execute done.\n", i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
    }
}

```
41.8 ExecutorServiceExample 测试
```java
package com.learn.concurrenty.juc.untils.executors;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * @ClassName: ExecutorServiceExample
 * @Description:
 * @Author: lin
 * @Date: 2020/4/13 14:01
 * {@link java.util.concurrent.ExecutorService}
 * History:
 * @<version> 1.0
 */
public class ExecutorServiceExample {
    public static void main(String[] args) throws InterruptedException {
//      isShutDown();
//      isTerminated();
//      executeRunnableError();
        executeRunnableTask();
    }

    /**
     * Question:
     * when invoked the shutdown method, can execute the new runnable?
     * Answer:
     * No !!! the executor service will rejected after shutdown.
     */
    private static  void isShutDown(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(()->{
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println(executorService.isShutdown());
        // 前面的任务还没有执行完，下面的打印不会打印出来
        // 如果注释掉  executorService.shutdown(), 那么就可以看到下面的可以打印出来了
        // 这个是因为上面任务已经执行完了
        executorService.shutdown();
        System.out.println(executorService.isShutdown());
        executorService.execute(()-> System.out.println("i will be execute after shutdown????"));
    }

    /**
     *
     */
    private static void isTerminated(){
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(()->{
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        executorService.shutdown();
        System.out.println(executorService.isShutdown());
        // 这个executor 还没有真正的总结掉，所以这是false
        System.out.println(executorService.isTerminated());
        // 是否在终结的过程中
        System.out.println(((ThreadPoolExecutor)executorService).isTerminating());
    }



    private static void executeRunnableError() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10, new MyThreadFactory());
        executorService.execute(()->{
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        IntStream.range(0, 10).boxed().forEach(i ->
                executorService.execute(()-> System.out.println(1/0)));
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.MINUTES);
        System.out.println("================================");
    }

    private static class MyThreadFactory implements ThreadFactory{
        private final static AtomicInteger SEQ = new AtomicInteger();

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("My-Thread-" + SEQ.getAndIncrement());
            thread.setUncaughtExceptionHandler((t,cause) ->{
                System.out.println("The thread " + t.getName() + " execute failed.");
                cause.printStackTrace();
                System.out.println("================================");
            });
            return thread   ;
        }
    }


    /**
     *
     *                                        |----->
     *                                        |----->
     * send request-----> store db ----10---> |----->
     *                                        |----->
     *                                        |----->
     */
    private static void executeRunnableTask() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10, new MyThreadFactory());
        IntStream.range(0, 10).boxed().forEach(i ->
                executorService.execute(
                        new MyTask(i) {
                            @Override
                            protected void error(Throwable cause) {
                                System.out.println("The no:" + i + " failed, update status to error");
                            }

                            @Override
                            protected void done() {
                                System.out.println("The no:" + i + " successful, update status to DONE");
                            }

                            @Override
                            protected void doExecute() {
                               if(i % 3 ==0){
                                   int tmp = i/0;
                               }
                            }

                            @Override
                            protected void doInit() {
                               //
                            }
                        }
                ));
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.MINUTES);
        System.out.println("================================");
    }

    private abstract static class MyTask implements Runnable{
        protected final int no;
        private MyTask(int no){
            this.no = no;
        }

        @Override
        public void run() {
            try {
                this.doInit();
                this.doExecute();
                this.done();
            }catch (Throwable cause){
                this.error(cause);
            }
        }

        protected abstract void error(Throwable cause);

        protected abstract void done();

        protected abstract void doExecute();

        protected abstract void doInit();
    }
}

```
41.9 ExecutorServiceExample 测试
```java
package com.learn.concurrenty.juc.untils.executors;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @ClassName: ExecutorServiceExample
 * @Description:
 * @Author: lin
 * @Date: 2020/4/13 14:01
 * {@link ExecutorService}
 * History:
 * @<version> 1.0
 */
public class ExecutorServiceExample3 {
    public static void main(String[] args) throws InterruptedException {
      /*test();*/
      /* testAllowCoreThreadTimeOut();*/
      /*testRemove();*/
       /*testPreStartCoreThread();*/
        testThreadPoolAdvice();
    }


    /**
     *
     * @throws InterruptedException
     */
    private static void test() throws InterruptedException {
        ThreadPoolExecutor executorService = (ThreadPoolExecutor)Executors.newFixedThreadPool(5);
        System.out.println(executorService.getActiveCount());
        executorService.execute(()->{
            try {
                // 这个任务运行结束，也不会结束整个操作，因为它里面已经有活动的线程。
                //
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        //休息20毫秒
        TimeUnit.MICROSECONDS.sleep(20);
        // 这个再来看看activeCount, 是一次性创建完 还是又创建了一个
        System.out.println(executorService.getActiveCount());
    }

    /**
     *  {@link ThreadPoolExecutor#allowCoreThreadTimeOut(boolean)}
     */
    private static void testAllowCoreThreadTimeOut(){
        // 比如执行五个任务， 超过五个任务 它就会有五个任务在里面，如果不进行释放它是永远也不会进行释放的
        // 虽然keepAliveTime 是0 ， 他需要去维持这个 coreSize的大小
        ThreadPoolExecutor executorService = (ThreadPoolExecutor)Executors.newFixedThreadPool(5);

        //设置keepAliveTime 时间
        executorService.setKeepAliveTime(10, TimeUnit.SECONDS);

        // 设置这个就会回收空闲线程吗？
        // 设置这个属性后，会出现 Exception in thread "main" java.lang.IllegalArgumentException: Core threads must have nonzero keep alive times，
        // 说明这个 这个keepAliveTime 不能为0, 所以在上面设置下时间,
        // 设置后 会自动回收，我们都可以不用去调用 shutdown了，
        // 设置了10s时间后 这个线程池里面的所以线程会被回收，并且这个线程池达到了terminated状态
        executorService.allowCoreThreadTimeOut(true);
        IntStream.range(0, 5).boxed().forEach(i ->{
            executorService.execute(()->{
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        });

    }

    /**
     * {@link ThreadPoolExecutor#remove(Runnable)}
     * @throws InterruptedException
     */
    private static void testRemove() throws InterruptedException {
        ThreadPoolExecutor executorService = (ThreadPoolExecutor)Executors.newFixedThreadPool(2);
        executorService.setKeepAliveTime(10, TimeUnit.SECONDS);
        executorService.allowCoreThreadTimeOut(true);
        IntStream.range(0, 5).boxed().forEach(i ->{
            // 这个是主线程 main 去调用
            // cpu 执行权， execute 会立即去创建线程 因为是异步的
            executorService.execute(()->{
                try {
                    TimeUnit.SECONDS.sleep(5);
                    System.out.println("===========i am finished========");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        });
        // 这里休眠 是为了确保 提交的任务在 它之前
        TimeUnit.MICROSECONDS.sleep(50);
        // 休眠就是让下面的线程不在 上面的线程执行的前面
        // 理论上下面的打印应该被执行，但是这个queue中任务被我们删除掉了
        Runnable runnable = ()->{
            System.out.println("i will never be executed!");
        };
        // 放入到queue 里面去
        executorService.execute(runnable);
        TimeUnit.MICROSECONDS.sleep(20);
        // 永远也不会被执行，因为被删掉了
    }

    /**
     * {@link ThreadPoolExecutor#prestartCoreThread()}
     */
    private static  void  testPreStartCoreThread(){
        // 初始化的时候 是0
        ThreadPoolExecutor executorService = (ThreadPoolExecutor)Executors.newFixedThreadPool(2);
        System.out.println(executorService.getActiveCount());

        System.out.println(executorService.prestartCoreThread());
        System.out.println(executorService.getActiveCount());

        // 不过调用多少次都不会超过coreSize的大小
        System.out.println(executorService.prestartCoreThread());
        System.out.println(executorService.getActiveCount());
        executorService.execute(()->
                {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        );

        executorService.execute(()->
                {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        );

        System.out.println(executorService.prestartCoreThread());
        System.out.println(executorService.getActiveCount());
    }


    /**
     * {@link ThreadPoolExecutor#prestartAllCoreThreads()}
     */
    private static  void  testPreStartAllCoreThread(){
        // 初始化的时候 是0
        ThreadPoolExecutor executorService = (ThreadPoolExecutor)Executors.newFixedThreadPool(2);
        executorService.setMaximumPoolSize(3);
        System.out.println(executorService.getActiveCount());
        // 预启动
        int num = executorService.prestartAllCoreThreads();
        System.out.println(num);
        // 启动结果还是 只有两个，并不没有到达max, 他只是按照coreSize来启动
        System.out.println(executorService.getActiveCount());

       /* System.out.println(executorService.prestartCoreThread());
        System.out.println(executorService.getActiveCount());

        System.out.println(executorService.prestartCoreThread());
        System.out.println(executorService.getActiveCount());
        executorService.execute(()->
                {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        );

        executorService.execute(()->
                {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        );

        System.out.println(executorService.prestartCoreThread());
        System.out.println(executorService.getActiveCount());*/
    }


    /**
     * 提交一批任务 有几个完成了，成不成功就不知道了
     */
    private static void testGetCompletedTaskCount(){}



    private static  void  testThreadPoolAdvice(){
        ExecutorService executorService = new MyThreadPoolExecutor(1,
                2, 30,
                TimeUnit.SECONDS, new ArrayBlockingQueue<>(1),
                r ->{
                    Thread t = new Thread(r);
                    return  t;
                }, new ThreadPoolExecutor.AbortPolicy());

        executorService.execute(new AbstractMyRunnable(1) {
            @Override
            public void run() {
                System.out.println("=======================");
            }
        });
    }


    private abstract static class AbstractMyRunnable implements Runnable{
        private final int no;

        protected AbstractMyRunnable(int no){
            this.no = no;
        }

        protected int getData(){
            return  this.no;
        }

    }



    private static class MyThreadPoolExecutor extends ThreadPoolExecutor{

        public MyThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        }

        @Override
        protected void beforeExecute(Thread t, Runnable r) {
            System.out.println("init the " + ((AbstractMyRunnable)r).getData());
        }

        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            if(null == t){
                System.out.println("successful " + ((AbstractMyRunnable)r).getData());
            }else {
                t.printStackTrace();
            }
        }
    }
}

```
41.10 ExecutorServiceExample4 测试
```java
package com.learn.concurrenty.juc.untils.executors;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @ClassName: ExecutorServiceExample
 * @Description:
 * @Author: lin
 * @Date: 2020/4/13 14:01
 * {@link ExecutorService}
 * History:
 * @<version> 1.0
 */
public class ExecutorServiceExample4 {
    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
        /*testInvokeAny();*/
//        testInvokeAnyTimeOut();
    }

    /**
     * Question:
     *  when the result returned, other callable will be keep on process?
     * Answer:
     *  other callable will be canceled (if other not process finished).
     * Note:
     *  The invokeAny will be blocked caller;
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private static  void  testInvokeAny() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        List<Callable<Integer>> collectList = IntStream.range(0, 5).boxed().map(i ->
                (Callable<Integer>) () -> {

                    TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(20));
                    // 那么其中有一个返回之后，其他的callable还会不会继续执行？
                    System.out.println(Thread.currentThread().getName()+ " :" + i);
                    return i;
                }).collect(Collectors.toList());
        //那个先完成了就返回哪一个
        Integer value = executorService.invokeAny(collectList);
        System.out.println("===============finished=================");
        System.out.println(value);
    }


    /**
     * {@link ExecutorService#invokeAny(Collection, long, TimeUnit)}
     */
    private static  void  testInvokeAnyTimeOut() throws ExecutionException, InterruptedException, TimeoutException {
        ExecutorService service = Executors.newFixedThreadPool(10);

        Integer value = service.invokeAny(
                IntStream.range(0, 5).boxed().map(i ->
                (Callable<Integer>) () -> {
                    TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(20));
                    System.out.println(Thread.currentThread().getName() + " :" + i);
                    return i;
                }
              ).collect(Collectors.toList()), 1, TimeUnit.SECONDS);
        System.out.println("===============finished=================");
        System.out.println(value);
    }


    /**
     * {@link ExecutorService#invokeAll(Collection)}
     * @throws InterruptedException
     */
    private static  void  testInvokeAll() throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(10);
        service.invokeAll(
                IntStream.range(0, 5).boxed().map(i ->
                        (Callable<Integer>) () -> {
                    // 先执行----转换----再输出

                            // 这里有一个缺点，就是其中有一个执行的很慢，
                            // 他也要将第一阶段执行完后后 才会去执行第二阶段
                            // 第二阶段执行完后才会去执行第三阶段
                            TimeUnit.SECONDS.sleep(5);
                            System.out.println(Thread.currentThread().getName() + " :" + i);
                            return i;
                        }
                ).collect(Collectors.toList())
                // parallelStream()
        ).stream().map(future -> {
            try {
                // 将future中的结果拿出来。
              return   future.get();
            } catch (Exception e) {
                throw  new RuntimeException(e);
            }
        }).forEach(System.out::println);
        // callable 执行完后才会打印
        System.out.println("===============finished=================");
    }




    /**
     * {@link ExecutorService#invokeAll(Collection, long, TimeUnit)}
     * @throws InterruptedException
     */
    private static  void  testInvokeAllTimeOut() throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(10);
        service.invokeAll(
                IntStream.range(0, 5).boxed().map(i ->
                        (Callable<Integer>) () -> {

                            TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(10));
                            System.out.println(Thread.currentThread().getName() + " :" + i);
                            return i;
                        }
                ).collect(Collectors.toList()), 1, TimeUnit.SECONDS
                // parallelStream()
        ).stream().map(future -> {
            try {
                // 将future中的结果拿出来。
                return   future.get();
            } catch (Exception e) {
                throw  new RuntimeException(e);
            }
        }).forEach(System.out::println);
        // callable 执行完后才会打印
        System.out.println("===============finished=================");
    }


    /**
     * {@link ExecutorService#submit(Runnable)} 
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private static  void  testSubmitRunnable() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        // 是一个异步方法，立即返回， 返回的是future
        Future<?> future = executorService.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        // 如果有结果说明执行 对Runnable执行结束了
        future.get();
    }


    /**
     * {@link ExecutorService#submit(Runnable, Object)}
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private static  void  testSubmitRunnableWithResult() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        String result = "DONE";
        Future<String> future = executorService.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, result);

        System.out.println(future.get());
    }

}

```
41.11 ExecutorServiceExample5 测试
```java
package com.learn.concurrenty.juc.untils.executors;

import java.util.concurrent.*;

/**
 * @ClassName: ExecutorServiceExample5
 * @Description:
 * @Author: lin
 * @Date: 2020/4/13 14:01
 * {@link ExecutorService}
 * History:
 * @<version> 1.0
 */
public class ExecutorServiceExample5 {
    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
        ThreadPoolExecutor executorService = (ThreadPoolExecutor)Executors.newFixedThreadPool(2);
        // 返回一个queue, 那么能往这个queue加一个runnable 让其运行喃
        // 不通过submit的方式 或者invoke的方式，我们直接到queue里面去加任务 这样可不可以运行喃。

        // 从运行的结果来看, executorService 去执行submit, invoke ,execute 的时候
        //  它类似于 不仅仅是接收到了一个任务，还是收到了一个信号，这个信号会触发 这个在线程池里面的线程
        // 的创建， 但是这个直接加进去  线程池里面的线程 不知道是不是要去创建线程 去执行。
        // 因为大家都在wait，都在等待这个信号。
        executorService.getQueue().add(()-> System.out.print("I am added directly into the queue"));

    }
}

```
41.12 ExecutorsExample 测试
```java
package com.learn.concurrenty.juc.untils.executors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @ClassName: ExecutorsExample
 * @Description:
 * @Author: lin
 * @Date: 2020/4/13 9:26
 * History:
 * @<version> 1.0
 */
public class ExecutorsExample {
    public static void main(String[] args) throws InterruptedException {
      useCachedThreadPool();
    }

    /**
     * 这个不自动shutdown ,是因为始终有一个线程是 lived 的.
     *
     * @throws InterruptedException
     */
    private static void  useSinglePool() throws InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        // 这个转换会失败，因为ThreadPoolExecutor不是 newSingleThreadExecutor的实例
//        System.out.println(((ThreadPoolExecutor)executorService).getActiveCount());

        //boxed将其封箱，就是将int装箱成integer
        IntStream.range(0, 10).boxed().forEach(i -> executorService
                .execute(()->{
                    try {
                        TimeUnit.SECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName()+ " [" + i +"] ");
                }));

        TimeUnit.SECONDS.sleep(1);
        System.out.println(((ThreadPoolExecutor)executorService).getActiveCount());
    }


    /**
     * new ThreadPoolExecutor(nThreads, nThreads,
     *                                       0L, TimeUnit.MILLISECONDS,
     *                                       new LinkedBlockingQueue<Runnable>());
     * 虽然keepAliveTime=0，但是这个不回收，
     * 因为这个idle(空闲)线程 不大于core,所以不会进行回收
     *
     * @throws InterruptedException
     */
    private static void  useFixedSizePool() throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(10);
        System.out.println(((ThreadPoolExecutor)service).getActiveCount());

        IntStream.range(0, 10).boxed().forEach(i -> service
                .execute(()->{
                    try {
                        TimeUnit.SECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName()+ " [" + i +"] ");
                }));

        TimeUnit.SECONDS.sleep(1);
        System.out.println(((ThreadPoolExecutor)service).getActiveCount());
    }



    /**
     *   return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
     *                                       60L, TimeUnit.SECONDS,
     *                                       new SynchronousQueue<Runnable>());
     * BlockingQueue是一个  SynchronousQueue<Runnable>()
     * 这个阻塞队列只能放一个元素的BlockingQueue,
     * 所以他不会把任务暂存起来，只有等到1分钟后 它发现有空闲的，他才会去处理
     * 发现这些线程大于core线程个数，core线程个数是0，那么就会将其空闲的线程全部回收了
     *
     * @throws InterruptedException
     */
    private static void  useCachedThreadPool() throws InterruptedException {
        ExecutorService service = Executors.newCachedThreadPool();
        System.out.println(((ThreadPoolExecutor)service).getActiveCount());

        service.execute(()-> System.out.println("==============="));
        System.out.println(((ThreadPoolExecutor)service).getActiveCount());

        IntStream.range(0, 100).boxed().forEach(i -> service
                .execute(()->{
                    try {
                        TimeUnit.SECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName()+ " [" + i +"] ");
                }));

        TimeUnit.SECONDS.sleep(1);
        System.out.println(((ThreadPoolExecutor)service).getActiveCount());
    }
}

```


41.13 ExecutorsExample2 测试
```java
package com.learn.concurrenty.juc.untils.executors;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @ClassName: ExecutorsExample
 * @Description:
 * @Author: lin
 * @Date: 2020/4/13 9:26
 * History:
 * @<version> 1.0
 */
public class ExecutorsExample2 {
    /**
     * 命令：dxdiag 这个是directx诊断工具命令
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        /**
         * 会自动结束，也就是说它 清光了所以的任务，
         * 包括queue里面的任务之后 它会全部结束
         */
        ExecutorService service = Executors.newWorkStealingPool();
        //查看cup核数
//        Optional.of(Runtime.getRuntime().availableProcessors())
//                .ifPresent(System.out::println);
        List<Callable<String>> callableList = IntStream.range(0, 20).boxed().map(i ->
                (Callable<String>) () -> {
                    System.out.println("Thread " + Thread.currentThread().getName());
                    sleepSeconds(2);
                    return "Task-" + i;
                }
        ).collect(Collectors.toList());

        // 这个返回是立即返回，但是结果要等到真正的线程池用完 启动了任务才会给你
        // 所以 这就叫future 设计模式，就是让你的程序不阻塞，可以进行下面的操作
        // 可以继续往下走
        //List<Future<String>> futures = service.invokeAll(callableList);
        service.invokeAll(callableList).stream().map(future ->{
            try {
               return future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }).forEach(System.out::println);
    }


    private  static  void sleepSeconds(long seconds){
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

```


41.13 FutureExample 测试
```java
package com.learn.concurrenty.juc.untils.executors;

import java.util.concurrent.*;

/**
 * @ClassName: FutureExample
 * @Description:
 * @Author: lin
 * @Date: 2020/4/14 14:35
 * History:
 * @<version> 1.0
 */
public class FutureExample {
    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
        /*testGet();*/
        testGetWithTimeOut();
    }

    /**
     * {@link Future#get()}
     */
    private static  void testGet() throws ExecutionException, InterruptedException {
        ExecutorService executorService =  Executors.newCachedThreadPool();
        Future<Integer> future = executorService.submit(()->{
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return  10;
        });
        //===========不用去等待， 快速返回有机会去执行其他的操作=====================
        System.out.println("==============i will be printed quickly.===================");
        //================================

        Thread callerThread = Thread.currentThread();
        new Thread(()->{
            // 这个线程启动了，但是还没有具备可以执行的running状态 会有可能先执行下面的代码
            // 所以这个让其休眠几秒，让其线程到达running状态
            try {
                TimeUnit.MILLISECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 让这个线程去打断看看能不能接着往下执行
            callerThread.interrupt();
        }).start();
        //  因为这个在拿取结果的时候需要等待10s
        // 在等的过程中 是无法打断的，所以这个get 方法 打断的是main
        Integer result = future.get();
        System.out.println("=====================" + result + "========================");
    }



    /**
     * {@link Future#get(long, TimeUnit)}
     */
    private static  void testGetWithTimeOut() throws ExecutionException, InterruptedException, TimeoutException {
        ExecutorService executorService =  Executors.newCachedThreadPool();
        Future<Integer> future = executorService.submit(()->{
            try {
                TimeUnit.SECONDS.sleep(10);
                System.out.println("=====================");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return  10;
        });

        Integer result = future.get(5, TimeUnit.SECONDS);
            //超时了之后 也会去执行这个
        System.out.println(result);

    }
}

```
41.14 FutureExample2 测试
```java
package com.learn.concurrenty.juc.untils.executors;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @ClassName: FutureExample2
 * @Description:
 * @Author: lin
 * @Date: 2020/4/14 14:35
 * History:
 * @<version> 1.0
 */
public class FutureExample2 {
    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
        /*testIsDone();*/
        testIsDone2();
    }

    /**
     * {@link Future#isDone()}
     */
    private static  void testIsDone() throws ExecutionException, InterruptedException {
        ExecutorService executorService =  Executors.newCachedThreadPool();
        Future<Integer> future = executorService.submit(()->{
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return  10;
        });
        Integer result = future.get();
        // 正常结束会返回
        // 还有一种是出现了错误
        System.out.println(result + " is done"+future.isDone());
    }


    /**
     * 如果出现了错误
     * {@link Future#isDone()}
     */
    private static  void testIsDone2() throws ExecutionException, InterruptedException {
        ExecutorService executorService =  Executors.newCachedThreadPool();
        Future<Integer> future = executorService.submit(()->{
            // 还有一种是出现了错误
              throw  new RuntimeException();
        });
        // 这了要捕获异常，不然异常就抛出去了不能进行异常处理
        try {

            Integer result = future.get();
            System.out.println(result);
        }catch (Exception e){
            // 出现了问题 也表示结束了，但是不一定代表着正确的结束了
            System.out.println( " is done"+future.isDone());
        }
    }


    /**
     * try to cancel  maybe failed.
     * <ul>
     *  <li>task is completed already.</li>
     *  <li>has already been cancelled. </li>
     * </>
     *
     */
    private static void testCancel() throws ExecutionException, InterruptedException {
        ExecutorService executorService =  Executors.newCachedThreadPool();
        AtomicBoolean running = new AtomicBoolean(true);
        Future<Integer> future = executorService.submit(()-> {
//            try {
//                TimeUnit.SECONDS.sleep(10);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

            while (running.get()){

            }
            return  10;
        });
        TimeUnit.MILLISECONDS.sleep(10);
        System.out.println(future.cancel(true));
        // 已经被cancel过那么就不能再次被cancel.
//        System.out.println(future.cancel(true));

        System.out.println(future.isDone());
        //结果已经返回，但是任务 还一直在执行，这个任务有可能非常耗时，并且没有打断它的地方
        System.out.println(future.isCancelled());
    }



    /**
     * try to cancel  maybe failed.
     * <ul>
     *  <li>task is completed already.</li>
     *  <li>has already been cancelled. </li>
     * </>
     *
     */
    private static void testCancel2() throws ExecutionException, InterruptedException {
        ExecutorService executorService =  Executors.newCachedThreadPool();
        AtomicBoolean running = new AtomicBoolean(true);
        Future<Integer> future = executorService.submit(()-> {
//            try {
//                TimeUnit.SECONDS.sleep(10);
//                System.out.println("=========================");
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            // 当遇到某个很长的任务，怎么去cancel喃，就是通过这种方式
            //!Thread.interrupted();
            while (!Thread.interrupted()){

            }
            System.out.println("11111111111111111111111");
            return  10;
        });
        TimeUnit.MILLISECONDS.sleep(10);
        System.out.println(future.cancel(true));
        System.out.println(future.isDone());
        System.out.println(future.isCancelled());
        // 因为已经 cancel 所以再次去拿取，是拿取不到的,会抛出异常
//        System.out.println(future.get());
    }

}

```
41.15 ScheduledExecutorServiceExample1 测试
```java
package com.learn.concurrenty.juc.untils.executors;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @ClassName: ScheduledExecutorServiceExample1
 * @Description:
 * @Author: lin
 * @Date: 2020/4/15 13:41
 * History:
 * @<version> 1.0
 */
public class ScheduledExecutorServiceExample1 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);

        /*ScheduledFuture<?> future = executor.schedule(()->
                System.out.println("i will be invoked!"), 2 , TimeUnit.SECONDS);
        // cancel之后还会执行吗？ cancel 之后就不会再执行了
        System.out.println(future.cancel(true));*/

       /* ScheduledFuture<Integer> scheduled =executor.schedule(()->2, 2, TimeUnit.SECONDS);
        System.out.println(scheduled.get());*/

        /**
         *period 2 seconds execute a task.
         * but the task spend 5 seconds actually
         *
         * solution 1: (crontab/quartz/Control-M)
         *
         * period first policy
         * 0:5 (1个任务在执行的过程中实际上花了5秒)
         * 2:5 (周期2s到了之后，下一个 有去启动了一个)
         * 4:5 (在第4秒时候又会去启动一个)
         *
         * solution 2:(JDK timer)
         * (周期还是2)
         * 0:5(第零秒执行了一个任务 花了5秒，等这个执行结束之后，不会去重启 一个
         * 去检查 这个执行了5秒 超过了这个周期2秒，所以从5秒开始有执行了一个)
         * 5:5
         * 10:5
         *
         * 下面的测试休眠5秒，看看这个是和那个相同
         */
        final AtomicLong interval = new AtomicLong(0L);
        ScheduledFuture<?> future =executor.scheduleAtFixedRate(()->
        {
           long currentTimeMills = System.currentTimeMillis();
           if(interval.get() == 0){
               System.out.printf("The first time trigger task at %d\n", currentTimeMills);
           }else {
               System.out.printf("The actually spend [%d]\n", currentTimeMills - interval.get());
           }
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            interval.set(currentTimeMills);
        }, 1,2, TimeUnit.SECONDS);

    }
}

```

41.16 ScheduledExecutorServiceExample2 测试
```java
package com.learn.concurrenty.juc.untils.executors;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @ClassName: ScheduledExecutorServiceExample2
 * @Description:
 * @Author: lin
 * @Date: 2020/4/15 13:41
 * History:
 * @<version> 1.0
 */
public class ScheduledExecutorServiceExample2 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        testScheduleWithFixedDelay();
    }

    private static  void  testScheduleWithFixedDelay(){
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);
        final AtomicLong interval = new AtomicLong(0L);
        //设置一个scheduled 它会固定的延迟
        ScheduledFuture<?> future = executor.scheduleWithFixedDelay(() ->
        {
            long currentTimeMills = System.currentTimeMillis();
            if(interval.get() == 0){
                System.out.printf("The first time trigger task at %d\n", currentTimeMills);
            }else {
                System.out.printf("The actually spend [%d]\n", currentTimeMills - interval.get());
            }
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            interval.set(currentTimeMills);
            System.out.println(Thread.currentThread().getName());
        }, 1, 2, TimeUnit.SECONDS);

    }

    private static  void test1() throws InterruptedException {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);
        // 这个值默认是false,就是在shutdown后 不在周期性的去执行
        System.out.println(executor.getContinueExistingPeriodicTasksAfterShutdownPolicy());
        final AtomicLong interval = new AtomicLong(0L);
        ScheduledFuture<?> future =executor.scheduleAtFixedRate(()->
        {
            long currentTimeMills = System.currentTimeMillis();
            if(interval.get() == 0){
                System.out.printf("The first time trigger task at %d\n", currentTimeMills);
            }else {
                System.out.printf("The actually spend [%d]\n", currentTimeMills - interval.get());
            }
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            interval.set(currentTimeMills);
            // period 是固定的
        }, 1,2, TimeUnit.SECONDS);

        // 让其真正执行了
        TimeUnit.MILLISECONDS.sleep(1200);
        executor.shutdown();
    }


    private static  void test2() throws InterruptedException{
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);
        System.out.println(executor.getContinueExistingPeriodicTasksAfterShutdownPolicy());
        final AtomicLong interval = new AtomicLong(0L);
        //设置一个scheduled 它会固定的延迟
        ScheduledFuture<?> future = executor.scheduleWithFixedDelay(() ->
        {
            long currentTimeMills = System.currentTimeMillis();
            if(interval.get() == 0){
                System.out.printf("The first time trigger task at %d\n", currentTimeMills);
            }else {
                System.out.printf("The actually spend [%d]\n", currentTimeMills - interval.get());
            }
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            interval.set(currentTimeMills);
            System.out.println(Thread.currentThread().getName());
            // delay 不是固定的
        }, 1, 2, TimeUnit.SECONDS);
    }
}

```
42.1 自定义实现LinkedList
```java
package com.learn.concurrenty.juc.collections;

/**
 * @ClassName: MyLinkedList
 * @Description: 自定义实现LinkedList
 * @Author: lin
 * @Date: 2020/4/16 9:28
 * History:
 * @<version> 1.0
 */
public class MyLinkedList<E> {

    private static final String PLAIN_NULL="null";
    /**
     * 因为是 单向的,所以第一个是first
     */
    private Node<E> first;

    /**
     * 定义一个空值
     */
    private final Node<E> NULL = (Node<E>) null;

    /**
     * 元素个数
     */
    private int size ;

    /**
     * 构造方法，初始化链表中第一个元素
     * 因为还没有插入数据，所以第一个元素是空值
     */
    public MyLinkedList(){
        this.first = NULL;
    }

    public int size(){
        return  size;
    }

    /**
     * 看看元素是否为空
     * @return
     */
    public boolean isEmpty(){
        return (size() ==0);
    }




    /**
     * 提供一个静态方法，添加多个元素
     * @param elements
     * @param <E>
     * @return
     */
    public static  <E> MyLinkedList<E> of(E ...elements){
        // 如果元素长度为0
        final  MyLinkedList<E> myLinkedList = new MyLinkedList<>();
        if(elements.length != 0){
            // 遍历去 将元素添加到这个list中去
            for (E e: elements) {
               myLinkedList.addFirst(e);
            }
        }
        return  myLinkedList;
    }

    /**
     * 添加元素
     * @param e
     * @return
     */
    public MyLinkedList<E> addFirst(E e){
        // 先声明一个Node,将元素放到节点中去
        final Node<E> newNode = new Node<>(e);
        // 因为链表中还没有元素,所以Node 的下一个元素就是first
        newNode.next = first;
        // 将元素个数++;
        size++;
        // 那么第一个元素就是 newNode
        this.first = newNode;
        // 返回当前对象
        return this ;
    }


    /**
     * 判断元素是存在
     * @param e
     * @return
     */
    public boolean contains(E e){
        //那么先从第一个节点开始
        Node<E> current = first;
        // 首先判断这元素是不是为空
        while (null != current ){
            if(current.value == e){
                //如果相等那么就返回true;
                return  true;
            }
            // 循环判断,第一个判断后不想等,那么就比较下一个
            // 将当前的下一个元素 取出来比较
            current = current.next;
        }
        return  false;
    }


    /**
     * 移除元素
     */
    public E removeFirst(){
        // 第一步，我们移除元素时,先判断元素链表中元素是否为空
        // 然后从第一个元素开始,就是后进先出的方式,
        // 这一点和出栈入栈的操作有点类似
        if(this.isEmpty()){
            // 可以抛出异常也可以 直接返回null
           throw  new NoElementException("The linked list is empty.");
        }
        // 将第一个元素放到临时节点中去
        Node<E> node = first;
        first = node.next;
        size -- ;
        // 返回移除元素的值,也就是移除了哪一个
        return node.value;
    }


    @Override
    public String toString() {
        if(this.isEmpty()){
            return  "[]";
        }else{
            StringBuilder builder = new StringBuilder("[");
            Node<E> current = first;
            while (current != null){
                builder.append( current.toString()).append(",");
                current = current.next;
            }
            // 当为空时 就是最后一个元素, 将builder中最后一个元素的符号进行替换
            builder.replace(builder.length()-1, builder.length(), "]");
            return  builder.toString();
        }
    }

    static class NoElementException extends RuntimeException{
        NoElementException(String message){
            super(message);
        }
    }

    // 单向链表: 每一个节点保留下一个节点的指针

    private static class  Node<E> {
        E  value;
        Node<E> next;
        public Node(E value){
            this.value = value;
        }

        @Override
        public String toString() {
            if(null != value){
                return  value.toString();
            }
            return PLAIN_NULL   ;
        }
    }


    public static void main(String[] args) {
        MyLinkedList<String> linkedList = MyLinkedList.of("Hello", "World", "Scala",
                "Java", "Thread");
        linkedList.addFirst("Current").addFirst("Test");
        System.out.println(linkedList.size());
        System.out.println(linkedList.contains("Test"));
        System.out.println("======================");
        System.out.println(linkedList);
        while (!linkedList.isEmpty()){
            System.out.println(linkedList.removeFirst());
        }
        System.out.println(linkedList.size());
        System.out.println(linkedList.isEmpty());
    }
}

```
42.2 PriorityLinkedList
```java
package com.learn.concurrenty.juc.collections;

/**
 * 优先级 链表
 * @ClassName: PriorityLinkedList
 * @Description: 
 * @Author: lin
 * @Date: 2020/4/16 9:28
 * History:
 * @<version> 1.0
 */
public class PriorityLinkedList<E extends Comparable<E>>  {

    private static final String PLAIN_NULL="null";
    /**
     * 因为是 单向的,所以第一个是first
     */
    private Node<E> first;

    /**
     * 定义一个空值
     */
    private final Node<E> NULL = (Node<E>) null;

    /**
     * 元素个数
     */
    private int size ;

    /**
     * 构造方法，初始化链表中第一个元素
     * 因为还没有插入数据，所以第一个元素是空值
     */
    public PriorityLinkedList(){
        this.first = NULL;
    }

    public int size(){
        return  size;
    }

    /**
     * 看看元素是否为空
     * @return
     */
    public boolean isEmpty(){
        return (size() ==0);
    }




    /**
     * 提供一个静态方法，添加多个元素
     * @param elements
     * @param <E>
     * @return
     */
    public static  <E extends  Comparable<E>> PriorityLinkedList<E> of(E ...elements){
        // 如果元素长度为0
        final PriorityLinkedList<E> myLinkedList = new PriorityLinkedList<>();
        if(elements.length != 0){
            // 遍历去 将元素添加到这个list中去
            for (E e: elements) {
               myLinkedList.addFirst(e);
            }
        }
        return  myLinkedList;
    }

    /**
     * 插入元素时候 去比较
     * 添加元素
     * @param e
     * @return
     */
    public PriorityLinkedList<E> addFirst(E e){
        // 先声明一个Node,将元素放到节点中去
        final Node<E> newNode = new Node<>(e);
        // 声明前面一个 节点
        Node<E> previous = NULL;
        // 要将前面的
        Node<E> current = first;
        // 当还没有到最后一个元素 并且 这个元素大于第一个
        //
        while (current!= null && e.compareTo(current.value) > 0){
            // 那么前一个元素就是第一个元素
             previous = current;
             current = current.next;
        }
        // 如果没有元素或者 比第一个都小
        if(previous == NULL){
            // 第一个元素就是这个新声明的
            first = newNode;
            // 将它的next 指向了 前一个的first
            //newNode.next = current;
        }else{
            // 找到了一个比当前元素大的 就要换一下 指向
            // 那么前一个元素的下一个 就是这个新定义的节点
            previous.next = newNode;
            // 这个新定义的节点的下一个 就是当前的 current;
            //newNode.next = current;
        }
        newNode.next = current;
        size ++;
        return  this;
    }


    /**
     * 判断元素是存在
     * @param e
     * @return
     */
    public boolean contains(E e){
        //那么先从第一个节点开始
        Node<E> current = first;
        // 首先判断这元素是不是为空
        while (null != current ){
            if(current.value == e){
                //如果相等那么就返回true;
                return  true;
            }
            // 循环判断,第一个判断后不想等,那么就比较下一个
            // 将当前的下一个元素 取出来比较
            current = current.next;
        }
        return  false;
    }


    /**
     * 移除元素
     */
    public E removeFirst(){
        // 第一步，我们移除元素时,先判断元素链表中元素是否为空
        // 然后从第一个元素开始,就是后进先出的方式,
        // 这一点和出栈入栈的操作有点类似
        if(this.isEmpty()){
            // 可以抛出异常也可以 直接返回null
           throw  new NoElementException("The linked list is empty.");
        }
        // 将第一个元素放到临时节点中去
        Node<E> node = first;
        first = node.next;
        size -- ;
        // 返回移除元素的值,也就是移除了哪一个
        return node.value;
    }


    @Override
    public String toString() {
        if(this.isEmpty()){
            return  "[]";
        }else{
            StringBuilder builder = new StringBuilder("[");
            Node<E> current = first;
            while (current != null){
                builder.append( current.toString()).append(",");
                current = current.next;
            }
            // 当为空时 就是最后一个元素, 将builder中最后一个元素的符号进行替换
            builder.replace(builder.length()-1, builder.length(), "]");
            return  builder.toString();
        }
    }



    static class NoElementException extends RuntimeException{
        NoElementException(String message){
            super(message);
        }
    }

    // 单向链表: 每一个节点保留下一个节点的指针

    private static class  Node<E> {
        E  value;
        Node<E> next;
        public Node(E value){
            this.value = value;
        }

        @Override
        public String toString() {
            if(null != value){
                return  value.toString();
            }
            return PLAIN_NULL   ;
        }
    }


    public static void main(String[] args) {
        PriorityLinkedList<Integer> linkedList = PriorityLinkedList.of(10, 1, 20, 0, 4, -5, 100, 36, 89, 67);
        System.out.println(linkedList);
    }
}

```
42.3 简单跳表 
```java
package com.learn.concurrenty.juc.collections;

import java.util.Random;

/**
 * @ClassName: SimpleSkipList
 * @Description: 简单跳表
 * @Author: lin
 * @Date: 2020/4/16 14:46
 * History:
 * @<version> 1.0
 */
public class SimpleSkipList {
    /**
     * 跳表的头和尾 都对应下层链表的头和尾
     */
    private final static byte HEAD_NODE = (byte) -1;
    private final static byte DATA_NODE = (byte) 0;
    private final static byte TAIL_NODE = (byte) -1;

    private static class Node{
        private Integer value;
        private Node up;
        private Node down;
        private Node left;
        private Node right;
        private byte bit;

        public  Node(Integer value, byte bit){
            this.value = value;
            this.bit = bit;
        }

        public Node(Integer value){
            this(value, DATA_NODE);
        }
    }

    /**
     * 节点的头
     */
    private Node head;
    /**
     * 节点的尾
     */
    private Node tail;

    /**
     * 节点的元素个数
     */
    private int size;
    /**
     * 高度
     */
    private int height;
    /**
     * 简单的随机算法
     */
    private Random random;

    /**
     * 构造方法 这个跳表中 头节点，和尾节点
     *
     */
    public  SimpleSkipList(){
        this.head = new Node(null, HEAD_NODE);
        this.tail = new Node(null , TAIL_NODE);
        head.right = tail;
        tail.left = head;
        // 简单随机数
        this.random = new Random(System.currentTimeMillis());
    }


    /**
     *  最底层 包含所有元素
     * 分层查找
     * @param element
     * @return
     */
    private Node find(Integer element){
        Node current = head;
        for(;;){
            // 不是最后一个 并且
            while (current.right.bit !=TAIL_NODE && current.right.value <= element){
                current = current.right;
            }
            // 如果下面的指针 不为空, 那么就将当前节点的下面指针
            // 指向 当前
            if(current.down != null){
                current = current.down;
            }else {
                break;
            }
        }
        // the current<=element< current.right(if exist)
        return  current;
    }

    /**
     * 插入元素
     * @param element
     */
    public void  add(Integer element){
        // 首先查询这个元素 相近的元素
        Node findNode = this.find(element);
        // 声明一个node
        Node newNode = new Node(element);

        /**  双向
         *
         *   +------+      +-----+        +-----+
         *            --->           --->
         *    9(node)      newNode         20(node)
         *            <---           <---
         *   +------+      +-----+        +-----+
         */

        newNode.left = findNode;
        // 那么新节点的 右指针 是 临近节点的右指针
        newNode.right = findNode.right;
        // 临近节点的 右指针的 左指针 就是 新节点
        findNode.right.left = newNode;
        findNode.right = newNode;


        // 需不需要提层数, 也就是将相应的节点提到 相应的其他层次去
        // 目前0层
        int currentLevel = 0;
        // 这里随机 结构 非常关键, 这里问题特别大
        // 如果这个随机数 小于 0.5 那么就要 提层数
        while (random.nextDouble() < 0.3d){
            // 判断是否大于层高
            if (currentLevel >= height){
                height++;
                Node dumyHead = new Node(null, HEAD_NODE);
                Node dumyTail = new Node(null, TAIL_NODE);

                dumyHead.right = dumyTail;
                dumyHead.down = head;
                head.up = dumyHead;

                dumyTail.left = dumyHead;
                dumyHead.down = tail;
                tail.up = dumyTail;

                head = dumyHead;
                tail = dumyTail;
            }
             while ((findNode != null) && findNode.up == null){
                  findNode = findNode.left;
             }
              findNode = findNode.up;

             Node upNode = new Node(element);
             upNode.left = findNode;
             upNode.right = findNode.right;
             upNode.down = newNode;

             findNode.right.left = upNode;
             findNode.right = upNode;
             newNode.up = upNode;

             newNode = upNode;
             currentLevel ++;
        }
        size++;
    }

    /**
     * 一层一层的取出来
     */
    public void dumpSkipList(){
        Node temp = head;
        int i = height+1;
        while (temp != null) {
            System.out.printf("Total [%d] height [%d]", height+1, i--);
            Node node = temp.right;
            while (node.bit == DATA_NODE){
                System.out.printf("->%d", node.value);
                node = node.right;
            }
            System.out.println("\n");
            temp = temp.down;
        }
        System.out.println("====================================");
    }


    /**
     *
     * @param element
     * @return
     */
    public boolean contains(Integer element){
        Node node = this.find(element);
        return  (node.value.equals(element));
    }

    /**
     *
     * @param element
     * @return
     */
    public  Integer get(Integer element){
        Node node = this.find(element);
        return  (node.value.equals(element)) ? node.value : null;
    }

    public int size(){
        return size;
    }

    /**
     * 看看元素是否为空
     * @return
     */
    public boolean isEmpty(){
        return (size() ==0);
    }


    public static void main(String[] args) {
        SimpleSkipList simpleSkipList = new SimpleSkipList();
        simpleSkipList.add(10);
        simpleSkipList.dumpSkipList();

        simpleSkipList.add(1);
        simpleSkipList.dumpSkipList();

        Random random = new Random();
        int count = 10;
        for (int i = 0; i < count ; i++) {
            simpleSkipList.add(random.nextInt(1000));
        }
        simpleSkipList.dumpSkipList();
    }
}

```
43.1 自定义一个CopyOnWriteMap
```java
package com.learn.concurrenty.juc.collections.custom;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 为什么jdk中没有实现 CopyOnWriteMap?
 * @ClassName: CopyOnWriteMap
 * @Description: 自定义一个CopyOnWriteMap
 * @Author: lin
 * @Date: 2020/4/20 15:54
 * History:
 * @<version> 1.0
 */
public class CopyOnWriteMap<K,V> implements Map<K,V> {
    private volatile Map<K, V> map;

    private ReentrantLock lock = new ReentrantLock();
    public CopyOnWriteMap(){
        this.init();
    }
    private void init(){
        this.map = new HashMap<>();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return null;
    }

    @Override
    public V put(K key, V value) {
        try {
            lock.lock();
            Map<K,V>  newMap = new HashMap<>(map);
            V val = newMap.put(key, value);
            this.map = newMap;
            return  val;
        }finally {
           lock.unlock();
        }

    }

    @Override
    public V remove(Object key) {
        try {
            //加锁
            lock.lock();
            // 创建一个新的,然后将原来的map放进行去
            Map<K,V>  newMap = new HashMap<>(map);
            V val = newMap.remove(key);
            this.map = newMap;
            return  val;
        }finally {
            lock.unlock();
        }
    }

    /**
     * 也是一个写操作，要加锁
     * @param m
     */
    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        try {
            //加锁
            lock.lock();
            // 创建一个新的,然后将原来的map放进行去
            Map<K,V>  newMap = new HashMap<>(map);
            // 将所有的元素放到这个map中去
            newMap.putAll(m);
            this.map = newMap;
        }finally {
            lock.unlock();
        }
    }

    @Override
    public void clear() {
        try {
            //加锁
            lock.lock();
           map = new HashMap<>();
        }finally {
            lock.unlock();
        }
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<V> values() {
        return map.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return map.entrySet();
    }
}

```
43.2 自定义一个MyQueue
```java
package com.learn.concurrenty.juc.collections.custom;

/**
 * @ClassName: MyQueue
 * @Description:
 * @Author: lin
 * @Date: 2020/4/20 17:14
 * History:
 * @<version> 1.0
 */
public class MyQueue<E> {
    private static class Node<E>{
        private E element;
        private Node<E> next;

        /**
         * 节点
         * @param element 元素
         * @param next  下一个节点
         */
        public Node(E element, Node<E> next) {
            this.element = element;
            this.next = next;
        }

        public E getElement() {
            return element;
        }

        public void setElement(E element) {
            this.element = element;
        }

        public Node<E> getNext() {
            return next;
        }

        public void setNext(Node<E> next) {
            this.next = next;
        }

        @Override
        public String toString() {
            return (element == null) ? "" : element.toString();
        }

    }

    private Node<E> first, last;
    private int size;

    public  int size(){
        return  size;
    }

    public boolean isEmpty(){
        return  size == 0;
    }

    /**
     * 第一个元素
     * @return
     */
    public E peekFirst(){
        return  first == null ? null : first.element;
    }

    /**
     * 最后一个元素
     * @return
     */
    public E peekLast(){
        return  last == null ? null : last.getElement();
    }

    /**
     * 添加元素
     * @param element
     */
    public void addLast(E element){
        Node<E> newNode = new Node<>(element, null);
        if(size() == 0){
            first = newNode;
        }else{
            last.setNext(newNode);
        }
        last = newNode;
        size ++;
    }

    public E removeFirst(){
        if(isEmpty()){return  null;}
        E element = first.getElement();
        first = first.getNext();
        size --;
        if(size() == 0){
            last = null;
        }
        return  element;
    }

    public static void main(String[] args) {
        MyQueue<String> myQueue = new MyQueue<>();
        myQueue.addLast("hello");
        myQueue.addLast("world");
        myQueue.addLast("java");

        System.out.println(myQueue.removeFirst());
        System.out.println(myQueue.removeFirst());
        System.out.println(myQueue.removeFirst());
    }
}

```


42. 跳表的技术特点
```
1.一种随机的数据结构
2.最底层包含整个跳表的所有元素
3.典型的空间换时间的算法

跳表节点 有上下左右4个指针
```





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

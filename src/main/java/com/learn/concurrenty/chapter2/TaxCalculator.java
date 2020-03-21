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

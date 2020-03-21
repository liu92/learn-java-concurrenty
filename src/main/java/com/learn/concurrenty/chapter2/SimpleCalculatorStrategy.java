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

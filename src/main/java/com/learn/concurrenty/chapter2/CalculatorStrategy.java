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

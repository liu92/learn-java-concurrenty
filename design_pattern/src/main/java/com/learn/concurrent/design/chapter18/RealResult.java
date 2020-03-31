package com.learn.concurrent.design.chapter18;

/**
 * @ClassName: RealResult
 * @Description:
 * @Author: lin
 * @Date: 2020/3/30 15:47
 * History:
 * @<version> 1.0
 */
public class RealResult implements  Result {
    private  final  Object  resultValue;

    RealResult(Object resultValue){
        this.resultValue = resultValue;
    }

    @Override
    public Object getResultValue() {
        return this.resultValue;
    }
}

package com.learn.concurrent.design.chapter18;

/**
 * @ClassName: MethodRequest
 * @Description:
 * @Author: lin
 * @Date: 2020/3/30 16:04
 * History:
 * @<version> 1.0
 */
public abstract class MethodRequest {
    protected  final  Servant servant;
    protected  final FutureResult futureResult;

    MethodRequest(Servant servant, FutureResult futureResult){
        this.servant = servant;
        this.futureResult = futureResult;
    }

    public abstract void execute();
}

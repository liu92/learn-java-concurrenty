package com.learn.concurrenty.juc.atomic;

/**
 * @ClassName: GetLockException
 * @Description:
 * @Author: lin
 * @Date: 2020/4/2 15:09
 * History:
 * @<version> 1.0
 */
public class GetLockException extends  Exception{
    public GetLockException(){
        super();
    }

    public GetLockException(String message){
        super(message);
    }
}

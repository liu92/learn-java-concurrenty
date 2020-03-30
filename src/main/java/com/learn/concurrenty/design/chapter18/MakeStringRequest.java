package com.learn.concurrenty.design.chapter18;

/**
 * 这个MakeStringRequest 会把ActiveObject 的每一个方法转换成对象
 * @ClassName: MakeStringRequest
 * @Description:
 * @Author: lin
 * @Date: 2020/3/30 16:06
 * History:
 * @<version> 1.0
 * {@link ActiveObject#makeString(int, char)}
 */
public class MakeStringRequest extends  MethodRequest {
    private final  int count;
    private final char fillChar;

    /**
     * 由于MakeStringRequest 要有返回值，返回值有可能陷入阻塞 执行时间比较长
     * 那么立即给一个FutureResult,
     * @param servant
     * @param futureResult
     * @param count
     * @param fillChar
     */
    MakeStringRequest(Servant servant, FutureResult futureResult, int count, char fillChar) {
        super(servant, futureResult);
        this.fillChar = fillChar;
        this.count = count;
    }

    @Override
    public void execute() {
        Result result = servant.makeString(count, fillChar);
        futureResult.setResult(result);
    }

}

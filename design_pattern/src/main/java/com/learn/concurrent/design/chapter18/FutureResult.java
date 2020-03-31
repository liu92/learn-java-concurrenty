package com.learn.concurrent.design.chapter18;

/**
 * @ClassName: FutureResult
 * @Description:
 * @Author: lin
 * @Date: 2020/3/30 15:52
 * History:
 * @<version> 1.0
 */
public class FutureResult implements  Result {
    private Result result;

    private boolean ready = false;

    /**
     * 将真正的result 传入,当别人通过 下面的getResultValue方法来
     * 取的时候，如果没有准备好 那么就wait,如果wait好了之后就将真正的结果给它
     * @param result
     */
    public  synchronized  void setResult(Result result) {
        this.result = result;
        this.ready = true;
        this.notifyAll();
    }

    @Override
    public synchronized Object getResultValue() {
        while (!ready){
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return this.result.getResultValue();
    }
}

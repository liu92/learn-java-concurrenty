package com.learn.concurrent.design.chapter18;

/**
 * @ClassName: MakeStringRequest
 * @Description:
 * @Author: lin
 * @Date: 2020/3/30 16:06
 * History:
 * @<version> 1.0
 * {@link ActiveObject#displayString(String)}
 */
public class DisplayStringRequest extends  MethodRequest {
    private final String text;

    DisplayStringRequest(Servant servant, final  String text) {
        super(servant, null);
        this.text = text;
    }

    @Override
    public void execute() {
        this.servant.displayString(text);
    }

}

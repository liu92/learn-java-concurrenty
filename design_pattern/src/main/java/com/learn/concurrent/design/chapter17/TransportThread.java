package com.learn.concurrent.design.chapter17;

import java.util.Random;

/**
 * 这个相当于流水线上的搬运工
 * @ClassName: TransportThread
 * @Description:
 * @Author: lin
 * @Date: 2020/3/30 9:58
 * History:
 * @<version> 1.0
 */
public class TransportThread extends  Thread {
    private  final  Channel channel;

    private static  final Random RANDOM = new Random(System.currentTimeMillis());

    public  TransportThread(String name,Channel channel){
        super(name);
        this.channel = channel;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; true; i++) {
                RequestCommodity request = new RequestCommodity(getName(), i);
                // 不同的搬运工，效率不一样 有快有慢
                this.channel.put(request);
                Thread.sleep(RANDOM.nextInt(1_000));
            }
        }catch (Exception e){

        }

    }







}

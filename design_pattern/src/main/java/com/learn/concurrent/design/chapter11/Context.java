package com.learn.concurrent.design.chapter11;

/**
 * @ClassName: Context
 * @Description:
 * @Author: lin
 * @Date: 2020/3/27 22:21
 * History:
 * @<version> 1.0
 */
public class Context {

    private  String name;

    private String cardId;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCardId() {
        return cardId;
    }
}

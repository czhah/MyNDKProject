package com.thedream.cz.myndkproject.data.entity;

/**
 * Created by cz on 2017/12/14.
 * 多布局
 */

public class MultiItemInfo {

    private int type;
    private int text;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getText() {
        return text;
    }

    public void setText(int text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "MultiItemInfo{" +
                "type=" + type +
                ", text=" + text +
                '}';
    }
}

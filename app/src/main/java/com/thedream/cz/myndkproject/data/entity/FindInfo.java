package com.thedream.cz.myndkproject.data.entity;

/**
 * Created by cz on 2017/12/1.
 * 发现页面数据
 */
public class FindInfo {

    private String text;

    private int width;

    private int height;

    public String toString() {
        return "FindInfo{" +
                "text='" + text + '\'' +
                ", width=" + width +
                ", height=" + height +
                '}';
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}

package com.thedream.cz.myndkproject.data.entity;

import com.thedream.cz.myndkproject.utils.PrintUtil;

import java.util.ArrayList;

/**
 * Created by cz on 2017/12/17.
 * 原型模式
 * 1、克隆只发生在引用类型，值类型(非引用)类型不需要克隆
 * 2、浅拷贝是副本的字段引用原始文档的字段，例如A、B都指向同一个引用，修改B后A也会跟着改变
 * <p>
 * 优点：
 * 1、原型模式是在内存中二进制流的拷贝，要比直接new一个对象性能好得多，
 * 特别是要在一个循环体中产生大量的对象时，使用原型模式比较好
 * 2、这既是优点也是缺点，直接在内存中拷贝，构造函数不会执行
 */

public class PrototypeInfo implements Cloneable {

    private String text;
    private ArrayList<String> imageList = new ArrayList<>();

    public PrototypeInfo() {
        PrintUtil.printCZ("我是构造函数");
    }

    public String getText() {
        return text;
    }

    public ArrayList<String> getImageList() {
        return imageList;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void addImage(String img) {
        imageList.add(img);
    }

    public void show() {
        PrintUtil.printCZ("---------------我是原型模式-------------");
        PrintUtil.printCZ("text: " + text);
        for (String img : imageList) {
            PrintUtil.printCZ("image: " + img);
        }
    }

    @Override
    public PrototypeInfo clone() {
        try {
            PrototypeInfo info = (PrototypeInfo) super.clone();
            //  值类型(非引用类型)不需要克隆，修改值类型不会引起原始文档的字段改变
            info.text = this.text;
            //  浅拷贝
//            info.imageList = this.imageList;
            //  深拷贝
            info.imageList = (ArrayList<String>) this.imageList.clone();
            return info;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}

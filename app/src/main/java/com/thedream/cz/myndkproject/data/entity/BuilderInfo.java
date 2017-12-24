package com.thedream.cz.myndkproject.data.entity;

import com.thedream.cz.myndkproject.utils.PrintUtil;

/**
 * Created by cz on 2017/12/17.
 * Builder模式
 * 1、Builder()创建一个Builder
 * 2、setter方法都返回的是Builder，也就是return this;
 * 3、最后create() 才创建想要的类 BuilderInfo
 * new Builder().setA().setB().create();
 * 优点：
 * 1、良好封装性，不必对外暴露实现细节
 * 2、便于扩展
 * 缺点：
 * 1、产生多余的Builder，消耗内存
 */

public class BuilderInfo {

    private String name;
    private int age;
    private String address;

    @Override
    public String toString() {
        return "BuilderInfo{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", address='" + address + '\'' +
                '}';
    }

    public void show() {
        PrintUtil.printCZ("我是建造者模式: " + toString());
    }

    public static class Builder {
        private final BuilderInfo info;

        public Builder() {
            info = new BuilderInfo();
        }

        public Builder setName(String name) {
            info.name = name;
            return this;
        }

        public Builder setAge(int age) {
            info.age = age;
            return this;
        }

        public Builder setAddress(String address) {
            info.address = address;
            return this;
        }

        public BuilderInfo create() {
            return info;
        }
    }

}

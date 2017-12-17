package com.thedream.cz.myndkproject.data.entity.helper;

import com.thedream.cz.myndkproject.listener.CalculateStrategy;

/**
 * Created by cz on 2017/12/17.
 * 策略模式
 * 主要用来分离算法，在相同的行为抽象下有不同的具体实现策略，这个模式
 * 很好展示了开闭原则，也就是定义抽象，注入不同的实现，从而达到很好的扩展性
 * 优点
 * 1、结构清晰明了，使用简单直观
 * 2、耦合性相对而言较低，扩展方便
 * 3、操作封装也更为彻底，数据更加安全
 * 缺点
 * 1、会增加更多的子类
 */

public class TranficCalculator {

    private CalculateStrategy strategy;

    public void setCalculateStrategy(CalculateStrategy calculateStrategy) {
        this.strategy = calculateStrategy;
    }

    public int price(int km) {
        return strategy.calculatePrice(km);
    }
}

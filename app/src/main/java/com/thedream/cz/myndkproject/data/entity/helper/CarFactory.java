package com.thedream.cz.myndkproject.data.entity.helper;

import com.thedream.cz.myndkproject.data.entity.BaseCar;

/**
 * Created by cz on 2017/12/17.
 * 工厂模式：
 * 优点：
 * 1、优秀的可扩展性
 * 缺点：
 * 1、扩展时需要新增类，会导致类结构的复杂化
 */

public class CarFactory {

    public static <T extends BaseCar> T getCar(Class<T> car) {
        BaseCar baseCar = null;
        try {
            baseCar = (BaseCar) Class.forName(car.getName()).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return (T) baseCar;
    }
}

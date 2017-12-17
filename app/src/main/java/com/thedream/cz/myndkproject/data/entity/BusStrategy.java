package com.thedream.cz.myndkproject.data.entity;

import com.thedream.cz.myndkproject.listener.CalculateStrategy;

/**
 * Created by cz on 2017/12/17.
 */

public class BusStrategy implements CalculateStrategy {

    @Override
    public int calculatePrice(int km) {
        if (km <= 10) return 1;
        //  超过10公里部分
        int extraTotal = km - 10;
        //  超过部分是5的倍数
        int extraFactor = extraTotal / 5;
        //  超过部分取余
        int fraction = extraTotal % 5;
        int price = 1 + extraFactor * 1;
        return fraction > 0 ? ++price : price;
    }
}

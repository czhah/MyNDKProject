package com.thedream.cz.myndkproject.listener;

/**
 * Created by cz on 2017/12/17.
 */

public interface CalculateStrategy {

    /**
     * 根据距离计算价格
     *
     * @param km
     * @return
     */
    int calculatePrice(int km);
}

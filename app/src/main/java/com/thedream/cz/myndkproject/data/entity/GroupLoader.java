package com.thedream.cz.myndkproject.data.entity;

import com.thedream.cz.myndkproject.utils.PrintUtil;

/**
 * Created by cz on 2017/12/23.
 */

public class GroupLoader extends Leader {
    @Override
    public int limit() {
        return 1000;
    }

    @Override
    public void handle(int money) {
        PrintUtil.printCZ("GroupLoader 批准了:" + money + " 元");
    }
}

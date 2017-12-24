package com.thedream.cz.myndkproject.data.entity;

import com.thedream.cz.myndkproject.utils.PrintUtil;

/**
 * Created by cz on 2017/12/23.
 */

public class BossLeader extends Leader {
    @Override
    public int limit() {
        return 10000;
    }

    @Override
    public void handle(int money) {
        PrintUtil.printCZ("BossLeader 批准了:" + money + " 元");
    }
}

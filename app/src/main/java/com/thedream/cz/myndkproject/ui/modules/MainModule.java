package com.thedream.cz.myndkproject.ui.modules;

import com.thedream.cz.myndkproject.bean.SideInfo;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2017/11/23.
 */
@Module
public class MainModule {

    @Provides
    public SideInfo provideSideInfo() {
        SideInfo info = new SideInfo();
        info.setId(111);
        info.setName("haha");
        return info;
    }
}

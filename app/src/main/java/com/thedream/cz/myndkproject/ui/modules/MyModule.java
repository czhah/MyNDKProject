package com.thedream.cz.myndkproject.ui.modules;

import com.thedream.cz.myndkproject.bean.MyInfo;

import java.util.Random;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2017/11/23.
 */
@Module
public class MyModule {

    private final int id;
    private final String name;

    public MyModule(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Provides
    int provideId() {
        return id;
    }

    @Provides
    String provideName() {
        return name;
    }


    @Provides
    MyInfo provideMyInfo() {
        Random rnd = new Random();
        MyInfo info = new MyInfo();
        info.setId(rnd.nextInt(500));
        return info;
    }
}

package com.thedream.cz.myndkproject.ui.modules;

import com.thedream.cz.myndkproject.ui.scopes.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2017/11/23.
 */
@PerActivity
@Module
public class SideInfoModule {

    private final int id;
    private final String name;

    public SideInfoModule(int id, String name) {
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
}

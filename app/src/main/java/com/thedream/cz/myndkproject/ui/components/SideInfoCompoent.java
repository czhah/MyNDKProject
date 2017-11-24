package com.thedream.cz.myndkproject.ui.components;

import com.thedream.cz.myndkproject.activity.MyDraggerActivity;
import com.thedream.cz.myndkproject.ui.modules.MyModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Administrator on 2017/11/23.
 */
@Singleton
@Component(modules = MyModule.class)
public interface SideInfoCompoent {

    void inject(MyDraggerActivity activity);
}

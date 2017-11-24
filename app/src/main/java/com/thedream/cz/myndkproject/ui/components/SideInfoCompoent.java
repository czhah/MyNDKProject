package com.thedream.cz.myndkproject.ui.components;

import com.thedream.cz.myndkproject.activity.MyDraggerActivity;
import com.thedream.cz.myndkproject.ui.modules.MyModule;

import dagger.Component;

/**
 * Created by Administrator on 2017/11/23.
 */
@Component(modules = MyModule.class)
public interface SideInfoCompoent {

    void inject(MyDraggerActivity activity);
}

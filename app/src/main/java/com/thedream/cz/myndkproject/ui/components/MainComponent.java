package com.thedream.cz.myndkproject.ui.components;

import com.thedream.cz.myndkproject.activity.MainActivity;
import com.thedream.cz.myndkproject.base.AppComponent;
import com.thedream.cz.myndkproject.bean.SideInfo;
import com.thedream.cz.myndkproject.ui.modules.MainModule;
import com.thedream.cz.myndkproject.ui.scopes.PerActivity;

import dagger.Component;

/**
 * Created by Administrator on 2017/11/23.
 */
@PerActivity
@Component(dependencies = AppComponent.class, modules = MainModule.class)
public interface MainComponent {

    //  对MainActivity进行依赖注入
    void inject(MainActivity activity);

    SideInfo getSideInfo();
}

package com.thedream.cz.myndkproject.injection.components;


import com.thedream.cz.myndkproject.injection.modules.ActivityModule;
import com.thedream.cz.myndkproject.ui.common.FirstActivity;

import dagger.Component;

/**
 * Created by Administrator on 2017/11/24.
 */
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(FirstActivity activity);
}

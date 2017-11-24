package com.thedream.cz.myndkproject.injection.components;

import com.thedream.cz.myndkproject.injection.modules.ApplicationModule;

import dagger.Component;

/**
 * Created by cz on 2017/11/24.
 * 全局的Component
 */
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
}

package com.thedream.cz.myndkproject.ui.common;

import android.support.multidex.MultiDexApplication;

import com.thedream.cz.myndkproject.injection.components.ApplicationComponent;
import com.thedream.cz.myndkproject.injection.components.DaggerApplicationComponent;
import com.thedream.cz.myndkproject.injection.modules.ApplicationModule;

/**
 * Created by Administrator on 2017/11/24.
 */

public class BaseApplication extends MultiDexApplication {

    private ApplicationComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();
    }

    public ApplicationComponent getAppComponent() {
        return appComponent;
    }
}

package com.thedream.cz.myndkproject.ui.common;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.thedream.cz.myndkproject.common.AppExecutors;

/**
 * Created by Administrator on 2017/11/24.
 */

public class BaseApplication extends MultiDexApplication {


    public static Context mApplication;
    private AppExecutors appExecutors;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;

        appExecutors = new AppExecutors();
    }

    public AppExecutors getAppExecutors() {
        return appExecutors;
    }

}

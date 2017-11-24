package com.thedream.cz.myndkproject.ui.modules;

import android.app.Activity;

/**
 * Created by Administrator on 2017/11/23.
 * 提供baseActivity的module
 */
public class ActivityModule {

    private final Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    public Activity provideActivity() {
        return activity;
    }
}

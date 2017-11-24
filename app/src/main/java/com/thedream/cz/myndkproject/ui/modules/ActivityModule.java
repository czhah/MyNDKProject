package com.thedream.cz.myndkproject.ui.modules;

import android.app.Activity;

import com.thedream.cz.myndkproject.ui.scopes.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2017/11/23.
 * 提供baseActivity的module
 */
@Module
public class ActivityModule {

    private final Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    @PerActivity
    public Activity provideActivity() {
        return activity;
    }
}

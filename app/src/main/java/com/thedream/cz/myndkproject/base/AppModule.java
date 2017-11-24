package com.thedream.cz.myndkproject.base;

import android.content.Context;

import com.thedream.cz.myndkproject.utils.ToastUtil;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2017/11/23.
 */
@Module
public class AppModule {

    Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return context;
    }

    @Provides
    @Singleton
    public ToastUtil provideToast() {
        return new ToastUtil(context);
    }
}

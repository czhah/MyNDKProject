package com.thedream.cz.myndkproject.base;

import android.content.Context;

import com.thedream.cz.myndkproject.utils.ToastUtil;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Administrator on 2017/11/23.
 */
@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    Context getContext();

    ToastUtil getToastUtil();
}

package com.thedream.cz.myndkproject.base;

import android.app.Application;

import com.cz.resource.skintool.listener.ILoadSkinListener;
import com.cz.resource.skintool.manager.SkinManager;

/**
 * Created by Administrator on 2017/11/1.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.getInstance().init(this);
        SkinManager.getInstance().loadSkin(new ILoadSkinListener() {
            @Override
            public void onPrepare() {

            }

            @Override
            public void onSuccess() {

            }

            @Override
            public void onFail() {

            }

            @Override
            public void onCancel() {

            }
        });
    }
}

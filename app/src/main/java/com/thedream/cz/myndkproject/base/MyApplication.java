package com.thedream.cz.myndkproject.base;

import android.app.Application;

import com.cz.resource.skintool.listener.ILoadSkinListener;
import com.cz.resource.skintool.manager.SkinManager;
import com.thedream.cz.myndkproject.common.AppExecutors;
import com.thedream.cz.myndkproject.common.DataRepository;
import com.thedream.cz.myndkproject.db.AppDatabase;

/**
 * Created by Administrator on 2017/11/1.
 */

public class MyApplication extends Application {

    private AppExecutors appExecutors;

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

        });

        appExecutors = new AppExecutors();
    }

    public AppExecutors getAppExecutors() {
        return appExecutors;
    }

    public AppDatabase getAppDatabase() {
        return AppDatabase.getInstance(this, appExecutors);
    }

    public DataRepository getDataRepository() {
        return DataRepository.getInstance(getAppDatabase());
    }
}

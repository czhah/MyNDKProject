package com.thedream.cz.myndkproject.ui.common;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.thedream.cz.myndkproject.data.CommonDataRepository;
import com.thedream.cz.myndkproject.data.UserDataRepository;
import com.thedream.cz.myndkproject.data.local.AppLocalData;

/**
 * Created by Administrator on 2017/11/24.
 */

public class BaseApplication extends MultiDexApplication {


    public static Context mApplication;
    //  公共数据类
    private CommonDataRepository mCommonDataRepository;
    //  用户数据类
    private UserDataRepository mUserDataRepository;
    //  活动数据类
    //  好友数据类

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;

        AppLocalData mAppLocalData = AppLocalData.getInstance(mApplication);

        mCommonDataRepository = CommonDataRepository.getInstance(mAppLocalData.getLoginDao());

        mUserDataRepository = UserDataRepository.getInstance();
    }


    public CommonDataRepository getCommonDataRepository() {
        return mCommonDataRepository;
    }

    public UserDataRepository getUserDataRepository() {
        return mUserDataRepository;
    }
}

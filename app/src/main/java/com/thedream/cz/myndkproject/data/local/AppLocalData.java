package com.thedream.cz.myndkproject.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.thedream.cz.myndkproject.data.entity.LoginInfo;
import com.thedream.cz.myndkproject.data.local.dao.LoginDao;

/**
 * Created by cz on 2017/11/29.
 * 本地数据库
 */
@Database(entities = {LoginInfo.class}, version = AppLocalData.DATABASE_VERSION, exportSchema = false)
public abstract class AppLocalData extends RoomDatabase {

    public static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "ndk_db";

    private static AppLocalData instance;

    public abstract LoginDao mLoginDao();

    public static AppLocalData getInstance(Context context) {
        if (null == instance) {
            synchronized (AppLocalData.class) {
                if (null == instance) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppLocalData.class, DATABASE_NAME).build();
                }
            }
        }
        return instance;
    }

    public LoginDao getLoginDao() {
        return instance.mLoginDao();
    }
}

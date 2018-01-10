package com.thedream.cz.myndkproject.data.local;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

import com.thedream.cz.myndkproject.constant.DatabaseConstant;
import com.thedream.cz.myndkproject.data.entity.CityInfo;
import com.thedream.cz.myndkproject.data.entity.DeviceInfo;
import com.thedream.cz.myndkproject.data.entity.LoginInfo;
import com.thedream.cz.myndkproject.data.local.dao.CityDao;
import com.thedream.cz.myndkproject.data.local.dao.DeviceDao;
import com.thedream.cz.myndkproject.data.local.dao.LoginDao;

/**
 * Created by cz on 2017/11/29.
 * 本地数据库
 */
@Database(entities = {LoginInfo.class, CityInfo.class, DeviceInfo.class}, version = DatabaseConstant.DATABASE_VERSION, exportSchema = false)
public abstract class AppLocalData extends RoomDatabase {

    private static AppLocalData instance;

    public abstract LoginDao loginDao();

    public abstract CityDao cityDao();

    public abstract DeviceDao deviceDao();

    public synchronized static AppLocalData getInstance(Context context) {
        if (null == instance) {
            synchronized (AppLocalData.class) {
                if (null == instance) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppLocalData.class, DatabaseConstant.DATABASE_NAME)
                            .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                            .build();
                }
            }
        }
        return instance;
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL(DatabaseConstant.CREATE_CITY_1_2);
        }
    };

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE DeviceInfo (id TEXT, name TEXT, address TEXT, PRIMARY KEY(id))");
        }
    };

}

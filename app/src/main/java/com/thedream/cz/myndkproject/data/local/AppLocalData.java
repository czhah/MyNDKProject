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
import com.thedream.cz.myndkproject.data.entity.DrinkInfo;
import com.thedream.cz.myndkproject.data.entity.LoginInfo;
import com.thedream.cz.myndkproject.data.local.dao.CityDao;
import com.thedream.cz.myndkproject.data.local.dao.DeviceDao;
import com.thedream.cz.myndkproject.data.local.dao.DrinkDao;
import com.thedream.cz.myndkproject.data.local.dao.LoginDao;

/**
 * Created by cz on 2017/11/29.
 * 本地数据库
 */
@Database(entities = {LoginInfo.class, CityInfo.class, DeviceInfo.class, DrinkInfo.class}, version = DatabaseConstant.DATABASE_VERSION, exportSchema = false)
public abstract class AppLocalData extends RoomDatabase {

    private static AppLocalData instance;

    public abstract LoginDao loginDao();

    public abstract CityDao cityDao();

    public abstract DeviceDao deviceDao();

    public abstract DrinkDao drinkDao();

    public synchronized static AppLocalData getInstance(Context context) {
        if (null == instance) {
            synchronized (AppLocalData.class) {
                if (null == instance) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppLocalData.class, DatabaseConstant.DATABASE_NAME)
                            .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6)
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
            database.execSQL("CREATE TABLE DeviceInfo (id TEXT not null, name TEXT, address TEXT, PRIMARY KEY(id))");
        }
    };

    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE tb_city ADD COLUMN city_address TEXT");
        }
    };

    static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL(DatabaseConstant.CREATE_DRINK_4_5);
        }
    };

    static final Migration MIGRATION_5_6 = new Migration(5, 6) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL(DatabaseConstant.ALERT_DRINK_5_6);
        }
    };

}

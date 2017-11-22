package com.thedream.cz.myndkproject.db;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.thedream.cz.myndkproject.common.AppExecutors;
import com.thedream.cz.myndkproject.db.dao.ProductDao;
import com.thedream.cz.myndkproject.db.entity.ProductEntity;
import com.thedream.cz.myndkproject.utils.PrintUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/11/22.
 */
@Database(entities = {ProductEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase sInstance;

    public abstract ProductDao productDao();

    public static final String DATABASE_NAME = "my-ndk-db";

    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();

    public static AppDatabase getInstance(final Context context, final AppExecutors executors) {
        if (sInstance == null) {
            synchronized (AppDatabase.class) {
                if (sInstance == null) {
                    sInstance = buildDatabase(context.getApplicationContext(), executors);
                    sInstance.updateDatabaseCreated(context.getApplicationContext());
                }
            }
        }

        return sInstance;
    }

    private void updateDatabaseCreated(Context context) {
        PrintUtil.printCZ("updateDatabaseCreated()");
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            setDatabaseCreated();
        }
    }

    private static AppDatabase buildDatabase(Context applicationContext, AppExecutors executors) {
        PrintUtil.printCZ("创建数据库!!!");
        return Room.databaseBuilder(applicationContext,
                AppDatabase.class, DATABASE_NAME)
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        executors.diskIO().execute(() -> {
                            addDelay();
                            AppDatabase appDatabase = AppDatabase.getInstance(applicationContext, executors);
                            List<ProductEntity> productEntities = DataGenerator.generateProducts();
                            insertData(appDatabase, productEntities);
                            appDatabase.setDatabaseCreated();
                        });
                    }
                }).build();
    }

    private static void addDelay() {
        PrintUtil.print("数据库模拟延时。。。");
        try {
            Thread.sleep(4000);
        } catch (InterruptedException ignored) {
        }
    }

    private void setDatabaseCreated() {
        PrintUtil.printCZ("通知数据库创建成功！！");
        mIsDatabaseCreated.setValue(true);
    }

    private static void insertData(AppDatabase appDatabase, List<ProductEntity> productEntities) {
        appDatabase.runInTransaction(() -> {
            PrintUtil.printCZ("插入数据insertData  假数据");
            appDatabase.productDao().insertAll(productEntities);
        });
    }

    public LiveData<Boolean> getDatabaseCreated() {
        return mIsDatabaseCreated;
    }
}

package com.thedream.cz.myndkproject.common;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;

import com.thedream.cz.myndkproject.db.AppDatabase;
import com.thedream.cz.myndkproject.db.entity.ProductEntity;
import com.thedream.cz.myndkproject.utils.PrintUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2017/11/22.
 */

public class DataRepository {

    private static DataRepository sInstance;
    private AppDatabase mDatabase;
    private MediatorLiveData<List<ProductEntity>> mObservableProducts;

    private DataRepository() {
    }

    private DataRepository(AppDatabase database) {
        PrintUtil.printCZ("DataRepository的构造方法");
        mDatabase = database;
        mObservableProducts = new MediatorLiveData<>();
        mObservableProducts.addSource(mDatabase.productDao().loadAllProducts(), productEntities -> {
            PrintUtil.printCZ("DataRepository 数据有更改:" + (mDatabase.getDatabaseCreated().getValue() != null));
            if (mDatabase.getDatabaseCreated().getValue() != null) {
                mObservableProducts.setValue(productEntities);
            }
        });
    }

    public static DataRepository getInstance(AppDatabase appDatabase) {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository(appDatabase);
                }
            }
        }
        return sInstance;
    }

    /**
     * Get the list of products from the database and get notified when the data changes.
     */
    public LiveData<List<ProductEntity>> getProducts() {
        return mObservableProducts;
    }

    public LiveData<ProductEntity> loadProduct(final int productId) {
        return mDatabase.productDao().loadProduct(productId);
    }

    public void loadMore() {
        AppExecutors appExecutors = new AppExecutors();
        appExecutors.diskIO().execute(() -> {
            PrintUtil.printCZ("加载更多数据");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            List<ProductEntity> list = new ArrayList<ProductEntity>();
            Random random = new Random();
            for (int i = 0; i < 50; i++) {
                ProductEntity productEntity = new ProductEntity();
                productEntity.setId(random.nextInt(1000 * i));
                productEntity.setName("加载更多：" + i);
                productEntity.setDescription(" xxx");
                productEntity.setPrice(5000 + i);
            }
            mDatabase.runInTransaction(() -> {
                mDatabase.productDao().insertAll(list);
            });

        });
    }

}

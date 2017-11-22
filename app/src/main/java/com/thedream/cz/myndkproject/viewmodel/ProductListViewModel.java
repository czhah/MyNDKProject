package com.thedream.cz.myndkproject.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.thedream.cz.myndkproject.base.MyApplication;
import com.thedream.cz.myndkproject.common.DataRepository;
import com.thedream.cz.myndkproject.db.entity.ProductEntity;
import com.thedream.cz.myndkproject.utils.PrintUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/11/22.
 */

public class ProductListViewModel extends AndroidViewModel {

    private final MediatorLiveData<List<ProductEntity>> mObservableProducts;
    //  管理数据库类
    private final DataRepository repository;

    public ProductListViewModel(@NonNull Application application) {
        super(application);
        PrintUtil.printCZ("执行了ProductListViewModel的构造方法");
        mObservableProducts = new MediatorLiveData<>();
        mObservableProducts.setValue(null);
        repository = ((MyApplication) application).getDataRepository();
        LiveData<List<ProductEntity>> products = repository.getProducts();
        mObservableProducts.addSource(products, new Observer<List<ProductEntity>>() {
            @Override
            public void onChanged(@Nullable List<ProductEntity> productEntities) {
                PrintUtil.printCZ("添加更改监听");
                mObservableProducts.setValue(productEntities);
            }
        });
    }

    public LiveData<List<ProductEntity>> getmObservableProducts() {
        return mObservableProducts;
    }

    public void loadMore() {
        repository.loadMore();
    }


}

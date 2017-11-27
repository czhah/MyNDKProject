package com.thedream.cz.myndkproject.ui.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.thedream.cz.myndkproject.ui.base.IBasePresenter;
import com.thedream.cz.myndkproject.ui.base.IBaseView;

/**
 * Created by cz on 2017/11/24.
 * Activity的基类
 */

public abstract class BaseActivity<P extends IBasePresenter> extends AppCompatActivity implements IBaseView<P> {

    protected P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPresenter(mPresenter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

package com.thedream.cz.myndkproject.ui.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.thedream.cz.myndkproject.injection.components.ApplicationComponent;
import com.thedream.cz.myndkproject.injection.modules.ActivityModule;
import com.thedream.cz.myndkproject.mvp.presenter.BasePresenter;
import com.thedream.cz.myndkproject.mvp.views.IBaseView;

import javax.inject.Inject;

/**
 * Created by cz on 2017/11/24.
 * Activity的基类
 */

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements IBaseView {

    @Inject
    protected P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActivityComponent(((BaseApplication) getApplication()).getAppComponent(), new ActivityModule(this));
        mPresenter.attachView(this);
    }

    protected abstract void setupActivityComponent(ApplicationComponent appComponent, ActivityModule activityModule);

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }
}

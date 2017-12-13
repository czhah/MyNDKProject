package com.thedream.cz.myndkproject.mvp.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.thedream.cz.myndkproject.mvp.factory.PresenterMvpFactoryImpl;
import com.thedream.cz.myndkproject.mvp.presenter.BaseMvpPresenter;
import com.thedream.cz.myndkproject.mvp.proxy.BaseMvpProxy;
import com.thedream.cz.myndkproject.mvp.proxy.PresenterProxyInterface;

/**
 * Created by cz on 2017/12/9.
 *   Fragment基类
 */

public class BaseMvpFragment<V extends BaseMvpView, P extends BaseMvpPresenter<V>> extends Fragment implements PresenterProxyInterface<V, P> {

    private static final String PRESENTER_SAVE_KEY = "presenter_save_key";

    //  P层代理类
    private BaseMvpProxy<V, P> mProxy = new BaseMvpProxy<>(PresenterMvpFactoryImpl.<V, P>createFactory(getClass()));

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mProxy.onRestoreInstanceState(savedInstanceState.getBundle(PRESENTER_SAVE_KEY));
        }
        mProxy.onCreate((V) this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mProxy.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle(PRESENTER_SAVE_KEY, mProxy.onSaveInstanceState());
    }


    @Override
    public P getMvpPresenter() {
        return mProxy.getMvpPresenter();
    }
}

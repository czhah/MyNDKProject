package com.thedream.cz.myndkproject.mvp.proxy;

import android.os.Bundle;

import com.thedream.cz.myndkproject.mvp.factory.PresenterMvpFactory;
import com.thedream.cz.myndkproject.mvp.presenter.BaseMvpPresenter;
import com.thedream.cz.myndkproject.mvp.view.BaseMvpView;
import com.thedream.cz.myndkproject.utils.PrintUtil;

/**
 * Created by cz on 2017/12/9.
 */

public class BaseMvpProxy<V extends BaseMvpView, P extends BaseMvpPresenter<V>> implements PresenterProxyInterface<V, P> {

    /**
     * 获取onSaveInstanceState中的bundle的key
     */
    private static final String PRESENTER_KEY = "presenter_key";
    private PresenterMvpFactory<V, P> mFactory;
    private P mPresenter;
    private Bundle mBundle;
    private boolean isAttchView;

    public BaseMvpProxy(PresenterMvpFactory<V, P> mFactory) {
        this.mFactory = mFactory;
    }

    /**
     * 获取创建的Presenter
     * 如果之前创建过，而且是意外销毁则从Bundle中恢复
     *
     * @return
     */
    @Override
    public P getMvpPresenter() {
        PrintUtil.printE("Proxy getMvpPresenter");
        if (mFactory != null) {
            if (mPresenter == null) {
                PrintUtil.printE("Proxy create MvpPresenter");
                mPresenter = mFactory.createMvpPresenter();
                mPresenter.onCreatePresenter(mBundle == null ? null : mBundle.getBundle(PRESENTER_KEY));
            }
        }
        return mPresenter;
    }

    public void onCreate(V mvpView) {
        getMvpPresenter();
        if (mPresenter != null && !isAttchView) {
            isAttchView = true;
            mPresenter.onAttachMvpView(mvpView);
        }
    }

    private void onDetachMvpView() {
        if (mPresenter != null && isAttchView) {
            mPresenter.onDetachMvpView();
            isAttchView = false;
        }
    }

    public void onDestroy() {
        if (mPresenter != null) {
            onDetachMvpView();
            mPresenter.onDestroyPresenter();
            mPresenter = null;
        }
    }

    public Bundle onSaveInstanceState() {
        PrintUtil.printE("Proxy  onSaveInstanceState() = 保存信息" + (mPresenter != null));
        Bundle bundle = new Bundle();
        getMvpPresenter();
        if (mPresenter != null) {
            //  这里待处理！！！
            Bundle presenterBundle = new Bundle();
            mPresenter.onSaveInstanceState(presenterBundle);
            bundle.putBundle(PRESENTER_KEY, presenterBundle);
        }
        return bundle;
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        PrintUtil.printE("Proxy  onRestoreInstanceState = 恢复信息" + (mPresenter != null));
        mBundle = savedInstanceState;
    }
}

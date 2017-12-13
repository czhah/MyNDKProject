package com.thedream.cz.myndkproject.mvp.proxy;

import android.os.Bundle;

import com.thedream.cz.myndkproject.mvp.factory.PresenterMvpFactory;
import com.thedream.cz.myndkproject.mvp.presenter.BaseMvpPresenter;
import com.thedream.cz.myndkproject.mvp.view.BaseMvpView;
import com.thedream.cz.myndkproject.utils.PrintUtil;

/**
 * Created by cz on 2017/12/9.
 *  P层工厂
 */
public class BaseMvpProxy<V extends BaseMvpView, P extends BaseMvpPresenter<V>> implements PresenterProxyInterface<V, P> {

    private static final String PRESENTER_KEY = "presenter_key";

    //  P层工程
    private PresenterMvpFactory<V, P> mFactory;
    //  P层引用
    private P mPresenter;
    //  是否覆盖到View层上面
    private boolean mIsAttchView;
    //  用于保存P层中的数据
    private Bundle mBundle;

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
            PrintUtil.printCZ("是否需要创建P层:" + (mPresenter == null));
            if (mPresenter == null) {
                PrintUtil.printCZ("在此处创建P层" + (mBundle == null));
                mPresenter = mFactory.createMvpPresenter();
                mPresenter.onCreatePresenter(mBundle == null ? null : mBundle.getBundle(PRESENTER_KEY));
            }
        }
        return mPresenter;
    }

    /**
     * 创建P层
     *
     * @param mvpView
     */
    public void onCreate(V mvpView) {
        getMvpPresenter();
        if (mPresenter != null && !mIsAttchView) {
            mIsAttchView = true;
            mPresenter.onAttachMvpView(mvpView);
        }
    }

    /**
     * 销毁P层
     */
    public void onDestroy() {
        if (mPresenter != null) {
            onDetachMvpView();
            mPresenter.onDestroyPresenter();
            mPresenter = null;
        }
    }

    /**
     * 销毁P层中保存的数据
     */
    private void onDetachMvpView() {
        if (mPresenter != null && mIsAttchView) {
            mPresenter.onDetachMvpView();
            mIsAttchView = false;
        }
    }

    /**
     * 用于保存因意外销毁时需要保存的数据
     * @return
     */
    public Bundle onSaveInstanceState() {
        PrintUtil.printE("Proxy  onSaveInstanceState() = 保存信息 判断mPresenter是否为空" + (mPresenter != null));
        getMvpPresenter();

        Bundle bundle = new Bundle();
        if (mPresenter != null) {
            bundle.putBundle(PRESENTER_KEY, mPresenter.onSaveInstanceState());
        }
        return bundle;
    }

    /**
     * 恢复意外销毁的数据
     * @param savedInstanceState
     */
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        PrintUtil.printE("Proxy  onRestoreInstanceState = 恢复信息" + (mPresenter != null));
        mBundle = savedInstanceState;
    }
}

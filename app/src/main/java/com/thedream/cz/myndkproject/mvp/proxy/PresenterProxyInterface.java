package com.thedream.cz.myndkproject.mvp.proxy;

import com.thedream.cz.myndkproject.mvp.presenter.BaseMvpPresenter;
import com.thedream.cz.myndkproject.mvp.view.BaseMvpView;

/**
 * Created by cz on 2017/12/9.
 * Presenter代理类的实现类
 */

public interface PresenterProxyInterface<V extends BaseMvpView, P extends BaseMvpPresenter<V>> {

    /**
     * 获取创建的Presenter
     * @return
     */
    P getMvpPresenter();
}

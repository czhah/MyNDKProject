package com.thedream.cz.myndkproject.mvp.factory;

import com.thedream.cz.myndkproject.mvp.presenter.BaseMvpPresenter;
import com.thedream.cz.myndkproject.mvp.view.BaseMvpView;

/**
 * Created by cz on 2017/12/9.
 */

public interface PresenterMvpFactory<V extends BaseMvpView, P extends BaseMvpPresenter<V>> {

    /**
     * 创建Presenter的接口方法
     * @return 需要创建的Presenter
     */
    P createMvpPresenter();
}

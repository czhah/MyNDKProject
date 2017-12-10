package com.thedream.cz.myndkproject.mvp.presenter;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.thedream.cz.myndkproject.mvp.view.BaseMvpView;
import com.thedream.cz.myndkproject.utils.PrintUtil;

/**
 * Created by cz on 2017/12/9.
 * Presenter基类：管理Presenter的声明周期
 */

public class BaseMvpPresenter<V extends BaseMvpView> {

    private V mView;

    /**
     * Presenter被创建后调用
     *
     * @param saveState
     */
    public void onCreatePresenter(@Nullable Bundle saveState) {
        PrintUtil.printE("P onCreatePresenter = ");
    }

    /**
     * 绑定View
     *
     * @param mvpView
     */
    public void onAttachMvpView(V mvpView) {
        mView = mvpView;
        PrintUtil.printE("P onResume = ");
    }

    /**
     * 解除绑定View
     */
    public void onDetachMvpView() {
        mView = null;
        PrintUtil.printE("P onDetachMvpView = ");
    }

    /**
     * Presenter被销毁时调用
     */
    public void onDestroyPresenter() {
        PrintUtil.printE("P onDestroy = ");
    }

    /**
     * 在Presenter意外销毁的时候被调用，它的调用时机和Activity、Fragment、View中的onSaveInstanceState
     *
     * @param outState
     */
    public void onSaveInstanceState(Bundle outState) {
        PrintUtil.printE("P onSaveInstanceState = ");
    }

    /**
     * 获取V层接口View
     *
     * @return 返回当前的MvpView
     */
    public V getMvpView() {
        return mView;
    }
}

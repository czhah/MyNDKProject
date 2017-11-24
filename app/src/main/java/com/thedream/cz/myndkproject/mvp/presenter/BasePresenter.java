package com.thedream.cz.myndkproject.mvp.presenter;

import android.content.Context;

import com.thedream.cz.myndkproject.mvp.views.IBaseView;

/**
 * Created by cz on 2017/11/24.
 * MVP模式 P层
 */

public class BasePresenter<V extends IBaseView> {
    protected V mView;
    protected Context mContext;

    public BasePresenter(Context mContext) {
        this.mContext = mContext;
    }

    public void attachView(V view) {
        this.mView = view;
    }

    public void onDestroy() {
        this.mView = null;
    }

    protected Context getContext() {
        return mContext;
    }
}

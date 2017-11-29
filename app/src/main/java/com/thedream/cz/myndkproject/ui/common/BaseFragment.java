package com.thedream.cz.myndkproject.ui.common;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thedream.cz.myndkproject.IBasePresenter;
import com.thedream.cz.myndkproject.IBaseView;

/**
 * Created by cz on 2017/11/28.
 * 基类的fragment
 */

public abstract class BaseFragment<T extends IBasePresenter> extends Fragment implements IBaseView<T> {

    protected T mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPresenter(mPresenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutInflaterId(), container, false);
    }

    protected abstract int getLayoutInflaterId();
}

package com.thedream.cz.myndkproject.ui.common;

/**
 * Created by Administrator on 2017/11/27.
 */

public class FirstPresenter implements FirstContract.Presenter {

    private FirstContract.View mView;


    public FirstPresenter(FirstContract.View view) {
        this.mView = view;
    }

}

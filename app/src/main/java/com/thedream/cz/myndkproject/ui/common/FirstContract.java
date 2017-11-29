package com.thedream.cz.myndkproject.ui.common;


import com.thedream.cz.myndkproject.IBasePresenter;
import com.thedream.cz.myndkproject.IBaseView;

/**
 * Created by cz on 2017/11/27.
 * MVP架构  接口
 */

public interface FirstContract {

    interface View extends IBaseView<Presenter> {


    }

    interface Presenter extends IBasePresenter {

    }
}

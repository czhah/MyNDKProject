package com.thedream.cz.myndkproject.ui.common;

import com.thedream.cz.myndkproject.ui.base.IBasePresenter;
import com.thedream.cz.myndkproject.ui.base.IBaseView;

/**
 * Created by cz on 2017/11/27.
 * MVP架构  接口
 */

public interface FirstContract {

    interface View extends IBaseView<Presenter> {

        void launchMain();

        void launchSetting();

        void finishActivity();

        void showMissingPermissionDialog();

    }

    interface Presenter extends IBasePresenter {

    }
}

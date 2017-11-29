package com.thedream.cz.myndkproject.ui.activity.user.login;

import com.thedream.cz.myndkproject.IBasePresenter;
import com.thedream.cz.myndkproject.IBaseView;

/**
 * Created by cz on 2017/11/28.
 * 用户登录
 */

public class UserLoginContract {

    interface Presenter extends IBasePresenter {
        void login(String name, String pwd);
    }

    interface View extends IBaseView<Presenter> {
        void showProgress(boolean isShow);

        void onResult(String result);

        void onError(int code);

    }
}

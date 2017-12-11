package com.thedream.cz.myndkproject.ui.activity.user.logintwo;

import com.thedream.cz.myndkproject.mvp.view.BaseMvpView;

/**
 * Created by cz on 2017/12/11.
 */

public interface UserLoginTwoView extends BaseMvpView {

    void showProgress(boolean active);

    void onResult(String result);

    void onError(int code);

    void showTip(String text);
}

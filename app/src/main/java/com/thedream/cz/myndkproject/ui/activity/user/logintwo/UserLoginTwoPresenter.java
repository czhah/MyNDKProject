package com.thedream.cz.myndkproject.ui.activity.user.logintwo;

import android.text.TextUtils;

import com.thedream.cz.myndkproject.bean.WebResultInfo;
import com.thedream.cz.myndkproject.data.CommonDataRepository;
import com.thedream.cz.myndkproject.data.entity.LoginInfo;
import com.thedream.cz.myndkproject.listener.OnResultListener;
import com.thedream.cz.myndkproject.mvp.presenter.BaseMvpPresenter;
import com.thedream.cz.myndkproject.ui.common.BaseApplication;
import com.thedream.cz.myndkproject.utils.PrintUtil;

import java.util.List;

/**
 * Created by cz on 2017/12/11.
 */

public class UserLoginTwoPresenter extends BaseMvpPresenter<UserLoginTwoView> {

    private CommonDataRepository mRepository;
    private String uid;

    public UserLoginTwoPresenter() {
        this.mRepository = ((BaseApplication) BaseApplication.mApplication).getCommonDataRepository();
    }

    private final OnResultListener listener = new OnResultListener<LoginInfo>() {
        @Override
        public void onSuccess(LoginInfo loginInfo) {
            if (getMvpView() == null) return;
            getMvpView().showProgress(false);
            if (loginInfo != null) {
                uid = loginInfo.getUid();
                getMvpView().onResult(loginInfo.toString());
            } else {
                getMvpView().showTip("数据为空");
            }
        }

        @Override
        public void onFailed(int code) {
            if (getMvpView() != null) return;
            getMvpView().showProgress(false);
            getMvpView().onError(code);
        }
    };

    public void login(String name, String pwd) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pwd)) {
            getMvpView().showTip("账号或密码不能为空");
            return;
        }
        getMvpView().showProgress(true);
        mRepository.userLogin(name, pwd, listener);
    }

    public void query() {
        if (TextUtils.isEmpty(uid)) {
            getMvpView().showTip("id不能为空");
            return;
        }
        getMvpView().showProgress(true);
        mRepository.queryLocalLogin(uid, listener);
    }

    public void queryList() {
        mRepository.queryLocalLogin(new OnResultListener<List<LoginInfo>>() {
            @Override
            public void onSuccess(List<LoginInfo> loginInfos) {
                if (loginInfos != null && loginInfos.size() > 0) {
                    for (LoginInfo info : loginInfos) {
                        PrintUtil.printCZ("查询所有:" + info.toString());
                    }
                } else {
                    if (getMvpView() != null) return;
                    getMvpView().showProgress(false);
                    getMvpView().onError(WebResultInfo.RESULT_FAILED);
                }
            }

            @Override
            public void onFailed(int code) {
                if (getMvpView() != null) return;
                getMvpView().showProgress(false);
                getMvpView().onError(code);
            }
        });
    }
}

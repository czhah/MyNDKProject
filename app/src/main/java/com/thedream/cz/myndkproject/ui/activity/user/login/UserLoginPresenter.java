package com.thedream.cz.myndkproject.ui.activity.user.login;

import android.text.TextUtils;

import com.thedream.cz.myndkproject.bean.WebResultInfo;
import com.thedream.cz.myndkproject.data.CommonDataRepository;
import com.thedream.cz.myndkproject.data.entity.LoginInfo;
import com.thedream.cz.myndkproject.listener.OnResultListener;
import com.thedream.cz.myndkproject.utils.PrintUtil;

import java.util.List;

/**
 * Created by cz on 2017/11/28.
 * 用户登录逻辑
 */
public class UserLoginPresenter implements UserLoginContract.Presenter {

    private UserLoginContract.View mView;
    private CommonDataRepository mRepository;
    private String uid;

    public UserLoginPresenter(UserLoginContract.View mView, CommonDataRepository repository) {
        this.mView = mView;
        this.mRepository = repository;
    }

    private final OnResultListener listener = new OnResultListener<LoginInfo>() {
        @Override
        public void onSuccess(LoginInfo loginInfo) {
            if (!mView.isActive()) return;
            mView.showProgress(false);
            if (loginInfo != null) {
                uid = loginInfo.getUid();
                mView.onResult(loginInfo.toString());
            } else {
                mView.showTip("数据为空");
            }
        }

        @Override
        public void onFailed(int code) {
            if (!mView.isActive()) return;
            mView.showProgress(false);
            mView.onError(code);
        }
    };

    @Override
    public void login(String name, String pwd) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pwd)) {
            mView.showTip("账号或密码不能为空");
            return;
        }
        mView.showProgress(true);
        mRepository.userLogin(name, pwd, listener);
    }

    @Override
    public void query() {
        if (TextUtils.isEmpty(uid)) {
            mView.showTip("id不能为空");
            return;
        }
        mView.showProgress(true);
        mRepository.queryLocalLogin(uid, listener);
    }

    @Override
    public void queryList() {
        mRepository.queryLocalLogin(new OnResultListener<List<LoginInfo>>() {
            @Override
            public void onSuccess(List<LoginInfo> loginInfos) {
                if (loginInfos != null && loginInfos.size() > 0) {
                    for (LoginInfo info : loginInfos) {
                        PrintUtil.printCZ("查询所有:" + info.toString());
                    }
                } else {
                    if (!mView.isActive()) return;
                    mView.showProgress(false);
                    mView.onError(WebResultInfo.RESULT_FAILED);
                }
            }

            @Override
            public void onFailed(int code) {
                if (!mView.isActive()) return;
                mView.showProgress(false);
                mView.onError(code);
            }
        });
    }

}

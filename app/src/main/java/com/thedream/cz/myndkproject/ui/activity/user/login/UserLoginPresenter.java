package com.thedream.cz.myndkproject.ui.activity.user.login;

import android.text.TextUtils;

import com.thedream.cz.myndkproject.RetrofitFactory;
import com.thedream.cz.myndkproject.api.IPublicApi;
import com.thedream.cz.myndkproject.bean.LoginInfo;
import com.thedream.cz.myndkproject.bean.WebResultInfo;
import com.thedream.cz.myndkproject.ui.common.BaseApplication;
import com.thedream.cz.myndkproject.utils.PrintUtil;
import com.thedream.cz.myndkproject.utils.ToastUtil;

import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by cz on 2017/11/28.
 * 用户登录逻辑
 */
public class UserLoginPresenter implements UserLoginContract.Presenter {

    private UserLoginContract.View mView;

    public UserLoginPresenter(UserLoginContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void login(String name, String pwd) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pwd)) {
            ToastUtil.showToast(BaseApplication.mApplication, "账号或密码不能为空");
            return;
        }
        mView.showProgress(true);
        RetrofitFactory.getRetrofit().create(IPublicApi.class)
                .login(name, pwd, Locale.getDefault().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WebResultInfo<LoginInfo>>() {
                    @Override
                    public void accept(@NonNull WebResultInfo<LoginInfo> webResultInfo) throws Exception {
                        mView.showProgress(false);
                        if (webResultInfo.getStatusCode() == 1) {
                            onSuccess(webResultInfo.getData());
                        } else {
                            onFailed(webResultInfo.getStatusCode());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        PrintUtil.printCZ("失败:" + throwable.toString());
                        mView.showProgress(false);
                        onFailed(-1);
                    }
                });


    }

    private void onSuccess(LoginInfo info) {
        mView.onResult(info.toString());
    }

    private void onFailed(int code) {
        mView.onError(code);
    }
}

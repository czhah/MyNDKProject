package com.thedream.cz.myndkproject.ui.activity.user.logintwo;

import android.os.Bundle;

import com.thedream.cz.myndkproject.R;
import com.thedream.cz.myndkproject.mvp.factory.CreatePresenter;
import com.thedream.cz.myndkproject.mvp.view.BaseMvpActivity;
import com.thedream.cz.myndkproject.ui.activity.find.content.FindActivity;
import com.thedream.cz.myndkproject.ui.dialog.LoadingDialog;
import com.thedream.cz.myndkproject.utils.PrintUtil;
import com.thedream.cz.myndkproject.utils.ToastUtil;

@CreatePresenter(UserLoginTwoPresenter.class)
public class UserLoginTwoActivity extends BaseMvpActivity<UserLoginTwoView, UserLoginTwoPresenter> implements UserLoginTwoView {

    private LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login_two);
        findViewById(R.id.btn_login).setOnClickListener(view -> {
            getMvpPresenter().login("13691923610", "123456");
        });
    }

    @Override
    public void showProgress(boolean isShow) {
        if (isShow) {
            mLoadingDialog = LoadingDialog.show(getSupportFragmentManager(), R.string.text_loading, false);
        } else {
            if (mLoadingDialog != null && mLoadingDialog.isVisible()) {
                mLoadingDialog.dismiss();
                mLoadingDialog = null;
            }
        }
    }

    @Override
    public void onResult(String result) {
        PrintUtil.printCZ("result：" + result);
        ToastUtil.showToast(this, "登录成功！");
        FindActivity.launch(this);
    }

    @Override
    public void onError(int code) {
        ToastUtil.showToast(this, "登录错误:" + code);
    }


    @Override
    public void showTip(String text) {
        ToastUtil.showToast(this, text);
    }
}

package com.thedream.cz.myndkproject.ui.activity.user.login;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;

import com.thedream.cz.myndkproject.R;
import com.thedream.cz.myndkproject.ui.common.BaseApplication;
import com.thedream.cz.myndkproject.ui.common.BaseFragment;
import com.thedream.cz.myndkproject.utils.PrintUtil;
import com.thedream.cz.myndkproject.utils.ToastUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserLoginFragment extends BaseFragment<UserLoginContract.Presenter> implements UserLoginContract.View {


    private EditText etName;
    private EditText etPwd;
    private ProgressDialog mProgress;

    public static UserLoginFragment newInstance() {
        return new UserLoginFragment();
    }

    @Override
    protected int getLayoutInflaterId() {
        return R.layout.fragment_user_login;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etName = (EditText) view.findViewById(R.id.et_name);
        etPwd = (EditText) view.findViewById(R.id.et_pwd);

        view.findViewById(R.id.btn_login).setOnClickListener((v) -> {
            String name = etName.getText().toString();
            String pwd = etPwd.getText().toString();
            mPresenter.login(name, pwd);
        });
        view.findViewById(R.id.btn_query).setOnClickListener((v) -> {
            mPresenter.query();
        });
        view.findViewById(R.id.btn_all).setOnClickListener((v) -> {
            mPresenter.queryList();
        });
    }

    @Override
    public void setPresenter(UserLoginContract.Presenter presenter) {
        if (null == presenter) {
            mPresenter = new UserLoginPresenter(this, ((BaseApplication) BaseApplication.mApplication).getCommonDataRepository());
        }

    }

    @Override
    public void showProgress(boolean isShow) {
        if (isShow) {
            mProgress = ProgressDialog.show(getContext(), "", "加载中...", false, false);
            mProgress.show();
        } else {
            if (mProgress != null && mProgress.isShowing()) {
                mProgress.dismiss();
                mProgress = null;
            }
        }
    }

    @Override
    public void onResult(String result) {
        PrintUtil.printCZ("result：" + result);
        ToastUtil.showToast(getContext(), "登录成功！");
    }

    @Override
    public void onError(int code) {
        ToastUtil.showToast(getContext(), "登录错误:" + code);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showTip(String text) {
        ToastUtil.showToast(getContext(), text);
    }

}

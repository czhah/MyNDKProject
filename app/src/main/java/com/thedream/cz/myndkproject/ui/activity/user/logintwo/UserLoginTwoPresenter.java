package com.thedream.cz.myndkproject.ui.activity.user.logintwo;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.thedream.cz.myndkproject.bean.WebResultInfo;
import com.thedream.cz.myndkproject.data.CommonDataRepository;
import com.thedream.cz.myndkproject.data.UserDataRepository;
import com.thedream.cz.myndkproject.data.entity.FileUpLoadInfo;
import com.thedream.cz.myndkproject.data.entity.LoginInfo;
import com.thedream.cz.myndkproject.listener.OnResultListener;
import com.thedream.cz.myndkproject.mvp.presenter.BaseMvpPresenter;
import com.thedream.cz.myndkproject.ui.common.BaseApplication;
import com.thedream.cz.myndkproject.utils.PrintUtil;

import java.io.File;
import java.util.List;

/**
 * Created by cz on 2017/12/11.
 */

public class UserLoginTwoPresenter extends BaseMvpPresenter<UserLoginTwoView> {

    private static final String EXTRA_UID = "extra_uid";

    private CommonDataRepository mRepository;
    private UserDataRepository mUserRepository;
    private String uid;
    private String token;

    public UserLoginTwoPresenter() {
        PrintUtil.printCZ("创建UserLoginTwoPresenter");
        this.mRepository = ((BaseApplication) BaseApplication.mApplication).getCommonDataRepository();
        this.mUserRepository = ((BaseApplication) BaseApplication.mApplication).getUserDataRepository();
    }

    @Override
    public void onCreatePresenter(@Nullable Bundle saveState) {
        super.onCreatePresenter(saveState);
        if (saveState == null) {
            return;
        }
        String uid = saveState.getString(EXTRA_UID);
        PrintUtil.printCZ("恢复之后的uid:" + uid);
    }

    @Override
    public Bundle onSaveInstanceState() {
        PrintUtil.printCZ("需要保存的uid: xxx");
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_UID, "xxxx");
        return bundle;
    }

    private final OnResultListener listener = new OnResultListener<LoginInfo>() {

        @Override
        public void onSuccess(LoginInfo loginInfo) {
            if (getMvpView() == null) return;
            getMvpView().showProgress(false);
            if (loginInfo != null) {
                uid = loginInfo.getUid();
                token = loginInfo.getToken();
                getMvpView().onResult(loginInfo.toString());
            } else {
                getMvpView().showTip("数据为空");
            }
        }

        @Override
        public void onFailed(int code) {
            if (getMvpView() == null) return;
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

    public void upLoad() {
        if (getMvpView() == null) {
            return;
        }
        if (TextUtils.isEmpty(uid) || TextUtils.isEmpty(token)) {
            getMvpView().showTip("请先登录");
            return;
        }
        String path = Environment.getExternalStorageDirectory() + File.separator + "test1.jpg";
        File file = new File(path);
        if (!file.exists()) {
            getMvpView().showTip("文件不存在！");
            return;
        }

        getMvpView().showProgress(true);
        mUserRepository.upLoadFile(uid, token, file, new OnResultListener<FileUpLoadInfo>() {
            @Override
            public void onSuccess(FileUpLoadInfo fileUpLoadInfo) {
                if (getMvpView() != null) {
                    getMvpView().showProgress(false);
                    getMvpView().onResult("上传成功:" + fileUpLoadInfo.getImg());
                }
            }

            @Override
            public void onFailed(int code) {
                if (getMvpView() != null) {
                    getMvpView().showProgress(false);
                    getMvpView().onError(code);
                }
            }
        });

    }

    public void downLoad() {
        getMvpView().showProgress(true);
        mUserRepository.downloadApk("http://timeto.lol/timeto.apk", new OnResultListener<String>() {
            @Override
            public void onSuccess(String fileUpLoadInfo) {
                if (getMvpView() != null) {
                    getMvpView().showProgress(false);
                    File file = new File(fileUpLoadInfo);
                    getMvpView().onResult("下载成功:" + file.exists());
                }
            }

            @Override
            public void onFailed(int code) {
                if (getMvpView() != null) {
                    getMvpView().showProgress(false);
                    getMvpView().onError(code);
                }
            }
        });

    }

    public void query() {
        if (TextUtils.isEmpty(uid)) {
            getMvpView().showTip("id不能为空");
            return;
        }
        getMvpView().showProgress(true);
        mRepository.queryLocalLogin(uid, listener);
    }

    @Override
    public void onDestroyPresenter() {
        super.onDestroyPresenter();
        PrintUtil.printCZ("这里执行一些取消请求的操作");
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
                    if (getMvpView() == null) return;
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

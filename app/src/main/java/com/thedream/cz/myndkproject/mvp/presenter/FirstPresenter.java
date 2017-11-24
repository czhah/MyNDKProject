package com.thedream.cz.myndkproject.mvp.presenter;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.thedream.cz.myndkproject.mvp.views.IFirstView;
import com.thedream.cz.myndkproject.utils.PrintUtil;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Administrator on 2017/11/24.
 */

public class FirstPresenter extends BasePresenter<IFirstView> {

    protected final String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN
    };

    static final int REQUEST_LOCATION = 100;

    private final MyHandler mHandler;

    /**
     * 判断是否需要检测，防止不停的弹框
     */
    private boolean isNeedCheck = true;

    @Inject
    public FirstPresenter(Context mContext) {
        super(mContext);
        mHandler = new MyHandler(this);
    }

    private static class MyHandler extends Handler {
        WeakReference<FirstPresenter> mPreseneter;

        public MyHandler(FirstPresenter presenter) {
            mPreseneter = new WeakReference<FirstPresenter>(presenter);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mPreseneter == null) return;
            FirstPresenter presenter = mPreseneter.get();
            if (presenter == null) return;
            if (msg.what == 0) {
                presenter.mView.launchMain();
            }
        }
    }

    public void checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23 && mContext.getApplicationInfo().targetSdkVersion >= 23) {
            if (isNeedCheck) {
                try {
                    List<String> needRequestPermissonList = new ArrayList<String>();
                    for (String perm : needPermissions) {
                        Method checkSelfMethod = mContext.getClass().getMethod("checkSelfPermission", String.class);
                        Method shouldShowRequestPermissionRationaleMethod = mContext.getClass().getMethod("shouldShowRequestPermissionRationale",
                                String.class);
                        if ((Integer) checkSelfMethod.invoke(mContext, perm) != PackageManager.PERMISSION_GRANTED
                                || (Boolean) shouldShowRequestPermissionRationaleMethod.invoke(mContext, perm)) {
                            needRequestPermissonList.add(perm);
                        }
                    }
                    if (needRequestPermissonList.size() > 0) {
                        String[] array = needRequestPermissonList.toArray(new String[needRequestPermissonList.size()]);
                        Method method = mContext.getClass().getMethod("requestPermissions", new Class[]{String[].class,
                                int.class});
                        method.invoke(mContext, array, REQUEST_LOCATION);
                        return;
                    }//Expected receiver of type android.content.ContextWrapper, but got com.thedream.cz.myndkproject.mvp.presenter.FirstPresenter
                } catch (Exception e) {
                    PrintUtil.print("检查权限出错:" + e.toString());
                }
            }
        }
        mHandler.sendEmptyMessageDelayed(0, 3000);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION:
                if (requestCode == REQUEST_LOCATION) {
                    //  检查获取到全部的权限
                    if (!verifyPermissions(grantResults)) {
                        showMissingPermissionDialog();
                    } else {
                        isNeedCheck = false;
                        mHandler.sendEmptyMessageDelayed(0, 3000);
                    }
                }
                break;
        }
    }

    private boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("提醒");
        builder.setMessage("去打开权限");
        // 拒绝, 退出应用
        builder.setNegativeButton("取消", (dialog, which) -> {
            mView.finishActivity();
        });
        builder.setPositiveButton("设置", (dialog, which) -> {
            mView.launchSetting();
        });
        builder.setCancelable(false);
        builder.show();
    }
}

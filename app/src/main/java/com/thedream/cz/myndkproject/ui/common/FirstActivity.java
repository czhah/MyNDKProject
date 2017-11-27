package com.thedream.cz.myndkproject.ui.common;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;

import com.thedream.cz.myndkproject.R;
import com.thedream.cz.myndkproject.activity.MainActivity;
import com.thedream.cz.myndkproject.utils.PrintUtil;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class FirstActivity extends BaseActivity<FirstContract.Presenter> implements FirstContract.View {

    static final int REQUEST_LOCATION = 100;

    /**
     * 判断是否需要检测，防止不停的弹框
     */
    private boolean isNeedCheck = true;

    private final String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN
    };

    private MyHandler mHandler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
    }

    @Override
    public void setPresenter(FirstContract.Presenter presenter) {
        if (null == presenter) {
            this.mPresenter = new FirstPresenter(this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkPermissions();
    }

    public void checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23 && getApplicationInfo().targetSdkVersion >= 23) {
            if (isNeedCheck) {
                try {
                    List<String> needRequestPermissonList = new ArrayList<String>();
                    for (String perm : needPermissions) {
                        Method checkSelfMethod = getClass().getMethod("checkSelfPermission", String.class);
                        Method shouldShowRequestPermissionRationaleMethod = getClass().getMethod("shouldShowRequestPermissionRationale",
                                String.class);
                        if ((Integer) checkSelfMethod.invoke(this, perm) != PackageManager.PERMISSION_GRANTED
                                || (Boolean) shouldShowRequestPermissionRationaleMethod.invoke(this, perm)) {
                            needRequestPermissonList.add(perm);
                        }
                    }
                    if (needRequestPermissonList.size() > 0) {
                        String[] array = needRequestPermissonList.toArray(new String[needRequestPermissonList.size()]);
                        Method method = getClass().getMethod("requestPermissions", new Class[]{String[].class,
                                int.class});
                        method.invoke(this, array, REQUEST_LOCATION);
                        return;
                    }//Expected receiver of type android.content.ContextWrapper, but got com.thedream.cz.myndkproject.mvp.presenter.FirstPresenter
                } catch (Exception e) {
                    PrintUtil.print("检查权限出错:" + e.toString());
                }
            }
        }
        mHandler.sendEmptyMessageDelayed(0, 3000);
    }

    @Override
    public void launchMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void launchSetting() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    @Override
    public void finishActivity() {
        this.finish();
    }

    @Override
    public void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提醒");
        builder.setMessage("去打开权限");
        // 拒绝, 退出应用
        builder.setNegativeButton("取消", (dialog, which) -> {
            finishActivity();
        });
        builder.setPositiveButton("设置", (dialog, which) -> {
            launchSetting();
        });
        builder.setCancelable(false);
        builder.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
            default:
                break;
        }
    }

    private static class MyHandler extends Handler {
        WeakReference<FirstActivity> activites;

        public MyHandler(FirstActivity activity) {
            activites = new WeakReference<FirstActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (activites == null) return;
            FirstActivity activity = activites.get();
            if (activity == null || activity.isFinishing()) return;
            if (msg.what == 0) {
                activity.launchMain();
            }
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
}

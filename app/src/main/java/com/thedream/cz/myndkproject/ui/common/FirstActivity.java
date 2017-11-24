package com.thedream.cz.myndkproject.ui.common;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.view.KeyEvent;

import com.thedream.cz.myndkproject.R;
import com.thedream.cz.myndkproject.activity.MainActivity;
import com.thedream.cz.myndkproject.injection.components.ApplicationComponent;
import com.thedream.cz.myndkproject.injection.components.DaggerActivityComponent;
import com.thedream.cz.myndkproject.injection.modules.ActivityModule;
import com.thedream.cz.myndkproject.mvp.presenter.FirstPresenter;
import com.thedream.cz.myndkproject.mvp.views.IFirstView;

public class FirstActivity extends BaseActivity<FirstPresenter> implements IFirstView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.checkPermissions();
    }

    @Override
    protected void setupActivityComponent(ApplicationComponent appComponent, ActivityModule activityModule) {
        DaggerActivityComponent.builder()
                .applicationComponent(appComponent)
                .activityModule(activityModule)
                .build()
                .inject(this);
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
        mPresenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}

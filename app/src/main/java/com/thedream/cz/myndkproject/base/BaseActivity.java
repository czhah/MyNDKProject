package com.thedream.cz.myndkproject.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.cz.resource.skintool.listener.ISkinUpdate;
import com.cz.resource.skintool.manager.SkinInflaterFactory;
import com.cz.resource.skintool.manager.SkinManager;

/**
 * Created by cz on 2017/11/1.
 * 基类
 */

public abstract class BaseActivity extends AppCompatActivity implements ISkinUpdate {
    private SkinInflaterFactory factory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        factory = new SkinInflaterFactory();
        getLayoutInflater().setFactory(factory);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SkinManager.getInstance().attch(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SkinManager.getInstance().detach(this);
        factory.clean();
    }

    @Override
    public void onThemeUpdate() {
        Log.i("cz", "onThemeUpdate: " + BaseActivity.this.getClass().getSimpleName());
        factory.applySkin();
    }
}

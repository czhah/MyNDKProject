package com.thedream.cz.myndkproject.activity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.cz.resource.skintool.config.SkinConfig;
import com.cz.resource.skintool.listener.ILoadSkinListener;
import com.cz.resource.skintool.manager.SkinManager;
import com.thedream.cz.myndkproject.R;
import com.thedream.cz.myndkproject.base.BaseActivity;

import java.io.File;

public class MySkinActivity extends BaseActivity {

    private final String TAG = "MySkinActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_skin);


        findViewById(R.id.btn_skin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String SKIN_NAME = "timetoskin.skin";
                String SKIN_DIR = Environment.getExternalStorageDirectory() + File.separator + SKIN_NAME;

                SkinConfig.saveCustomSkinPath(MySkinActivity.this, SKIN_DIR);
                SkinManager.getInstance().loadSkin(new ILoadSkinListener() {
                    @Override
                    public void onPrepare() {
                        Log.i(TAG, "onPrepare");
                    }

                    @Override
                    public void onSuccess() {
                        Log.i(TAG, "onSuccess");
                    }

                    @Override
                    public void onFail() {
                        Log.i(TAG, "onFail");
                    }

                    @Override
                    public void onCancel() {
                        Log.i(TAG, "onCancel");
                    }
                });
            }
        });
        findViewById(R.id.btn_restore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  还原
                SkinManager.getInstance().restoreDefault();
            }
        });
    }
}

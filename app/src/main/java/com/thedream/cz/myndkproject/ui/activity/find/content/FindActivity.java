package com.thedream.cz.myndkproject.ui.activity.find.content;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.thedream.cz.myndkproject.R;
import com.thedream.cz.myndkproject.base.BaseActivity;
import com.thedream.cz.myndkproject.utils.ActivityUtil;

/**
 * Created by chenzhuang on 2017/12/1.
 * Describe: 发现页面功能
 */
public class FindActivity extends BaseActivity {

    public static void launch(Context context) {
        ((Activity) context).startActivity(new Intent(context, FindActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        FindFragment findFragment = (FindFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (findFragment == null) {
            findFragment = FindFragment.getInstance();
            ActivityUtil.addFragmentToActivity(getSupportFragmentManager(), findFragment, R.id.contentFrame);
        }
    }
}

package com.thedream.cz.myndkproject.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/11/23.
 */

public class ToastUtil {
    private Context context;

    public ToastUtil(Context context) {
        this.context = context.getApplicationContext();
    }

    public void showToast(String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }
}

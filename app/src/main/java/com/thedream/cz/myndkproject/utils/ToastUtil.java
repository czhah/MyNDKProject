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

    public static void showToast(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }
}

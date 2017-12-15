package com.thedream.cz.myndkproject.utils;

import android.content.Context;
import android.widget.Toast;

import com.thedream.cz.myndkproject.R;

/**
 * Created by Administrator on 2017/11/23.
 */

public class ToastUtil {

    public ToastUtil(Context context) {
    }

    public static void showToast(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }

    public static void showResult(Context context, int result) {
        PrintUtil.printCZ("请求失败: " + result);
        int resId = R.string.error_fail;
        switch (result) {
            case -1:
                resId = R.string.error_fail;
                break;
            default:
                break;
        }
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }
}

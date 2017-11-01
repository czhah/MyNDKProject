package com.cz.resource.skintool.config;

import android.content.Context;

import com.cz.resource.skintool.utils.PreferencesUtil;

/**
 * Created by cz on 2017/10/31.
 */

public class SkinConfig {

    public static final String SKIN_NAMESPACE = "http://schemas.android.com/android/skin";
    public static final String SKIN_ENABLE = "skin_enable";
    public static final String SKIN_DEFAULT = "skin_default_path";  //  使用默认皮肤
    public static final String SKIN_PATH = "skin_current_path";  //  当前皮肤的路径


    public static String getSkinPath(Context context) {
        return PreferencesUtil.getString(context, SKIN_PATH, SKIN_DEFAULT);
    }

    public static void saveCustomSkinPath(Context context, String path) {
        PreferencesUtil.putString(context, SKIN_PATH, path);
    }

    public static void saveDefaultSkinPath(Context context) {
        PreferencesUtil.putString(context, SKIN_PATH, SKIN_DEFAULT);
    }

    public static boolean isDefaultSkin(Context context) {
        return SKIN_DEFAULT.equals(getSkinPath(context));
    }

}

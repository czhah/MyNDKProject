package com.thedream.cz.myndkproject.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by Administrator on 2017/12/4.
 */

public class WindowUtil {
    /**
     * 把dp转成px
     *
     * @param context context
     * @param dpVal   dp value
     * @return pixel
     */
    public static int dp2px(Context context, int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    /**
     * 把dp转成px
     *
     * @param context
     * @param dpVal
     * @return
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }
}

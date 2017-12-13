package com.cz.resource.baserecyclerviewadapterhelper.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.TypedValue;
import android.view.Display;

/**
 * Created by cz on 2017/12/13.
 * 屏幕辅助类
 */

public class WindowHelper {

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

    /**
     * 获取屏幕宽
     *
     * @param mContext
     * @return
     */
    public static int getScreenWidth(Context mContext) {
        Display display = ((Activity) mContext).getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        return point.x;
    }
}

package com.thedream.cz.myndkproject.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.TypedValue;
import android.view.Display;

/**
 * Created by Administrator on 2017/12/4.
 */

public class WindowUtil {

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

    public static int getScreenWidth(Context mContext) {
        Display display = ((Activity) mContext).getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        return point.x;
    }
}

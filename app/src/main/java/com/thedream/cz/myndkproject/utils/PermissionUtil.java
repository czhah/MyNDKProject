package com.thedream.cz.myndkproject.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/**
 * Created by Administrator on 2017/11/13.
 */

public class PermissionUtil {

    public static boolean checkLocalPermission(Context mContext) {
        if (mContext == null) return false;
        //  ACCESS_COARSE_LOCATION:粗略定位;ACCESS_FINE_LOCATION:通过gps定位
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }
}

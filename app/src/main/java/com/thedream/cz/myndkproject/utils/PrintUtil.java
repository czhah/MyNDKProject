package com.thedream.cz.myndkproject.utils;

import android.os.Build;
import android.util.Log;

/**
 * Created by Administrator on 2017/11/15.
 */

public class PrintUtil {

    //  日志开关，release下为false
    private static final boolean SWITCH_LOG = true;

    private static final String TAG = "CZ";
    private static final String TAG_CZ = "CZ";

    public static void print(String text) {
        if (SWITCH_LOG) Log.d(TAG, text);
    }

    public static void print(Object text) {
        if (SWITCH_LOG) Log.d(TAG, text != null ? text.toString() : "null");
    }

    public static void printW(String text) {
        Log.w(TAG, text);
        //  测试保存到本地
        if (SWITCH_LOG) {
            saveLogToFile(text);
        }
    }

    public static void printCZ(String text) {
        if (SWITCH_LOG) Log.i(TAG_CZ, text);
    }

    public static void saveLogToFile(String msg) {
        StringBuilder sb = new StringBuilder();
        //  time
        sb.append("-->time: " + System.currentTimeMillis());
        //  phone+version
        sb.append("--version: " + Build.MANUFACTURER + "," + Build.MODEL + "," + Build.VERSION.SDK_INT);
        sb.append("--error: " + msg + "\n");
//        LogSaveThread.addLog(sb.toString(), TTFileUtil.getLogDirPath()+"/"+TAG+".txt",
//                TTTimeUtil.checkLogSavePastTime());
    }
}

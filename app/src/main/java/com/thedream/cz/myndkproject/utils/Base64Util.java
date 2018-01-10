package com.thedream.cz.myndkproject.utils;

import android.util.Base64;

/**
 * Created by cz on 2018/1/8.
 */

public class Base64Util {


    public static byte[] decode(String privateKey) {
        return Base64.decode(privateKey, Base64.DEFAULT);
    }

    public static String encode(byte[] sign) {
        return Base64.encodeToString(sign, Base64.DEFAULT);
    }
}

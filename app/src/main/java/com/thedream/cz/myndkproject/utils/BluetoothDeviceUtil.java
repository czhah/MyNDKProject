package com.thedream.cz.myndkproject.utils;

import android.bluetooth.BluetoothDevice;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2017/11/15.
 */

public class BluetoothDeviceUtil {

    //  取消绑定
    public static boolean removeBond(Class clz, BluetoothDevice device) throws Exception {
        Method method = clz.getMethod("removeBond");
        Boolean returnValue = (Boolean) method.invoke(device);
        return returnValue.booleanValue();
    }

    //  取消用户输入
    static public boolean cancelPairingUserInput(Class btClass, BluetoothDevice device) throws Exception {
        Method createBondMethod = btClass.getMethod("cancelPairingUserInput");
        Boolean returnValue = (Boolean) createBondMethod.invoke(device);
        return returnValue.booleanValue();
    }

    public static int turn16to10(String str) {
        int sum = 0;
        for (int i = 0; i < str.length(); i++) {
            sum *= 16;
            char c = str.charAt(i);
            if (c >= 'A' && c <= 'F') {
                sum += c - 'A' + 10;
            } else if (c >= 'a' && c <= 'f') {
                sum += c - 'a' + 10;
            } else if (c >= '0' && c <= '9') {
                sum += c - '0';
            }
        }
        return sum;//[NSString stringWithFormat:@"%d",sum];
    }
    /** */
    /**
     * @函数功能: BCD码转为10进制串(阿拉伯数据)
     * @输入参数: BCD码
     * @输出结果: 10进制串
     */
    public static String bcd2Str(byte[] bytes) {
        StringBuffer temp = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            temp.append((byte) ((bytes[i] & 0xf0) >>> 4));
            temp.append((byte) (bytes[i] & 0x0f));
        }
        return temp.toString().substring(0, 1).equalsIgnoreCase("0") ? temp
                .toString().substring(1) : temp.toString();
    }

    public static int int2bcd(int num) {
        int bcdout = 0;
        bcdout = (num / 10) << 4;
        bcdout |= (num % 10);
        return bcdout;
    }

    public static int bcd2int(byte num) {
        return ((num & 0xf0) >> 4) * 10 + (num & 0x0f);
    }
    /** */
    /**
     * @函数功能: 10进制串转为BCD码
     * @输入参数: 10进制串
     * @输出结果: BCD码
     */
    public static byte[] str2Bcd(String asc) {
        int len = asc.length();
        int mod = len % 2;
        if (mod != 0) {
            asc = "0" + asc;
            len = asc.length();
        }
        byte abt[] = new byte[len];
        if (len >= 2) {
            len = len / 2;
        }
        byte bbt[] = new byte[len];
        abt = asc.getBytes();
        int j, k;
        for (int p = 0; p < asc.length() / 2; p++) {
            if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
                j = abt[2 * p] - '0';
            } else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
                j = abt[2 * p] - 'a' + 0x0a;
            } else {
                j = abt[2 * p] - 'A' + 0x0a;
            }
            if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
                k = abt[2 * p + 1] - '0';
            } else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
                k = abt[2 * p + 1] - 'a' + 0x0a;
            } else {
                k = abt[2 * p + 1] - 'A' + 0x0a;
            }
            int a = (j << 4) + k;
            byte b = (byte) a;
            bbt[p] = b;
        }
        return bbt;
    }

}

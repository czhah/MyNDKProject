package com.thedream.cz.myndkproject.bluetool;

/**
 * Created by Administrator on 2017/11/15.
 */

public class BluetoolGattAttributes {

    public static final int SCAN_TIME_OUT = 12 * 1000;
    public static final String CLIENT_CHARACTERISTIC_CONFIG = "";

    //  时间服务
    public static final String FFE0 = "0000ffe0-0000-1000-8000-00805f9b34fb";
    //  时间读取/设置
    public static final String FFE1 = "0000ffe1-0000-1000-8000-00805f9b34fb";
    //  时间设置(设置提醒，设置提醒时间给设备)
    public static final String FFE2 = "0000ffe2-0000-1000-8000-00805f9b34fb";
    //  获取当前连接速率 0：低速。1：高速
    public static final String FFE3 = "0000ffe3-0000-1000-8000-00805f9b34fb";

    //  动作服务
    public static final String FFF0 = "0000fff0-0000-1000-8000-00805f9b34fb";
    //  动作数据(设置 当设备发生倾倒、搅拌等动作时发送通知给手机)
    public static final String FFF1 = "0000fff1-0000-1000-8000-00805f9b34fb";
    public static final String FFF2 = "0000fff2-0000-1000-8000-00805f9b34fb";
    public static final String FFF3 = "0000fff3-0000-1000-8000-00805f9b34fb";

    //  电量查询
    public static final String FFD0 = "0000ffd0-0000-1000-8000-00805f9b34fb";

    //  固件升级
    public static final String FFC0_OAD_SERVICE_UUID = "f000ffc0-0451-4000-b000-000000000000";
    public static final String FFC1_OAD_IMAGE_NOTIFY_UUID = "f000ffc1-0451-4000-b000-000000000000";
    public static final String FFC2_OAD_IMAGE_BLOCK_REQUEST_UUID = "f000ffc2-0451-4000-b000-000000000000";


    public static final String FF00 = "00001800-0000-1000-8000-00805f9b34fb";
    public static final String FF01 = "00001801-0000-1000-8000-00805f9b34fb";
    public static final String FF0A = "0000180a-0000-1000-8000-00805f9b34fb";

    //  设置通知的开关 Descriptor
    public static final String ENABLE_NOTIFY = "00002902-0000-1000-8000-00805f9b34fb";

    /**
     service uuid:00001800-0000-1000-8000-00805f9b34fb type:0
     characteristic uuid:00002a00-0000-1000-8000-00805f9b34fb permissions:0 properties:2 write type:2
     characteristic uuid:00002a01-0000-1000-8000-00805f9b34fb permissions:0 properties:2 write type:2
     characteristic uuid:00002a02-0000-1000-8000-00805f9b34fb permissions:0 properties:10 write type:2
     characteristic uuid:00002a03-0000-1000-8000-00805f9b34fb permissions:0 properties:8 write type:2
     characteristic uuid:00002a04-0000-1000-8000-00805f9b34fb permissions:0 properties:2 write type:2

     service uuid:00001801-0000-1000-8000-00805f9b34fb type:0
     characteristic uuid:00002a05-0000-1000-8000-00805f9b34fb permissions:0 properties:32 write type:2
     descriptor uuid:00002902-0000-1000-8000-00805f9b34fb

     service uuid:0000180a-0000-1000-8000-00805f9b34fb type:0
     characteristic uuid:00002a23-0000-1000-8000-00805f9b34fb permissions:0 properties:2 write type:2
     characteristic uuid:00002a24-0000-1000-8000-00805f9b34fb permissions:0 properties:2 write type:2
     characteristic uuid:00002a25-0000-1000-8000-00805f9b34fb permissions:0 properties:2 write type:2
     characteristic uuid:00002a26-0000-1000-8000-00805f9b34fb permissions:0 properties:2 write type:2
     characteristic uuid:00002a27-0000-1000-8000-00805f9b34fb permissions:0 properties:2 write type:2
     characteristic uuid:00002a28-0000-1000-8000-00805f9b34fb permissions:0 properties:2 write type:2
     characteristic uuid:00002a29-0000-1000-8000-00805f9b34fb permissions:0 properties:2 write type:2
     characteristic uuid:00002a2a-0000-1000-8000-00805f9b34fb permissions:0 properties:2 write type:2
     characteristic uuid:00002a50-0000-1000-8000-00805f9b34fb permissions:0 properties:2 write type:2

     service uuid:0000ffd0-0000-1000-8000-00805f9b34fb type:0
     characteristic uuid:0000ffd1-0000-1000-8000-00805f9b34fb permissions:0 properties:2 write type:2
     characteristic uuid:0000ffd2-0000-1000-8000-00805f9b34fb permissions:0 properties:2 write type:2
     characteristic uuid:0000ffd3-0000-1000-8000-00805f9b34fb permissions:0 properties:18 write type:2
     characteristic uuid:0000ffd4-0000-1000-8000-00805f9b34fb permissions:0 properties:2 write type:2
     characteristic uuid:0000ffd5-0000-1000-8000-00805f9b34fb permissions:0 properties:10 write type:2
     characteristic uuid:0000ffd6-0000-1000-8000-00805f9b34fb permissions:0 properties:10 write type:2
     characteristic uuid:0000ffd7-0000-1000-8000-00805f9b34fb permissions:0 properties:10 write type:2
     characteristic uuid:0000ffd8-0000-1000-8000-00805f9b34fb permissions:0 properties:10 write type:2

     service uuid:0000fff0-0000-1000-8000-00805f9b34fb type:0
     characteristic uuid:0000fff1-0000-1000-8000-00805f9b34fb permissions:0 properties:32 write type:2
     descriptor uuid:00002902-0000-1000-8000-00805f9b34fb
     characteristic uuid:0000fff2-0000-1000-8000-00805f9b34fb permissions:0 properties:10 write type:2
     characteristic uuid:0000fff3-0000-1000-8000-00805f9b34fb permissions:0 properties:10 write type:2

     service uuid:0000ffe0-0000-1000-8000-00805f9b34fb type:0
     characteristic uuid:0000ffe1-0000-1000-8000-00805f9b34fb permissions:0 properties:10 write type:2
     characteristic uuid:0000ffe2-0000-1000-8000-00805f9b34fb permissions:0 properties:8 write type:2
     characteristic uuid:0000ffe3-0000-1000-8000-00805f9b34fb permissions:0 properties:10 write type:2

     service uuid:f000ffc0-0451-4000-b000-000000000000 type:0
     characteristic uuid:f000ffc1-0451-4000-b000-000000000000 permissions:0 properties:28 write type:1
     descriptor uuid:00002902-0000-1000-8000-00805f9b34fb
     descriptor uuid:00002901-0000-1000-8000-00805f9b34fb

     characteristic uuid:f000ffc2-0451-4000-b000-000000000000 permissions:0 properties:28 write type:1
     descriptor uuid:00002902-0000-1000-8000-00805f9b34fb
     descriptor uuid:00002901-0000-1000-8000-00805f9b34fb
     */
}

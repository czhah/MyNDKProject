package com.thedream.cz.myndkproject.constant;

/**
 * @author chenzhuang
 * @time 2018/1/9 14:55
 * @class 数据库常量类
 */

public class DatabaseConstant {

    //  数据库名称
    public static final String DATABASE_NAME = "ndk_db";

    //  数据库版本
    public static final int DATABASE_VERSION = 6;

    //  city表
    public static final String TB_CITY_NAME = "tb_city";
    //  city表id
    public static final String FIELD_CITY_ID = "city_id";
    //  city表name
    public static final String FIELD_CITY_NAME = "city_name";
    //  city表cid
    public static final String FIELD_CITY_CID = "city_cid";
    //  city表address
    public static final String FIELD_CITY_ADDRESS = "city_address";


    //  drink表
    public static final String TB_DRINK_NAME = "tb_drink";
    //  city表id
    public static final String FIELD_DRINK_ID = "drink_id";
    //  city表name
    public static final String FIELD_DRINK_WEIGHT = "drink_weight";
    //  city表目标
    public static final String FIELD_DRINK_TARGET = "drink_target";
    //  设备id
    public static final String FIELD_DRINK_DEVICE_ID = "drink_device_id";

    //  数据库升级到2版本
    public static final String CREATE_CITY_1_2 = "CREATE TABLE " + TB_CITY_NAME + " (" + FIELD_CITY_ID + " TEXT PRIMARY KEY not null, " + FIELD_CITY_NAME + " TEXT, " + FIELD_CITY_CID + " TEXT)";
    //  数据库升级到5版本
    public static final String CREATE_DRINK_4_5 = "CREATE TABLE " + TB_DRINK_NAME + " (" + FIELD_DRINK_ID + " TEXT PRIMARY KEY NOT NULL, " + FIELD_DRINK_WEIGHT + " TEXT)";
    //  数据库升级到6版本
    public static final String ALERT_DRINK_5_6 = "ALTER TABLE " + TB_DRINK_NAME + " ADD COLUMN " + FIELD_DRINK_TARGET + " INTEGER NOT NULL DEFAULT 0";
}

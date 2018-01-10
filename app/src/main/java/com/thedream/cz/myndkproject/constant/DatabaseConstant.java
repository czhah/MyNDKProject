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
    public static final int DATABASE_VERSION = 3;

    //  city表
    public static final String TB_CITY_NAME = "tb_city";
    //  city表id
    public static final String FIELD_CITY_ID = "city_id";
    //  city表name
    public static final String FIELD_CITY_NAME = "city_name";
    //  city表cid
    public static final String FIELD_CITY_CID = "city_cid";
    //  city建表语句
    public static final String CREATE_CITY_1_2 = "CREATE TABLE " + TB_CITY_NAME + " (" + FIELD_CITY_ID + " TEXT PRIMARY KEY, " + FIELD_CITY_NAME + " TEXT, " + FIELD_CITY_CID + " TEXT)";


}

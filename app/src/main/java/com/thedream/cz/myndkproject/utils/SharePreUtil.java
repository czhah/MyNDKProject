package com.thedream.cz.myndkproject.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2017/11/15.
 */

public class SharePreUtil {

    /**
     * 公共的属性，退出时不清理
     */
    private static final String SHAPE_PUBLIC = "cz_public";
    /**
     * 当前手机的位置坐标
     */
    public static final String KEY_PUBLIC_LOCATION = "phoneLocation";
    /**
     * 最后更新时间
     */
    public static final String KEY_PUBLIC_UPDATE_TIME = "updateTime";
    /**
     * 城市更新时间
     */
    public static final String KEY_PUBLIC_CITY_UPDATE_TIME = "updateTime";
    //  当前地址更新时间
    public static final String KEY_PUBLIC_LOCATION_UPDATE_TIME = "myLocationTime";
    //  日志保存时间
    public static final String KEY_PUBLIC_LOG_SAVE_TIME = "logSaveTime";
    //  引导页显示
    public static final String KEY_PUBLIC_GUIDE_TYPE = "guideType";
    //  视频引导页显示
    public static final String KEY_PUBLIC_GUIDE_VIDEO = "guideVideo";

    /**
     * 私有的属性，退出时要清空
     */
    private static final String SHAPE_PRIVATE = "cz_private";
    //  刷新收藏页面
    public static final String KEY_PRIVATE_DEVICE_ADDRESS = "refreshCollect";

    /**
     * 存储数据（默认存储共有属性中）
     *
     * @param context 上下文
     * @param key     键
     * @param object  值
     */
    public static void put(Context context, String key, Object object) {
        put(context, key, object, true);
    }

    /**
     * 存储数据（默认存储共有属性中）
     *
     * @param context  上下文
     * @param key      键
     * @param object   值
     * @param isPublic 是否是共有属性
     */
    public static void put(Context context, String key, Object object, boolean isPublic) {
        SharedPreferences sp = context.getSharedPreferences(isPublic ? SHAPE_PUBLIC : SHAPE_PRIVATE,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 获取数据(共有属性)
     *
     * @param context       上下文
     * @param key           键
     * @param defaultObject 默认值
     * @return
     */
    public static Object get(Context context, String key, Object defaultObject) {
        return get(context, key, defaultObject, true);
    }

    /**
     * 获取数据
     *
     * @param context       上下文
     * @param key           键
     * @param defaultObject 默认值
     * @param isPublic      数据类型
     * @return
     */
    public static Object get(Context context, String key, Object defaultObject, boolean isPublic) {
        SharedPreferences sp = context.getSharedPreferences(isPublic ? SHAPE_PUBLIC : SHAPE_PRIVATE,
                Context.MODE_PRIVATE);
        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }
        return null;
    }

    /**
     * 清理私有信息(退出时)
     *
     * @param context
     */
    public static void clearPrivate(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHAPE_PRIVATE,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            editor.commit();
        }
    }
}

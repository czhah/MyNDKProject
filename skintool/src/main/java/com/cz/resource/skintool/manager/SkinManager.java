package com.cz.resource.skintool.manager;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import com.cz.resource.skintool.config.SkinConfig;
import com.cz.resource.skintool.listener.ILoadSkinListener;
import com.cz.resource.skintool.listener.ISkinUpdate;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenzhuang on 2017/10/31.
 * 换肤管理类
 */

public class SkinManager {

    private static SkinManager instance;

    private static Object synchronizedLock = new Object();

    private Context mContext;
    private String skinPackageName;
    private Resources mResources;
    private boolean isDefaultSkin = true;
    private List<ISkinUpdate> skinObservers;


    private void SkinManager() {
    }

    public static SkinManager getInstance() {
        if (instance == null) {
            synchronized (synchronizedLock) {
                if (instance == null) {
                    instance = new SkinManager();
                }
            }
        }
        return instance;
    }

    public void init(Context ctx) {
        mContext = ctx.getApplicationContext();
    }

    public void loadSkin(ILoadSkinListener callback) {
        if (SkinConfig.isDefaultSkin(mContext)) return;
        loadSkin(SkinConfig.getSkinPath(mContext), callback);
    }

    private void loadSkin(final String skinPath, final ILoadSkinListener callback) {
        new AsyncTask<String, Void, Resources>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                callback.onPrepare();
            }

            @Override
            protected Resources doInBackground(String... params) {
                File file = new File(params[0]);
                if (!file.exists()) return null;
                String path = file.getAbsolutePath();
                PackageManager packageManager = mContext.getPackageManager();
                PackageInfo packageInfo = packageManager.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
                skinPackageName = packageInfo.packageName;
                try {
                    AssetManager assetManager = AssetManager.class.newInstance();
                    //  addAssetPath()方法就是将path对应的apk资源链接到AssetManager中
                    Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);

                    addAssetPath.invoke(assetManager, path);
                    Resources superRes = mContext.getResources();
                    return new Resources(assetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Resources resources) {
                super.onPostExecute(resources);
                mResources = resources;
                if (resources != null) {
                    SkinConfig.saveCustomSkinPath(mContext, skinPath);
                    isDefaultSkin = false;
                    notifySkinUpdate();
                    callback.onSuccess();
                } else {
                    SkinConfig.saveDefaultSkinPath(mContext);
                    isDefaultSkin = true;
                    callback.onFail();
                }
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
                callback.onFail();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[]{skinPath});
    }


    public void restoreDefault() {
        SkinConfig.saveDefaultSkinPath(mContext);
        isDefaultSkin = true;
        notifySkinUpdate();
    }

    public void attch(ISkinUpdate iSkinUpdate) {
        if (skinObservers == null) skinObservers = new ArrayList<>();
        if (!skinObservers.contains(iSkinUpdate)) {
            skinObservers.add(iSkinUpdate);
        }
    }

    public void detach(ISkinUpdate iSkinUpdate) {
        if (skinObservers == null) return;
        if (skinObservers.contains(iSkinUpdate)) {
            skinObservers.remove(iSkinUpdate);
        }
    }

    public void notifySkinUpdate() {
        if (skinObservers == null) return;
        for (ISkinUpdate iSkinUpdate : skinObservers) {
            iSkinUpdate.onThemeUpdate();
        }
    }

    public boolean isExternalSkin() {
        return !isDefaultSkin && mResources != null;
    }


    /**
     * 获取颜色资源
     *
     * @param resId
     * @return
     */
    public int getColor(int resId) {
        int originColor = mContext.getResources().getColor(resId);
        if (mResources == null || SkinConfig.isDefaultSkin(mContext)) return originColor;
        String resName = mContext.getResources().getResourceEntryName(resId);
        int colorId = mResources.getIdentifier(resName, "color", skinPackageName);
        if (colorId != 0) {
            try {
                int color = mResources.getColor(colorId);
                return color;
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }
        }
        return originColor;
    }

    /**
     * 获取图片资源
     *
     * @param resId
     * @return
     */
    public Drawable getMipmap(int resId) {
        Drawable originDrawable = mContext.getResources().getDrawable(resId);
        if (mResources == null || SkinConfig.isDefaultSkin(mContext)) return originDrawable;
        String resName = mContext.getResources().getResourceEntryName(resId);
        int mipmapId = mResources.getIdentifier(resName, "mipmap", skinPackageName);
        if (mipmapId != 0) {
            try {
                return mResources.getDrawable(mipmapId);
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }
        }
        return originDrawable;
    }

    public Drawable getDrawable(int resId) {
        Drawable originDrawable = mContext.getResources().getDrawable(resId);
        if (mResources == null || SkinConfig.isDefaultSkin(mContext)) return originDrawable;
        String resName = mContext.getResources().getResourceEntryName(resId);
        int drawableId = mResources.getIdentifier(resName, "drawable", skinPackageName);
        if (drawableId != 0) {
            try {
                return mResources.getDrawable(drawableId);
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }
        }
        return originDrawable;
    }

    /**
     * 获取字体资源
     *
     * @param resId
     * @return
     */
    public String getString(int resId) {
        String originStr = mContext.getResources().getString(resId);
        if (mResources == null || SkinConfig.isDefaultSkin(mContext)) return originStr;
        String resName = mContext.getResources().getResourceEntryName(resId);
        int strId = mResources.getIdentifier(resName, "string", skinPackageName);
        if (strId != 0) {
            try {
                return mResources.getString(strId);
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }
        }
        return originStr;
    }

}

package com.cz.resource.skintool.entry;

import android.util.Log;
import android.view.View;

import com.cz.resource.skintool.manager.SkinManager;

/**
 * Created by cz on 2017/10/31.
 * 背景属性
 */

public class BackgroundAttr extends SkinAttr {

    @Override
    public void apply(View view) {
        Log.i("cz", "BackgroundAttr  attrValueTypeName:" + attrValueTypeName);
        if (RES_TYPE_NAME_COLOR.equals(attrValueTypeName)) {
            //  color
            view.setBackgroundColor(SkinManager.getInstance().getColor(resId));
        } else if (RES_TYPE_NAME_MIPMAP.equals(attrValueTypeName)) {
            //  mipmap
            view.setBackground(SkinManager.getInstance().getMipmap(resId));
        } else if (RES_TYPE_NAME_DRAWABLE.equals(attrValueTypeName)) {
            //  mipmap
            view.setBackground(SkinManager.getInstance().getDrawable(resId));
        }
    }
}

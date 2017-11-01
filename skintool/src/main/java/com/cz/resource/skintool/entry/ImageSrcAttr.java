package com.cz.resource.skintool.entry;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.cz.resource.skintool.manager.SkinManager;

/**
 * Created by cz on 2017/10/31.
 * 图片src属性
 */

public class ImageSrcAttr extends SkinAttr {
    @Override
    public void apply(View view) {
        Log.i("cz", "ImageSrcAttr  attrValueTypeName:" + attrValueTypeName);
        if (view instanceof ImageView) {
            if (RES_TYPE_NAME_MIPMAP.equals(attrValueTypeName)) {
                //  mipmap
                ((ImageView) view).setImageDrawable(SkinManager.getInstance().getMipmap(resId));
            } else if (RES_TYPE_NAME_DRAWABLE.equals(attrValueTypeName)) {
                //  drawable
                ((ImageView) view).setImageDrawable(SkinManager.getInstance().getDrawable(resId));
            }
        }
    }
}

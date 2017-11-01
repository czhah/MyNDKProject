package com.cz.resource.skintool.entry;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cz.resource.skintool.manager.SkinManager;

/**
 * Created by cz on 2017/10/31.
 * 字体颜色属性
 */

public class TextColorAttr extends SkinAttr {

    @Override
    public void apply(View view) {
        Log.i("cz", "TextColorAttr  attrValueTypeName:" + attrValueTypeName);
        if (RES_TYPE_NAME_COLOR.equals(attrValueTypeName)) {
            //  drawable
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(SkinManager.getInstance().getColor(resId));
            }
        }
    }
}

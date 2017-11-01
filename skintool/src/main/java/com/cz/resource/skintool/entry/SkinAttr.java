package com.cz.resource.skintool.entry;

import android.view.View;

/**
 * Created by Administrator on 2017/10/31.
 */

public abstract class SkinAttr {
    protected static final String RES_TYPE_NAME_COLOR = "color";
    protected static final String RES_TYPE_NAME_DRAWABLE = "drawable";
    protected static final String RES_TYPE_NAME_MIPMAP = "mipmap";

    /**
     * name of the attr, ex: background or textSize or textColor
     */
    public String attrName;

    /**
     * id of the attr value refered to, normally is [2130745655]
     */
    public int resId;

    /**
     * entry name of the value , such as app_name
     */
    public String attrValueRefName;

    /**
     * type of the value , such as color or drawable
     */
    public String attrValueTypeName;

    /**
     * Use to apply view with new TypedValue
     *
     * @param view
     */
    public abstract void apply(View view);

}

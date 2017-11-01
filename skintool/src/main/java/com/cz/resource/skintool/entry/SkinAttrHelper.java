package com.cz.resource.skintool.entry;

/**
 * Created by cz on 2017/10/31.
 */

public class SkinAttrHelper {

    public static final String BACKGROUND = "background";
    public static final String TEXT_COLOR = "textColor";
    public static final String SRC = "src";

    public static SkinAttr getAttr(String attributeName, int resId, String entryName, String typeName) {
        SkinAttr skinAttr = null;
        if (BACKGROUND.equals(attributeName)) {
            //  背景
            skinAttr = new BackgroundAttr();
        } else if (TEXT_COLOR.equals(attributeName)) {
            //  字体颜色
            skinAttr = new TextColorAttr();
        } else if (SRC.equals(attributeName)) {
            //  ImageView src
            skinAttr = new ImageSrcAttr();
        } else {
            return skinAttr;
        }

        skinAttr.attrName = attributeName;
        skinAttr.resId = resId;
        skinAttr.attrValueRefName = entryName;
        skinAttr.attrValueTypeName = typeName;
        return skinAttr;
    }
}

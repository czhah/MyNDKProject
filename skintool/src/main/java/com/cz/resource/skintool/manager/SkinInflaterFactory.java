package com.cz.resource.skintool.manager;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.cz.resource.skintool.config.SkinConfig;
import com.cz.resource.skintool.entry.SkinAttr;
import com.cz.resource.skintool.entry.SkinAttrHelper;
import com.cz.resource.skintool.entry.SkinItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/31.
 */

public class SkinInflaterFactory implements LayoutInflater.Factory {

    private final String TAG = "SkinInflaterFactory";
    private List<SkinItem> skinItems = new ArrayList<>();

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        boolean skin_enable = attrs.getAttributeBooleanValue(SkinConfig.SKIN_NAMESPACE, SkinConfig.SKIN_ENABLE, false);
        if (!skin_enable) {
            //  该View不包含标记
            return null;
        }
        //  记录被标记的属性
        View view = createView(context, name, attrs);
        if (view == null) return null;
        parseSkinAttr(context, attrs, view);
        return view;
    }

    private View createView(Context context, String name, AttributeSet attrs) {
        View view = null;
        Log.i("cz", "name: " + name);
        try {
            if (-1 == name.indexOf('.')) {
                if ("View".equals(name)) {
                    view = LayoutInflater.from(context).createView(name, "android.view.", attrs);
                }
                if (view == null) {
                    view = LayoutInflater.from(context).createView(name, "android.widget.", attrs);
                }
                if (view == null) {
                    view = LayoutInflater.from(context).createView(name, "android.webkit.", attrs);
                }
            } else {
                view = LayoutInflater.from(context).createView(name, null, attrs);
            }
        } catch (Exception e) {
            view = null;
        }
        return view;
    }

    private void parseSkinAttr(Context context, AttributeSet attrs, View view) {
        List<SkinAttr> attrList = new ArrayList<>();
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            String attributeName = attrs.getAttributeName(i);
            String attributeValue = attrs.getAttributeValue(i);
            Log.i("cz", "attributeName:" + attributeName + "  attributeValue:" + attributeValue);
            //  根据value判断是color,text,drawable
            if (attributeValue.startsWith("@")) {
                //  资源id
                int id = Integer.parseInt(attributeValue.substring(1));
                String entryName = context.getResources().getResourceEntryName(id);
                String typeName = context.getResources().getResourceTypeName(id);
                SkinAttr skinAttr = SkinAttrHelper.getAttr(attributeName, id, entryName, typeName);
                if (skinAttr != null) attrList.add(skinAttr);

                Log.i("cz", "添加进入  attributeName:" + attributeName + "  attributeValue:" + attributeValue);
            }
        }
        if (attrList.size() > 0) {
            SkinItem item = new SkinItem();
            item.view = view;
            item.attrs = attrList;
            skinItems.add(item);

            if (SkinManager.getInstance().isExternalSkin()) {
                item.apply();
            }
        }
    }

    public void applySkin() {
        if (skinItems == null || skinItems.size() == 0) return;
        for (SkinItem item : skinItems) {
            if (item.view != null) {
                item.apply();
            }
        }
    }

    public void clean() {
        if (skinItems == null || skinItems.size() == 0) return;
        for (SkinItem item : skinItems) {
            item.clean();
        }
    }
}

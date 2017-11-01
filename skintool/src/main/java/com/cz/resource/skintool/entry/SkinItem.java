package com.cz.resource.skintool.entry;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cz on 2017/10/31.
 * 换肤的控件
 */

public class SkinItem {

    public View view;

    public List<SkinAttr> attrs;

    public SkinItem() {
        attrs = new ArrayList<>();
    }

    public void apply() {
        if (attrs == null || attrs.size() == 0) return;
        for (SkinAttr skinAttr : attrs) {
            skinAttr.apply(view);
        }
    }

    public void clean() {
        if (attrs == null || attrs.size() == 0) {
            return;
        }
        for (SkinAttr at : attrs) {
            at = null;
        }
    }
}

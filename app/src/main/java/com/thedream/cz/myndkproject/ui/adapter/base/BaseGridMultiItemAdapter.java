package com.thedream.cz.myndkproject.ui.adapter.base;

import android.support.annotation.LayoutRes;
import android.util.SparseIntArray;
import android.view.ViewGroup;

/**
 * Created by CZ on 2017/12/11.
 *  GridLayoutManager：不同布局适配基类
 */

public abstract class BaseGridMultiItemAdapter<T, K extends BaseViewHolder> extends BaseQuickAdapter<T, K> {

    private SparseIntArray layouts;

    public static final int TYPE_NOT_FOUND = -404;

    private @LayoutRes int getLayoutId(int viewType) {
        return layouts.get(viewType, TYPE_NOT_FOUND);
    }

    /**
     * 这里把单个布局拦截了，使用多布局方式
     *
     * @return
     */
    @Override
    protected int getLayoutResId() {
        return 0;
    }

    protected void addItemType(int type, @LayoutRes int layoutResId) {
        if (layouts == null) {
            layouts = new SparseIntArray();
        }
        layouts.put(type, layoutResId);
    }

    @Override
    protected int getDefItemViewType(int position) {
        int itemType = getItemType(mData.get(position));
        return itemType != TYPE_NOT_FOUND ? itemType : super.getDefItemViewType(position);
    }

    @Override
    protected K createDefViewHolder(ViewGroup parent, int type) {
        return createBaseViewHolder(parent, getLayoutId(type));
    }

    public abstract int getItemType(T info);
}

package com.cz.resource.baserecyclerviewadapterhelper;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.util.SparseIntArray;
import android.view.ViewGroup;

/**
 * Created by CZ on 2017/12/11.
 *  GridLayoutManager：不同布局适配基类
 */

public abstract class BaseGridMultiItemAdapter<T, K extends BaseViewHolder> extends BaseQuickAdapter<T, K> {

    private SparseIntArray layouts;

    public static final int TYPE_NOT_FOUND = -404;

    /**
     * 根据数据类型返回viewType
     *
     * @param info
     * @return
     */
    public abstract int getItemType(T info);

    /**
     * 根据viewType返回每一行的列数
     * @param viewType
     * @param spanSize
     * @return
     */
    public abstract int getSpanSizeByType(int viewType, int spanSize);

    public BaseGridMultiItemAdapter(final int spanSize) {
        setSpanSizeLookUp(new SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                return getSpanSizeByType(getItemViewType(position), spanSize);
            }
        });
    }

    /**
     * 提供给GridLayoutManager适配多布局使用
     * @param type
     * @param layoutResId
     */
    protected void addItemType(int type, @LayoutRes int layoutResId) {
        if (layouts == null) {
            layouts = new SparseIntArray();
        }
        layouts.put(type, layoutResId);
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

    @Override
    protected int getDefItemViewType(int position) {
        int itemType = getItemType(mData.get(position));
        return itemType != TYPE_NOT_FOUND ? itemType : super.getDefItemViewType(position);
    }

    @Override
    protected K createDefViewHolder(ViewGroup parent, int type) {
        return createBaseViewHolder(parent, getLayoutId(type));
    }

    private
    @LayoutRes
    int getLayoutId(int viewType) {
        return layouts.get(viewType, TYPE_NOT_FOUND);
    }

}

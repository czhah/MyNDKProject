package com.thedream.cz.myndkproject.ui.adapter.base;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/8.
 */

public abstract class BaseStaggeredGrideAdapter<T, K extends BaseViewHolder> extends BaseQuickAdapter {

    protected abstract boolean isLeft(int position);

    protected abstract void checkHeightAndLocal();

    @Override
    protected void refreshData(List list) {
        this.mData = list == null ? new ArrayList<T>() : list;
        checkHeightAndLocal();
        notifyDataSetChanged();
    }

    @Override
    protected void loadMore(List list) {
        if (list == null) return;
        this.mData.addAll(list);
        checkHeightAndLocal();
        notifyDataSetChanged();
    }
}

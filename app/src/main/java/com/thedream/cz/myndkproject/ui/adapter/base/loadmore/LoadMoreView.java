package com.thedream.cz.myndkproject.ui.adapter.base.loadmore;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;

import com.thedream.cz.myndkproject.ui.adapter.base.BaseViewHolder;

/**
 * Created by cz on 2017/12/11.
 * 加载更多
 */

public abstract class LoadMoreView {

    public static final int STATUS_DEFAULT = 1;
    public static final int STATUS_LOADING = 2;
    public static final int STATUS_FAIL = 3;
    public static final int STATUS_END = 4;

    private int mLoadMoreStatus = STATUS_DEFAULT;
    private boolean mLoadMoreEndGone = false;

    public int getLoadMoreStatus() {
        return mLoadMoreStatus;
    }

    public void setLoadMoreStatus(int mLoadMoreStatus) {
        this.mLoadMoreStatus = mLoadMoreStatus;
    }

    public void convert(BaseViewHolder holder) {
        switch (mLoadMoreStatus) {
            case STATUS_DEFAULT:
                visibleLoading(holder, false);
                visibleLoadFail(holder, false);
                visibleLoadEnd(holder, false);
                break;
            case STATUS_LOADING:
                visibleLoading(holder, true);
                visibleLoadFail(holder, false);
                visibleLoadEnd(holder, false);
                break;
            case STATUS_FAIL:
                visibleLoadFail(holder, true);
                visibleLoading(holder, false);
                visibleLoadEnd(holder, false);
                break;
            case STATUS_END:
                visibleLoadEnd(holder, true);
                visibleLoading(holder, false);
                visibleLoadFail(holder, false);
                break;
            default:
                break;
        }
    }

    private void visibleLoading(BaseViewHolder holder, boolean visible) {
        holder.setVisible(getLoadingViewId(), visible);
    }


    private void visibleLoadFail(BaseViewHolder holder, boolean visible) {
        holder.setVisible(getLoadFailViewId(), visible);
    }


    private void visibleLoadEnd(BaseViewHolder holder, boolean visible) {
        holder.setVisible(getLoadEndViewId(), visible);
    }

    public abstract
    @IdRes
    int getLoadFailViewId();

    public abstract
    @IdRes
    int getLoadEndViewId();

    public abstract
    @IdRes
    int getLoadingViewId();

    public abstract
    @LayoutRes
    int getLoadLayoutId();
}

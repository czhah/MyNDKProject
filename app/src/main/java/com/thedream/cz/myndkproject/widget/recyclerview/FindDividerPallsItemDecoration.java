package com.thedream.cz.myndkproject.widget.recyclerview;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.thedream.cz.myndkproject.ui.adapter.StaggeredGridAdapter;
import com.thedream.cz.myndkproject.ui.adapter.base.BaseQuickAdapter;
import com.thedream.cz.myndkproject.ui.adapter.base.BaseStaggeredGridAdapter;
import com.thedream.cz.myndkproject.utils.WindowUtil;

/**
 * Created by chenzhuang on 2017/12/4.
 * 瀑布流间隔
 */

public class FindDividerPallsItemDecoration extends RecyclerView.ItemDecoration {

    private int divier = 0;

    public FindDividerPallsItemDecoration(Context context) {
        this(context, 10);
    }

    public FindDividerPallsItemDecoration(Context context, float dpValue) {
        divier = WindowUtil.dp2px(context, dpValue);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int spanCount = getSpanCount(parent);
        int itemCount = parent.getAdapter().getItemCount();
        int itemPosition = parent.getChildAdapterPosition(view);
        RecyclerView.Adapter adapter = parent.getAdapter();
        if (adapter instanceof StaggeredGridAdapter) {
            if (adapter.getItemViewType(itemPosition) == StaggeredGridAdapter.DEFAULT_TYPE) {
                int realPosition = ((StaggeredGridAdapter) adapter).getRealPosition(itemPosition);
                boolean isLeft = ((StaggeredGridAdapter) adapter).isLeft(realPosition);
                boolean isTop = realPosition < spanCount;
                boolean isBottom = itemPosition > (itemCount - spanCount);
                outRect.set(isLeft ? 0 : divier, isTop ? 0 : divier, isLeft ? divier : 0, isBottom ? 0 : divier);
            } else {
                outRect.set(0, 0, 0, 0);
            }
        }else if(adapter instanceof BaseStaggeredGridAdapter){
            if(adapter.getItemViewType(itemPosition) == BaseQuickAdapter.CONTENT_VIEW) {
                int realPosition = ((BaseStaggeredGridAdapter) adapter).getRealPos(itemPosition);
                boolean isLeft = ((BaseStaggeredGridAdapter) adapter).isLeft(realPosition);
                boolean isTop = realPosition < spanCount;
                boolean isBottom = itemPosition > (itemCount - spanCount);
                outRect.set(isLeft ? 0 : divier, isTop ? 0 : divier, isLeft ? divier : 0, isBottom ? 0 : divier);
            }else if(adapter.getItemViewType(itemPosition) == BaseQuickAdapter.HEADER_VIEW) {
                outRect.set(0, 0, 0, divier * 2);
            }else {
                outRect.set(0, 0, 0, 0);
            }
        } else {
            outRect.set(divier, divier, divier, divier);
        }

    }

    /**
     * 获取列数
     *
     * @param parent
     * @return
     */
    private int getSpanCount(RecyclerView parent) {
        //  列数
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }
        return spanCount;
    }
}
package com.thedream.cz.myndkproject.widget.recyclerview;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.thedream.cz.myndkproject.ui.adapter.StaggeredGridAdapter;
import com.thedream.cz.myndkproject.utils.PrintUtil;
import com.thedream.cz.myndkproject.utils.WindowUtil;

/**
 * Created by chenzhuang on 2017/12/4.
 * 瀑布流间隔
 */

public class FindDividerPallsItemDecoration extends RecyclerView.ItemDecoration {

    private boolean hasHeadAndFoot;
    private int leftHeight = 0;
    private int rightHeight = 0;
    private int divier = 0;

    public FindDividerPallsItemDecoration(Context context) {
        this(context, 10);
    }

    public FindDividerPallsItemDecoration(Context context, int dpValue) {
        this(context, dpValue, true);
    }

    public FindDividerPallsItemDecoration(Context context, int dpValue, boolean hasHeadAndFoot) {
        divier = WindowUtil.dp2px(context, dpValue);
        this.hasHeadAndFoot = hasHeadAndFoot;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int spanCount = getSpanCount(parent);
        int itemCount = parent.getAdapter().getItemCount();
        int itemPosition = parent.getChildAdapterPosition(view);
//        if(hasHeadAndFoot && (itemPosition == 0 || itemPosition == itemCount - 1)){
//            outRect.set(0, 0, 0, 0);
//        } else {
//        }
        RecyclerView.Adapter adapter = parent.getAdapter();
        if (adapter instanceof StaggeredGridAdapter) {
            boolean isLeft = ((StaggeredGridAdapter) adapter).isLeft(itemPosition);
            boolean isTop = itemPosition < spanCount;
            boolean isBottom = itemPosition >= (itemCount - spanCount);
            outRect.set(isLeft ? 0 : divier, isTop ? 0 : divier, isLeft ? divier : 0, isBottom ? 0 : divier);
        } else {
            outRect.set(divier, divier, divier, divier);
        }

    }

    /**
     * 判断是否是最后一列
     *
     * @param parent
     * @param spanCount    列数
     * @param itemCount    child的总数
     * @param itemPosition 当前child的下标
     * @return
     */
    private boolean isLastColum(RecyclerView parent, int spanCount, int itemCount, int itemPosition) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            if ((itemPosition + 1) % spanCount == 0) {
                //  是最后一列
                return true;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                //  纵向
                if ((itemPosition + 1) % spanCount == 0) {
                    return true;
                }
            } else {
                //  横向
                itemCount = itemCount - itemCount % spanCount;
                if (itemPosition >= itemCount)// 如果是最后一列，则不需要绘制右边
                    return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是最后一行
     *
     * @param parent
     * @param spanCount
     * @param itemCount
     * @param itemPosition
     * @return
     */
    private boolean isLastRaw(RecyclerView parent, int spanCount, int itemCount, int itemPosition) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            itemCount = itemCount - (itemCount % spanCount == 0 ? spanCount : itemCount % spanCount);
            if (itemPosition >= itemCount)// 如果是最后一列，则不需要绘制右边
                return true;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                //  纵向
                itemCount = itemCount - itemCount % spanCount;
                if (itemPosition >= itemCount)// 如果是最后一列，则不需要绘制右边
                    return true;
            } else {
                //  搞不太懂
                //  横向
                if ((itemPosition + 1) % spanCount == 0) {
                    return true;
                }
            }
        }

        return false;
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
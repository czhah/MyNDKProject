package com.cz.resource.baserecyclerviewadapterhelper;

import android.content.Context;

import com.cz.resource.baserecyclerviewadapterhelper.utils.WindowHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cz on 2017/12/8.
 *      瀑布流+item间隔 适配器
 */

public abstract class BaseStaggeredGridAdapter<T, K extends BaseViewHolder> extends BaseQuickAdapter<T, K> {

    protected Map<Integer, Integer> heightList = new HashMap<Integer, Integer>();
    protected Map<Integer, Boolean> leftList = new HashMap<>();

    public static final float STAGGERED_ITEM_DECORATION = 1.5F;

    protected final int mSpanCount;
    protected final int screenWidth;
    protected final int viewWidth;
    protected final int decoration;

    public BaseStaggeredGridAdapter(Context context, int spanCount) {
        mSpanCount = spanCount;
        screenWidth = WindowHelper.getScreenWidth(context);
        //  view之间的间隔
        decoration = WindowHelper.dp2px(context, STAGGERED_ITEM_DECORATION);
        //  view宽度是屏幕宽度去掉间隔后除以2
        viewWidth = (screenWidth - decoration * 2) / 2;
    }

    /**
     * 计算每个item的高度
     * @param realPos
     * @return
     */
    protected abstract int calculateHeightByRealPos(int realPos);

    @Override
    public void refreshData(List list) {
        refreshData(list, false);
        initHeightAndLocal();
        notifyDataSetChanged();
    }

    @Override
    public void loadMore(List list) {
        if (list == null) return;
        final int newSize = list.size();
        loadMore(list, false);
        initHeightAndLocal();
        notifyMoreChange(newSize);
    }

    /**
     * 根据数据初始化每个item的高度和位置
     */
    private void initHeightAndLocal() {
        if (mData == null) return;
        leftList.clear();
        heightList.clear();
        int left = 0;
        int right = 0;
        for (int i = 0; i < mData.size(); i++) {
            //  计算每个View的高度
            int height = calculateHeightByRealPos(i);
            heightList.put(i, height);
            //  计算每个View的位置
            boolean isContent = i >= mSpanCount && i < (mData.size() - mSpanCount);
            if (left <= right) {
                left += height + (isContent ? decoration * 2 : decoration);
                leftList.put(i, true);
            } else {
                right += height + (isContent ? decoration * 2 : decoration);
                leftList.put(i, false);
            }
        }
    }

    /**
     * 用于瀑布流每个item之间的间距
     * @param position
     * @return
     */
    public boolean isLeft(int position){
        if (leftList == null || position >= leftList.size()) return false;
        return leftList.get(position);
    }

}

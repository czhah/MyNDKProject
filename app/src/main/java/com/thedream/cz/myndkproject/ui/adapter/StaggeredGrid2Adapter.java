package com.thedream.cz.myndkproject.ui.adapter;

import android.content.Context;
import android.graphics.Color;

import com.thedream.cz.myndkproject.R;
import com.thedream.cz.myndkproject.data.entity.FindInfo;
import com.thedream.cz.myndkproject.ui.adapter.base.BaseStaggeredGridAdapter;
import com.thedream.cz.myndkproject.ui.adapter.base.BaseViewHolder;

/**
 * Created by CZ on 2017/12/11.
 *      瀑布流适配器
 */
public class StaggeredGrid2Adapter extends BaseStaggeredGridAdapter<FindInfo, BaseViewHolder> {

    private final int[] colorArr = {Color.BLUE, Color.CYAN, Color.GREEN, Color.MAGENTA, Color.RED};

    public StaggeredGrid2Adapter(Context context, int spanCount) {
        super(context, spanCount);
    }

    @Override
    protected int calculateHeightByRealPos(int realPos) {
        FindInfo findInfo = mData.get(realPos);
        return viewWidth / findInfo.getWidth() * findInfo.getHeight();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.item_find_layout;
    }

    @Override
    protected void convert(BaseViewHolder holder, FindInfo info) {
        holder.setWidthAndHeight(R.id.tv_find_text, viewWidth, heightList.get(getRealPos(holder.getLayoutPosition())))
                .setText(R.id.tv_find_text, info.getText())
                .setBackgroundColor(R.id.tv_find_text, colorArr[getRealPos(holder.getLayoutPosition()) % colorArr.length]);
    }

}

package com.thedream.cz.myndkproject.ui.adapter;

import com.cz.resource.baserecyclerviewadapterhelper.BaseGridMultiItemAdapter;
import com.cz.resource.baserecyclerviewadapterhelper.BaseViewHolder;
import com.thedream.cz.myndkproject.R;
import com.thedream.cz.myndkproject.data.entity.MultiItemInfo;
import com.thedream.cz.myndkproject.utils.PrintUtil;

/**
 * Created by cz on 2017/12/14.
 * 适配多布局
 */

public class FindMultiAdapter extends BaseGridMultiItemAdapter<MultiItemInfo, BaseViewHolder> {

    //  头布局
    private static final int TYPE_HEAD = 1100;
    //  底部布局
    private static final int TYPE_CONTENT = 1101;

    public FindMultiAdapter(int spanSize) {
        super(spanSize);
        addItemType(TYPE_HEAD, R.layout.item_find_head_layout);
        addItemType(TYPE_CONTENT, R.layout.item_find_content_layout);
    }

    @Override
    public int getItemType(MultiItemInfo info) {
        if (info.getType() == 0) {
            return TYPE_HEAD;
        } else {
            return TYPE_CONTENT;
        }
    }

    @Override
    public int getSpanSizeByType(int viewType, int spanSize) {
        PrintUtil.printCZ("getSpanSizeByType: " + viewType);
        if (viewType == TYPE_HEAD) {
            return spanSize;
        }
        return 1;
    }

    @Override
    protected void convert(BaseViewHolder holder, MultiItemInfo info) {
        int viewType = holder.getItemViewType();
        if (viewType == TYPE_HEAD) {
            holder.setText(R.id.tv_find_head, "头布局：" + info.getText());
        } else {
            holder.setText(R.id.tv_find_content, "内容：" + info.getText());
        }
    }
}

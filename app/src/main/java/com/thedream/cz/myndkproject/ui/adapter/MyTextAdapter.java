package com.thedream.cz.myndkproject.ui.adapter;

import com.cz.resource.baserecyclerviewadapterhelper.BaseQuickAdapter;
import com.cz.resource.baserecyclerviewadapterhelper.BaseViewHolder;
import com.thedream.cz.myndkproject.R;

/**
 * Created by cz on 2018/1/6.
 */

public class MyTextAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    @Override
    protected int getLayoutResId() {
        return R.layout.item_text_layout;
    }

    @Override
    protected void convert(BaseViewHolder holder, String info) {
        holder.setText(R.id.tv_text, info);
    }
}

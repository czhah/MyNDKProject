package com.cz.resource.baserecyclerviewadapterhelper.loadmore;

import com.cz.resource.baserecyclerviewadapterhelper.R;

/**
 * Created by cz on 2017/12/11.
 *      加载更多的布局
 */

public class SimpleLoadMoreView extends LoadMoreView {

    @Override
    public int getLoadFailViewId() {
        return R.id.frame_simple_load_more_fail;
    }

    @Override
    public int getLoadEndViewId() {
        return R.id.frame_simple_load_more_end;
    }

    @Override
    public int getLoadingViewId() {
        return R.id.ll_simple_load_more_loading;
    }

    @Override
    public int getLoadLayoutId() {
        return R.layout.view_simple_load_more_layout;
    }
}

package com.thedream.cz.myndkproject.ui.adapter.base;

import com.thedream.cz.myndkproject.R;

/**
 * Created by cz on 2017/12/11.
 *      加载更多的布局
 */

public class SimpleLoadMoreView extends LoadMoreView {

    @Override
    protected int getLoadFailViewId() {
        return R.id.frame_simple_load_more_fail;
    }

    @Override
    protected int getLoadEndViewId() {
        return R.id.frame_simple_load_more_end;
    }

    @Override
    protected int getLoadingViewId() {
        return R.id.ll_simple_load_more_loading;
    }

    @Override
    protected int getLoadLayoutId() {
        return R.layout.view_simple_load_more_layout;
    }
}

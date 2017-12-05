package com.thedream.cz.myndkproject.ui.activity.find.content;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.thedream.cz.myndkproject.R;
import com.thedream.cz.myndkproject.data.entity.FindInfo;
import com.thedream.cz.myndkproject.ui.adapter.StaggeredGridAdapter;
import com.thedream.cz.myndkproject.ui.common.BaseFragment;
import com.thedream.cz.myndkproject.utils.PrintUtil;
import com.thedream.cz.myndkproject.utils.ToastUtil;
import com.thedream.cz.myndkproject.widget.ScrollLayout;
import com.thedream.cz.myndkproject.widget.recyclerview.FindDividerPallsItemDecoration;

import java.util.List;

import static com.thedream.cz.myndkproject.ui.adapter.StaggeredGridAdapter.ITEM_DECORATION;

/**
 * Created by cz on 2017/11/29.
 *      发现页面列表
 */

public class FindFragment extends BaseFragment<FindContract.Presenter> implements FindContract.View {

    private ProgressDialog mProgress;
    private StaggeredGridAdapter mAdapter;
    private ScrollLayout mScrollLayout;

    private FindFragment() {
    }

    public static FindFragment getInstance() {
        return new FindFragment();
    }

    @Override
    protected int getLayoutInflaterId() {
        return R.layout.fragment_find_list_layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mScrollLayout = (ScrollLayout) view.findViewById(R.id.scrollLayout);
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.addItemDecoration(new FindDividerPallsItemDecoration(getContext(), ITEM_DECORATION));
        mRecyclerView.setAdapter(mAdapter = new StaggeredGridAdapter(getContext(), 2));
        initHeadView();
    }

    private void initHeadView() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        PrintUtil.printCZ("onActivityCreated");
        mPresenter.refresh();
    }

    @Override
    public void setPresenter(FindContract.Presenter presenter) {
        if (null == presenter) {
            mPresenter = new FindPresenter(this);
        }
    }

    @Override
    public void showProgress(boolean alive) {
        if (alive) {
            mProgress = ProgressDialog.show(getContext(), "", "加载中...", false, false);
            mProgress.show();
        } else {
            if (mProgress != null && mProgress.isShowing()) {
                mProgress.dismiss();
                mProgress = null;
            }
        }
    }

    @Override
    public void refresh(List<FindInfo> list) {
        mAdapter.refresh(list);
        mScrollLayout.invalidate();
    }

    @Override
    public void loadMore(List<FindInfo> list) {
        mAdapter.loadMore(list);
    }

    @Override
    public boolean isAlive() {
        return isAdded();
    }

    @Override
    public void showTip(String text) {
        ToastUtil.showToast(getContext(), text);
    }

}

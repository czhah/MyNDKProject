package com.thedream.cz.myndkproject.ui.activity.find.content;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.cz.resource.baserecyclerviewadapterhelper.BaseQuickAdapter;
import com.cz.resource.baserecyclerviewadapterhelper.BaseStaggeredGridAdapter;
import com.thedream.cz.myndkproject.R;
import com.thedream.cz.myndkproject.data.entity.FindInfo;
import com.thedream.cz.myndkproject.ui.adapter.StaggeredGrid2Adapter;
import com.thedream.cz.myndkproject.ui.common.BaseFragment;
import com.thedream.cz.myndkproject.ui.dialog.LoadingDialog;
import com.thedream.cz.myndkproject.utils.PrintUtil;
import com.thedream.cz.myndkproject.utils.ToastUtil;
import com.thedream.cz.myndkproject.widget.recyclerview.FindDividerPallsItemDecoration;

import java.util.List;

/**
 * Created by cz on 2017/11/29.
 *      发现页面列表
 */

public class FindFragment extends BaseFragment<FindContract.Presenter> implements FindContract.View {

    private StaggeredGrid2Adapter mAdapter;
    private LoadingDialog mDialog;

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
        RecyclerView mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.addItemDecoration(new FindDividerPallsItemDecoration(getContext(), BaseStaggeredGridAdapter.STAGGERED_ITEM_DECORATION));
        mRecyclerView.setAdapter(mAdapter = new StaggeredGrid2Adapter(getContext(), 2));
        initHeadView();
    }

    private void initHeadView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_banner, null);
        mAdapter.addHeaderView(view);
        View emptyView = LayoutInflater.from(getContext()).inflate(R.layout.view_empty_layout, null);
        mAdapter.setEmptyView(emptyView);
        mAdapter.setLoadMoreEnable(true);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mPresenter.loadMore();
            }
        });
        mAdapter.setOnItemClickListener((v, position) -> {
            FindInfo info = mAdapter.getItem(position);
            ToastUtil.showToast(getContext(), "点击了第 " + info.getText() + " 个");
        });
        mAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, int position) {
                FindInfo info = mAdapter.getItem(position);
                ToastUtil.showToast(getContext(), "长按了第 " + info.getText() + " 个");
                return true;
            }
        });
        mAdapter.setOnItemChildClickListener((v, position) -> {
            FindInfo info = mAdapter.getItem(position);
            ToastUtil.showToast(getContext(), "点击了Child第 " + info.getText() + " 个");
        });
//        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
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
            mDialog = LoadingDialog.show(getFragmentManager(), R.string.text_loading, false);
        } else {
            if (mDialog != null && mDialog.isVisible()) {
                mDialog.dismiss();
                mDialog = null;
            }
        }
    }

    @Override
    public void onSuccess(boolean isRefresh, List<FindInfo> list) {
        if (isRefresh) {
            mAdapter.refreshData(list);
        } else {
            if (list != null && list.size() > 0) {
                mAdapter.loadMoreComplete();
                mAdapter.loadMore(list);
            } else {
                mAdapter.loadMoreEnd();
            }
        }
    }

    @Override
    public void onFail(boolean isRefresh, String msg) {
        ToastUtil.showToast(getContext(), msg);
        if (!isRefresh) mAdapter.loadMoreFail();
    }

    @Override
    public boolean isAlive() {
        return isAdded();
    }

}

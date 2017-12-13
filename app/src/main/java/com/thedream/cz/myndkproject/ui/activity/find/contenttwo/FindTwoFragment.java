package com.thedream.cz.myndkproject.ui.activity.find.contenttwo;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thedream.cz.myndkproject.R;
import com.thedream.cz.myndkproject.mvp.factory.CreatePresenter;
import com.thedream.cz.myndkproject.mvp.view.BaseMvpFragment;
import com.thedream.cz.myndkproject.ui.dialog.LoadingDialog;
import com.thedream.cz.myndkproject.utils.ToastUtil;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FindTwoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@CreatePresenter(FindTwoPresenter.class)
public class FindTwoFragment extends BaseMvpFragment<FindTwoView, FindTwoPresenter> implements FindTwoView {

    private LoadingDialog mLoadingDialog;

    public FindTwoFragment() {
    }

    public static FindTwoFragment newInstance() {
        return new FindTwoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_find_two, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void showProgress(boolean active) {
        if (active) {
            mLoadingDialog = LoadingDialog.show(getChildFragmentManager(), R.string.text_loading, false);
        } else {
            if (mLoadingDialog != null && mLoadingDialog.isVisible()) {
                mLoadingDialog.dismiss();
                mLoadingDialog = null;
            }
        }
    }

    @Override
    public void onSuccess(boolean isRefresh, List list) {

    }

    @Override
    public void onFail(boolean isRefresh, int result) {
        ToastUtil.showResult(getContext(), result);
    }

}

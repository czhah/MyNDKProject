package com.thedream.cz.myndkproject.ui.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.thedream.cz.myndkproject.R;

/**
 * Created by cz on 2017/12/13.
 * 加载框
 */
public class LoadingDialog extends DialogFragment {

    private static final String TAG = "LoadingDialog";

    private
    @StringRes
    int contentId = 0;
    private boolean cancelable = false;

    public LoadingDialog() {
        //  这里必须有无参构造函数
    }

    private LoadingDialog(@StringRes int content, boolean cancelable) {
        this.contentId = content;
        this.cancelable = cancelable;
    }

    public static LoadingDialog show(FragmentManager fm, @StringRes int textId, boolean cancelable) {
        LoadingDialog mLoadingDialog = new LoadingDialog(textId, cancelable);
        mLoadingDialog.show(fm, TAG);
        return mLoadingDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  处理切换屏幕导致LoadingDialog不消失问题
        if (savedInstanceState != null) {
            LoadingDialog mLoadingDialog = (LoadingDialog) getFragmentManager().findFragmentByTag(TAG);
            if (mLoadingDialog != null) {
                mLoadingDialog.dismissAllowingStateLoss();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(cancelable);
        return inflater.inflate(R.layout.dialog_loading_layout, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (contentId == 0) {
            return;
        }
        ((TextView) view.findViewById(R.id.tv_loading_content)).setText(contentId);
    }
}

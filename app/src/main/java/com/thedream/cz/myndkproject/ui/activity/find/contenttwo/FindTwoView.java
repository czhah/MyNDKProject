package com.thedream.cz.myndkproject.ui.activity.find.contenttwo;

import com.thedream.cz.myndkproject.data.entity.MultiItemInfo;
import com.thedream.cz.myndkproject.mvp.view.BaseMvpView;

import java.util.List;

/**
 * Created by cz on 2017/12/13.
 */

public interface FindTwoView extends BaseMvpView {

    void showProgress(boolean active);

    void onSuccess(boolean isRefresh, List<MultiItemInfo> list);

    void onFail(boolean isRefresh, int result);
}

package com.thedream.cz.myndkproject.ui.activity.find.content;

import com.thedream.cz.myndkproject.IBasePresenter;
import com.thedream.cz.myndkproject.IBaseView;
import com.thedream.cz.myndkproject.data.entity.FindInfo;

import java.util.List;

/**
 * Created by cz on 2017/12/1.
 * 发现页面瀑布流数据
 */

public interface FindContract {

    interface Presenter extends IBasePresenter {

        void refresh();

        void loadMore();

        void reLoad();
    }

    interface View extends IBaseView<Presenter> {
        void showProgress(boolean alive);

        void onSuccess(boolean isRefresh, List<FindInfo> list);

        void onFail(boolean isRefresh, String msg);

        boolean isAlive();

    }

}

package com.thedream.cz.myndkproject.ui.activity.find.contenttwo;

import com.thedream.cz.myndkproject.bean.WebResultInfo;
import com.thedream.cz.myndkproject.data.entity.MultiItemInfo;
import com.thedream.cz.myndkproject.listener.OnResultListener;
import com.thedream.cz.myndkproject.mvp.presenter.BaseMvpPresenter;
import com.thedream.cz.myndkproject.utils.PrintUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by cz on 2017/12/13.
 */

public class FindTwoPresenter extends BaseMvpPresenter<FindTwoView> {

    public FindTwoPresenter() {
    }

    private final OnResultListener listener = new OnResultListener<List<MultiItemInfo>>() {
        @Override
        public void onSuccess(List<MultiItemInfo> list) {
            if (getMvpView() == null) return;
            getMvpView().showProgress(false);
            if (list != null) {
                getMvpView().onSuccess(true, list);
            } else {
                getMvpView().onFail(true, WebResultInfo.RESULT_FAILED);
            }
        }

        @Override
        public void onFailed(int code) {
            if (getMvpView() == null) return;
            getMvpView().showProgress(false);
            getMvpView().onFail(true, code);
        }
    };

    public void refresh() {
        if (getMvpView() == null) return;
        getMvpView().showProgress(true);
        Observable.create(new ObservableOnSubscribe<List<? extends MultiItemInfo>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<? extends MultiItemInfo>> e) throws Exception {
                PrintUtil.printCZ("执行中...");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                List<MultiItemInfo> list = new ArrayList<MultiItemInfo>();
                for (int i = 0; i < 100; i++) {
                    MultiItemInfo info = new MultiItemInfo();
                    info.setType((i % 3) == 0 ? 0 : 1);
                    info.setText(i);
                    list.add(info);
                }
                e.onNext(list);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((list -> {
                    PrintUtil.printCZ("成功线程：" + Thread.currentThread());
                    listener.onSuccess(list);
                }), (throwable -> {
                    PrintUtil.printCZ("失败线程：" + Thread.currentThread());
                    listener.onFailed(WebResultInfo.RESULT_FAILED);
                }));
    }
}

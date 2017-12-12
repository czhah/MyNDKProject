package com.thedream.cz.myndkproject.ui.activity.find.content;

import com.thedream.cz.myndkproject.data.entity.FindInfo;
import com.thedream.cz.myndkproject.utils.PrintUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by cz on 2017/12/1.
 * 瀑布流
 */

public class FindPresenter implements FindContract.Presenter {
    private final FindContract.View mView;
    private int pageNum = 1;
    public FindPresenter(FindContract.View view) {
        this.mView = view;
    }

    @Override
    public void refresh() {
        pageNum = 1;
        mView.showProgress(true);
        Observable.just(1).map(new Function<Integer, List<FindInfo>>() {
            @Override
            public List<FindInfo> apply(@NonNull Integer integer) throws Exception {
                List<FindInfo> list = new ArrayList<>();
                int[] array = {155, 110, 144, 161, 102, 75, 175, 123, 209, 90};
                for (int i = integer; i < 30; i++) {
                    FindInfo info = new FindInfo();
                    info.setText(i + "");
                    info.setWidth(array[i % array.length]);
                    info.setHeight(200);
                    list.add(info);
                }
                Thread.sleep(3000);
                return list;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((list) -> {
                    if (!mView.isAlive()) return;
                    mView.showProgress(false);
                    mView.onSuccess(true, list);
                }, (throwable -> {
                    PrintUtil.printCZ("加载失败:" + throwable.toString());
                    if (!mView.isAlive()) return;
                    mView.showProgress(false);
                    mView.onFail(true, "加载失败:" + throwable.getMessage());
                }));
    }

    @Override
    public void loadMore() {
        pageNum++;
        reLoad();
    }

    @Override
    public void reLoad() {
        mView.showProgress(true);
        Observable.just(1).map(new Function<Integer, List<FindInfo>>() {
            @Override
            public List<FindInfo> apply(@NonNull Integer integer) throws Exception {
                List<FindInfo> list = new ArrayList<>();
                int[] array = {155, 110, 144, 161, 102, 75, 175, 123, 209, 90};
                for (int i = integer; i < 30; i++) {
                    FindInfo info = new FindInfo();
                    info.setText("加载更多:" + i);
                    info.setWidth(array[i % array.length]);
                    info.setHeight(200);
                    list.add(info);
                }
                Thread.sleep(3000);
                if (pageNum == 3) {
                    throw new IllegalArgumentException("测试重新加载");
                }
                return list;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((list) -> {
                    if (!mView.isAlive()) return;
                    mView.showProgress(false);
                    if (pageNum == 4) list.clear();
                    mView.onSuccess(false, list);
                }, (throwable -> {
                    PrintUtil.printCZ("加载失败:" + throwable.toString());
                    if (!mView.isAlive()) return;
                    mView.showProgress(false);
                    mView.onFail(false, "加载失败:" + throwable.getMessage());
                }));
    }
}

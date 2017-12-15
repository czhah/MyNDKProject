package com.thedream.cz.myndkproject.data;

import com.thedream.cz.myndkproject.RetrofitFactory;
import com.thedream.cz.myndkproject.bean.WebResultInfo;
import com.thedream.cz.myndkproject.data.entity.CityInfo;
import com.thedream.cz.myndkproject.data.entity.LoginInfo;
import com.thedream.cz.myndkproject.data.local.dao.LoginDao;
import com.thedream.cz.myndkproject.data.remote.IPublicApi;
import com.thedream.cz.myndkproject.listener.OnResultListener;
import com.thedream.cz.myndkproject.utils.PrintUtil;

import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by cz on 2017/11/29.
 * 公共数据类
 */
public class CommonDataRepository {

    private static CommonDataRepository instance;
    private Class<IPublicApi> iPublicApiClass = IPublicApi.class;

    private LoginDao mLoginDao;

    private CommonDataRepository(LoginDao loginDao) {
        this.mLoginDao = loginDao;
    }

    public static CommonDataRepository getInstance(LoginDao loginDao) {
        if (null == instance) {
            synchronized (CommonDataRepository.class) {
                if (null == instance) instance = new CommonDataRepository(loginDao);
            }
        }
        return instance;
    }

    /**
     * 用户登录
     *
     * @param name     用户名
     * @param pwd      密码
     * @param listener
     */
    public void userLogin(String name, String pwd, OnResultListener<LoginInfo> listener) {
        RetrofitFactory.getRetrofit().create(iPublicApiClass)
                .login(name, pwd, Locale.getDefault().toString())
                .doOnNext(loginInfoWebResultInfo -> {
                    if (loginInfoWebResultInfo.getStatusCode() == WebResultInfo.RESULT_SUCCESS) {
                        PrintUtil.printCZ("插入结果：" + mLoginDao.insertLogin(loginInfoWebResultInfo.getData()));
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((webResultInfo) -> {
                    PrintUtil.printCZ("线程:" + Thread.currentThread());
                    if (webResultInfo.getStatusCode() == WebResultInfo.RESULT_SUCCESS) {
                        listener.onSuccess(webResultInfo.getData());
                    } else {
                        listener.onFailed(webResultInfo.getStatusCode());
                    }
                }, (throwable) -> {
                    listener.onFailed(WebResultInfo.RESULT_FAILED);
                });
    }

    /**
     * 查询本地数据
     *
     * @param uid
     * @param listener
     */
    public void queryLocalLogin(String uid, OnResultListener<LoginInfo> listener) {
        mLoginDao.queryLoginById(uid)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((loginInfo -> {
                    PrintUtil.printCZ("成功线程：" + Thread.currentThread());
                    listener.onSuccess(loginInfo);
                }), (throwable -> {
                    PrintUtil.printCZ("失败线程：" + Thread.currentThread());
                    listener.onFailed(WebResultInfo.RESULT_FAILED);
                }));
    }

    /**
     * 查询所有
     *
     * @param listener
     */
    public void queryLocalLogin(OnResultListener<List<LoginInfo>> listener) {
        Observable.create(new ObservableOnSubscribe<List<LoginInfo>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<LoginInfo>> e) throws Exception {
                e.onNext(mLoginDao.queryList());
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((loginInfo -> {
                    PrintUtil.printCZ("成功线程：" + Thread.currentThread());
                    listener.onSuccess(loginInfo);
                }), (throwable -> {
                    PrintUtil.printCZ("失败线程：" + Thread.currentThread());
                    listener.onFailed(WebResultInfo.RESULT_FAILED);
                }));

//        Observable.just("haha").subscribeOn(Schedulers.newThread())
//                .map(new Function<String, Integer>() {
//                    @Override
//                    public Integer apply(@NonNull String s) throws Exception {
//                        PrintUtil.printCZ("map 线程:" + Thread.currentThread());
//                        return 1;
//                    }
//                }).observeOn(AndroidSchedulers.mainThread())
//                .doOnNext((integer -> {
//                    PrintUtil.printCZ("doOnNext 线程:" + Thread.currentThread());
//                }))
//                .observeOn(Schedulers.io())
//                .subscribe((integer -> {
//                    PrintUtil.printCZ("subscribe 线程:" + Thread.currentThread());
//
//                }));
    }


    public void queryCityList(OnResultListener<List<CityInfo>> listener) {
        RetrofitFactory.getRetrofit().create(iPublicApiClass)
                .getCity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listWebResultInfo -> {
                    if (listWebResultInfo.getStatusCode() == WebResultInfo.RESULT_SUCCESS) {
                        listener.onSuccess(listWebResultInfo.getData());
                    } else {
                        listener.onFailed(listWebResultInfo.getStatusCode());
                    }
                }, throwable -> {
                    listener.onFailed(WebResultInfo.RESULT_FAILED);
                });

    }

}

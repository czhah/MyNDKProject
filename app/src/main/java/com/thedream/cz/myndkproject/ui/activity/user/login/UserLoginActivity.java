package com.thedream.cz.myndkproject.ui.activity.user.login;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.thedream.cz.myndkproject.R;
import com.thedream.cz.myndkproject.data.entity.CityInfo;
import com.thedream.cz.myndkproject.data.entity.LoginInfo;
import com.thedream.cz.myndkproject.data.local.AppLocalData;
import com.thedream.cz.myndkproject.listener.OnResultListener;
import com.thedream.cz.myndkproject.ui.common.BaseActivity;
import com.thedream.cz.myndkproject.ui.common.BaseApplication;
import com.thedream.cz.myndkproject.utils.PrintUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class UserLoginActivity extends BaseActivity {

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;


    private List<String> mTitleList = new ArrayList<>();
    private List<Fragment> mViewList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        ButterKnife.bind(this);
//        UserLoginFragment statisticsFragment = (UserLoginFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.contentFrame);
//        if (statisticsFragment == null) {
//            statisticsFragment = UserLoginFragment.newInstance();
//            ActivityUtil.addFragmentToActivity(getSupportFragmentManager(), statisticsFragment, R.id.contentFrame);
//        }

//        OtherFragment fragment1 = OtherFragment.newInstance("1", "1");
//        NumberFragment fragment2 = NumberFragment.newInstance("2", "2");
//        NumberFragment fragment3 = NumberFragment.newInstance("3", "3");
//
//        mViewList.add(fragment1);
//        mViewList.add(fragment2);
//        mViewList.add(fragment3);
//
//        mTitleList.add("Tab1");
//        mTitleList.add("Tab2");
//        mTitleList.add("Tab3");
//
//        tabLayout.setTabMode(TabLayout.MODE_FIXED);
//        tabLayout.addTab(tabLayout.newTab());
//        tabLayout.addTab(tabLayout.newTab());
//        tabLayout.addTab(tabLayout.newTab());
//
//        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager()
//                , mViewList
//                , mTitleList);
//        viewPager.setAdapter(myPagerAdapter);
//        tabLayout.setupWithViewPager(viewPager);
//        tabLayout.setTabsFromPagerAdapter(myPagerAdapter);

        ((BaseApplication) BaseApplication.mApplication).getCommonDataRepository().queryLocalLogin(new OnResultListener<List<LoginInfo>>() {
            @Override
            public void onSuccess(List<LoginInfo> loginInfos) {
                if (loginInfos != null) {
                    for (LoginInfo info : loginInfos) {
                        PrintUtil.printCZ("查询数据:" + info.toString());
                    }
                }
            }

            @Override
            public void onFailed(int code) {
                PrintUtil.printCZ("失败:" + code);
            }
        });
    }

    class MyPagerAdapter extends FragmentPagerAdapter {
        List<Fragment> list;//ViewPager要填充的fragment列表
        List<String> title;//tab中的title文字列表

        //使用构造方法来将数据传进去
        public MyPagerAdapter(FragmentManager fm, List<Fragment> list, List<String> title) {
            super(fm);
            this.list = list;
            this.title = title;
        }

        @Override
        public Fragment getItem(int position) {//获得position中的fragment来填充
            return list.get(position);
        }

        @Override
        public int getCount() {//返回FragmentPager的个数
            return list.size();
        }

        //FragmentPager的标题,如果重写这个方法就显示不出tab的标题内容
        @Override
        public CharSequence getPageTitle(int position) {
            return title.get(position);
        }
    }

    @OnClick(R.id.btn_save)
    public void save() {
        Observable.create(new ObservableOnSubscribe<List<Long>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Long>> e) throws Exception {
                List<CityInfo> list = new ArrayList<CityInfo>();
                for (int i = 0; i < 10; i++) {
                    CityInfo info = new CityInfo();
                    info.setId("" + i);
                    info.setCityName("城市" + i);
                    info.setCityId(i * 10 + "");
                    list.add(info);
                }

                e.onNext(AppLocalData.getInstance(UserLoginActivity.this).cityDao().insertCity(list));
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Long>>() {
                    @Override
                    public void accept(@NonNull List<Long> longs) throws Exception {
                        if (longs != null) {
                            for (Long l : longs) {
                                PrintUtil.printCZ("下标:" + l.longValue());
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        PrintUtil.printCZ("保存:" + throwable.toString());
                    }
                });
    }

    @OnClick(R.id.btn_query)
    public void query() {
        Observable.create(new ObservableOnSubscribe<List<CityInfo>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<CityInfo>> e) throws Exception {
                e.onNext(AppLocalData.getInstance(UserLoginActivity.this).cityDao().queryCity());
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<CityInfo>>() {
                    @Override
                    public void accept(@NonNull List<CityInfo> cityInfos) throws Exception {
                        if (cityInfos != null) {
                            for (CityInfo info : cityInfos) {
                                PrintUtil.printCZ("查询:" + info.toString());
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        PrintUtil.printCZ("查询:" + throwable.toString());
                    }
                });
    }
}

package com.thedream.cz.myndkproject.ui.activity.user.login;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.thedream.cz.myndkproject.R;
import com.thedream.cz.myndkproject.ui.common.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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

        OtherFragment fragment1 = OtherFragment.newInstance("1", "1");
        NumberFragment fragment2 = NumberFragment.newInstance("2", "2");
        NumberFragment fragment3 = NumberFragment.newInstance("3", "3");

        mViewList.add(fragment1);
        mViewList.add(fragment2);
        mViewList.add(fragment3);

        mTitleList.add("Tab1");
        mTitleList.add("Tab2");
        mTitleList.add("Tab3");

        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());

        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager()
                , mViewList
                , mTitleList);
        viewPager.setAdapter(myPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabsFromPagerAdapter(myPagerAdapter);
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
}

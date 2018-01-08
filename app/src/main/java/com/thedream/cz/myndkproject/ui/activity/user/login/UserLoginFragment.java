package com.thedream.cz.myndkproject.ui.activity.user.login;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.EditText;

import com.thedream.cz.myndkproject.R;
import com.thedream.cz.myndkproject.ui.activity.find.content.FindActivity;
import com.thedream.cz.myndkproject.ui.common.BaseApplication;
import com.thedream.cz.myndkproject.ui.common.BaseFragment;
import com.thedream.cz.myndkproject.ui.dialog.LoadingDialog;
import com.thedream.cz.myndkproject.utils.PrintUtil;
import com.thedream.cz.myndkproject.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserLoginFragment extends BaseFragment<UserLoginContract.Presenter> implements UserLoginContract.View {

    private EditText etName;
    private EditText etPwd;
    private LoadingDialog mLoadingDialog;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    public static UserLoginFragment newInstance() {
        return new UserLoginFragment();
    }

    @Override
    protected int getLayoutInflaterId() {
        return R.layout.fragment_user_login;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        etName = (EditText) view.findViewById(R.id.et_name);
        etPwd = (EditText) view.findViewById(R.id.et_pwd);

        view.findViewById(R.id.btn_login).setOnClickListener((v) -> {
            String name = etName.getText().toString();
            String pwd = etPwd.getText().toString();
            mPresenter.login(name, pwd);
        });
        view.findViewById(R.id.btn_query).setOnClickListener((v) -> {
            mPresenter.query();
        });
        view.findViewById(R.id.btn_all).setOnClickListener((v) -> {
            mPresenter.queryList();
        });


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        NumberFragment fragment1 = NumberFragment.newInstance("1", "1");
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

        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getChildFragmentManager()
                , mViewList
                , mTitleList);
        viewPager.setAdapter(myPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabsFromPagerAdapter(myPagerAdapter);
    }

    @Override
    public void setPresenter(UserLoginContract.Presenter presenter) {
        if (null == presenter) {
            mPresenter = new UserLoginPresenter(this, ((BaseApplication) BaseApplication.mApplication).getCommonDataRepository());
        }

    }

    @Override
    public void showProgress(boolean isShow) {
        if (isShow) {
            mLoadingDialog = LoadingDialog.show(getChildFragmentManager(), R.string.text_loading, false);
        } else {
            if (mLoadingDialog != null && mLoadingDialog.isVisible()) {
                mLoadingDialog.dismiss();
                mLoadingDialog = null;
            }
        }
    }

    @Override
    public void onResult(String result) {
        PrintUtil.printCZ("result：" + result);
        ToastUtil.showToast(getContext(), "登录成功！");
        FindActivity.launch(getContext());
    }

    @Override
    public void onError(int code) {
        ToastUtil.showToast(getContext(), "登录错误:" + code);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showTip(String text) {
        ToastUtil.showToast(getContext(), text);
    }

    private List<String> mTitleList = new ArrayList<>();
    private List<NumberFragment> mViewList = new ArrayList<>();

    class MyPagerAdapter extends FragmentPagerAdapter {
        List<NumberFragment> list;//ViewPager要填充的fragment列表
        List<String> title;//tab中的title文字列表

        //使用构造方法来将数据传进去
        public MyPagerAdapter(FragmentManager fm, List<NumberFragment> list, List<String> title) {
            super(fm);
            this.list = list;
            this.title = title;
        }

        @Override
        public NumberFragment getItem(int position) {//获得position中的fragment来填充
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

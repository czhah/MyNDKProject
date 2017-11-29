package com.thedream.cz.myndkproject.ui.activity.user.login;

import android.os.Bundle;

import com.thedream.cz.myndkproject.R;
import com.thedream.cz.myndkproject.ui.common.BaseActivity;
import com.thedream.cz.myndkproject.utils.ActivityUtil;

public class UserLoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        UserLoginFragment statisticsFragment = (UserLoginFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);
        if (statisticsFragment == null) {
            statisticsFragment = UserLoginFragment.newInstance();
            ActivityUtil.addFragmentToActivity(getSupportFragmentManager(), statisticsFragment, R.id.contentFrame);
        }
    }
}

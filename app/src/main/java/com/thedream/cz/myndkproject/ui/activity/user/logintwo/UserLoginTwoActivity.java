package com.thedream.cz.myndkproject.ui.activity.user.logintwo;

import android.os.Bundle;

import com.thedream.cz.myndkproject.R;
import com.thedream.cz.myndkproject.mvp.view.BaseMvpActivity;
import com.thedream.cz.myndkproject.mvp.view.BaseMvpView;

public class UserLoginTwoActivity extends BaseMvpActivity implements BaseMvpView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login_two);
    }
}

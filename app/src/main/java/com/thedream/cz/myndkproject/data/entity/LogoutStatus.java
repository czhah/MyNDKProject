package com.thedream.cz.myndkproject.data.entity;

import android.content.Context;
import android.content.Intent;

import com.thedream.cz.myndkproject.listener.ILoginStatus;
import com.thedream.cz.myndkproject.ui.activity.user.logintwo.UserLoginTwoActivity;

/**
 * Created by cz on 2017/12/23.
 */

public class LogoutStatus implements ILoginStatus {
    @Override
    public void forward(Context context) {
        context.startActivity(new Intent(context, UserLoginTwoActivity.class));
    }
}

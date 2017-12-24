package com.thedream.cz.myndkproject.data.entity;

import android.content.Context;

import com.thedream.cz.myndkproject.listener.ILoginStatus;
import com.thedream.cz.myndkproject.utils.ToastUtil;

/**
 * Created by cz on 2017/12/23.
 */

public class LoginStatus implements ILoginStatus {
    @Override
    public void forward(Context context) {
        ToastUtil.showToast(context, "品论微博");
    }
}

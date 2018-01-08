package com.thedream.cz.myndkproject.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.thedream.cz.myndkproject.utils.PrintUtil;
import com.thedream.cz.myndkproject.utils.ToastUtil;

/**
 * Created by cz on 2017/12/23.
 */

public class ThirdReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int limit = intent.getIntExtra("limit", -1001);
        PrintUtil.printCZ("onReceive limit:" + limit);
        if (limit == 10) {
            Bundle bundle = getResultExtras(true);
            ToastUtil.showToast(context, bundle.getString("new"));
            abortBroadcast();
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("new", "来自ThirdReceiver的广播");
            setResultExtras(bundle);
        }
    }
}
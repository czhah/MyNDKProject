package com.thedream.cz.myndkproject.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.thedream.cz.myndkproject.utils.PrintUtil;
import com.thedream.cz.myndkproject.utils.ToastUtil;

public class FirstReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int limit = intent.getIntExtra("limit", -1001);
        PrintUtil.printCZ("FirstReceiver limit:" + limit);
        if (limit == 1000) {
            ToastUtil.showToast(context, intent.getStringExtra("msg"));
            abortBroadcast();
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("new", "来自FirstReceiver的广播");
            setResultExtras(bundle);
        }
    }
}

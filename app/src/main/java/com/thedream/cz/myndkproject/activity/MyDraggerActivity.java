package com.thedream.cz.myndkproject.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.thedream.cz.myndkproject.R;
import com.thedream.cz.myndkproject.bean.MyInfo;
import com.thedream.cz.myndkproject.bean.SideInfo;
import com.thedream.cz.myndkproject.ui.components.DaggerSideInfoCompoent;
import com.thedream.cz.myndkproject.ui.components.SideInfoCompoent;
import com.thedream.cz.myndkproject.ui.modules.MyModule;
import com.thedream.cz.myndkproject.utils.PrintUtil;

import javax.inject.Inject;

public class MyDraggerActivity extends AppCompatActivity {


    @Inject
    SideInfo info;

    @Inject
    MyInfo myInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_dragger);
//
        SideInfoCompoent sideInfoCompoent = DaggerSideInfoCompoent.builder().myModule(new MyModule(-1, "测试")).build();
        sideInfoCompoent.inject(this);

        findViewById(R.id.btn_show).setOnClickListener(v -> {
            PrintUtil.printCZ("信息:" + info.toString());
            PrintUtil.printCZ("信息2:" + myInfo.toString());

//            mToastUtil.showToast("测试!!!");
        });
    }
}

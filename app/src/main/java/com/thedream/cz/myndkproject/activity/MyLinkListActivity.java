package com.thedream.cz.myndkproject.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.thedream.cz.myndkproject.R;

import java.util.ArrayList;
import java.util.List;

public class MyLinkListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_link_list);

        List<String> list = new ArrayList<>();
        list.add("haha");
        list.add("wuwuw");
        list.remove(0);

        findViewById(R.id.btn_single_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(R.id.btn_double_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}

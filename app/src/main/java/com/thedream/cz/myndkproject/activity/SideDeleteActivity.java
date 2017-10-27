package com.thedream.cz.myndkproject.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.thedream.cz.myndkproject.R;
import com.thedream.cz.myndkproject.adapter.SideDeleteAdapter;
import com.thedream.cz.myndkproject.customview.recyclerview.LinearItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class SideDeleteActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<String> list = new ArrayList<>();
    private SideDeleteAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_delete);

        initData();
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter = new SideDeleteAdapter(this));
        mRecyclerView.addItemDecoration(new LinearItemDecoration());
        mAdapter.refreshData(list);

        Button btnClick = (Button) findViewById(R.id.btn_click);
        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.onSwiped(5);
            }
        });
    }

    private void initData() {
        for(int i = 0; i < 50; i++) {
            list.add("123456789456456413163465fdfdjdfdjfdjlkdjfldjl"+i);
        }
    }
}

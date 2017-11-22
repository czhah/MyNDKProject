package com.thedream.cz.myndkproject.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.thedream.cz.myndkproject.R;
import com.thedream.cz.myndkproject.adapter.TextAdapter;
import com.thedream.cz.myndkproject.db.entity.ProductEntity;
import com.thedream.cz.myndkproject.utils.PrintUtil;
import com.thedream.cz.myndkproject.viewmodel.ProductListViewModel;

import java.util.List;

public class GuideActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private TextAdapter mAdapter;
    private ProductListViewModel viewModel;
    private int pageNum = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        findViewById(R.id.btn_load).setOnClickListener(v -> {
            loadMore();
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter = new TextAdapter(this));

        viewModel = ViewModelProviders.of(this).get(ProductListViewModel.class);
        viewModel.getmObservableProducts().observe(this, new Observer<List<ProductEntity>>() {
            @Override
            public void onChanged(@Nullable List<ProductEntity> productEntities) {
                PrintUtil.printCZ("是否为空:" + productEntities);
                if (productEntities != null) {
                    if (pageNum == 1) {
                        mAdapter.refreshData(productEntities);
                    } else {
                        mAdapter.loadMoreData(productEntities);
                    }
                }
            }
        });
    }

    private void loadMore() {
        pageNum++;
        Toast.makeText(this, "加载更多", Toast.LENGTH_SHORT).show();
        viewModel.loadMore();
    }
}

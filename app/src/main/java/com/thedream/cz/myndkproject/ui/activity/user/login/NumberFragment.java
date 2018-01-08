package com.thedream.cz.myndkproject.ui.activity.user.login;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thedream.cz.myndkproject.R;
import com.thedream.cz.myndkproject.ui.adapter.MyTextAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NumberFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NumberFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    @BindView(R.id.tv_text)
    TextView tvText;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    public NumberFragment() {
        // Required empty public constructor
    }

    public static NumberFragment newInstance(String param1, String param2) {
        NumberFragment fragment = new NumberFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_number, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mParam1.equals("1")) {
            tvText.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
            MyTextAdapter mAdapter = new MyTextAdapter();
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mRecyclerView.setAdapter(mAdapter);
            List<String> list = new ArrayList<>();
            for (int i = 0; i < 50; i++) {
                list.add(String.valueOf(i));
            }
            mAdapter.refreshData(list);
        } else {
            tvText.setText(mParam1);
            mRecyclerView.setVisibility(View.INVISIBLE);
        }
    }

}

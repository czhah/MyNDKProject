package com.thedream.cz.myndkproject.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.thedream.cz.myndkproject.R;
import com.thedream.cz.myndkproject.bean.QuickSort;
import com.thedream.cz.myndkproject.bean.ShellSort;
import com.thedream.cz.myndkproject.bean.Tree;
import com.thedream.cz.myndkproject.utils.PrintUtil;

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

        findViewById(R.id.btn_tree).setOnClickListener(v -> tree());
        findViewById(R.id.btn_shell).setOnClickListener(v -> shellSort());
        findViewById(R.id.btn_quick).setOnClickListener(view -> quickSort());
    }

    private void quickSort() {
        int maxSize = 16;
        QuickSort sort = new QuickSort(maxSize);

        for (int j = 0; j < maxSize; j++) {
            sort.insert((long) (Math.random() * 99));
        }

        sort.diplay();
        sort.quickSort();
        sort.diplay();
    }

    private void shellSort() {
        int maxSize = 10;
        ShellSort shellSort = new ShellSort(maxSize);

        for (int j = 0; j < maxSize; j++) {
            shellSort.insert((long) (Math.random() * 99));
        }
        shellSort.diplay();
        shellSort.shellSort();
        shellSort.diplay();
    }

    private void tree() {
        Tree tree = new Tree();

        tree.insert(50, 1.5);
        tree.insert(25, 1.1);
        tree.insert(75, 1.7);
        tree.insert(12, 1.3);
        tree.insert(37, 1.2);
        tree.insert(44, 1.9);
        tree.insert(30, 2.0);
        tree.insert(33, 2.2);
        tree.insert(87, 1.4);
        tree.insert(77, 1.1);
        tree.insert(97, 1.8);
        tree.insert(85, 0.9);
        tree.insert(100, 2.3);

        tree.inOrder();

        PrintUtil.printCZ("删除值:" + tree.delete(75));

        tree.inOrder();

        PrintUtil.printCZ("查找30的值:" + tree.find(30));
    }
}

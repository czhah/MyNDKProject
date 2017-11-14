package com.thedream.cz.myndkproject.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.thedream.cz.myndkproject.R;
import com.thedream.cz.myndkproject.base.MySignLinkList;

public class MyLinkedListActivity extends AppCompatActivity {

    private int[] array = {5, 2, 66, 22, 7, 45, 0, 88, 46};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_linked_list);


        Button btnLink = (Button) findViewById(R.id.btn_link);
        btnLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLink();
            }
        });

        findViewById(R.id.btn_bubble).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bubbleSort();
            }
        });

        findViewById(R.id.btn_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectSort();
            }
        });

        findViewById(R.id.btn_insert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertSort();
            }
        });
    }

    private void bubbleSort() {
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = i + 1; j < array.length; j++) {
                if (array[i] < array[j]) swep(i, j);
            }
        }

        printArray();
    }

    private void insertSort() {
        int j;
        int temp;
        for (int i = 1; i < array.length; i++) {
            j = i;
            temp = array[i];
            while (j > 0 && temp < array[j - 1]) {
                array[j] = array[j - 1];
                j--;
            }
            array[j] = temp;
        }

        printArray();
    }

    private void selectSort() {
        for (int i = 0; i < array.length - 1; i++) {
            int k = i;
            for (int j = i + 1; j < array.length; j++) {
                if (array[k] > array[j]) k = j;
            }
            swep(i, k);
        }

        printArray();
    }

    private void printArray() {
        for (int i = 0; i < array.length; i++) {
            Log.i("cz", "" + array[i]);
        }
    }

    private void swep(int i, int j) {
        final int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    private void openLink() {

        MySignLinkList<String> linkList = new MySignLinkList<>();
        linkList.add("1");
        linkList.add("2");
        linkList.add(0, "3");
        for (int i = 0; i < linkList.size(); i++) {
            Log.i("cz", linkList.get(i));
        }

    }
}

package com.thedream.cz.myndkproject.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.thedream.cz.myndkproject.R;
import com.thedream.cz.myndkproject.base.MySignLinkList;

public class MyLinkedListActivity extends AppCompatActivity {

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

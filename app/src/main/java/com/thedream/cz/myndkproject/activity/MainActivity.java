
package com.thedream.cz.myndkproject.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.thedream.cz.myndkproject.R;
import com.thedream.cz.myndkproject.ui.GuideActivity;
import com.thedream.cz.myndkproject.ui.activity.user.login.UserLoginActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_13).setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, UserLoginActivity.class))
        );
        findViewById(R.id.btn_12).setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, UserLoginActivity.class))
        );
        findViewById(R.id.btn_11).setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, MyDraggerActivity.class))
        );
        findViewById(R.id.btn_10).setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, GuideActivity.class))
        );
        findViewById(R.id.btn_09).setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, MyBLEActivity.class))
        );

        findViewById(R.id.btn_08).setOnClickListener(v -> {
            Log.i("cz", "低版本不支持haha:" + Build.VERSION.SDK_INT);
            startActivity(new Intent(MainActivity.this, MyBluetoothActivity.class));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            }
        });
        findViewById(R.id.btn_07).setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, MyLinkListActivity.class)));
        findViewById(R.id.btn_06).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MySkinActivity.class));
            }
        });
        findViewById(R.id.btn_05).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SideDeleteActivity.class));
            }
        });
        findViewById(R.id.btn_04).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, BannerActivity.class));
            }
        });
        findViewById(R.id.btn_03).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CustomImgAvtivity.class));
            }
        });
        findViewById(R.id.btn_02).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, VideoCompoundActivity.class));
            }
        });
        findViewById(R.id.btn_01).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AutoPlayActivity.class));
            }
        });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

package com.thedream.cz.myndkproject.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.thedream.cz.myndkproject.R;
import com.thedream.cz.myndkproject.ndk.AutoPlayer;

public class AutoPlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_play);

        findViewById(R.id.btn_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play(view);
            }
        });
    }

    private void play(View view) {
        AutoPlayer autoPlayer = new AutoPlayer();
        autoPlayer.sound();
    }
}

package com.thedream.cz.myndkproject.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.thedream.cz.myndkproject.R;
import com.thedream.cz.myndkproject.ndk.AutoPlayer;

public class AutoPlayActivity extends AppCompatActivity {

    private AutoPlayer autoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_play);
        autoPlayer = new AutoPlayer();
        findViewById(R.id.btn_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play(view);
            }
        });
        findViewById(R.id.btn_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stop();
            }
        });
    }

    private void stop() {
        autoPlayer.stop();
    }

    private void play(View view) {
        autoPlayer.sound();
    }

    @Override
    protected void onStop() {
        super.onStop();
        autoPlayer.stop();
    }
}

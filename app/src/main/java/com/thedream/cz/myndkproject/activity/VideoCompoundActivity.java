package com.thedream.cz.myndkproject.activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.thedream.cz.myndkproject.R;
import com.thedream.cz.myndkproject.ndk.VideoSynthesizer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class VideoCompoundActivity extends AppCompatActivity {


    private VideoSynthesizer videoSynthesizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_compound);

        final String path_one = new File(Environment.getExternalStorageDirectory(), "video_one.mp4").getAbsolutePath();
        final String path_two = new File(Environment.getExternalStorageDirectory(), "video_two.mp4").getAbsolutePath();
        final String output_one= new File(Environment.getExternalStorageDirectory(), "video_output_one.pcm").getAbsolutePath();
        final String output_two= new File(Environment.getExternalStorageDirectory(), "video_output_two.pcm").getAbsolutePath();

        File input = new File(path_one);
        if(input.exists()) input.delete();
        File outputFile = new File(output_one);
        if(outputFile.exists()) outputFile.delete();

        File outputFile2 = new File(output_two);
        if(outputFile2.exists()) outputFile2.delete();

        findViewById(R.id.btn_compound).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkFile(path_one, R.raw.video_one) || checkFile(path_two, R.raw.video_two)) return ;
                compound(path_one, path_two, output_one, output_two);
            }
        });

        videoSynthesizer = new VideoSynthesizer();
    }

    private boolean checkFile(String path, int id) {
        if(!new File(path).exists()) {
            whiteFile(path, id);
            return true;
        }
        Log.i("cz", "文件存在--->path ："+path);
        return false;
    }

    private void whiteFile(String path, int id) {
        File file = new File(path);
        try {
            if(!file.exists()) file.createNewFile();
            InputStream stream = getResources().openRawResource(id);
            FileOutputStream fos = new FileOutputStream(file);
            int len = -1;
            byte[] buffer = new byte[1024];
            while((len = stream.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                fos.flush();
            }
            fos.close();
            stream.close();
            Log.i("cz", "写入成功-->path ："+path);
            return ;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i("cz", "写入失败-->path ："+path);
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("cz", "写入失败-->path ："+path);
        }
    }

    private void compound(String input_one, String input_two, String output_one, String output_two) {
        videoSynthesizer.compound(input_one, input_two, output_one, output_two);
    }
}

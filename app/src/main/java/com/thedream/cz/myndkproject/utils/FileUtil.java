package com.thedream.cz.myndkproject.utils;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2017/11/1.
 */

public class FileUtil {

    private static void whiteFile(Context ctx, String path, int id) {
        File file = new File(path);
        try {
            if (!file.exists()) file.createNewFile();
            InputStream stream = ctx.getResources().openRawResource(id);
            FileOutputStream fos = new FileOutputStream(file);
            int len = -1;
            byte[] buffer = new byte[1024];
            while ((len = stream.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                fos.flush();
            }
            fos.close();
            stream.close();
            Log.i("cz", "写入成功-->path ：" + path);
            return;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i("cz", "写入失败-->path ：" + path);
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("cz", "写入失败-->path ：" + path);
        }
    }
}

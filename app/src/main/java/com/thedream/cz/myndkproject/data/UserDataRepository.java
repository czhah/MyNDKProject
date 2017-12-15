package com.thedream.cz.myndkproject.data;

import android.os.Environment;

import com.thedream.cz.myndkproject.RetrofitFactory;
import com.thedream.cz.myndkproject.bean.WebResultInfo;
import com.thedream.cz.myndkproject.data.entity.FileUpLoadInfo;
import com.thedream.cz.myndkproject.data.remote.IFileApi;
import com.thedream.cz.myndkproject.data.remote.IUserApi;
import com.thedream.cz.myndkproject.listener.OnResultListener;
import com.thedream.cz.myndkproject.utils.PrintUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2017/12/15.
 */

public class UserDataRepository {

    private static UserDataRepository instance;
    private Class<IUserApi> iUserApi = IUserApi.class;
    private Class<IFileApi> iFileApi = IFileApi.class;


    private UserDataRepository() {
    }

    public static UserDataRepository getInstance() {
        if (null == instance) {
            synchronized (UserDataRepository.class) {
                if (null == instance) instance = new UserDataRepository();
            }
        }
        return instance;
    }

    public void upLoadUserHead(String uid, String token, File file, OnResultListener<FileUpLoadInfo> listener) {
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("image/jpeg"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body = MultipartBody.Part.createFormData("img", file.getName(), requestFile);

        RequestBody uidBody = RequestBody.create(MediaType.parse("multipart/form-data"), uid);
        RequestBody tokenBody = RequestBody.create(MediaType.parse("multipart/form-data"), token);

        RetrofitFactory.getRetrofit().create(iUserApi)
                .upLoadUserHead(body, uidBody, tokenBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((webResultInfo) -> {
                    PrintUtil.printCZ("线程:" + Thread.currentThread());
                    if (webResultInfo.getStatusCode() == WebResultInfo.RESULT_SUCCESS) {
                        listener.onSuccess(webResultInfo.getData());
                    } else {
                        listener.onFailed(webResultInfo.getStatusCode());
                    }
                }, (throwable) -> {
                    listener.onFailed(WebResultInfo.RESULT_FAILED);
                });
    }

    public void upLoadFile(String uid, String token, File file, OnResultListener<FileUpLoadInfo> listener) {
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("image/jpeg"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body = MultipartBody.Part.createFormData("img", file.getName(), requestFile);

        RequestBody uidBody = RequestBody.create(MediaType.parse("multipart/form-data"), uid);
        RequestBody tokenBody = RequestBody.create(MediaType.parse("multipart/form-data"), token);

        Map<String, RequestBody> map = new HashMap<>();
        map.put("uid", uidBody);
        map.put("token", tokenBody);
        RetrofitFactory.getRetrofit().create(iFileApi)
                .upLoadFile("UploadServlet", body, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((webResultInfo) -> {
                    PrintUtil.printCZ("线程:" + Thread.currentThread());
                    if (webResultInfo.getStatusCode() == WebResultInfo.RESULT_SUCCESS) {
                        listener.onSuccess(webResultInfo.getData());
                    } else {
                        listener.onFailed(webResultInfo.getStatusCode());
                    }
                }, (throwable) -> {
                    listener.onFailed(WebResultInfo.RESULT_FAILED);
                });
    }


    public void downloadApk(String path, OnResultListener<String> listener) {
        RetrofitFactory.getRetrofit().create(iFileApi)
                .downLoadFile(path)
                .map(new Function<ResponseBody, String>() {
                    @Override
                    public String apply(@NonNull ResponseBody responseBody) throws Exception {
                        return writeResponseBodyToDisk(responseBody);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    listener.onSuccess(s);
                }, (throwable) -> {
                    listener.onFailed(WebResultInfo.RESULT_FAILED);
                });

    }

    private String writeResponseBodyToDisk(ResponseBody body) throws Exception {
        PrintUtil.printCZ("contentType:>>>>" + body.contentType().toString());
        String type = body.contentType().toString();
        PrintUtil.printCZ("type:" + type);
        String path = Environment.getExternalStorageDirectory() + File.separator + "myApp.apk";
        PrintUtil.printCZ("path:>>>>" + path);
        File futureStudioIconFile = new File(path);
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            byte[] fileReader = new byte[4096];
            long fileSize = body.contentLength();
            long fileSizeDownloaded = 0;
            inputStream = body.byteStream();
            outputStream = new FileOutputStream(futureStudioIconFile);
            while (true) {
                int read = inputStream.read(fileReader);
                if (read == -1) {
                    break;
                }
                outputStream.write(fileReader, 0, read);
                fileSizeDownloaded += read;
                PrintUtil.printCZ("file download: " + fileSizeDownloaded + " of " + fileSize);
            }
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }
        return path;
    }

}

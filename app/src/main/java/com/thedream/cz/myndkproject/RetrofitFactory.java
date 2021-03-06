package com.thedream.cz.myndkproject;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.thedream.cz.myndkproject.listener.IProgressListener;
import com.thedream.cz.myndkproject.ui.common.BaseApplication;
import com.thedream.cz.myndkproject.utils.NetWorkUtil;
import com.thedream.cz.myndkproject.utils.PrintUtil;
import com.thedream.cz.myndkproject.utils.ProgressUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by cz on 2017/11/27.
 * 网络请求数据
 */

public class RetrofitFactory {

    private static Retrofit retrofit;

    private static final Interceptor cacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetWorkUtil.isNetworkConnected(BaseApplication.mApplication)) {
                //  没有网络 读取缓存
                request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
            }
            Response originalResponse = chain.proceed(request);
            if (NetWorkUtil.isNetworkConnected(BaseApplication.mApplication)) {
                //  有网络时 设置缓存
                int maxAge = 0; // read from cache
                return originalResponse.newBuilder()
                        .body(new ProgressUtil(originalResponse.body(), new IProgressListener() {
                            @Override
                            public void onProgress(long progress, long total, boolean isDone) {
                                PrintUtil.printCZ("onProgress   progress:" + progress + "  total:" + total + "  isDone:" + isDone);
                            }
                        }))
                        .header("Cache-Control", "public ,max-age=" + maxAge)
                        .removeHeader("Pragma")
                        .build();
            } else {
                PrintUtil.printCZ("没有网络");
                //  没有网络时 直接读取缓存
                int maxStale = 60 * 60 * 24;
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .removeHeader("Pragma")
                        .build();
            }
        }
    };

    public static Retrofit getRetrofit() {

        synchronized (RetrofitFactory.class) {
            if (retrofit == null) {
                File file = new File(BaseApplication.mApplication.getCacheDir(), "httpclient");
                PrintUtil.printCZ("缓存路径:" + file.getPath());
                Cache cache = new Cache(file, AppConstant.NETWORK_CACHE_SIZE);

                ClearableCookieJar cookieJar =
                        new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(BaseApplication.mApplication));
                OkHttpClient.Builder builder = new OkHttpClient.Builder()
                        .cache(cache)
                        .cookieJar(cookieJar)
                        .addInterceptor(cacheControlInterceptor)
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .writeTimeout(45, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(true);//  失败重连

                if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                    builder.addInterceptor(interceptor);
                }

                retrofit = new Retrofit.Builder()
                        .baseUrl(AppConstant.API_URL)
                        .client(builder.build())
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build();
            }
            return retrofit;
        }
    }

}

package com.thedream.cz.myndkproject.data.remote;

import com.thedream.cz.myndkproject.bean.WebResultInfo;
import com.thedream.cz.myndkproject.data.entity.LoginInfo;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by cz on 2017/11/29.
 * 公共请求类
 */
public interface IPublicApi {

    //  http://timeto.lol/timeto/public?method=login&type=2&automaticLogin=0&username=13691923610&pwd=123456&language=zh_CN
    //  用户登录
    @POST("public?method=login&type=2&automaticLogin=0")
    Observable<WebResultInfo<LoginInfo>> login(@Query("username") String username, @Query("pwd") String pwd,
                                               @Query("language") String language);
}

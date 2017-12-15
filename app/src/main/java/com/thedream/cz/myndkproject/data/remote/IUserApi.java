package com.thedream.cz.myndkproject.data.remote;

import com.thedream.cz.myndkproject.bean.WebResultInfo;
import com.thedream.cz.myndkproject.data.entity.FileUpLoadInfo;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by cz on 2017/12/15.
 * 用户请求类
 */

public interface IUserApi {

    @Multipart
    @POST("UploadServlet")
    Observable<WebResultInfo<FileUpLoadInfo>> upLoadUserHead(@Part MultipartBody.Part part,
                                                             @Part("uid") RequestBody uid,
                                                             @Part("token") RequestBody token);


}

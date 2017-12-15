package com.thedream.cz.myndkproject.data.remote;

import com.thedream.cz.myndkproject.bean.WebResultInfo;
import com.thedream.cz.myndkproject.data.entity.FileUpLoadInfo;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by cz on 2017/12/15.
 * 文件上传与下载
 */

public interface IFileApi {

    /**
     * 上传单个文件
     *
     * @param name Url路径
     * @param part File参数
     * @param map  其他参数
     * @return
     */
    @Multipart
    @POST("{name}")
    Observable<WebResultInfo<FileUpLoadInfo>> upLoadFile(@Path("name") String name,
                                                         @Part MultipartBody.Part part,
                                                         @PartMap Map<String, RequestBody> map);

    @Multipart
    @POST("{name}")
    Observable<WebResultInfo<FileUpLoadInfo>> upLoadFile(@Path("name") String name,
                                                         @PartMap Map<String, RequestBody> map);

    /**
     * 批量上传文件
     *
     * @param name
     * @param part
     * @param map
     * @return
     */
    @Multipart
    @POST("{name}")
    Observable<WebResultInfo<FileUpLoadInfo>> upLoadFileList(@Path("name") String name,
                                                             @Part MultipartBody.Part[] part,
                                                             @PartMap Map<String, RequestBody> map);

    @Streaming
    @GET
    Observable<ResponseBody> downLoadFile(@Url String fileUrl);
}

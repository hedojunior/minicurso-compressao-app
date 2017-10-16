package com.hedo.minicursocompressao.retrofit;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Criado por hedo.junior em 14/10/2017.
 */

interface IMainService {
    String URL_BASE = "http://url.da.api/";

    @FormUrlEncoded
    @POST("image")
    Call<String> uploadImage(@Field("encodedImage") String encodedImage);

    @GET("image")
    Call<String> getImage(@Query("fileName") String fileName);
}

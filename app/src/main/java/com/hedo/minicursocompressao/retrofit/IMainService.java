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

public interface IMainService {
    String URL_BASE = "http://192.168.25.107:8080/";

    @FormUrlEncoded
    @POST("image")
    Call<String> uploadImage(@Field("encodedImage") String encodedImage);

    @GET("image")
    Call<String> getImage(@Query("fileName") String fileName);
}

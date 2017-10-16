package com.hedo.minicursocompressao.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Criado por hedo.junior em 14/10/2017.
 */

public class MainConsumer {

    private IMainService service;
    private Retrofit retrofit;

    public MainConsumer() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl(IMainService.URL_BASE)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                .build();

        this.service = retrofit.create(IMainService.class);
    }

    public Call<String> uploadImage(String base64Image) {
        return this.service.uploadImage(base64Image);
    }

    public Call<String> getImage(String fileName) {
        return this.service.getImage(fileName);
    }
}

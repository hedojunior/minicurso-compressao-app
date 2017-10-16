package com.hedo.minicursocompressao.retrofit;

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

    /**
     * Construtor padrão da classe que cria uma instância do Retrofit, utilizando
     * Gson como conversor de JSON, e instancia, por polimorfismo, uma instância
     * do serviço IMainService.
     */
    public MainConsumer() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl(IMainService.URL_BASE)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                .build();

        this.service = retrofit.create(IMainService.class);
    }

    /**
     *
     * @param base64Image String código Base64 da imagem
     * @return Call<String> callback da requisição com a resposta em String
     */
    public Call<String> uploadImage(String base64Image) {
        return this.service.uploadImage(base64Image);
    }

    /**
     *
     * @param fileName String nome do arquivo de imagem salvo no servidor
     * @return Call<String> callback da requisição com a imagem em Base64
     */
    public Call<String> getImage(String fileName) {
        return this.service.getImage(fileName);
    }
}

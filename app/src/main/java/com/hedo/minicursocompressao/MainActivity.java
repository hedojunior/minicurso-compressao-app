package com.hedo.minicursocompressao;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hedo.minicursocompressao.retrofit.MainConsumer;
import com.hedo.minicursocompressao.util.Base64Util;
import com.hedo.minicursocompressao.util.FileUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import id.zelory.compressor.Compressor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements Callback<String> {

    private final static int PICK_IMAGE = 10;

    /*
        Essa anotação é o equivalente a:
        nonCompressedImageSizeTv = (TextView) findViewById(R.id.non_compressed_image_size_tv)
     */
    @ViewById(R.id.non_compressed_image_size_tv)
    TextView nonCompressedImageSizeTv;

    @ViewById(R.id.compressed_image_size_tv)
    TextView compressedImageSizeTv;

    @ViewById(R.id.file_name_tv)
    TextView fileNameTv;

    @ViewById(R.id.non_compressed_image_imv)
    ImageView nonCompressedImV;

    @ViewById(R.id.compressed_image_imv)
    ImageView compressedImV;

    @ViewById(R.id.downloaded_image_imv)
    ImageView downloadedImageImV;

    @ViewById(R.id.upload_ibt)
    ImageButton uploadIbt;

    @ViewById(R.id.download_ibt)
    ImageButton downloadIbt;

    ProgressDialog loader;

    MainConsumer consumer;

    Bitmap compressedImage;

    String fileName;

    /**
     * Usando a biblioteca AndroidAnnotations, não é necessário sobrescrever o método onCreate.
     * a anotação @AfterViews avisa ao AndroidAnnotations que o código dentro do método anotado
     * por ela deve ser executado após a criação da activity e o vínculo das Views do layout
     * à ela (os findViewById), que são feitos automaticamente apenas utilizando a anotação
     * @ViewById passando o identificador da view como parâmetro.
     */
    @AfterViews
    void init() {
        //TODO: instanciar classe consumidora da API
    }

    /*
        Essa anotação é equivalente a:
            nonCompressedImageImV.setOnClickListener(new OnClick...)
     */
    @Click(R.id.non_compressed_image_imv)
    protected void pickImage() {
        //TODO: Implementar intent para busca de imagem no dispositivo
    }

    @Click(R.id.upload_ibt)
    protected void uploadImage() {
        //TODO: Transformar a imagem em Base64 e enviar para o servidor
    }

    @Click(R.id.download_ibt)
    protected void downloadImage() {
        //TODO: Implementar o download da imagem do servidor
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //TODO: implementar lógica de tratamento do retorno da galeria e compressão da imagem
    }

    @Background
    void parseAndDisplayImage(Uri data) {
        //TODO: Transformar a Uri que retorna em uma Bitmap
    }

    @Background
    void compressImage(Uri uri) {
        //TODO: Adicionar lógica para compressão da imagem utilizando a biblioteca Compressor
    }

    /**
     *
     * Método usado para preencher as views da tela, vinculando as imagens
     * aos ImageViews e o tamanho delas ao TextView abaixo
     *
     * @param bitmap Bitmap imagem que vai ser exibida pelo ImageView
     * @param imageView ImageView ImageView que vai exibir a imagem acima
     * @param textView TextView campo de texto que vai exibir o tamanho atual da imagem
     */
    @UiThread
    void setImage(Bitmap bitmap, ImageView imageView, TextView textView) {
        imageView.setImageBitmap(bitmap);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        textView.setText(FileUtil.getReadableFileSize(bitmap.getByteCount()));
    }

    /**
     * Mostra um toast com mensagem de erro genérica.
     */
    void showErrorToast() {
        Toast.makeText(this, "Ocorreu um erro, tente a operação novamente.", Toast.LENGTH_LONG).show();
    }

    /**
     *
     * Mostra um toast com uma mensagem de erro ao usuário.
     *
     * @param errorMessage String mensagem específica de erro.
     */
    void showErrorToast(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    /**
     *
     * Método utilitário para mostrar ou esconder o loader que ocupa a tela inteira
     *
     * https://developer.android.com/reference/android/app/ProgressDialog.html
     *
     * @param shouldShow boolean indica se o loader deve ou não ser mostrado.
     * @param message String mensagem mostrada no loader, abaixo do título.
     */
    void toggleLoader(boolean shouldShow, String message) {
        if (loader == null || shouldShow) {
            loader = ProgressDialog.show(this, getString(R.string.aguarde), message, true, false);
        } else if (!shouldShow) {
            loader.dismiss();
        }
    }


    @Override
    public void onResponse(Call<String> call, Response<String> response) {
        //TODO: Armazenar o nome do arquivo retornado e exibí-lo na tela
    }

    @Override
    public void onFailure(Call<String> call, Throwable t) {
        showErrorToast(t.getMessage());
        toggleLoader(false, null);
    }
}

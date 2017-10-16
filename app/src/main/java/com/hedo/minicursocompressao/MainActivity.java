package com.hedo.minicursocompressao;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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
import java.text.DecimalFormat;

import id.zelory.compressor.Compressor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements Callback<String> {

    private final static int PICK_IMAGE = 10;

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

    ProgressDialog dialog;

    MainConsumer consumer;

    Bitmap compressedImage;

    String fileName;

    @AfterViews
    void init() {
        consumer = new MainConsumer();
    }

    @Click(R.id.non_compressed_image_imv)
    protected void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Click(R.id.upload_ibt)
    protected void uploadImage() {
        if (compressedImage != null) {
            toggleLoader(true, "Fazendo upload da imagem...");
            String base64 = Base64Util.bitmapToBase64(compressedImage);
            consumer.uploadImage(base64).enqueue(this);
        }
    }

    @Click(R.id.download_ibt)
    protected void downloadImage() {
        if (fileName != null) {
            toggleLoader(true, "Baixando imagem...");
            consumer.getImage(fileName).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Bitmap bitmap = Base64Util.base64ToBitmap(response.body());
                    downloadedImageImV.setImageBitmap(bitmap);
                    downloadedImageImV.setVisibility(View.VISIBLE);
                    toggleLoader(false, null);
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    showErrorToast(t.getMessage());
                    toggleLoader(false, null);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                showErrorToast();
                return;
            }

            toggleLoader(true, "Processando imagem...");
            parseAndDisplayImage(data.getData());
            compressImage(data.getData());
        }
    }

    @Background
    void parseAndDisplayImage(Uri data) {
        try {
            InputStream stream = this.getContentResolver().openInputStream(data);
            Bitmap bitmap = BitmapFactory.decodeStream(stream);
            setImage(bitmap, nonCompressedImV, nonCompressedImageSizeTv);

        } catch (FileNotFoundException e) {
            showErrorToast();
            e.printStackTrace();
        }
    }

    @Background
    void compressImage(Uri uri) {
        try {
            dialog.setMessage("Comprimindo imagem...");
            compressedImage = new Compressor(this)
                    .setMaxWidth(170)
                    .setMaxHeight(200)
                    .setQuality(90)
                    .setCompressFormat(Bitmap.CompressFormat.WEBP)
                    .compressToBitmap(FileUtil.from(this, uri));

            setImage(compressedImage, compressedImV, compressedImageSizeTv);
            toggleLoader(false, null);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @UiThread
    void setImage(Bitmap bitmap, ImageView imageView, TextView textView) {
        imageView.setImageBitmap(bitmap);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        textView.setText(FileUtil.getReadableFileSize(bitmap.getByteCount()));
    }

    void showErrorToast() {
        Toast.makeText(this, "Erro ao buscar imagem, tente novamente.", Toast.LENGTH_SHORT).show();
    }

    void showErrorToast(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    void toggleLoader(boolean shouldShow, String message) {
        if (dialog == null || shouldShow) {
            dialog = ProgressDialog.show(this, "Aguarde", message, true, false);
        } else if (!shouldShow) {
            dialog.dismiss();
        }
    }


    @Override
    public void onResponse(Call<String> call, Response<String> response) {
        fileName = response.body();
        fileNameTv.setText(fileName);
        toggleLoader(false, null);
    }

    @Override
    public void onFailure(Call<String> call, Throwable t) {
        showErrorToast(t.getMessage());
        toggleLoader(false, null);
    }
}

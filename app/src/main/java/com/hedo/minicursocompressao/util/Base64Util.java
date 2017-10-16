package com.hedo.minicursocompressao.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Criado por hedo.junior em 14/10/2017.
 */

public class Base64Util {

    /**
     *
     * @param bmp Bitmap Imagem que vai ser transformada em Base64
     * @return String código base64
     */
    public static String bitmapToBase64(Bitmap bmp) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 75, byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }

    /**
     *
     * @param base64String String código Base64 a ser transformado em imagem
     * @return Bitmap imagem restaurada do Base64
     */
    public static Bitmap base64ToBitmap(String base64String) {
        byte[] imgBytes = Base64.decode(base64String, Base64.URL_SAFE);

        return BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);
    }
}

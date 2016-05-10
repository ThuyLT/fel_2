package com.framgia.e_learningsimple.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.framgia.e_learningsimple.R;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ThuyIT on 25/04/2016.
 */
public class DrawImageViewSrcTask extends AsyncTask<String, Void, Bitmap> {
    ImageView mImageView;
    boolean hasError = false;

    public DrawImageViewSrcTask(ImageView imageView) {
        this.mImageView = imageView;
    }

    protected Bitmap doInBackground(String... urls) {
        String url = urls[0];
        Bitmap bitmap = null;
        try {
            InputStream in = new java.net.URL(url).openStream();
            bitmap = BitmapFactory.decodeStream(in);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    protected void onPostExecute(Bitmap result) {
        if (result == null) {
            mImageView.setImageResource(R.mipmap.ic_framgia);
        } else {
            mImageView.setImageBitmap(result);
        }
    }
}


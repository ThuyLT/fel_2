package com.framgia.e_learningsimple.network;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.framgia.e_learningsimple.R;

/**
 * Created by ThuyIT on 20/04/2016.
 */
public class CheckNetwork {
    public static boolean isInternetConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean result = false;
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (networkInfo != null && networkInfo.isConnected()) {
                result = true;
            }
        }
        if (!result) {
            new AlertDialog.Builder(context).setMessage(R.string.msg_no_internet);
        }
        return result;
    }
}

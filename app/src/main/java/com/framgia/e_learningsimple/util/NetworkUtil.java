package com.framgia.e_learningsimple.util;

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
public class NetworkUtil {
    public static String sAuthToken;
    public static boolean isInternetConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean result = false;
        if (connectivityManager != null) {
            NetworkInfo networkWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo networkMobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if ((networkWifi != null && networkWifi.isConnected())|| (networkMobile!=null && networkMobile.isConnected())) {
                result = true;
            }
        }
        if (!result) {
            new AlertDialog.Builder(context).setMessage(R.string.msg_no_internet);
        }
        return result;
    }
}

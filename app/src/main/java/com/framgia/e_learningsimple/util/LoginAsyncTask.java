package com.framgia.e_learningsimple.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.framgia.e_learningsimple.R;
import com.framgia.e_learningsimple.model.RequestHelper;
import com.framgia.e_learningsimple.model.ResponseHelper;
import com.framgia.e_learningsimple.url.UrlJson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

/**
 * Created by ThuyIT on 20/04/2016.
 */
public class LoginAsyncTask extends MyAsynctask<String, Void, String> {
    public static final String EMAIL_PARAMNAME = "session[email]";
    public static final String PASSWORD_PARAMNAME = "session[password]";
    String mUserEmail;
    String mUserPassword;
    private int mStatusCode;
    private String mResponeBody;

    private SharedPreferences mSharedPreferences;

    public LoginAsyncTask(Context mContext, SharedPreferences sharedPreferences) {
        super(mContext);
        this.mSharedPreferences = sharedPreferences;
    }

    @Override
    protected String doInBackground(String... args) {
        mUserEmail = args[0];
        mUserPassword = args[0];
        ValueName valueName1 = new ValueName(EMAIL_PARAMNAME, mUserEmail);
        ValueName valueName2 = new ValueName(PASSWORD_PARAMNAME, mUserPassword);
        ResponseHelper helper = null;
        try {
            helper = RequestHelper.executeRequest(UrlJson.LOGIN, RequestHelper.Method.POST, valueName1, valueName2);
            mStatusCode = helper.getResponeCode();
            mResponeBody = helper.getResponeBody();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Toast.makeText(mContext, mContext.getString(R.string.dialog_login_success), Toast.LENGTH_SHORT).show();
    }

    private void notifyError(String defaultMessage) {
        try {
            JSONObject responseJson = new JSONObject(mResponeBody);
            Toast.makeText(mContext, responseJson.getString("message"), Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(mContext, defaultMessage, Toast.LENGTH_SHORT).show();
        }
    }
}
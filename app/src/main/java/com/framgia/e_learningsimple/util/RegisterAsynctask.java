package com.framgia.e_learningsimple.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.framgia.e_learningsimple.R;
import com.framgia.e_learningsimple.constant.JsonKeyConstant;
import com.framgia.e_learningsimple.model.RequestHelper;
import com.framgia.e_learningsimple.model.ResponseHelper;
import com.framgia.e_learningsimple.network.ErrorNetwork;
import com.framgia.e_learningsimple.sharepreference.SharePreferenceUtil;
import com.framgia.e_learningsimple.url.UrlJson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

/**
 * Created by ThuyIT on 21/04/2016.
 */
public class RegisterAsynctask extends MyAsynctask<String, Void, String> {
    String mUserName, mUserEmail, mUserPassword, mUserRePassword;
    public static final String NAME_PARAMNAME = "user[name]";
    public static final String EMAIL_PARAMNAME = "user[email]";
    public static final String PASSWORD_PARAMNAME = "user[password]";
    public static final String REPASSWORD_PARAMNAME = "user[password_confirmation]";
    private int mStatusCode;
    private String mResponseBody;
    private SharedPreferences mSharedPreferences;

    public RegisterAsynctask(Context mContext) {
        super(mContext);
    }

    @Override
    protected String doInBackground(String... args) {
        mUserName = args[0];
        mUserEmail = args[1];
        mUserPassword = args[2];
        mUserRePassword = args[3];

        ValueName nameValue1 = new ValueName(NAME_PARAMNAME, mUserName);
        ValueName nameValue2 = new ValueName(EMAIL_PARAMNAME, mUserEmail);
        ValueName nameValue3 = new ValueName(PASSWORD_PARAMNAME, mUserPassword);
        ValueName rePasswordValue = new ValueName(REPASSWORD_PARAMNAME, mUserRePassword);
        ResponseHelper responseHelper = null;
        try {
            ResponseHelper mHelper = RequestHelper.executeRequest(UrlJson.REGISTER_URL, RequestHelper.Method.POST,
                    nameValue1, nameValue2, nameValue3, rePasswordValue);
            mStatusCode = mHelper.getResponeCode();
            mResponseBody = mHelper.getResponeBody();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Toast.makeText(mContext, mContext.getString(R.string.dialog_register_success), Toast.LENGTH_SHORT).show();
    }
}
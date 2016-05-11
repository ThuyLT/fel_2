package com.framgia.e_learningsimple.util;

import android.app.Activity;
import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.framgia.e_learningsimple.R;
import com.framgia.e_learningsimple.activity.HomeActivity;
import com.framgia.e_learningsimple.activity.LoginActivity;
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
 * Created by ThuyIT on 20/04/2016.
 */
public class LoginAsyncTask extends MyAsynctask<String, Void, String> {
    public static final String EMAIL_PARAMNAME = "session[email]";
    public static final String PASSWORD_PARAMNAME = "session[password]";
    String mUserEmail;
    String mUserPassword;
    private int mStatusCode;
    private String mResponeBody;
    private OnLoginSuccess mOnLoginSuccess;

    private SharedPreferences mSharedPreferences;

    public LoginAsyncTask(Context mContext, SharedPreferences sharedPreferences, OnLoginSuccess onLoginSuccess) {
        super(mContext);
        this.mSharedPreferences = sharedPreferences;
        mOnLoginSuccess = onLoginSuccess;
    }

    @Override
    protected String doInBackground(String... args) {
        mUserEmail = args[0];
        mUserPassword = args[1];
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
        switch (mStatusCode) {
            case ErrorNetwork.OK:
                try {
                    storeUserInfo();
                    mOnLoginSuccess.onSuccess(mResponeBody);
                } catch (JSONException e) {
                    Toast.makeText(mContext, mContext.getString(R.string.error_response_data), Toast.LENGTH_SHORT).show();
                }
                break;
            case ErrorNetwork.UNAUTHORIZED:
                String errorMessage = mContext.getString(R.string.error_unauthorized);
                notifyError(errorMessage);
                break;
            default:
                ResponseHelper.httpStatusNotify(mContext, mStatusCode);
        }
    }

    private void storeUserInfo() throws JSONException {
        JSONObject responseJson = new JSONObject(mResponeBody);
        JSONObject userDataJson = responseJson.optJSONObject(JsonKeyConstant.USER);
        SharePreferenceUtil.putString(mSharedPreferences, JsonKeyConstant.EMAIL_FILED, mUserEmail);
        SharePreferenceUtil.putString(mSharedPreferences, JsonKeyConstant.NAME_FIELD, userDataJson.optString(JsonKeyConstant.NAME_FIELD));
        SharePreferenceUtil.putString(mSharedPreferences, JsonKeyConstant.AUTHO_TOKEN_FIELD, userDataJson.optString(JsonKeyConstant.AUTHO_TOKEN_FIELD));
        SharePreferenceUtil.putString(mSharedPreferences, JsonKeyConstant.KEY_USER_ID, userDataJson.optString(JsonKeyConstant.KEY_ID));
        SharePreferenceUtil.putString(mSharedPreferences, JsonKeyConstant.KEY_USER_PASSWORD, mUserPassword);
        SharePreferenceUtil.putString(mSharedPreferences, JsonKeyConstant.KEY_USER_AVATAR_URL, userDataJson.optString(JsonKeyConstant.AVATAR));
    }

    private void notifyError(String defaultMessage) {
        try {
            JSONObject responseJson = new JSONObject(mResponeBody);
            Toast.makeText(mContext, responseJson.getString(JsonKeyConstant.MESSAGE), Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(mContext, defaultMessage, Toast.LENGTH_SHORT).show();
        }
    }

    public interface OnLoginSuccess {
        public void onSuccess( String responseBody) throws JSONException;
    }

}
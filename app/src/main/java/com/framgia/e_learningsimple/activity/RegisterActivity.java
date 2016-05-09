package com.framgia.e_learningsimple.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.framgia.e_learningsimple.R;
import com.framgia.e_learningsimple.constant.JsonKeyConstant;
import com.framgia.e_learningsimple.model.RequestHelper;
import com.framgia.e_learningsimple.model.ResponseHelper;
import com.framgia.e_learningsimple.network.ErrorNetwork;
import com.framgia.e_learningsimple.sharepreference.SharePreferenceUtil;
import com.framgia.e_learningsimple.url.UrlJson;
import com.framgia.e_learningsimple.util.MyAsynctask;
import com.framgia.e_learningsimple.util.NetworkUtil;
import com.framgia.e_learningsimple.util.ValidationLogin;
import com.framgia.e_learningsimple.util.ValueName;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.Iterator;

/**
 * Created by ThuyIT on 21/04/2016.
 */
public class RegisterActivity extends Activity {
    EditText mEditTextName, mEditTextEmail, mEditTextPassword, mEditTextRePassword;
    Button mButtonCancel, mButtonDone;
    private SharedPreferences mSharedPreferences;
    private OnSignupSuccess mOnSignupSuccess;

    public void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        setContentView(R.layout.activity_register);
        mSharedPreferences = getSharedPreferences(JsonKeyConstant.USER_SHARED_PREF, Context.MODE_PRIVATE);
        mEditTextEmail = (EditText) findViewById(R.id.edit_email);
        mEditTextPassword = (EditText) findViewById(R.id.edit_password);
        mEditTextRePassword = (EditText) findViewById(R.id.edit_password_confirm);
        mEditTextName = (EditText) findViewById(R.id.edit_fullname);

        mButtonDone = (Button) findViewById(R.id.button_done);
        mButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidConditions()) {
                    String name = mEditTextName.getText().toString();
                    String email = mEditTextEmail.getText().toString();
                    String password = mEditTextPassword.getText().toString();
                    String passwordConfirmation = mEditTextRePassword.getText().toString();
                    mEditTextName.setError(null);
                    mEditTextEmail.setError(null);
                    mEditTextPassword.setError(null);
                    mEditTextRePassword.setError(null);
                    new RegisterAsynctask(RegisterActivity.this).execute(name, email, password, passwordConfirmation);
                }
            }
        });

        mOnSignupSuccess = new OnSignupSuccess( ) {
            @Override
            public void onSuccess(String responseBody) {
                Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                String activitiesStr = null;
                try {
                    activitiesStr = new JSONObject(responseBody).optJSONObject(JsonKeyConstant.USER).optString(JsonKeyConstant.KEY_ACTIVITIES);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                SharePreferenceUtil.putString(mSharedPreferences, JsonKeyConstant.KEY_ACTIVITIES, activitiesStr);
                startActivity(intent);
                finish();
            }
        };
    }

    private boolean isValidConditions() {
        ValidationLogin validationUtils = new ValidationLogin(this);
        if (!NetworkUtil.isInternetConnected(RegisterActivity.this)) {
            Toast.makeText(RegisterActivity.this, getString(R.string.msg_no_internet), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            boolean isValidName = validationUtils.isValidName(mEditTextName);
            boolean isValidEmail = validationUtils.isValidEmail(mEditTextEmail);
            boolean isValidPassword = validationUtils.validatePassword(mEditTextPassword);
            boolean isValidPwdConfirmation = validationUtils.isValidRePassword(mEditTextRePassword, mEditTextPassword);
            return isValidName && isValidEmail && isValidPassword && isValidPwdConfirmation;
        }
    }

    private class RegisterAsynctask extends MyAsynctask<String, Void, Void> {
        String mUserName, mUserEmail, mUserPassword;
        private String NAME_PARAMNAME = "user[name]";
        private String EMAIL_PARAMNAME = "user[email]";
        private String PASSWORD_PARAMNAME = "user[password]";
        private String REPASSWORD_PARAMNAME = "user[password_confirmation]";
        private int mStatusCode;
        private String mResponseBody;

        public RegisterAsynctask(Context mContext) {
            super(mContext);
        }

        @Override
        protected Void doInBackground(String... args) {
            mUserName = args[0];
            mUserEmail = args[1];
            mUserPassword = args[2];
            String mUserRePassword = args[3];

            ValueName nameValue1 = new ValueName(NAME_PARAMNAME, mUserName);
            ValueName nameValue2 = new ValueName(EMAIL_PARAMNAME, mUserEmail);
            ValueName nameValue3 = new ValueName(PASSWORD_PARAMNAME, mUserPassword);
            ValueName rePasswordValue = new ValueName(REPASSWORD_PARAMNAME, mUserRePassword);
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
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            switch (mStatusCode) {
                case ErrorNetwork.OK:
                    try {
                        storeUserInfo();


                    } catch (JSONException e) {
                        Toast.makeText(mContext, mContext.getString(R.string.error_response_data), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ErrorNetwork.UNAUTHORIZED:
                    String errorMessage = getString(R.string.error_unauthorized);
                    notifyErrors(errorMessage);
                    break;
                default:
                    ResponseHelper.httpStatusNotify(RegisterActivity.this, mStatusCode);
            }
        }


        private void notifyErrors(String defaultMessage) {
            try {
                JSONObject responseJson = new JSONObject(mResponseBody);
                JSONObject messageJson = responseJson.getJSONObject(JsonKeyConstant.MESSAGE);
                Iterator errors = messageJson.keys();
                while (errors.hasNext()) {
                    String key = (String) errors.next();
                    Toast.makeText(RegisterActivity.this, String.format(JsonKeyConstant.SCORE, key, messageJson.get(key)),
                            Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                Toast.makeText(RegisterActivity.this, defaultMessage, Toast.LENGTH_SHORT).show();
            }
        }

        private void storeUserInfo() throws JSONException {
            JSONObject responseJson = new JSONObject(mResponseBody);
            JSONObject userDataJson = responseJson.optJSONObject(JsonKeyConstant.USER);
            SharePreferenceUtil.putString(mSharedPreferences, JsonKeyConstant.EMAIL_FILED, mUserEmail);
            SharePreferenceUtil.putString(mSharedPreferences, JsonKeyConstant.NAME_FIELD, mUserName);
            SharePreferenceUtil.putString(mSharedPreferences, JsonKeyConstant.AUTHO_TOKEN_FIELD, userDataJson.optString(JsonKeyConstant.AUTHO_TOKEN_FIELD));
            SharePreferenceUtil.putString(mSharedPreferences, JsonKeyConstant.KEY_USER_ID, userDataJson.optString(JsonKeyConstant.KEY_ID));
            SharePreferenceUtil.putString(mSharedPreferences, JsonKeyConstant.KEY_USER_PASSWORD, mUserPassword);
            SharePreferenceUtil.putString(mSharedPreferences, JsonKeyConstant.KEY_USER_AVATAR_URL, userDataJson.optString(JsonKeyConstant.AVATAR));
        }
    }

    public interface OnSignupSuccess {
        public void onSuccess( String responseBody) throws JSONException;
    }
}
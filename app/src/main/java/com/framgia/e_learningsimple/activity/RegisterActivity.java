package com.framgia.e_learningsimple.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.framgia.e_learningsimple.R;
import com.framgia.e_learningsimple.constant.JsonKeyConstant;
import com.framgia.e_learningsimple.util.LoginAsyncTask;
import com.framgia.e_learningsimple.util.NetworkUtil;
import com.framgia.e_learningsimple.util.RegisterAsynctask;
import com.framgia.e_learningsimple.util.ValidationLogin;

/**
 * Created by ThuyIT on 21/04/2016.
 */
public class RegisterActivity extends Activity {
    EditText mEditTextName, mEditTextEmail, mEditTextPassword, mEditTextRePassword;
    Button mButtonCancel, mButtonDone;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPreferences = getSharedPreferences(JsonKeyConstant.USER_SHARED_PREF, Context.MODE_PRIVATE);
        if (TextUtils.isEmpty(mSharedPreferences.getString(JsonKeyConstant.AUTHO_TOKEN_FIELD, null))) {
            setContentView(R.layout.activity_register);
            mEditTextEmail = (EditText) findViewById(R.id.edit_email);
            mEditTextPassword = (EditText) findViewById(R.id.edit_password);
            mEditTextRePassword = (EditText) findViewById(R.id.edit_password_confirm);
            mEditTextName = (EditText) findViewById(R.id.edit_fullname);
            mButtonCancel = (Button) findViewById(R.id.button_cancel);
            mButtonDone = (Button) findViewById(R.id.button_done);
            mButtonDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = mEditTextEmail.getText().toString();
                    String password = mEditTextPassword.getText().toString();
                    String rePassword = mEditTextRePassword.getText().toString();
                    String name = mEditTextName.getText().toString();
                    if (isValidConditions()) {
                        mEditTextEmail.setError(null);
                        mEditTextPassword.setError(null);
                        mEditTextRePassword.setError(null);
                        mEditTextName.setError(null);
                        new RegisterAsynctask(RegisterActivity.this).execute(email, password, rePassword, name);
                    }
                }
            });
        } else {
            Toast.makeText(RegisterActivity.this, getString(R.string.dialog_register_success), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private boolean isValidConditions() {
        ValidationLogin validationUtils = new ValidationLogin(this);
        if (!NetworkUtil.isInternetConnected(RegisterActivity.this)) {
            Toast.makeText(RegisterActivity.this, getString(R.string.msg_no_internet), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            boolean isValidEmail = validationUtils.isValidEmail(mEditTextEmail);
            boolean isValidPassword = validationUtils.validatePassword(mEditTextPassword);
            boolean isRePassWord = validationUtils.isValidRePassword(mEditTextRePassword, mEditTextPassword);
            boolean isValidName = validationUtils.isValidName(mEditTextName);
            return isValidEmail && isValidPassword && isRePassWord && isValidName;
        }
    }
}

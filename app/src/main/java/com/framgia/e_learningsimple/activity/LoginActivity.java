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
import com.framgia.e_learningsimple.util.NetworkUtil;
import com.framgia.e_learningsimple.util.ValidationLogin;
import com.framgia.e_learningsimple.constant.JsonKeyConstant;
import com.framgia.e_learningsimple.util.LoginAsyncTask;

public class LoginActivity extends Activity {
    EditText mEditTextEmail;
    EditText mEditTextPassword;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPreferences = getSharedPreferences(JsonKeyConstant.USER_SHARED_PREF, Context.MODE_PRIVATE);
        if (TextUtils.isEmpty(mSharedPreferences.getString(JsonKeyConstant.AUTHO_TOKEN_FIELD, null))) {
            setContentView(R.layout.activity_login);
            Button loginButton = (Button) findViewById(R.id.btn_login);
            mEditTextEmail = (EditText) findViewById(R.id.edit_email);
            mEditTextPassword = (EditText) findViewById(R.id.edit_password);

            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = mEditTextEmail.getText().toString();
                    String password = mEditTextPassword.getText().toString();
                    if (isValidConditions()) {
                        mEditTextEmail.setError(null);
                        mEditTextPassword.setError(null);
                        new LoginAsyncTask(LoginActivity.this, mSharedPreferences).execute(email, password);
                    }
                }
            });

            TextView textViewLinkToRegister = (TextView) findViewById(R.id.text_link_to_register);
            textViewLinkToRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                }
            });
        } else {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }
    }

    private boolean isValidConditions() {
        ValidationLogin validationUtils = new ValidationLogin(this);
        if (!NetworkUtil.isInternetConnected(LoginActivity.this)) {
            Toast.makeText(LoginActivity.this, getString(R.string.msg_no_internet), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            boolean isValidEmail = validationUtils.isValidEmail(mEditTextEmail);
            boolean isValidPassword = validationUtils.validatePassword(mEditTextPassword);
            return isValidEmail && isValidPassword;
        }
    }
}
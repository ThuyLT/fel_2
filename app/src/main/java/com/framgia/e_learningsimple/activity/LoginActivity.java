package com.framgia.e_learningsimple.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.framgia.e_learningsimple.R;
import com.framgia.e_learningsimple.network.CheckNetwork;
import com.framgia.e_learningsimple.util.CheckLogin;
import com.framgia.e_learningsimple.constant.JsonParse;
import com.framgia.e_learningsimple.util.LoginAsyncTask;

public class LoginActivity extends Activity {
    EditText mEditTextEmail;
    EditText mEditTextPassword;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPreferences = getSharedPreferences(JsonParse.USER_SHARED_PREF, Context.MODE_PRIVATE);
        if (TextUtils.isEmpty(mSharedPreferences.getString(JsonParse.AUTHO_TOKEN_FIELD, null))) {
            setContentView(R.layout.activity_login);
            Button button_Login = (Button) findViewById(R.id.btn_login);
            mEditTextEmail = (EditText) findViewById(R.id.edit_email);
            mEditTextPassword = (EditText) findViewById(R.id.edit_password);

            button_Login.setOnClickListener(new View.OnClickListener() {
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
        } else {
            Toast.makeText(LoginActivity.this, getString(R.string.dialog_login_success), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private boolean isValidConditions() {
        String isValidEmail = mEditTextEmail.getText().toString();
        String validatePassword = mEditTextPassword.getText().toString();
        CheckLogin validationUtils = new CheckLogin(this);
        boolean flag = true;
        if (!CheckNetwork.isInternetConnected(LoginActivity.this)) {
            Toast.makeText(LoginActivity.this, getString(R.string.msg_no_internet), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            if (!validationUtils.isValidEmail(mEditTextEmail.getText().toString())) {
                Toast.makeText(LoginActivity.this, getString(R.string.error_email_invalid), Toast.LENGTH_SHORT).show();
                flag = false;
            } else if (!validationUtils.validatePassword(mEditTextPassword)) {
                Toast.makeText(LoginActivity.this, getString(R.string.error_password_short), Toast.LENGTH_SHORT).show();
                flag = false;
            }
            if (mEditTextEmail.getText().toString().isEmpty()) {
                Toast.makeText(LoginActivity.this, getString(R.string.error_email_blank), Toast.LENGTH_SHORT).show();
                flag = false;
            } else if (!validationUtils.validatePassword(mEditTextPassword)) {
                Toast.makeText(LoginActivity.this, getString(R.string.error_password_blank), Toast.LENGTH_SHORT).show();
                flag = false;
            }
            return flag;
        }
    }
}

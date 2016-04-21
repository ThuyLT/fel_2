package com.framgia.e_learningsimple.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;

import com.framgia.e_learningsimple.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ThuyIT on 20/04/2016.
 */
public class ValidationLogin {
    public static int NAME_MIN_LENGTH = 3;

    public boolean isValidEmail(EditText editTextEmail) {
        CharSequence target = editTextEmail.getText().toString();
        if (TextUtils.isEmpty(target)) {
            editTextEmail.setError(mContext.getString(R.string.error_email_blank));
            return false;
        } else {
            if (android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()) {
                return true;
            } else {
                editTextEmail.setError(mContext.getString(R.string.error_email_invalid));
                return false;
            }
        }
    }

    public static final int PASSWORD_MIN_LENGTH = 6;

    private Context mContext;

    public ValidationLogin(Context mContext) {
        this.mContext = mContext;
    }

    public boolean isValidName(EditText editTextName) {
        boolean isValid = false;
        String name = editTextName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            editTextName.setError(mContext.getString(R.string.error_name_blank));
        } else if (name.length() < NAME_MIN_LENGTH) {
            editTextName.setError(mContext.getString(R.string.error_name_short));
        } else {
            isValid = true;
        }
        return isValid;
    }

    public boolean validatePassword(EditText editTextPasssword) {
        boolean isValid = false;
        String password = editTextPasssword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            editTextPasssword.setError(mContext.getString(R.string.error_password_blank));
        } else if (password.length() < PASSWORD_MIN_LENGTH) {
            editTextPasssword.setError(mContext.getString(R.string.error_password_short));
        } else {
            isValid = true;
        }
        return isValid;
    }

    public boolean isValidRePassword(EditText editTextPasssword, EditText editTextRePasssword) {
        boolean isValid = false;
        String password = editTextPasssword.getText().toString();
        String passwordConfirmation = editTextRePasssword.getText().toString();
        if (!password.equals(passwordConfirmation)) {
            editTextPasssword.setError(mContext.getString(R.string.error_repassword_not_match));
        } else if (password.length() < PASSWORD_MIN_LENGTH) {
            editTextPasssword.setError(mContext.getString(R.string.error_repassword_blank));
        } else {
            isValid = true;
        }
        return isValid;
    }
}

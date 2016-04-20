package com.framgia.e_learningsimple.model;

import android.content.Context;
import android.widget.Toast;

import com.framgia.e_learningsimple.R;
import com.framgia.e_learningsimple.network.ErrorNetwork;

/**
 * Created by ThuyIT on 20/04/2016.
 */
public class ResponseHelper {
    private int mResponeCode;
    private String mResponeBody;

    public int getResponeCode() {
        return mResponeCode;
    }

    public String getResponeBody() {
        return mResponeBody;
    }

    public ResponseHelper(int code, String body){
        this.mResponeCode = code;
        this.mResponeBody = body;
    }
}

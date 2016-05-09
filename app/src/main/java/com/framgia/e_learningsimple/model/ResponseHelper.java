package com.framgia.e_learningsimple.model;

import android.content.Context;
import android.widget.Toast;

import com.framgia.e_learningsimple.R;
import com.framgia.e_learningsimple.network.ErrorNetwork;
import com.framgia.e_learningsimple.util.DialogUtil;

/**
 * Created by ThuyIT on 20/04/2016.
 */
public class ResponseHelper {
    private int mResponeCode;
    private String mResponeBody;
    private Context mContext;

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

    public static void httpStatusNotify(Context context, int statusCode) {
        switch (statusCode) {
            case ErrorNetwork.OK:
                DialogUtil.showAlert(context,R.string.ok);
                break;
            case ErrorNetwork.BAD_REQUEST:
                DialogUtil.showAlert(context,R.string.bad_request);
            break;
            case ErrorNetwork.UNAUTHORIZED:
                DialogUtil.showAlert(context,R.string.error_unauthorized);
                break;
            case ErrorNetwork.NOT_FOUND:
                DialogUtil.showAlert(context,R.string.error_server_not_found);
                break;
            default:
                DialogUtil.showAlert(context,R.string.error_unknown);
        }
    }
}

package com.framgia.e_learningsimple.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.framgia.e_learningsimple.R;
import com.framgia.e_learningsimple.constant.JsonKeyConstant;
import com.framgia.e_learningsimple.model.RequestHelper;
import com.framgia.e_learningsimple.model.ResponseHelper;
import com.framgia.e_learningsimple.network.ErrorNetwork;
import com.framgia.e_learningsimple.sharepreference.SharePreferenceUtil;
import com.framgia.e_learningsimple.url.UrlJson;
import com.framgia.e_learningsimple.util.MyAsynctask;
import com.framgia.e_learningsimple.util.ValueName;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by ThuyIT on 25/04/2016.
 */
public class LessonActivity extends Activity {
    private SharedPreferences mSharedPreferences;
    private TextView mTextViewLessonName;
    private String mAuthToken;
    private int mCategoryId;
    private String mCategoryName;
    private int mLessonId;
    private String mLessonName;

    public void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        setContentView(R.layout.activity_lesson);
        initialize();
        if (mCategoryId != -1 && !TextUtils.isEmpty(mAuthToken)) {
            new CreateLessionAsynTask(LessonActivity.this).execute(mAuthToken, String.valueOf(mCategoryId));
        } else {
            finish();
        }
    }

    public void onBackPressed() {
    }

    private void initialize() {
        mTextViewLessonName = (TextView) findViewById(R.id.text_lesson_name);
        mSharedPreferences = getSharedPreferences(JsonKeyConstant.USER_SHARED_PREF, Context.MODE_PRIVATE);
        mAuthToken = mSharedPreferences.getString(JsonKeyConstant.AUTHO_TOKEN_FIELD, null);
        Intent data = getIntent();
        mCategoryId = data.getIntExtra(JsonKeyConstant.KEY_CATEGORY_ID, -1);
        mCategoryName = data.getStringExtra(JsonKeyConstant.KEY_CATEGORY_NAME);
        if (TextUtils.isEmpty(mCategoryName)) {
            mCategoryName = getString(R.string.default_category_name);
        }
    }

    private class CreateLessionAsynTask extends MyAsynctask<String, Void, Void> {
        String mAuthTokenParamName = JsonKeyConstant.AUTHO_TOKEN_FIELD;
        private int mStatusCode;
        private String mResponseBody;

        public CreateLessionAsynTask(Context context) {
            super(context);
        }

        protected Void doInBackground(String... args) {
            String mAuthToken = args[0];
            String mCategoryId = args[1];
            String mCreateLessonUrl = String.format(UrlJson.LESSON_URl_FORMAT, mCategoryId);
            ValueName nameValue1 = new ValueName(mAuthTokenParamName, mAuthToken);
            ResponseHelper mResponseHelper = null;
            try {
                mResponseHelper = RequestHelper.executeRequest(mCreateLessonUrl, RequestHelper.Method.POST, nameValue1);
                mStatusCode = mResponseHelper.getResponeCode();
                mResponseBody = mResponseHelper.getResponeBody();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            switch (mStatusCode) {
                case ErrorNetwork.OK:
                    try {
                        JSONObject lessonJson = new JSONObject(mResponseBody).optJSONObject(JsonKeyConstant.LESSON);
                        String lessonName = lessonJson.optString(JsonKeyConstant.NAME_FIELD, JsonKeyConstant.DEFAULT_LESSON_NAME);
                        mTextViewLessonName.setText(lessonName);
                        String mWordsData = lessonJson.optString(JsonKeyConstant.WORDS);
                        mLessonId = lessonJson.optInt(JsonKeyConstant.KEY_ID);
                        mLessonName = lessonJson.optString(JsonKeyConstant.NAME_FIELD);
                        SharePreferenceUtil.putString(mSharedPreferences, JsonKeyConstant.KEY_WORDS_DATA, mWordsData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    ResponseHelper.httpStatusNotify(LessonActivity.this, mStatusCode);
            }
        }

    }

}

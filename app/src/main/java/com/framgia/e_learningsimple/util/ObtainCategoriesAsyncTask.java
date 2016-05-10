package com.framgia.e_learningsimple.util;

import android.app.Activity;

import com.framgia.e_learningsimple.constant.JsonKeyConstant;
import com.framgia.e_learningsimple.model.RequestHelper;
import com.framgia.e_learningsimple.model.ResponseHelper;
import com.framgia.e_learningsimple.network.ErrorNetwork;
import com.framgia.e_learningsimple.url.UrlJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by ThuyIT on 25/04/2016.
 */
public class ObtainCategoriesAsyncTask extends MyAsynctask<String, Void, String> {
    protected int mStatusCode;
    protected String mAuthToken;
    protected String mResponseBody;
    protected int mPage;
    protected ArrayList<Category> mCategories = new ArrayList<Category>();
    String mAuthTokenParamName = JsonKeyConstant.AUTHO_TOKEN_FIELD;
    String mPageParamName = JsonKeyConstant.PAGE;

    public ObtainCategoriesAsyncTask(Activity activity, ArrayList<Category> categories, String authToken, int page) {
        super(activity);
        this.mCategories = categories;
        this.mAuthToken = authToken;
        this.mPage = page;
    }

    protected String doInBackground(String... args) {
        ValueName nameValue1 = new ValueName(mAuthTokenParamName, mAuthToken);
        ValueName nameValue2 = new ValueName(mPageParamName, String.valueOf(mPage));
        ResponseHelper mResponseHelper = null;
        try {
            mResponseHelper = RequestHelper.executeRequest(UrlJson.CATEGORIES_URL, RequestHelper.Method.GET, nameValue1, nameValue2);
            mStatusCode = mResponseHelper.getResponeCode();
            mResponseBody = mResponseHelper.getResponeBody();
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
                    updateCategoriesList();
                } catch (JSONException e) {
                    // Do nothing
                }
                break;
            default:
                ResponseHelper.httpStatusNotify(mContext, mStatusCode);
        }
    }

    protected void updateCategoriesList() throws JSONException {
        JSONArray mCategoryJson = new JSONObject(mResponseBody).optJSONArray(JsonKeyConstant.CATEGORIES);
        int numOfWords = mCategoryJson.length();
        for (int i = 0; i < numOfWords; i++) {
            int mCategoryId = mCategoryJson.optJSONObject(i).optInt(JsonKeyConstant.KEY_ID);
            String mCategoryName = mCategoryJson.optJSONObject(i).optString(JsonKeyConstant.NAME_FIELD);
            String mCategoryPhotoUrl = mCategoryJson.optJSONObject(i).optString(JsonKeyConstant.PHOTO);
            int mCategoryLearnedWords = mCategoryJson.optJSONObject(i).optInt(JsonKeyConstant.LEARNED_WORDS);
            mCategories.add(new Category(mCategoryId, mCategoryName, mCategoryPhotoUrl, mCategoryLearnedWords));
        }
    }
}
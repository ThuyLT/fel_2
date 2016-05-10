package com.framgia.e_learningsimple.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.framgia.e_learningsimple.R;
import com.framgia.e_learningsimple.adapter.UserAdapter;
import com.framgia.e_learningsimple.constant.JsonKeyConstant;
import com.framgia.e_learningsimple.model.User;
import com.framgia.e_learningsimple.util.Category;
import com.framgia.e_learningsimple.util.DrawImageViewSrcTask;
import com.framgia.e_learningsimple.util.NetworkUtil;
import com.framgia.e_learningsimple.util.ObtainCategoriesAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by ThuyIT on 25/04/2016.
 */
public class HomeActivity extends Activity {

    private SharedPreferences mSharedPreferences;
    private ImageView mImageAvatar;
    private TextView mTextViewCurrentUserName;
    private TextView mTextViewCurrentUserEmail;
    private Button mBtnWordList;
    private Button mBtnLessons;
    private String mUserAvatarUrl;
    private String mUserName;
    private String mUserEmail;
    private ArrayList<User> mActivitiesList = new ArrayList<User>();
    private ArrayList<Category> mCategoriesList = new ArrayList<Category>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        initialize();
        mTextViewCurrentUserName.setText(mUserName);
        mTextViewCurrentUserEmail.setText(mUserEmail);
        new DrawImageViewSrcTask(mImageAvatar).execute(mUserAvatarUrl);
        ListView listViewUserActivities = (ListView) findViewById(R.id.list_activities);
        UserAdapter mUserAdapter = new UserAdapter(this, R.layout.item_user_activity, mActivitiesList);
        listViewUserActivities.setAdapter(mUserAdapter);
        setUpButtonsEventHanlder();
    }

    private void initialize() {
        mSharedPreferences = getSharedPreferences(JsonKeyConstant.USER_SHARED_PREF, MODE_PRIVATE);
        NetworkUtil.sAuthToken = mSharedPreferences.getString(JsonKeyConstant.AUTHO_TOKEN_FIELD, null);
        mImageAvatar = (ImageView) findViewById(R.id.image_avatar);
        mTextViewCurrentUserName = (TextView) findViewById(R.id.text_current_user_name);
        mTextViewCurrentUserEmail = (TextView) findViewById(R.id.text_current_user_email);
        mUserName = mSharedPreferences.getString(JsonKeyConstant.NAME_FIELD, "");
        mUserEmail = mSharedPreferences.getString(JsonKeyConstant.EMAIL_FILED, "");
        mUserAvatarUrl = mSharedPreferences.getString(JsonKeyConstant.KEY_USER_AVATAR_URL, "");
        Intent data = getIntent();
        String activitiesStr = mSharedPreferences.getString(JsonKeyConstant.KEY_ACTIVITIES, "");
        mActivitiesList = convertFromJsonString(activitiesStr);
        mBtnWordList = (Button) findViewById(R.id.btn_words);
        mBtnLessons = (Button) findViewById(R.id.btn_lessons);
    }

    private ArrayList<User> convertFromJsonString(String jsonStr) {
        ArrayList<User> activitiesList = new ArrayList<User>();
        if (!TextUtils.isEmpty(jsonStr)) {
            try {
                JSONArray activitiesJson = new JSONArray(jsonStr);
                int numOfActivities = activitiesJson.length();
                for (int i = 0; i < numOfActivities; i++) {
                    String content = activitiesJson.optJSONObject(i).optString(JsonKeyConstant.CONTENT);
                    String time = activitiesJson.optJSONObject(i).optString(JsonKeyConstant.CREATE_AT);
                    activitiesList.add(new User(content, time));
                }
            } catch (JSONException e) {
                // mActivitiesList is not be changed
            }
        }
        return activitiesList;
    }

    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.exit)
                .setPositiveButton(R.string.logout, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSharedPreferences.edit().clear().commit();
                        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                        finish();
                    }
                })
                .setNegativeButton(R.string.hide, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        moveTaskToBack(true);
                    }
                }).show();
    }

    private void setUpButtonsEventHanlder() {
        mBtnWordList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtil.isInternetConnected(HomeActivity.this)) {
                    new ObtainCategoriesAsyncTask(HomeActivity.this, mCategoriesList, NetworkUtil.sAuthToken, 1).execute();
                }
            }
        });
        mBtnLessons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtil.isInternetConnected(HomeActivity.this)) {
                    startActivity(new Intent(HomeActivity.this, CategoryActivity.class));
                }
            }
        });
    }

}
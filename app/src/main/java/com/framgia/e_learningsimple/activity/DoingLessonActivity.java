package com.framgia.e_learningsimple.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Network;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.framgia.e_learningsimple.R;
import com.framgia.e_learningsimple.adapter.LessonFragmentPagerAdapter;
import com.framgia.e_learningsimple.constant.JsonKeyConstant;
import com.framgia.e_learningsimple.fragment.QuestionFragment;
import com.framgia.e_learningsimple.model.RequestHelper;
import com.framgia.e_learningsimple.model.ResponseHelper;
import com.framgia.e_learningsimple.network.ErrorNetwork;
import com.framgia.e_learningsimple.sharepreference.SharePreferenceUtil;
import com.framgia.e_learningsimple.url.UrlJson;
import com.framgia.e_learningsimple.util.Answer;
import com.framgia.e_learningsimple.util.MyAsynctask;
import com.framgia.e_learningsimple.util.NetworkUtil;
import com.framgia.e_learningsimple.util.Question;
import com.framgia.e_learningsimple.util.UserAnswer;
import com.framgia.e_learningsimple.util.ValueName;
import com.framgia.e_learningsimple.util.Word;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ThuyIT on 12/05/2016.
 */
public class DoingLessonActivity extends FragmentActivity {
    private ArrayList<ValueName> mRequestParamsPair = new ArrayList<ValueName>();
    private ArrayList<UserAnswer> mUserAnswers = new ArrayList<UserAnswer>();
    private SharedPreferences mSharedPreferences;
    private ArrayList<Question> mQuestionList = new ArrayList<Question>();
    private ViewPager mViewPager;
    private TextView mTextViewLessonName;
    private int mLessonId;
    private String mLessonName;
    private String mCategoryName;
    private List<Fragment> mQuestionFragmentList = new ArrayList<Fragment>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doing_lesson);
        initialize();
        mTextViewLessonName.setText(mLessonName);
        LessonFragmentPagerAdapter lessonFragmentPagerAdapter =
                new LessonFragmentPagerAdapter(getSupportFragmentManager(), mQuestionList, mUserAnswers, mRequestParamsPair, new QuestionFragment.OnPagerChangeListener() {
                    @Override
                    public void onPagerChange(int position) {
                        mViewPager.setCurrentItem(position + 1);
                    }
                });
        mViewPager.setAdapter(lessonFragmentPagerAdapter);
        setupEventHanlders();
    }

    public void onBackPressed() {
    }

    private void initialize() {
        mSharedPreferences = getSharedPreferences(JsonKeyConstant.USER_SHARED_PREF, Context.MODE_PRIVATE);
        NetworkUtil.sAuthToken= mSharedPreferences.getString(JsonKeyConstant.AUTHO_TOKEN_FIELD, null);
        Intent data = getIntent();
        mLessonId = data.getIntExtra(JsonKeyConstant.KEY_LESSON_ID, -1);
        mLessonName = data.getStringExtra(JsonKeyConstant.KEY_LESSON_NAME);
        if (TextUtils.isEmpty(mLessonName)) {
            mLessonName = getString(R.string.default_lesson_name);
        }
        mCategoryName = data.getStringExtra(JsonKeyConstant.KEY_CATEGORY_NAME);
        if (TextUtils.isEmpty(mCategoryName)) {
            mCategoryName = getString(R.string.default_category_name);
        }
        initQuestionList();
        mViewPager = (ViewPager) findViewById(R.id.view_pager_question);
        mViewPager.setOffscreenPageLimit(mQuestionList.size());
        mTextViewLessonName = (TextView) findViewById(R.id.text_lesson_name);
    }

    private void initQuestionList() {
        String wordsData = mSharedPreferences.getString(JsonKeyConstant.KEY_WORDS_DATA, null);
        try {
            JSONArray wordsJson = new JSONArray(wordsData);
            int numOfWords = wordsJson.length();
            for (int i = 0; i < numOfWords; i++) {
                JSONObject wordData = wordsJson.optJSONObject(i);
                int wordId = wordData.optInt(JsonKeyConstant.KEY_ID);
                String wordContent = wordData.optString(JsonKeyConstant.CONTENT);
                String status = wordData.optString(JsonKeyConstant.KEY_WORD_STATUS);
                int resultId = wordData.optInt(JsonKeyConstant.RESULT_ID);
                JSONArray answersJson = wordData.optJSONArray(JsonKeyConstant.ANSWERS);
                ArrayList<Answer> answers = new ArrayList<Answer>();
                int numOfAnswers = answersJson.length();
                for (int j = 0; j < numOfAnswers; j++) {
                    JSONObject answerData = answersJson.optJSONObject(j);
                    int answerId = answerData.optInt(JsonKeyConstant.KEY_ID);
                    String answerContent = answerData.optString(JsonKeyConstant.CONTENT);
                    boolean isCorrect = answerData.optBoolean(JsonKeyConstant.IS_CORRECT);
                    answers.add(new Answer(answerId, answerContent, isCorrect));
                }
                mQuestionList.add(new Question(new Word(wordId, wordContent, answers, status), resultId));
                mUserAnswers.add(i, new UserAnswer(wordContent, getString(R.string.not_answered), false));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setupEventHanlders() {
        ImageButton finishDoingLesson = (ImageButton) findViewById(R.id.btn_finish);
        finishDoingLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLessonId != JsonKeyConstant.LESSON_ID && !TextUtils.isEmpty(NetworkUtil.sAuthToken)) {
                    new ObtainLessonResult(DoingLessonActivity.this).execute(NetworkUtil.sAuthToken, String.valueOf(mLessonId));
                } else {
                    finish();
                }
            }
        });
    }

    private class ObtainLessonResult extends MyAsynctask<String, Void, String> {
        String authTokenParamName = JsonKeyConstant.AUTHO_TOKEN_FIELD;
        private int mStatusCode;
        private String mResponseBody;

        public ObtainLessonResult(Context context) {
            super(context);
        }

        protected String doInBackground(String... args) {
            String authToken = args[0];
            String updateLessonUrl = String.format(UrlJson.UPDATE_LESSON_URl_FORMAT, String.valueOf(mLessonId));
            ValueName nameValue1 = new ValueName(authTokenParamName, authToken);
            ValueName nameValue2 = new ValueName("lesson[learned]", JsonKeyConstant.KEY_LEARNER);
            mRequestParamsPair.add(nameValue1);
            mRequestParamsPair.add(nameValue2);
            ResponseHelper responseHelper = null;
            try {
                responseHelper = RequestHelper.executeRequest(updateLessonUrl, RequestHelper.Method.PATCH, mRequestParamsPair);
                mStatusCode = responseHelper.getResponeCode();
                mResponseBody = responseHelper.getResponeBody();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

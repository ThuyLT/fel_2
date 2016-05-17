package com.framgia.e_learningsimple.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.framgia.e_learningsimple.R;
import com.framgia.e_learningsimple.adapter.LessonResultAdapter;
import com.framgia.e_learningsimple.adapter.UserAnswerAdapter;
import com.framgia.e_learningsimple.constant.JsonKeyConstant;
import com.framgia.e_learningsimple.model.Result;
import com.framgia.e_learningsimple.util.UserAnswer;

import java.util.ArrayList;

/**
 * Created by lethuy on 16/05/2016.
 */
public class ResultActivity extends Activity {
    private String mLessonName;
    private String mCategoryName;
    private ArrayList<UserAnswer> mUserAnswers = new ArrayList<>();
    private TextView mTextViewLessonScore;
    private TextView mTextViewLessonName;
    private ListView mListViewUserResults;
    private int mTotalCount = 0;
    private int mCorrectCount = 0;
    private ImageButton mBtnBack;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        initialize();
        calculateResult();
        UserAnswerAdapter userAnwserAdapter = new UserAnswerAdapter(this, R.layout.item_result, mUserAnswers);
        mListViewUserResults.setAdapter(userAnwserAdapter);
        String score = String.format(JsonKeyConstant.SCORE_FORMAT, mCorrectCount, mTotalCount);
        mTextViewLessonScore.setText(score);
        mTextViewLessonName.setText(mLessonName);
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, CategoryActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initialize() {
        Intent data = getIntent();
        mUserAnswers = (ArrayList<UserAnswer>) data.getSerializableExtra(JsonKeyConstant.KEY_USER_ANSWERS);
        mLessonName = data.getStringExtra(JsonKeyConstant.KEY_LESSON_NAME);
        if (TextUtils.isEmpty(mLessonName)) {
            mLessonName = getString(R.string.default_lesson_name);
        }
        mCategoryName = data.getStringExtra(JsonKeyConstant.KEY_CATEGORY_NAME);
        if (TextUtils.isEmpty(mCategoryName)) {
            mCategoryName = getString(R.string.default_category_name);
        }
        mTextViewLessonScore = (TextView) findViewById(R.id.score);
        mListViewUserResults = (ListView) findViewById(R.id.list_user_results);
        mTextViewLessonName = (TextView) findViewById(R.id.text_lesson_name);
        mBtnBack = (ImageButton) findViewById(R.id.btn_back);
    }

    public void calculateResult() {
        mTotalCount = mUserAnswers.size();
        for (int i = 0; i < mTotalCount; i++) {
            if (mUserAnswers.get(i).isCorrect()) {
                mCorrectCount++;
            }
        }
    }
}
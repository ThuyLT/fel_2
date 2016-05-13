package com.framgia.e_learningsimple.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;

import com.framgia.e_learningsimple.fragment.QuestionFragment;
import com.framgia.e_learningsimple.util.Question;
import com.framgia.e_learningsimple.util.UserAnswer;
import com.framgia.e_learningsimple.util.ValueName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ThuyIT on 12/05/2016.
 */
public class LessonFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<Question> mQuestions;
    private ArrayList<UserAnswer> mUserAnswers;
    ArrayList<ValueName> mNameValuePairs;
    QuestionFragment.OnPagerChangeListener mOnPagerChangeListener;

    public LessonFragmentPagerAdapter(FragmentManager fragmentManager, List<Question> questions,
                                      ArrayList<UserAnswer> userAnswers, ArrayList<ValueName> valueNames, QuestionFragment.OnPagerChangeListener onPagerChangeListener) {
        super(fragmentManager);
        mQuestions = questions;
        mUserAnswers = userAnswers;
        mNameValuePairs = valueNames;
        mOnPagerChangeListener = onPagerChangeListener;
    }

    @Override
    public Fragment getItem(int position) {

        QuestionFragment questionFragment = new QuestionFragment(mQuestions.get(position), mNameValuePairs, position, mUserAnswers);
        questionFragment.setOnPagerChangeListener(mOnPagerChangeListener);
        return questionFragment;
    }

    @Override
    public int getCount() {
        return mQuestions.size();
    }
}

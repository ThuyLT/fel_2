package com.framgia.e_learningsimple.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.framgia.e_learningsimple.R;
import com.framgia.e_learningsimple.adapter.AnswerAdapter;
import com.framgia.e_learningsimple.util.Answer;
import com.framgia.e_learningsimple.util.Question;
import com.framgia.e_learningsimple.util.UserAnswer;
import com.framgia.e_learningsimple.util.ValueName;
import com.framgia.e_learningsimple.util.Word;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by ThuyIT on 12/05/2016.
 */
public class QuestionFragment extends Fragment {
    private final String RESULT_RESULT_ID_FORMAT = "lesson[results_attributes][%s][id]";
    private final String RESULT_ANWSER_ID_FORMAT = "lesson[results_attributes][%s][answer_id]";
    private ArrayList<ValueName> mNameValuePairs;
    private ArrayList<UserAnswer> mUserAnswers = new ArrayList<UserAnswer>();
    private ArrayList<Answer> mAnswersList;
    private int mFragmentIndex;
    private Question mQuestion;
    private TextView mTextViewQuestionName;
    private TextView mTextViewQuestionContent;
    private ListView mlistViewAnswers;
    private View mFragmentView;
    private Activity mActivity;
    private TextToSpeech mTextToSpeech;
    private OnPagerChangeListener mOnPagerChangeListener;

    public QuestionFragment(Question question, ArrayList<ValueName> valueNames, int position, ArrayList<UserAnswer> userAnswers) {
        mQuestion = question;
        mNameValuePairs = valueNames;
        mFragmentIndex = position;
        mUserAnswers = userAnswers;
    }

    private static void setViewAndChildrenEnabled(View view, boolean enabled) {
        view.setEnabled(enabled);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int count = viewGroup.getChildCount();
            for (int i = 0; i < count; i++) {
                View child = viewGroup.getChildAt(i);
                setViewAndChildrenEnabled(child, enabled);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_question, container, false);
        initialize();
        mTextViewQuestionName.setText(mActivity.getString(R.string.label_question) + (mFragmentIndex + 1));
        mTextViewQuestionContent.setText(mQuestion.getWord().getContent());
        final HashMap<String, String> params = new HashMap<String, String>();
        mTextViewQuestionContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextToSpeech.speak(mTextViewQuestionContent.getText().toString(), TextToSpeech.QUEUE_FLUSH, params);
            }
        });
        AnswerAdapter answerAdapter = new AnswerAdapter(mActivity, R.layout.item_answer, mAnswersList);
        mlistViewAnswers.setAdapter(answerAdapter);
        return mFragmentView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mlistViewAnswers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Button btnAnswerSelected = (Button) view.findViewById(R.id.text_anwser_content);
                //noinspection deprecation
                btnAnswerSelected.setBackgroundColor(getResources().getColor(R.color.actionbar));
                setViewAndChildrenEnabled((View) view.getParent().getParent(), false);
                Answer answer = mAnswersList.get(position);
                updateNameValuePairs(mFragmentIndex, mNameValuePairs, mQuestion, answer);
                updateUserAnswers(mUserAnswers, mQuestion.getWord(), answer);
                if (mOnPagerChangeListener != null) {
                    mOnPagerChangeListener.onPagerChange(mFragmentIndex);
                }
            }
        });
    }

    private void initialize() {
        mActivity = getActivity();
        mAnswersList = mQuestion.getWord().getAnswers();
        initViewComponent();
        mTextToSpeech = new TextToSpeech(mActivity.getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    mTextToSpeech.setLanguage(Locale.US);
                }
            }
        });
    }

    private void initViewComponent() {
        mTextViewQuestionName = (TextView) mFragmentView.findViewById(R.id.text_question_name);
        mTextViewQuestionContent = (TextView) mFragmentView.findViewById(R.id.text_question_content);
        mlistViewAnswers = (ListView) mFragmentView.findViewById(R.id.list_answers);
    }

    private void updateNameValuePairs(int index, ArrayList<ValueName> nameValuePairs, Question question, Answer answer) {
        ValueName nameValue1 = new ValueName(
                String.format(RESULT_RESULT_ID_FORMAT, String.valueOf(index)),
                String.valueOf(question.getResultId())
        );
        ValueName nameValue2 = new ValueName(
                String.format(RESULT_ANWSER_ID_FORMAT, String.valueOf(index)),
                String.valueOf(answer.getId())
        );

        nameValuePairs.add(nameValue1);
        nameValuePairs.add(nameValue2);
    }

    private void updateUserAnswers(ArrayList<UserAnswer> userAnswers, Word word, Answer answer) {
        userAnswers.set(mFragmentIndex, new UserAnswer(word.getContent(), answer.getContent(), answer.isCorrect()));
    }

    public void setOnPagerChangeListener(OnPagerChangeListener onPagerChangeListener) {
        mOnPagerChangeListener = onPagerChangeListener;
    }

    public interface OnPagerChangeListener{
        void onPagerChange(int position);
    }
}

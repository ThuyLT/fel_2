package com.framgia.e_learningsimple.util;

import java.util.ArrayList;

/**
 * Created by ThuyIT on 10/05/2016.
 */
public class Word {
    private int mId;
    private String mContent;
    private String mStatus;
    private ArrayList<Answer> mAnswers;

    public Word(String content, ArrayList<Answer> answers, String status) {
        this.mContent = content;
        this.mAnswers = answers;
        this.mStatus = status;
    }

    public Word(int id, String content, ArrayList<Answer> answers, String status) {
        this.mId = id;
        this.mContent = content;
        this.mAnswers = answers;
        this.mStatus = status;
    }

    public int getId() {
        return mId;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public String getContent() {
        return mContent;
    }

    public ArrayList<Answer> getAnswers() {
        return mAnswers;
    }

    public Answer getCorrectAnswer() {
        Answer correctAnswer = null;
        for (Answer answer : mAnswers) {
            if (answer.isCorrect()) {
                correctAnswer = answer;
                break;
            }
        }
        return correctAnswer;
    }
}

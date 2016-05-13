package com.framgia.e_learningsimple.util;

/**
 * Created by ThuyIT on 12/05/2016.
 */
public class Question {
    private Word mWord;
    private int mResultId;

    public Question(Word word, int resultId) {
        this.mWord = word;
        this.mResultId = resultId;
    }

    public Word getWord() {
        return mWord;
    }

    public int getResultId() {
        return mResultId;
    }
}

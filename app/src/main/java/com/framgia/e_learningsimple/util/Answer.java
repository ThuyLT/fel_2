package com.framgia.e_learningsimple.util;

/**
 * Created by ThuyIT on 10/05/2016.
 */
public class Answer {
    private int mId;
    private String mContent;
    private boolean mICorrect;

    public Answer(String content, boolean isCorrect) {
        this.mContent = content;
        this.mICorrect = isCorrect;
    }

    public Answer(int id, String content, boolean isCorrect) {
        this.mId = id;
        this.mContent = content;
        this.mICorrect = isCorrect;
    }

    public int getId() {
        return mId;
    }

    public String getContent() {
        return mContent;
    }

    public boolean isCorrect() {
        return mICorrect;
    }
}

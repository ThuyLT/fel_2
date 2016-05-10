package com.framgia.e_learningsimple.model;

/**
 * Created by ThuyIT on 25/04/2016.
 */
public class User {
    private String mContent;
    private String mTime;

    public User(String mContent, String mTime) {
        this.mContent = mContent;
        this.mTime = mTime;
    }

    public String getContent() {
        return mContent;
    }

    public String getTime() {
        return mTime;
    }
}
package com.framgia.e_learningsimple.model;

/**
 * Created by lethuy on 16/05/2016.
 */
public class Result {
    private int mId;
    private String mLessonName;
    private String mCategoryName;
    private String mScore;

    public Result(String lessonName, String categoryName, String score) {
        this.mLessonName = lessonName;
        this.mCategoryName = categoryName;
        this.mScore = score;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getLessonName() {
        return mLessonName;
    }

    public String getCategoryName() {
        return mCategoryName;
    }

    public String getScore() {
        return mScore;
    }
}

package com.framgia.e_learningsimple.util;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ThuyIT on 25/04/2016.
 */
public class Category implements Parcelable {
    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
    private int mId;
    private String mName;
    private String mPhotoUrl;
    private int mSumOfLearnedWords;

    public Category(int id, String name, String photoUrl, int sumOfLearnedWords) {
        this.mId = id;
        this.mName = name;
        this.mPhotoUrl = photoUrl;
        this.mSumOfLearnedWords = sumOfLearnedWords;
    }

    protected Category(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
        mPhotoUrl = in.readString();
        mSumOfLearnedWords = in.readInt();
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getPhotoUrl() {
        return mPhotoUrl;
    }

    public int getSumOfLearnedWords() {
        return mSumOfLearnedWords;
    }

    public String toString() {
        return mName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mName);
        dest.writeString(mPhotoUrl);
        dest.writeInt(mSumOfLearnedWords);
    }
}


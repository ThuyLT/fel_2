package com.framgia.e_learningsimple.util;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ThuyIT on 12/05/2016.
 */
public class UserAnswer implements Parcelable {
    public static final Creator<UserAnswer> CREATOR = new Creator<UserAnswer>() {
        @Override
        public UserAnswer createFromParcel(Parcel in) {
            return new UserAnswer(in);
        }

        @Override
        public UserAnswer[] newArray(int size) {
            return new UserAnswer[size];
        }
    };
    private String mWordContent;
    private String mAnwserContent;
    private boolean mIsCorrect;

    public UserAnswer(String wordContent, String anwserContent, boolean isCorrect) {
        this.mWordContent = wordContent;
        this.mAnwserContent = anwserContent;
        this.mIsCorrect = isCorrect;
    }

    protected UserAnswer(Parcel in) {
        mWordContent = in.readString();
        mAnwserContent = in.readString();
        mIsCorrect = in.readByte() != 0;
    }

    public String getWordContent() {
        return mWordContent;
    }

    public String getAnwserContent() {
        return mAnwserContent;
    }

    public boolean isCorrect() {
        return mIsCorrect;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mWordContent);
        dest.writeString(mAnwserContent);
        dest.writeByte((byte) (mIsCorrect ? 1 : 0));
    }
}

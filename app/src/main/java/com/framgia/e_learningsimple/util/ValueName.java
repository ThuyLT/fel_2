package com.framgia.e_learningsimple.util;

/**
 * Created by ThuyIT on 20/04/2016.
 */
public class ValueName {
    private String mName;
    private String mValue;

    public ValueName(String name, String value){
        this.mName = name;
        this.mValue = value;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        this.mValue = value;
    }
}

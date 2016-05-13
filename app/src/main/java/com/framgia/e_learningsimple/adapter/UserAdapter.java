package com.framgia.e_learningsimple.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.framgia.e_learningsimple.R;
import com.framgia.e_learningsimple.model.User;

import java.util.ArrayList;

/**
 * Created by ThuyIT on 25/04/2016.
 */
public class UserAdapter extends ArrayAdapter<User> {
    private Context mContext;
    private int mIdLayout;
    private ArrayList<User> mList;
    private LayoutInflater mInflater;

    public UserAdapter(Context context, int idLayout, ArrayList<User> list) {
        super(context, idLayout, list);
        this.mContext = context;
        this.mIdLayout = idLayout;
        this.mList = list;
        this.mInflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        User userActivity = mList.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(mIdLayout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.mActivityContent = (TextView) convertView.findViewById(R.id.text_activity_content);
            viewHolder.mActivityTime = (TextView) convertView.findViewById(R.id.text_activity_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.mActivityContent.setText(userActivity.getContent());
        viewHolder.mActivityTime.setText(userActivity.getTime());
        return convertView;
    }

    public static class ViewHolder {
        TextView mActivityContent;
        TextView mActivityTime;
    }
}
package com.framgia.e_learningsimple.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.framgia.e_learningsimple.R;
import com.framgia.e_learningsimple.model.Result;

import java.util.ArrayList;

/**
 * Created by lethuy on 16/05/2016.
 */
public class LessonResultAdapter extends ArrayAdapter<Result> {
    private Context mContext;
    private int mIdLayout;
    private ArrayList<Result> mList;
    private LayoutInflater mInflater;

    public LessonResultAdapter(Context context, int idLayout, ArrayList<Result> list) {
        super(context, idLayout, list);
        this.mContext = context;
        this.mIdLayout = idLayout;
        this.mList = list;
        this.mInflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        Result lessonResult = mList.get(mList.size() - 1 - position);
        if (convertView == null) {
            convertView = mInflater.inflate(mIdLayout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textLessonName = (TextView) convertView.findViewById(R.id.text_lesson_name);
            viewHolder.textCategoryName = (TextView) convertView.findViewById(R.id.text_category_name);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textLessonName.setText(lessonResult.getLessonName());
        viewHolder.textCategoryName.setText(lessonResult.getCategoryName());
        viewHolder.textLessonScore.setText(lessonResult.getScore());
        return convertView;
    }

    static class ViewHolder {
        TextView textLessonName;
        TextView textCategoryName;
        TextView textLessonScore;
    }
}
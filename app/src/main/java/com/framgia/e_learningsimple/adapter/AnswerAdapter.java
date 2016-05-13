package com.framgia.e_learningsimple.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.framgia.e_learningsimple.R;
import com.framgia.e_learningsimple.util.Answer;

import java.util.ArrayList;

/**
 * Created by ThuyIT on 12/05/2016.
 */
public class AnswerAdapter extends ArrayAdapter<Answer> {
    private Context mContext;
    private int mIdLayout;
    private ArrayList<Answer> mList;
    private LayoutInflater mInflater;

    public AnswerAdapter(Context context, int idLayout, ArrayList<Answer> list) {
        super(context, idLayout, list);
        this.mContext = context;
        this.mIdLayout = idLayout;
        this.mList = list;
        this.mInflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        Answer answer = mList.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(mIdLayout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.anwserContent = (Button) convertView.findViewById(R.id.text_anwser_content);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.anwserContent.setText(answer.getContent());
        return convertView;
    }

    static class ViewHolder {
        Button anwserContent;
    }
}

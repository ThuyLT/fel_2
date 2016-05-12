package com.framgia.e_learningsimple.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.framgia.e_learningsimple.R;
import com.framgia.e_learningsimple.util.UserAnswer;

import java.util.ArrayList;

/**
 * Created by ThuyIT on 12/05/2016.
 */
public class UserAnswerAdapter extends ArrayAdapter<UserAnswer> {
    private int mIdLayout;
    private ArrayList<UserAnswer> mList;
    private LayoutInflater mInflater;
    private Context mContext;

    public UserAnswerAdapter(Context context, int idLayout, ArrayList<UserAnswer> list) {
        super(context, idLayout, list);
        this.mContext = context;
        this.mIdLayout = idLayout;
        this.mList = list;
        this.mInflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        UserAnswer userAnswer = mList.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(mIdLayout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imageAnswerStatus = (ImageView) convertView.findViewById(R.id.image_answer_status);
            viewHolder.textOriginWord = (TextView) convertView.findViewById(R.id.text_origin_word);
            viewHolder.textUserAnswer = (TextView) convertView.findViewById(R.id.text_user_answer);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        int imageResId = userAnswer.isCorrect() ? R.mipmap.ic_correct : R.mipmap.ic_incorrect;
        viewHolder.imageAnswerStatus.setImageResource(imageResId);
        viewHolder.textOriginWord.setText(userAnswer.getWordContent());
        viewHolder.textUserAnswer.setText(userAnswer.getAnwserContent());
        return convertView;
    }

    static class ViewHolder {
        ImageView imageAnswerStatus;
        TextView textOriginWord;
        TextView textUserAnswer;
    }
}

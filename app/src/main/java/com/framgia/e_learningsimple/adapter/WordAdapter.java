package com.framgia.e_learningsimple.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.framgia.e_learningsimple.R;
import com.framgia.e_learningsimple.util.Answer;
import com.framgia.e_learningsimple.util.Word;

import java.util.ArrayList;

/**
 * Created by ThuyIT on 10/05/2016.
 */
public class WordAdapter extends RecyclerView.Adapter<WordAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Word> mWord;
    private LayoutInflater mInflater;

    public WordAdapter(Context context, ArrayList<Word> list) {
        this.mContext = context;
        this.mWord = list;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.item_word, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Word word = mWord.get(position);
        Answer correctAnswerOfWord = word.getCorrectAnswer();
        viewHolder.mOriginWord.setText(word.getContent());
        if (correctAnswerOfWord != null) {
            viewHolder.mWordMeaning.setText(correctAnswerOfWord.getContent());
        } else {
            viewHolder.mWordMeaning.setText(mContext.getString(R.string.unknow_answer));
            viewHolder.mWordMeaning.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
        }
    }

    @Override
    public int getItemCount() {
        return mWord.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mOriginWord;
        private TextView mWordMeaning;

        public ViewHolder(View itemView) {
            super(itemView);
            mOriginWord = (TextView) itemView.findViewById(R.id.text_origin_word);
            mWordMeaning = (TextView) itemView.findViewById(R.id.text_word_meaning);
        }
    }
}

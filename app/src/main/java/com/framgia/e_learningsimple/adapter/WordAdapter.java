package com.framgia.e_learningsimple.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.framgia.e_learningsimple.R;
import com.framgia.e_learningsimple.constant.JsonKeyConstant;
import com.framgia.e_learningsimple.util.Answer;
import com.framgia.e_learningsimple.util.Word;

import java.util.ArrayList;

/**
 * Created by ThuyIT on 10/05/2016.
 */
public class WordAdapter extends RecyclerView.Adapter<WordAdapter.ViewHolder> implements Filterable{
    private Context mContext;
    private ArrayList<Word> mWord;
    private ArrayList<Word> mFilterWorlds;
    private LayoutInflater mInflater;

    public WordAdapter(Context context, ArrayList<Word> list) {
        this.mContext = context;
        this.mWord = list;
        mFilterWorlds = list;
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
        Word word = mFilterWorlds.get(position);
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
        return mFilterWorlds.size();
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

    private class ItemFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            if (filterString.compareTo(JsonKeyConstant.KEY_ALL_WORD) == 0) {
                results.values = mWord;
                results.count = mWord.size();

                return results;
            }
            ArrayList<Word> listFilter = new ArrayList<>();
            int count = mWord.size();
            for (int i = 0; i < count; i++){
                Word word = mWord.get(i);
               if(word.getStatus().compareTo(filterString) == 0){
                   listFilter.add(word);
               }
            }

            results.values = listFilter;
            results.count = listFilter.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mFilterWorlds = (ArrayList<Word>) results.values;
            notifyDataSetChanged();
        }
    }

    @Override
    public Filter getFilter() {
        return new ItemFilter();
    }
}

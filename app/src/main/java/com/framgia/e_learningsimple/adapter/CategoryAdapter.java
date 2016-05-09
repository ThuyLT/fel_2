package com.framgia.e_learningsimple.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.framgia.e_learningsimple.R;
import com.framgia.e_learningsimple.activity.LessonActivity;
import com.framgia.e_learningsimple.constant.JsonKeyConstant;
import com.framgia.e_learningsimple.util.Category;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by ThuyIT on 25/04/2016.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private Context mContext;
    private static ArrayList<Category> mCategories;
    private LayoutInflater mInflater;

    public CategoryAdapter(Context context, ArrayList<Category> list) {
        this.mCategories = list;
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.item_category, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Category category = mCategories.get(position);
        new SetImageViewSrcTask(viewHolder.imageCategoryPhoto).execute(category.getPhotoUrl());
        viewHolder.mTextCategoryName.setText(category.getName());
        viewHolder.mTextCategoryStatus.setText(String.format("%s : %s",
                mContext.getString(R.string.num_of_words_learned),
                category.getSumOfLearnedWords()));
        viewHolder.mCategoryId = category.getId();
        viewHolder.mCategoryName = category.getName();
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private int mCategoryId;
        private String mCategoryName;
        private ImageView imageCategoryPhoto;
        private TextView mTextCategoryName;
        private TextView mTextCategoryStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            imageCategoryPhoto = (ImageView) itemView.findViewById(R.id.img_category_photo);
            mTextCategoryName = (TextView) itemView.findViewById(R.id.text_category_name);
            mTextCategoryStatus = (TextView) itemView.findViewById(R.id.text_category_status);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, LessonActivity.class);
                    intent.putExtra(JsonKeyConstant.KEY_CATEGORY_ID, mCategoryId);
                    intent.putExtra(JsonKeyConstant.KEY_CATEGORY_NAME, mCategoryName);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    public class SetImageViewSrcTask extends AsyncTask<String, Void, Bitmap> {
        ImageView mImageView;
        boolean hasError = false;

        public SetImageViewSrcTask(ImageView imageView) {
            this.mImageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            if (result == null) {
                mImageView.setImageResource(R.mipmap.ic_framgia);
            } else {
                mImageView.setImageBitmap(result);
            }
        }
    }

}
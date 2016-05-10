package com.framgia.e_learningsimple.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.ImageButton;

import com.framgia.e_learningsimple.R;
import com.framgia.e_learningsimple.adapter.CategoryAdapter;
import com.framgia.e_learningsimple.constant.JsonKeyConstant;
import com.framgia.e_learningsimple.listener.EndlessRecyclerOnScrollListener;
import com.framgia.e_learningsimple.util.Category;
import com.framgia.e_learningsimple.util.ObtainCategoriesAsyncTask;

import java.util.ArrayList;

/**
 * Created by ThuyIT on 25/04/2016.
 */
public class CategoryActivity extends Activity {
    private String mAuthToken;
    private SharedPreferences mSharedPreferences;
    private ArrayList<Category> mCategoryList = new ArrayList<Category>();
    private CategoryAdapter mCategoryAdapter;
    private RecyclerView mRecycleViewCategories;
    private ImageButton mBtnBack;
    private LinearLayoutManager mLayoutManager;
    private int mCurrentPage = 1;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        initialize();
        setupCategoryRecycleView();
        if (!TextUtils.isEmpty(mAuthToken)) {
            new ObtainCategoriesTask(CategoryActivity.this, mCategoryList, mAuthToken, mCurrentPage)
                    .execute(mAuthToken);
        }
        setUpEventHandlers();
    }

    private void initialize() {
        mSharedPreferences = getSharedPreferences(JsonKeyConstant.USER_SHARED_PREF, Context.MODE_PRIVATE);
        mAuthToken = mSharedPreferences.getString(JsonKeyConstant.AUTHO_TOKEN_FIELD, null);
        mBtnBack = (ImageButton) findViewById(R.id.btn_back);
        mRecycleViewCategories = (RecyclerView) findViewById(R.id.list_categories);
        mCategoryAdapter = new CategoryAdapter(this, mCategoryList);
        mLayoutManager = new LinearLayoutManager(this);
    }

    private void setupCategoryRecycleView() {
        mRecycleViewCategories.setHasFixedSize(true);
        mRecycleViewCategories.setLayoutManager(mLayoutManager);
        mRecycleViewCategories.setAdapter(mCategoryAdapter);
    }

    private void setUpEventHandlers() {
        mRecycleViewCategories.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {
            public void onLoadMore() {
                new ObtainCategoriesTask(CategoryActivity.this, mCategoryList, mAuthToken, mCurrentPage)
                        .execute();
            }
        });
    }

    private class ObtainCategoriesTask extends ObtainCategoriesAsyncTask {
        ObtainCategoriesTask(Activity activity, ArrayList<Category> categories, String authToken, int currentPage) {
            super(activity, categories, authToken, currentPage);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            mCurrentPage++;
            mCategoryAdapter.notifyDataSetChanged();
        }
    }
}

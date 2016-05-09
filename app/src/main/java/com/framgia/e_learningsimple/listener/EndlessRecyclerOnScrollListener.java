package com.framgia.e_learningsimple.listener;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by ThuyIT on 25/04/2016.
 */
public abstract class EndlessRecyclerOnScrollListener extends
        RecyclerView.OnScrollListener {
    private static String TAG = EndlessRecyclerOnScrollListener.class
            .getSimpleName();
    int mFirstVisibleItem, mVisibleItemCount, mTotalItemCount;
    private int mPreviousTotal;
    private boolean mLoading = true;
    private int mVisibleThreshold = 5;
    private int mCurrentPage = 1;

    private LinearLayoutManager mLinearLayoutManager;

    public EndlessRecyclerOnScrollListener(
            LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        mVisibleItemCount = recyclerView.getChildCount();
        mTotalItemCount = mLinearLayoutManager.getItemCount();
        mFirstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

        if (mLoading) {
            if (mTotalItemCount > mPreviousTotal) {
                mLoading = false;
                mPreviousTotal = mTotalItemCount;
            }
            // Do more
        }
        if (!mLoading
                && (mTotalItemCount - mVisibleItemCount) <= (mFirstVisibleItem + mVisibleThreshold)) {
            // End has been reached

            // Do something
            onLoadMore();
            mLoading = true;
        }
    }

    public void reset() {
        mFirstVisibleItem = mVisibleItemCount = mTotalItemCount = 0;
        mPreviousTotal = 0;
        mLoading = true;
        mVisibleThreshold = 5;
        mCurrentPage = 1;
    }

    public abstract void onLoadMore();
}


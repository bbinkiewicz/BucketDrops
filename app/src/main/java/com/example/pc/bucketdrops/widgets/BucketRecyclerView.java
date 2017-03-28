package com.example.pc.bucketdrops.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.example.pc.bucketdrops.extras.Util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class BucketRecyclerView extends RecyclerView{

    private List<View> mEmptyViews = Collections.emptyList();
    private List<View> mNonEmptyViews = Collections.emptyList();

    private AdapterDataObserver mObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            toggleViews();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            toggleViews();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            toggleViews();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            toggleViews();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            toggleViews();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            toggleViews();
        }
    };

    private void toggleViews() {

        if (getAdapter() != null && !mNonEmptyViews.isEmpty() && !mEmptyViews.isEmpty()) {

            //there is nothing in the adapter
            if (getAdapter().getItemCount() == 0) {

                //show all empty views
                Util.showViews(mEmptyViews);
                //hide Recycler View
                setVisibility(View.GONE);

                //hide all views which should not be visible when adapter is empty
                Util.hideViews(mNonEmptyViews);
                //there is something in the adapter
            } else {
                //hide all views which should be visible only when there is no data in the adapter
                Util.hideViews(mEmptyViews);
                //show the Recycler
                setVisibility(View.VISIBLE);
                //show all the views which should be on the screen when there is something in the adapter
                Util.showViews(mNonEmptyViews);


            }
        }
    }

    public BucketRecyclerView(Context context) {
        super(context);
    }

    public BucketRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BucketRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);

        if(adapter!=null){
            adapter.registerAdapterDataObserver(mObserver);
        }
        mObserver.onChanged();
    }

    public void hideIfEmpty(View ...views) {

        mNonEmptyViews = Arrays.asList(views);
    }

    public void showIfEmpty(View ...EmptyViews) {

        mEmptyViews = Arrays.asList(EmptyViews);
    }
}

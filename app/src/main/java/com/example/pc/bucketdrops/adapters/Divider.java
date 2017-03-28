package com.example.pc.bucketdrops.adapters;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.pc.bucketdrops.R;



public class Divider extends RecyclerView.ItemDecoration {

    private Drawable mDivider;
    private int mOrientation;

    public Divider(Context context, int orientation){

        mDivider = context.getDrawable(R.drawable.divider);

        if(orientation != LinearLayoutManager.VERTICAL){
            throw new IllegalArgumentException("This item decoration can be used only with vertical orientation");
        }
            mOrientation = orientation;

    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {

        if (mOrientation == LinearLayoutManager.VERTICAL) {

            drawHorizontalDivider(c, parent, state);
        }
    }


    private void drawHorizontalDivider(Canvas c, RecyclerView parent, RecyclerView.State state) {

        int left, top, right, bottom;

        left = parent.getPaddingLeft();
        right = parent.getWidth() - parent.getPaddingRight();
        int counter = parent.getChildCount();

        for (int i=0; i<counter-1; i++){


                View current = parent.getChildAt(i);
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) current.getLayoutParams();

                top = current.getBottom();
                bottom = top + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);

        }


    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
    }
}

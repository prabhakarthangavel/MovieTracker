package com.learning.movietracker.utl;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.learning.movietracker.R;

public class CustomLinearLayout extends LinearLayout {
    public static Integer starsCount;
    public CustomLinearLayout(Context context) {
        super(context);
    }

    public CustomLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleChildClick(v);
                }
            });
        }
    }

    private void handleChildClick(View view) {
        starsCount = indexOfChild(view) + 1;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (i < starsCount) {
                ((ImageView) child).setImageResource(R.drawable.icons8_star_28_gold);
            } else {
                ((ImageView) child).setImageResource(R.drawable.icons8_star_28_outline);
            }
        }
    }
}

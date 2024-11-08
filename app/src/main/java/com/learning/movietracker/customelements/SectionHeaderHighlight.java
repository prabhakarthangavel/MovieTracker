package com.learning.movietracker.customelements;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class SectionHeaderHighlight extends View {
    private Paint paint;
//    private RectF rectF;

    public SectionHeaderHighlight(Context context) {
        super(context);
        init();
    }

    public SectionHeaderHighlight(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.parseColor("#3c8031")); // Your desired color
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
    }

//    private void init() {
//        paint = new Paint();
//        paint.setColor(Color.parseColor("#3c8031")); // Your desired color
//        paint.setAntiAlias(true);
//        rectF = new RectF(0, 0, 300, 300); // Adjust size as needed
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        canvas.drawRoundRect(rectF, 16, 16, paint); // Radius values for X and Y
//    }
}

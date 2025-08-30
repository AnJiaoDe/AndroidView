package com.cy.androidview.loadingview;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import com.cy.androidview.ScreenUtils;

public class FourCircleLoadingView extends View {
    private Paint paint;
    private int[] colors;
    private float radius;
    private float rotation;
    private float trans;
    private AnimatorSet animatorSet;

    public FourCircleLoadingView(Context context) {
        this(context, null);
    }

    public FourCircleLoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);

        setColors(new int[]{0xff1bb7ac,0xff9b92ef,0xffea4642,0xffb7e234});
        setRadius(ScreenUtils.dpAdapt(context, 4));

        ValueAnimator valueAnimatorScale = ValueAnimator.ofFloat(radius*0.5f,radius,radius*0.5f);
        valueAnimatorScale.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimatorScale.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimatorScale.setDuration(4000);
        valueAnimatorScale.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                radius= (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimatorScale.start();

        ValueAnimator valueAnimatorRotation = ValueAnimator.ofFloat(0,360);
        valueAnimatorRotation.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimatorRotation.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimatorRotation.setDuration(4000);
        valueAnimatorRotation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                rotation= (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimatorRotation.start();

        ValueAnimator valueAnimatorTrans = ValueAnimator.ofFloat(0,1,0);
        valueAnimatorTrans.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimatorTrans.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimatorTrans.setDuration(4000);
        valueAnimatorTrans.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                trans = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimatorTrans.start();

        animatorSet = new AnimatorSet();
        animatorSet.playTogether(valueAnimatorScale, valueAnimatorRotation,valueAnimatorTrans); // 同时执行
        animatorSet.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        animatorSet.cancel();
    }

    public void setColors(@ColorInt int[] colors) {
        if (colors.length > 4)
            throw new IllegalArgumentException("The length of colors cannot exceed 4");
        this.colors = colors;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.rotate(rotation,getWidth()*0.5f,getHeight()*0.5f);
        float cx, cy;
        for (int i = 0; i < 4; i++) {
            paint.setColor(colors[i]);
            cx = getWidth() * 0.5f + radius * 2 * (i <= 1 ? -1 : 1)* trans;
            cy = getHeight() * 0.5f + radius * 2 * (i == 0 || i == 3 ? -1 : 1)* trans;
            canvas.drawCircle(cx, cy, radius, paint);
        }
    }
}

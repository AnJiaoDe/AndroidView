package com.cy.androidview.loadingview;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cy.androidview.LogUtils;
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
        colors = new int[4];
//        setColors(new int[]{0xff1bb7ac, 0xff9b92ef, 0xffea4642, 0xffb7e234});
        setRadius(ScreenUtils.dpAdapt(context, 4.5f));

        ValueAnimator valueAnimatorRotation = ValueAnimator.ofFloat(0, 360);
        valueAnimatorRotation.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimatorRotation.setInterpolator(new LinearInterpolator());
        valueAnimatorRotation.setDuration(2500);
        valueAnimatorRotation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                rotation = (float) animation.getAnimatedValue();
                invalidate();
            }
        });

        ValueAnimator valueAnimatorTrans = ValueAnimator.ofFloat(1, 0.5f, 1);
        valueAnimatorTrans.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimatorTrans.setInterpolator(new LinearInterpolator());
        //时间设置为旋转的一半或者更短，才会有心跳的感觉
        valueAnimatorTrans.setDuration(1250);
        valueAnimatorTrans.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                trans = (float) animation.getAnimatedValue();
                invalidate();
            }
        });

        ValueAnimator rainbow = ValueAnimator.ofFloat(0, 360);
        rainbow.setRepeatCount(ValueAnimator.INFINITE);
        //不让颜色变化太快，亮瞎狗眼
        rainbow.setDuration(10000);
        rainbow.setInterpolator(new AccelerateDecelerateInterpolator());
        rainbow.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float globalHue = (float) animation.getAnimatedValue();
                for (int i = 0; i < 4; i++) {
                    float hue = (globalHue + i * 90) % 360;
                    colors[i] = Color.HSVToColor(new float[]{hue, 0.7f, 0.9f});
                }
                invalidate();
            }
        });

        animatorSet = new AnimatorSet();
        animatorSet.playTogether(valueAnimatorRotation, valueAnimatorTrans, rainbow);
    }

    public void startAnim() {
        stopAnim();
        animatorSet.start();
    }

    public void stopAnim() {
        //onAnimationUpdate会立马结束回调
        animatorSet.cancel();
    }

    public boolean isAniming() {
        return animatorSet.isRunning();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnim();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnim();
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE) {
            startAnim();
        } else {
            stopAnim();
        }
    }
//    public void setColors(@ColorInt int[] colors) {
//        if (colors.length != 4)
//            throw new IllegalArgumentException("The length of colors must be 4");
//        this.colors = colors;
//    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.rotate(rotation, getWidth() * 0.5f, getHeight() * 0.5f);
        float cx, cy;
        for (int i = 0; i < 4; i++) {
            paint.setColor(colors[i]);
            cx = getWidth() * 0.5f + radius * 2 * (i <= 1 ? -1 : 1) * trans;
            cy = getHeight() * 0.5f + radius * 2 * (i == 0 || i == 3 ? -1 : 1) * trans;
            canvas.drawCircle(cx, cy, radius, paint);
        }
    }
}

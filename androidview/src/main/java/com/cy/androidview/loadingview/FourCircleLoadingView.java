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
import androidx.annotation.Nullable;

import com.cy.androidview.ScreenUtils;

import java.util.ArrayList;

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

        ValueAnimator valueAnimatorScale = ValueAnimator.ofFloat(radius,1.5f*radius,radius);
        valueAnimatorScale.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimatorScale.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimatorScale.setDuration(2000);
        valueAnimatorScale.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
//                radius= (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimatorScale.start();

        ValueAnimator valueAnimatorRotation = ValueAnimator.ofFloat(0,360);
        valueAnimatorRotation.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimatorRotation.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimatorRotation.setDuration(2000);
        valueAnimatorRotation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                rotation= (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimatorRotation.start();

        ValueAnimator valueAnimatorTrans = ValueAnimator.ofFloat(0.5f,1,0.5f);
        valueAnimatorTrans.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimatorTrans.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimatorTrans.setDuration(2000);
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


public class LuFourBallIndicator extends Indicator {

    public static int ball1Color = Color.WHITE;
    public static int ball2Color = Color.WHITE;
    public static int ball3Color = Color.WHITE;
    public static int ball4Color = Color.WHITE;
    float radius = 0;
    private float degress;
    private float[] centerX = new float[4], centerY = new float[4];
    private int color[] = new int[4];

    private ArrayList<ValueAnimator> mCopyXAnimators = new ArrayList<>();
    private ArrayList<ValueAnimator> mCopyYAnimators = new ArrayList<>();

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.rotate(degress,centerX(),centerY());
        for (int i = 0; i < 4; i++) {
            canvas.save();
            paint.setColor(color[i]);
            canvas.drawCircle(centerX[i], centerY[i], radius, paint);
            canvas.restore();
        }

    }

    public void resetAnimals() {

        stop();

        float startX=getWidth()/4;
        float startY=getWidth()/4;
        radius = startX / 2;

        if (mCopyXAnimators.size() > 0) {
            mCopyXAnimators.get(0).setFloatValues(startX, getWidth()/2 - radius, startX);
            mCopyXAnimators.get(1).setFloatValues(getWidth() - startX, getWidth()/2 + radius, getWidth() - startX);
            mCopyXAnimators.get(2).setFloatValues(getWidth() - startX, getWidth()/2 + radius, getWidth() - startX);
            mCopyXAnimators.get(3).setFloatValues(startX, getWidth()/2 - radius, startX);

            mCopyYAnimators.get(0).setFloatValues(startY, getHeight()/2 - radius, startY);
            mCopyYAnimators.get(1).setFloatValues(startY, getHeight()/2 - radius, startY);
            mCopyYAnimators.get(2).setFloatValues(getHeight() - startY, getHeight()/2 + radius, getHeight() - startY);
            mCopyYAnimators.get(3).setFloatValues(getHeight() - startY, getHeight()/2 + radius, getHeight() - startY);
        }

        start();
    }
    @Override
    public ArrayList<ValueAnimator> onCreateAnimators() {
        ArrayList<ValueAnimator> animators=new ArrayList<>();

        //1、添加自身360度旋转动画
        ValueAnimator rotateAnim=ValueAnimator.ofFloat(0, 360);
        addUpdateListener(rotateAnim,new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                degress= (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        rotateAnim.setDuration(2500);
        rotateAnim.setInterpolator(new LinearInterpolator());//速率是恒定的；
        rotateAnim.setRepeatCount(-1);
        animators.add(rotateAnim);

        mCopyXAnimators.clear();    mCopyYAnimators.clear();
        color[0] = ball1Color;  color[1] = ball2Color;  color[2] = ball3Color;  color[3] = ball4Color;
        float startX=getWidth()/4;
        float startY=getWidth()/4;
        radius = startX / 2;
        //2、添加第一个点的运动动画
        for (int i = 0; i < 4; i++) {
            //三个三角形分别从三个顶点出发
            final int index=i;
            ValueAnimator pointXAnim = ValueAnimator.ofFloat(startX, getWidth()/2 - radius, startX);
            if (i==1){
                pointXAnim = ValueAnimator.ofFloat(getWidth() - startX, getWidth()/2 + radius, getWidth() - startX);
            }
            else if (i==2){
                pointXAnim = ValueAnimator.ofFloat(getWidth() - startX, getWidth()/2 + radius, getWidth() - startX);
            }
            else if (i==3){
                pointXAnim = ValueAnimator.ofFloat(startX, getWidth()/2 - radius, startX);
            }

            ValueAnimator pointYAnim = ValueAnimator.ofFloat(startY, getHeight()/2 - radius, startY);
            if (i==1){
                pointYAnim = ValueAnimator.ofFloat(startY, getHeight()/2 - radius, startY);
            }
            else if (i==2){
                pointYAnim = ValueAnimator.ofFloat(getHeight() - startY, getHeight()/2 + radius, getHeight() - startY);
            }
            else if (i==3){
                pointYAnim = ValueAnimator.ofFloat(getHeight() - startY, getHeight()/2 + radius, getHeight() - startY);
            }

            pointXAnim.setDuration(1200);//动画时长
            pointXAnim.setInterpolator(new LinearInterpolator());//速率是恒定的；
            pointXAnim.setRepeatCount(-1);//无限重复
            addUpdateListener(pointXAnim, new ValueAnimator.AnimatorUpdateListener() {
                //监听X值的变化
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    centerX[index]= (float) animation.getAnimatedValue();
                    postInvalidate();
                }
            });

            pointYAnim.setDuration(1200);//动画时长
            pointYAnim.setInterpolator(new LinearInterpolator());//速率是恒定的；
            pointYAnim.setRepeatCount(-1);//无限重复
            addUpdateListener(pointYAnim, new ValueAnimator.AnimatorUpdateListener() {
                //监听X值的变化
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    centerY[index]= (float) animation.getAnimatedValue();
                    postInvalidate();
                }
            });

            animators.add(pointXAnim);  mCopyXAnimators.add(pointXAnim);
            animators.add(pointYAnim);  mCopyYAnimators.add(pointYAnim);
        }

        return animators;
    }

}


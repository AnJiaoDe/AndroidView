package com.cy.androidview.swipe;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.FloatEvaluator;
import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.telecom.Call;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

import com.cy.androidview.LogUtils;

public class SwipeBackLayout extends FrameLayout {
    private View contentView;
    private float edgeSize = -1;
    private float zoom = 1; // 缩放
    private float zoom_max = 10; // 缩放
    private float zoom_min = 0.1f; // 缩放
    private float translate_x, translate_y;
    private GestureDetector gestureDetector;
    private ScaleGestureDetector scaleGestureDetector;

    public static final int STATE_IDLE = 0;
    public static final int STATE_DRAGGING = 1;
    public static final int STATE_SETTLING = 2;
    private int dragState = STATE_IDLE;
    private boolean finishActivity = false;
    private float start_x;
    private float start_y;
    private float end_x;
    private float end_y;
    private boolean onScaling = false;
    private Activity activity;

    public SwipeBackLayout(@NonNull final Activity activity) {
        super(activity);
        this.activity = activity;
        gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            //注意：多指触摸缩放的时候，这里也会回调
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                translate_x = Math.max(-getWidth(), Math.min(getWidth(), translate_x - distanceX));
                translate_y = Math.max(-getHeight(), Math.min(getHeight(), translate_y - distanceY));
                if (dragState == STATE_IDLE && (Math.abs(translate_x) > edgeSize || Math.abs(translate_y) > edgeSize)) {
                    //convertActivityToTranslucent 这里也是必须得，苟泽GG
                    dragState = TransparentUtils.convertActivityToTranslucent(activity) ? STATE_DRAGGING : STATE_IDLE;
                }
                if (dragState == STATE_DRAGGING) {
                    if (!onScaling) {
                        // 方法一：使用 Math.hypot（推荐）
                        double distance = Math.hypot(Math.abs(translate_x) - getWidth() * 0.5, Math.abs(translate_y) - getHeight() * 0.5);
//                        double distance = Math.sqrt(Math.pow(Math.abs(translate_x) - getWidth() * 0.5, 2) + Math.pow(Math.abs(translate_y) - getHeight() * 0.5, 2));
                        zoom = (float) (distance / Math.hypot(0 - getWidth() * 0.5, 0 - getHeight() * 0.5));
                    }
                    invalidate();
                }
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
        scaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                onScaling = true;
                zoom *= detector.getScaleFactor();
                zoom = Math.max(zoom_min, Math.min(zoom, zoom_max));
                invalidate();
                return true;
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (edgeSize < 0) edgeSize = w * 0.3f;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (contentView == null) return;
        contentView.layout(0, 0, right, bottom);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.scale(zoom, zoom, getWidth() * 1f / 2 + translate_x, getHeight() * 1f / 2 + translate_y);
        //必须先scale后translate，否则会往相反方向translate
        canvas.translate(translate_x, translate_y);
        super.dispatchDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //处理多指触摸，这2个玩意是不能少的，否则贼复杂，还搞不定
        scaleGestureDetector.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                translate_x = 0;
                translate_y = 0;
                dragState = STATE_IDLE;
                onScaling = false;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                if (dragState != STATE_DRAGGING) {
                    translate_x = 0;
                    translate_y = 0;
                }
                start_x = translate_x;
                start_y = translate_y;
                end_x = 0;
                end_y = 0;
                finishActivity = false;
                if (!onScaling && (Math.abs(translate_x) > getWidth() * 0.5f || Math.abs(translate_y) > getWidth() * 0.5f)) {
                    finishActivity = true;
                    if (Math.abs(translate_x) / getWidth() > Math.abs(translate_y) / getHeight()) {
                        end_x = translate_x > 0 ? getWidth() : -getWidth();
                        end_y = translate_y;
                    } else {
                        end_x = translate_x;
                        end_y = translate_y > 0 ? getHeight() : -getHeight();
                    }
                }
                float start;
                float end;
                if (Math.abs(end_x - start_x) > Math.abs(end_y - start_y)) {
                    start = start_x;
                    end = end_x;
                } else {
                    start = start_y;
                    end = end_y;
                }
                final float tx = translate_x;
                final float ty = translate_y;
                final float z = zoom;
                ValueAnimator valueAnimator = ValueAnimator.ofFloat(start, end);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        translate_x = tx + animation.getAnimatedFraction() * (end_x - start_x);
                        translate_y = ty + animation.getAnimatedFraction() * (end_y - start_y);
                        //退出做缩放会导致动画太快，贼丑
                        if (!finishActivity) zoom = z + animation.getAnimatedFraction() * (1 - z);
                        invalidate();
                    }
                });

                valueAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        if (finishActivity) {
                            if (!activity.isFinishing()) {
                                activity.finish();
                                activity.overridePendingTransition(0, 0);
                            }
                        }
                    }
                });
                valueAnimator.setDuration(300);
                valueAnimator.setInterpolator(new DecelerateInterpolator());
                valueAnimator.setEvaluator(new FloatEvaluator());
                valueAnimator.start();
                break;
        }
        //必须true 否则GG
        return true;
    }

    public void attachActivity(Activity activity) {
        TypedArray a = activity.getTheme().obtainStyledAttributes(new int[]{
                android.R.attr.windowBackground
        });
        int background = a.getResourceId(0, 0);
        a.recycle();
        ViewGroup decor = (ViewGroup) activity.getWindow().getDecorView();
        View decorChild = decor.getChildAt(0);
        decorChild.setBackgroundResource(background);
        decor.removeView(decorChild);
        addView(decorChild, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        contentView = decorChild;
        decor.addView(this, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void setEdgeSize(float edgeSize) {
        this.edgeSize = edgeSize;
    }
}

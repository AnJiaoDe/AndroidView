package com.cy.androidview.swipeview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.FloatEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

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
    private VelocityTracker velocityTracker;
    private float maxVelocity;
    private float minVelocity;

    public SwipeBackLayout(@NonNull final Activity activity) {
        super(activity);
        this.activity = activity;
        final ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
        maxVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        minVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            /**
             * 计算两个 MotionEvent 之间的距离（基于 pointer index 0）
             * @param e1 第一个事件
             * @param e2 第二个事件
             * @return 两点之间的欧几里得距离
             */
            private float getDistance(MotionEvent e1, MotionEvent e2) {
                float dx = e2.getX() - e1.getX();
                float dy = e2.getY() - e1.getY();
                return (float) Math.hypot(dx, dy);
            }

            //注意：多指触摸缩放的时候，这里也会回调,e1是down ,e2是move
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if (distanceX < 0 && !canScrollHorizontal(SwipeBackLayout.this, false, 1, e1.getX(), e1.getY())
                        || distanceX > 0 && !canScrollHorizontal(SwipeBackLayout.this, false, -1, e1.getX(), e1.getY())) {
                    translate_x = Math.max(-getWidth(), Math.min(getWidth(), translate_x - distanceX));
                }
                if (distanceY < 0 && !canScrollVertical(SwipeBackLayout.this, false, 1, e1.getX(), e1.getY())
                        || distanceY > 0 && !canScrollVertical(SwipeBackLayout.this, false, -1, e1.getX(), e1.getY())) {
                    translate_y = Math.max(-getHeight(), Math.min(getHeight(), translate_y - distanceY));
                }
                if (dragState == STATE_IDLE && (Math.abs(translate_x) > edgeSize || Math.abs(translate_y) > edgeSize)) {
                    //convertActivityToTranslucent 这里也是必须得，苟泽GG
                    dragState = TransparentUtils.convertActivityToTranslucent(activity) ? STATE_DRAGGING : STATE_IDLE;
                }
                if (dragState == STATE_DRAGGING) {
                    if (!onScaling) {
                        // 方法一：使用 Math.hypot（推荐）
//                        double distance = Math.hypot(Math.abs(translate_x) - getWidth() * 0.5, Math.abs(translate_y) - getHeight() * 0.5);
//                        double distance = Math.hypot(Math.abs(translate_x) - getWidth() * 0.5, Math.abs(translate_y) - getHeight() * 0.5);
//                        double distance = Math.sqrt(Math.pow(Math.abs(translate_x) - getWidth() * 0.5, 2)
//                                + Math.pow(Math.abs(translate_y) - getHeight() * 0.5, 2));
                        zoom = Math.min(1, Math.max(0, 1 - getDistance(e1, e2) / Math.max(getWidth(), getHeight())));
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
        if (edgeSize < 0) edgeSize = w * 0.4f;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (contentView == null) return;
        contentView.layout(0, 0, right, bottom);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.drawColor(0x33000000);
        canvas.scale(zoom, zoom, getWidth() * 1f / 2 + translate_x, getHeight() * 1f / 2 + translate_y);
        //必须先scale后translate，否则会往相反方向translate
        canvas.translate(translate_x, translate_y);
        super.dispatchDraw(canvas);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(event);
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
                velocityTracker.computeCurrentVelocity(1000, maxVelocity);
                final float xvel = clampMag(velocityTracker.getXVelocity(), minVelocity, maxVelocity);
                final float yvel = clampMag(velocityTracker.getYVelocity(), minVelocity, maxVelocity);
                velocityTracker.recycle();
                velocityTracker = null;

                if (dragState != STATE_DRAGGING) {
                    translate_x = 0;
                    translate_y = 0;
                }
                start_x = translate_x;
                start_y = translate_y;
                end_x = 0;
                end_y = 0;
                finishActivity = false;
                if (!onScaling && dragState == STATE_DRAGGING) {
                    if (Math.abs(xvel) > minVelocity + (maxVelocity - minVelocity) * 0.2 || Math.abs(yvel) > minVelocity + (maxVelocity - minVelocity) * 0.2) {
                        finishActivity = true;
                        if (Math.abs(xvel) > Math.abs(yvel)) {
                            end_x = xvel > minVelocity ? getWidth() : -getWidth();
                            end_y = translate_y;
                        } else {
                            end_x = translate_x;
                            end_y = yvel > minVelocity ? getHeight() : -getHeight();
                        }
                    } else if (Math.abs(translate_x) > getWidth() * 0.5f || Math.abs(translate_y) > getWidth() * 0.5f) {
                        finishActivity = true;
                        if (Math.abs(translate_x) / getWidth() > Math.abs(translate_y) / getHeight()) {
                            end_x = translate_x > 0 ? getWidth() : -getWidth();
                            end_y = translate_y;
                        } else {
                            end_x = translate_x;
                            end_y = translate_y > 0 ? getHeight() : -getHeight();
                        }
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
                        dragState = STATE_SETTLING;
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
                        dragState = STATE_IDLE;
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
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_MOVE:
                //处理多指触摸，这2个玩意是不能少的，否则贼复杂，还搞不定
                scaleGestureDetector.onTouchEvent(event);
                gestureDetector.onTouchEvent(event);
                break;
        }
        //必须是true ,否则GG
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

    private boolean canScrollHorizontal(@NonNull View v, boolean checkV, float dx, float x, float y) {
        if (v instanceof ViewGroup) {
            final ViewGroup group = (ViewGroup) v;
            final int scrollX = v.getScrollX();
            final int scrollY = v.getScrollY();
            final int count = group.getChildCount();
            // Count backwards - let topmost views consume scroll distance first.
            for (int i = count - 1; i >= 0; i--) {
                // TODO: Add versioned support here for transformed views.
                // This will not work for transformed views in Honeycomb+
                final View child = group.getChildAt(i);
                if (x + scrollX >= child.getLeft() && x + scrollX < child.getRight()
                        && y + scrollY >= child.getTop() && y + scrollY < child.getBottom()
                        && canScrollHorizontal(child, true, dx, x + scrollX - child.getLeft(),
                        y + scrollY - child.getTop())) {
                    return true;
                }
            }
        }
        return checkV && v.canScrollHorizontally((int) -dx);
    }

    private boolean canScrollVertical(@NonNull View v, boolean checkV, float dy, float x, float y) {
        if (v instanceof ViewGroup) {
            final ViewGroup group = (ViewGroup) v;
            final int scrollX = v.getScrollX();
            final int scrollY = v.getScrollY();
            final int count = group.getChildCount();
            // Count backwards - let topmost views consume scroll distance first.
            for (int i = count - 1; i >= 0; i--) {
                // TODO: Add versioned support here for transformed views.
                // This will not work for transformed views in Honeycomb+
                final View child = group.getChildAt(i);
                if (x + scrollX >= child.getLeft() && x + scrollX < child.getRight()
                        && y + scrollY >= child.getTop() && y + scrollY < child.getBottom()
                        && canScrollVertical(child, true, dy, x + scrollX - child.getLeft(),
                        y + scrollY - child.getTop())) {
                    return true;
                }
            }
        }
        return checkV && v.canScrollVertically((int) -dy);
    }

    private float clampMag(float value, float absMin, float absMax) {
        final float absValue = Math.abs(value);
        if (absValue < absMin) return 0;
        if (absValue > absMax) return value > 0 ? absMax : -absMax;
        return value;
    }

    public void setEdgeSize(float edgeSize) {
        this.edgeSize = edgeSize;
    }
}

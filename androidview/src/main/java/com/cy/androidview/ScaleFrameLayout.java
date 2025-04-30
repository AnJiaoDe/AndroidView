package com.cy.androidview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ScaleFrameLayout extends FrameLayout {
    private float zoom = 1; // 缩放
    private float zoom_max = 10; // 缩放
    private float zoom_min = 0.1f; // 缩放

    private float translate_x, translate_y;

    private boolean enableZoom = true;
    private boolean touchEventDownReturnTrue = true;
    private boolean touchEventUpReset = false;

    private Callback callback;

    private GestureDetector gestureDetector;
    private ScaleGestureDetector scaleGestureDetector;
    private boolean useDoubleTap=true;
    public ScaleFrameLayout(@NonNull Context context) {
        this(context, null);
    }

    public ScaleFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if(!useDoubleTap)return true;
                if (zoom == 1.f) {
                    zoom = 2.f;
                } else {
                    zoom = 1.f;
                    translate_x = 0;
                    translate_y = 0;
                }
                invalidate();
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                translate_x -= distanceX;
                translate_y -= distanceY;
                translate_x = Math.max(-getWidth()*0.5f, Math.min(getWidth()*0.5f, translate_x));
                translate_y = Math.max(-getHeight()*0.5f, Math.min(getHeight()*0.5f, translate_y));
                invalidate();
                return true;
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                if (callback != null) callback.onSingleTapUp();
                return super.onSingleTapUp(e);
            }
        });
        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                if (enableZoom) {
                    zoom *= detector.getScaleFactor();
                    zoom = Math.max(zoom_min, Math.min(zoom, zoom_max));
                    invalidate();
                }
                return true;
            }
        });
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public boolean isUseDoubleTap() {
        return useDoubleTap;
    }

    public void setUseDoubleTap(boolean useDoubleTap) {
        this.useDoubleTap = useDoubleTap;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.scale(zoom, zoom, getWidth() * 1f / 2 + translate_x, getHeight() * 1f / 2 + translate_y);
        //必须先scale后translate，否则会往相反方向translate
        canvas.translate(translate_x, translate_y);
        if (callback != null) callback.onCanvasChange(zoom, translate_x, translate_y);
        super.dispatchDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //处理多指触摸，这2个玩意是不能少的，否则贼复杂，还搞不定
        scaleGestureDetector.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if (touchEventDownReturnTrue) return true;
                break;
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_UP:
                if (touchEventUpReset) {
                    zoom = 1;
                    translate_x = 0;
                    translate_y = 0;
                    invalidate();
                }
                break;
        }
        return super.onTouchEvent(event);
    }


    public float getZoom_max() {
        return zoom_max;
    }

    public void setZoom_max(float zoom_max) {
        this.zoom_max = zoom_max;
    }

    public float getZoom_min() {
        return zoom_min;
    }

    public void setZoom_min(float zoom_min) {
        this.zoom_min = zoom_min;
    }

    public float getZoom() {
        return zoom;
    }

    public float getTranslate_x() {
        return translate_x;
    }

    public float getTranslate_y() {
        return translate_y;
    }

    public boolean isEnableZoom() {
        return enableZoom;
    }

    public void setEnableZoom(boolean enableZoom) {
        this.enableZoom = enableZoom;
    }


    public boolean isTouchEventDownReturnTrue() {
        return touchEventDownReturnTrue;
    }

    public void setTouchEventDownReturnTrue(boolean touchEventDownReturnTrue) {
        this.touchEventDownReturnTrue = touchEventDownReturnTrue;
    }

    public boolean isTouchEventUpReset() {
        return touchEventUpReset;
    }

    public void setTouchEventUpReset(boolean touchEventUpReset) {
        this.touchEventUpReset = touchEventUpReset;
    }

    public static interface Callback {
        public void onCanvasChange(float scale, float dx, float dy);

        public void onSingleTapUp();
    }
}


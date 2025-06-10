package com.cy.androidview.swipe;

import android.content.Context;
import android.graphics.Canvas;
import android.telecom.Call;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

import com.cy.androidview.LogUtils;

public class SwipeBackLayout extends FrameLayout {
    private Callback callback;
    private View contentView;
    private float downX;
    private float downY;
    private float edgeSize = -1;
    private int left_content;
    private int top_content;
    private float zoom = 1; // 缩放
    private float zoom_max = 10; // 缩放
    private float zoom_min = 0.1f; // 缩放
    private boolean enableZoom = true;

    private float translate_x, translate_y;
    private GestureDetector gestureDetector;
    private ScaleGestureDetector scaleGestureDetector;

    public static final int STATE_IDLE = 0;
    public static final int STATE_DRAGGING = 1;
    public static final int STATE_SETTLING = 2;
    private int dragState = STATE_IDLE;

    public SwipeBackLayout(@NonNull Context context) {
        this(context, null);
    }

    public SwipeBackLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//                if(!useScroll)return true;
                translate_x -= distanceX;
                translate_y -= distanceY;
                translate_x = Math.max(-getWidth(), Math.min(getWidth(), translate_x));
                translate_y = Math.max(-getHeight(), Math.min(getHeight(), translate_y));

                if (dragState == STATE_IDLE && translate_x > edgeSize) {
                    dragState = callback != null && callback.convertActivityToTranslucent() ? STATE_DRAGGING : STATE_IDLE;
                }
                if (dragState == STATE_DRAGGING) {
                    invalidate();
                }
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return super.onFling(e1, e2, velocityX, velocityY);
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

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (edgeSize < 0) edgeSize = w * 0.3f;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (contentView == null) return;
        contentView.layout(left_content, top_content, right, bottom);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.scale(zoom, zoom, getWidth() * 1f / 2 + translate_x, getHeight() * 1f / 2 + translate_y);
        //必须先scale后translate，否则会往相反方向translate
        canvas.translate(translate_x, translate_y);
//        if (callback != null) callback.onCanvasChange(zoom, translate_x, translate_y);
        super.dispatchDraw(canvas);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //处理多指触摸，这2个玩意是不能少的，否则贼复杂，还搞不定
        scaleGestureDetector.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                dragState = STATE_IDLE;
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:

                if(callback!=null)callback.onFinishActivity(translate_x,translate_y);
                break;
        }
        //必须true 否则GG
        return true;
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getActionMasked()) {
//            case MotionEvent.ACTION_DOWN:
//            case MotionEvent.ACTION_POINTER_DOWN:
//                downX =event. getX();
//                downY = event. getY();
//                LogUtils.log("ACTION_DOWN",downX);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                float moveX =event.  getX();
//                float moveY = event. getY();
//                if (moveX > downX && moveX - downX >= edgeSize) {
//                    LogUtils.log("ACTION_MOVE",">= edgeSize");
//                    if(callback!=null&&callback.convertActivityToTranslucent()){
//                        left_content = (int) moveX;
//                        requestLayout();
//                    }
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_POINTER_UP:
//            case MotionEvent.ACTION_CANCEL:
//                break;
//        }
//        return true;
//    }

    public void setEdgeSize(float edgeSize) {
        this.edgeSize = edgeSize;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void setContentView(View contentView) {
        this.contentView = contentView;
    }

    public static interface Callback {
        public boolean convertActivityToTranslucent();

        public void onFinishActivity(float dx, float dy);

        public void onDragStateChange(int state);
    }
}

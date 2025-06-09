package com.cy.androidview.swipe;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;

import com.cy.androidview.LogUtils;
import com.cy.androidview.R;
import com.cy.androidview.ScreenUtils;

public class SwipeLayout extends FrameLayout {
    private View contentView;
    private int left_content;
    private int top_content;
    private Drawable drawable_shadow_left;
    private Drawable mShadowRight;
    private Drawable mShadowBottom;
    private final Rect childRect;
    private float downX;
    private static final int SWIPE_THRESHOLD = 200; // 触发返回的滑动距离
    private ViewGroup viewDecor;
    private boolean draging = false;
    private float dx;
    private boolean convertToTransed = false;
    private Callback callback;
    private ViewDragHelper viewDragHelper;
    private int edgeFlag = ViewDragHelper.EDGE_ALL;
    private float scrollPercent;
    private static final float DEFAULT_SCROLL_THRESHOLD = 0.4f;
    private static final int OVERSCROLL_DISTANCE = 10;
    private float scrollFinishThreshold = DEFAULT_SCROLL_THRESHOLD;

    public SwipeLayout(Context context) {
        this(context, null);
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        childRect = new Rect();
        setShadow(R.drawable.shadow_left, 0);
//        setShadow(R.drawable.shadow_right, 0);
//        setShadow(R.drawable.shadow_bottom, 0);
        viewDragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(@NonNull View child, int pointerId) {
                boolean dragEnable = viewDragHelper.isEdgeTouched(edgeFlag, pointerId);
                if(dragEnable&&callback!=null) dragEnable= callback.convertActivityToTranslucent();
                return dragEnable;
            }

            @Override
            public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
                int ret = 0;
//                if ((mCurrentSwipeOrientation & EDGE_LEFT) != 0) {
                ret = Math.min(child.getWidth(), Math.max(left, 0));
//                } else if ((mCurrentSwipeOrientation & EDGE_RIGHT) != 0) {
//                    ret = Math.min(0, Math.max(left, -child.getWidth()));
//                }
                return ret;
            }

            @Override
            public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);

//                if ((mCurrentSwipeOrientation & EDGE_LEFT) != 0) {
                scrollPercent = Math.abs((float) left / (contentView.getWidth() + drawable_shadow_left.getIntrinsicWidth()));
//                } else if ((mCurrentSwipeOrientation & EDGE_RIGHT) != 0) {
//                    scrollPercent = Math.abs((float) left / (mContentView.getWidth() + mShadowRight.getIntrinsicWidth()));
//                }
                float scale = 1 - 0.2f * scrollPercent;
                contentView.setScaleX(scale);
                contentView.setScaleY(scale);
                left_content = left;
                top_content = top;
                LogUtils.log("scrollPercent",scrollPercent);
                requestLayout();
                if (callback != null) callback.onDragScrolled(scrollPercent);
            }

            @Override
            public int getViewHorizontalDragRange(@NonNull View child) {
                return 1;
            }

            @Override
            public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
                final int childWidth = releasedChild.getWidth();
                int left = 0, top = 0;
//                if ((mCurrentSwipeOrientation & EDGE_LEFT) != 0) {
                left = xvel > 0 || xvel == 0 && scrollPercent > scrollFinishThreshold ?
                        (childWidth + drawable_shadow_left.getIntrinsicWidth() + OVERSCROLL_DISTANCE) : 0;
//                } else if ((mCurrentSwipeOrientation & EDGE_RIGHT) != 0) {
//                    left = xvel < 0 || xvel == 0 && mScrollPercent > mScrollFinishThreshold ? -(childWidth
//                            + mShadowRight.getIntrinsicWidth() + OVERSCROLL_DISTANCE) : 0;
//                }
                Log.e("onViewReleased",""+left);
                viewDragHelper.settleCapturedViewAt(left, top);
                invalidate();
            }

            @Override
            public void onViewDragStateChanged(int state) {
                super.onViewDragStateChanged(state);
                if (callback != null) callback.onDragStateChange(state);
            }

            @Override
            public void onEdgeTouched(int edgeFlags, int pointerId) {
                super.onEdgeTouched(edgeFlags, pointerId);
            }
        });
    }

    /**
     * onViewReleased  自动滑动
     */
    @Override
    public void computeScroll() {
//        mScrimOpacity = 1 - mScrollPercent;
        if (viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (contentView != null)
            contentView.layout(left_content, top_content,
                    left_content + contentView.getMeasuredWidth(),
                    top_content + contentView.getMeasuredHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
//        final boolean drawContent = child == mContentView;
        boolean ret = super.drawChild(canvas, child, drawingTime);
        child.getHitRect(childRect);
//        if ((mEdgeFlag & EDGE_LEFT) != 0) {
        drawable_shadow_left.setBounds(childRect.left - drawable_shadow_left.getIntrinsicWidth(), childRect.top,
                childRect.left, childRect.bottom);
        drawable_shadow_left.setAlpha(80);
        drawable_shadow_left.draw(canvas);
        return ret;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
         viewDragHelper.processTouchEvent(event);
         return true;
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        LogUtils.log("onTouchEvent");
//        switch (event.getActionMasked()) {
//            case MotionEvent.ACTION_DOWN:
//            case MotionEvent.ACTION_POINTER_DOWN:
//                downX = event.getX();
//                draging = false;
//                //必须在DOWN事件中设置透明，否则滑动的时候会出现黑屏的情况，GG
//                if (callback != null)
//                    convertToTransed = callback.convertActivityToTranslucent();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                if (!convertToTransed) break;
//                float moveX = event.getX();
//                dx = moveX - downX;
//                if (dx >= SWIPE_THRESHOLD) draging = true;
//                if (draging) {
//                    setLeft_content((int) dx);
//                    requestLayout();
//                }
////                if (deltaX > SWIPE_THRESHOLD) {
////                    finish();
////                    overridePendingTransition(0, R.anim.slide_out_right);
////                }
//                break;
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_POINTER_UP:
//            case MotionEvent.ACTION_CANCEL:
//                if (!convertToTransed) break;
//                if (dx > ScreenUtils.getScreenWidth(getContext()) * 0.5) {
////                    finish();
//                } else {
//                    setLeft_content(0);
//                    requestLayout();
//                }
//                break;
//        }
//        return super.onTouchEvent(event);
//    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void setShadow(int resId, int edgeFlag) {
        setShadow(getResources().getDrawable(resId), edgeFlag);
    }

    public void setShadow(Drawable shadow, int edgeFlag) {
//        if ((edgeFlag & EDGE_LEFT) != 0) {
        drawable_shadow_left = shadow;
//        } else if ((edgeFlag & EDGE_RIGHT) != 0) {
//            mShadowRight = shadow;
//        } else if ((edgeFlag & EDGE_BOTTOM) != 0) {
//            mShadowBottom = shadow;
//        }
        invalidate();
    }

    public void setContentView(View contentView) {
        this.contentView = contentView;
    }

    public void setLeft_content(int left_content) {
        this.left_content = left_content;
    }

    public void setTop_content(int top_content) {
        this.top_content = top_content;
    }

    public static interface Callback {
        public boolean convertActivityToTranslucent();

        public void onDragScrolled(float scrollPercent);

        public void onDragStateChange(int state);
    }
}

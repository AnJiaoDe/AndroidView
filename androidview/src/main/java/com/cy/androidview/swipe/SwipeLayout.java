package com.cy.androidview.swipe;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.customview.widget.ViewDragHelper;

import com.cy.androidview.LogUtils;
import com.cy.androidview.R;

public class SwipeLayout extends FrameLayout {
    private View contentView;
    private int margin_left;
    private int margin_top;
    private Drawable mShadowLeft;
    private Drawable mShadowRight;
    private Drawable mShadowBottom;
    private final Rect childRect;

    public SwipeLayout(Context context) {
        this(context, null);
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        childRect = new Rect();
        setShadow(R.drawable.shadow_left, 0);
        setShadow(R.drawable.shadow_right, 0);
        setShadow(R.drawable.shadow_bottom, 0);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (contentView != null)
            contentView.layout(margin_left, margin_top,
                    margin_left + contentView.getMeasuredWidth(),
                    margin_top + contentView.getMeasuredHeight());
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
        mShadowLeft.setBounds(childRect.left - mShadowLeft.getIntrinsicWidth(), childRect.top,
                childRect.left, childRect.bottom);
//        mShadowLeft.setAlpha((int) (mScrimOpacity * FULL_ALPHA));
        mShadowLeft.draw(canvas);
        return ret;
    }

    public void setShadow(int resId, int edgeFlag) {
        setShadow(getResources().getDrawable(resId), edgeFlag);
    }

    public void setShadow(Drawable shadow, int edgeFlag) {
//        if ((edgeFlag & EDGE_LEFT) != 0) {
        mShadowLeft = shadow;
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

    public void setMargin_left(int margin_left) {
        this.margin_left = margin_left;
    }

    public void setMargin_top(int margin_top) {
        this.margin_top = margin_top;
    }
}

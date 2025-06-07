package com.cy.androidview.swipe;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.cy.androidview.LogUtils;

public class SwipeLayout extends FrameLayout {
    private View contentView;
    private  int margin_left;
    private  int margin_top;
    public SwipeLayout(Context context) {
        this(context,null);
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (contentView != null)
            contentView.layout(margin_left, margin_top,
                    margin_left + contentView.getMeasuredWidth(),
                    margin_top + contentView.getMeasuredHeight());
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

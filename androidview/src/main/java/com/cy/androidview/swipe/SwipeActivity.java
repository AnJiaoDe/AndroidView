package com.cy.androidview.swipe;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.activity.ComponentActivity;
import androidx.annotation.Nullable;

import com.cy.androidview.R;
import com.cy.androidview.ScreenUtils;

public class SwipeActivity extends ComponentActivity {
    private float downX;
    private static final int SWIPE_THRESHOLD = 200; // 触发返回的滑动距离
    private ViewGroup viewDecor;
    private boolean draging = false;
    private float dx;
    private View contentView;
    private SwipeLayout swipeLayout;
    private ViewGroup decorChild;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        swipeLayout = new SwipeLayout(this);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().getDecorView().setBackgroundDrawable(null);
        TransparentUtils.convertActivityToTranslucent(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        TypedArray a = getTheme().obtainStyledAttributes(new int[]{
                android.R.attr.windowBackground
        });
        int background = a.getResourceId(0, 0);
        a.recycle();

        ViewGroup decor = (ViewGroup) getWindow().getDecorView();
        decorChild = (ViewGroup) decor.getChildAt(0);
        decorChild.setBackgroundResource(background);
        decor.removeView(decorChild);
        swipeLayout.addView(decorChild, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        swipeLayout.setContentView(decorChild);
        decor.addView(swipeLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                downX = event.getX();
                draging = false;
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                dx = moveX - downX;
                if (dx >= SWIPE_THRESHOLD) draging = true;
                if (draging){
                    swipeLayout.setMargin_left((int) dx);
                    swipeLayout.requestLayout();
                }
//                if (deltaX > SWIPE_THRESHOLD) {
//                    finish();
//                    overridePendingTransition(0, R.anim.slide_out_right);
//                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                if (dx > ScreenUtils.getScreenWidth(this) * 0.5) {
                    finish();
                } else {
                    swipeLayout.setMargin_left(0);
                    swipeLayout.requestLayout();
                }
                break;
        }
        return super.onTouchEvent(event);
    }
}

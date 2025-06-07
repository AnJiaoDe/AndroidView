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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // 去除标题栏（必须在 setContentView 前）
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置背景透明
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 让状态栏和导航栏也透明
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.setStatusBarColor(Color.TRANSPARENT);

        super.onCreate(savedInstanceState);

        viewDecor = (ViewGroup) getWindow().getDecorView();
        contentView = findViewById(android.R.id.content);
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
                if (draging)
                    contentView.setTranslationX(dx);
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
                    contentView.setTranslationX(0);
                }
                break;
        }
        return super.onTouchEvent(event);
    }
}

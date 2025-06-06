package com.cy.androidview.swipe;

import android.os.Bundle;
import android.view.MotionEvent;

import androidx.activity.ComponentActivity;
import androidx.annotation.Nullable;

import com.cy.androidview.R;

public class SwipeActivity extends ComponentActivity {
    private float downX;
    private static final int SWIPE_THRESHOLD = 200; // 触发返回的滑动距离
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaX = event.getX() - downX;
                if (deltaX > SWIPE_THRESHOLD) {
                    finish();
                    overridePendingTransition(0, R.anim.slide_out_right);
                    return true;
                }
                break;
        }
        return super.onTouchEvent(event);
    }
}

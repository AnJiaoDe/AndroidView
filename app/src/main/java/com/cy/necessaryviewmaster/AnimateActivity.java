package com.cy.necessaryviewmaster;

import android.os.Bundle;

import com.cy.androidview.swipe.SwipeActivity;

public class AnimateActivity extends SwipeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animate);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        overridePendingTransition(0, R.anim.slide_out_right); // 退出动画
    }
}
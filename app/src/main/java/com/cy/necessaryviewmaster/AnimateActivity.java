package com.cy.necessaryviewmaster;

import android.os.Bundle;
import android.view.View;

public class AnimateActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animate);
    }

    @Override
    public void onClick(View v) {

    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        overridePendingTransition(0, R.anim.slide_out_right); // 退出动画
//    }
}
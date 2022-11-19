package com.cy.necessaryviewmaster;

import android.os.Bundle;
import android.view.View;



public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_click).setOnClickListener(this);
        findViewById(R.id.btn_rectangle).setOnClickListener(this);
        findViewById(R.id.btn_shape).setOnClickListener(this);
        findViewById(R.id.btn_card).setOnClickListener(this);
        findViewById(R.id.btn_roundiv).setOnClickListener(this);
        findViewById(R.id.btn_selector).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_click:
                startAppcompatActivity(ClickActivity.class);
                break;
            case R.id.btn_rectangle:
                startAppcompatActivity(RectangleActivity.class);
                break;
            case R.id.btn_shape:
                startAppcompatActivity(ShapeActivity.class);
                break;
            case R.id.btn_card:
                startAppcompatActivity(CardActivity.class);
                break;
            case R.id.btn_roundiv:
                startAppcompatActivity(RoundedIVActivity.class);
                break;
            case R.id.btn_selector:
                startAppcompatActivity(SelectorActivity.class);
                break;
        }
    }
}

package com.cy.necessaryviewmaster;

import android.os.Bundle;
import android.view.View;

public class ShapeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shape);

        findViewById(R.id.rsf).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}

package com.cy.necessaryviewmaster;

import android.os.Bundle;
import android.view.View;

public class RectangleActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rectangle);

        findViewById(R.id.riv).setOnClickListener(this);
        findViewById(R.id.rfl).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}

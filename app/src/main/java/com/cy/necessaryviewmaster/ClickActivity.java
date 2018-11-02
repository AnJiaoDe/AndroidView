package com.cy.necessaryviewmaster;

import android.os.Bundle;
import android.view.View;


public class ClickActivity extends com.cy.necessaryviewmaster.BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click);
        findViewById(R.id.rpfl).setOnClickListener(this);
        findViewById(R.id.rpfl2).setOnClickListener(this);
        findViewById(R.id.civ1).setOnClickListener(this);
        findViewById(R.id.civ2).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}

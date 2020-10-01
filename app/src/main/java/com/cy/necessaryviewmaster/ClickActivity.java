package com.cy.necessaryviewmaster;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


public class ClickActivity extends com.cy.necessaryviewmaster.BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ripple_color_filter);
        findViewById(R.id.rpfl).setOnClickListener(this);
        findViewById(R.id.rpfl2).setOnClickListener(this);
        findViewById(R.id.civ1).setOnClickListener(this);
        findViewById(R.id.civ2).setOnClickListener(this);
        findViewById(R.id.civ3).setOnClickListener(this);
        findViewById(R.id.ivcf1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ClickActivity.this,"点击",Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ClickActivity.this,"点击父view",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View v) {
    }
}

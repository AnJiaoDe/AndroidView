package com.cy.necessaryviewmaster;

import android.os.Bundle;
import android.view.View;

import com.cy.androidview.selectorview.FrameLayoutSelector;
import com.cy.androidview.selectorview.TextViewSelector;

public class SelectorActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector);
        TextViewSelector tvs = findViewById(R.id.tvs);
        tvs.setOnCheckedChangeListener(new TextViewSelector.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(TextViewSelector iv, boolean isChecked, boolean byTouch) {
            LogUtils.log("onCheckedChanged",isChecked+":"+byTouch);
            }
        });
        FrameLayoutSelector fs=findViewById(R.id.fs);
        fs.setOnCheckedChangeListener(new FrameLayoutSelector.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(FrameLayoutSelector iv, boolean isChecked, boolean byTouch) {
                tvs.setChecked(true);
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}

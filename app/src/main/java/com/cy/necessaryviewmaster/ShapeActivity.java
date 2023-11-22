package com.cy.necessaryviewmaster;

import android.graphics.Shader;
import android.os.Bundle;
import android.view.View;

import com.cy.androidview.shapeview.TextViewShape;

public class ShapeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shape);

        findViewById(R.id.rsf).setOnClickListener(this);
        TextViewShape tv_gradient = findViewById(R.id.TextViewShape);
        tv_gradient.getShapeBackground().setGradientColors(
                new int[]{0xffff0000, 0xffffff00, 0xff00ff00, 0xff00ffff, 0xff0000ff, 0xffff00ff}).shape();
    }

    @Override
    public void onClick(View v) {

    }
}

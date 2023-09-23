package com.cy.necessaryviewmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.cy.androidview.shadow.ImageViewShadow;

public class ShadowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shadow);
        ImageViewShadow imageViewShadow =findViewById(R.id.PicShadowView);

        ImageViewShadow imageViewShadow2 =findViewById(R.id.PicShadowView2);
        CheckBox cb_colorFilter=findViewById(R.id.cb_colorFilter);
        cb_colorFilter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                imageViewShadow.setColorFilter(b?Color.RED:-1);
            }
        });
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageViewShadow2.setImageResource(R.drawable.cb_zuji_normal);
            }
        });

    }
}
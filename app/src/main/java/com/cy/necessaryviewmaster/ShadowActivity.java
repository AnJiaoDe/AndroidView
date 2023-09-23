package com.cy.necessaryviewmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.cy.androidview.shadow.PicShadowView;

public class ShadowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shadow);
        PicShadowView picShadowView=findViewById(R.id.PicShadowView);

        PicShadowView picShadowView2=findViewById(R.id.PicShadowView2);
        CheckBox cb_colorFilter=findViewById(R.id.cb_colorFilter);
        cb_colorFilter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                picShadowView.setColorFilter(b?Color.RED:-1);
            }
        });
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picShadowView2.setImageResource(R.drawable.cb_zuji_normal);
            }
        });

    }
}
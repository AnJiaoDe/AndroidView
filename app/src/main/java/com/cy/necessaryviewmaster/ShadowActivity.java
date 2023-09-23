package com.cy.necessaryviewmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.cy.androidview.shadow.PicShadowView;

public class ShadowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shadow);
        PicShadowView picShadowView=findViewById(R.id.PicShadowView);
        picShadowView.setOnCheckedChangeListener(new PicShadowView.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(PicShadowView picShadowView, boolean isChecked) {
                Toast.makeText(ShadowActivity.this, isChecked?"选中":"未选中", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
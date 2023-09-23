package com.cy.necessaryviewmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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
        ViewGroup layout=findViewById(R.id.layout);
        PicShadowView picShadowView2=findViewById(R.id.PicShadowView2);
        picShadowView2.setOnCheckedChangeListener(new PicShadowView.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(PicShadowView picShadowView, boolean isChecked) {
                layout.setVisibility(layout.getVisibility()==View.VISIBLE?View.GONE:View.VISIBLE);
            }
        });
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                layout.setVisibility(layout.getVisibility()==View.VISIBLE?View.GONE:View.VISIBLE);
                picShadowView2.setImageResource(R.drawable.cb_zuji_normal);
                picShadowView2.setImageResourceChecked(R.drawable.after_sale_icon);
            }
        });
    }
}
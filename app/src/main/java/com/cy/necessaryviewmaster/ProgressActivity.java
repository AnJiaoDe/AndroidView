package com.cy.necessaryviewmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.cy.androidview.progress.VCutProgressView;

public class ProgressActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        VCutProgressView cutProgressView=findViewById(R.id.VCutProgressView);
//        cutProgressView.setProgress(70);
    }

    @Override
    public void onClick(View v) {

    }
}
package com.cy.necessaryviewmaster;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cy.androidview.LogUtils;
import com.cy.androidview.swipe.SwipeBackLayout;
import com.cy.androidview.swipe.SwipeLayout;
import com.cy.androidview.swipe.TransparentUtils;


/**
 * Created by lenovo on 2017/4/25.
 */

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    private SwipeBackLayout swipeBackLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //必须，否则GG
        if (TransparentUtils.convertActivityToTranslucent(this))
            swipeBackLayout = new SwipeBackLayout(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (swipeBackLayout != null) swipeBackLayout.attachActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.log("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.log("onPause");
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void showToast(int string_id) {
        Toast.makeText(this, getResources().getString(string_id), Toast.LENGTH_SHORT).show();
    }

    public void startActivity(Class<?> cls) {
        startActivity(new Intent(this, cls));
    }
}

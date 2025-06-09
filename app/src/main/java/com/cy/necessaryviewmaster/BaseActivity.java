package com.cy.necessaryviewmaster;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cy.androidview.swipe.SwipeLayout;
import com.cy.androidview.swipe.TransparentUtils;


/**
 * Created by lenovo on 2017/4/25.
 */

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    private String toast_permission;
    private OnPermissionHaveListener onPermissionHaveListener;
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context_applicaiton;

    private SwipeLayout swipeLayout;
    private ViewGroup decorChild;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("SP", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        context_applicaiton = getApplicationContext();
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().getDecorView().setBackgroundDrawable(null);

        swipeLayout = new SwipeLayout(this);
        swipeLayout.setCallback(new SwipeLayout.Callback() {
            @Override
            public boolean convertActivityToTranslucent() {
                return TransparentUtils.convertActivityToTranslucent(BaseActivity.this);
            }

            @Override
            public void onDragScrolled(float scrollPercent) {
                if (scrollPercent >= 1) {
                    if (!isFinishing()) {
                        finish();
                        overridePendingTransition(0, 0);
                    }
                }
            }

            @Override
            public void onDragStateChange(int state) {

            }
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        TypedArray a = getTheme().obtainStyledAttributes(new int[]{
                android.R.attr.windowBackground
        });
        int background = a.getResourceId(0, 0);
        a.recycle();

        ViewGroup decor = (ViewGroup) getWindow().getDecorView();
        decorChild = (ViewGroup) decor.getChildAt(0);
        decorChild.setBackgroundResource(background);
        decor.removeView(decorChild);
        swipeLayout.addView(decorChild, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        swipeLayout.setContentView(decorChild);
        decor.addView(swipeLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public Context getContext_applicaiton() {
        return context_applicaiton;
    }

    public void setContext_applicaiton(Context context_applicaiton) {
        this.context_applicaiton = context_applicaiton;
    }


    //??????????????????????????????????????????????????????????????????????

    /*
            Acitivity跳转
             */
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void showToast(int string_id) {
        Toast.makeText(this, getResources().getString(string_id), Toast.LENGTH_SHORT).show();
    }


    public void startAppcompatActivity(Class<?> cls) {
        startActivity(new Intent(this, cls));
    }


    public SharedPreferences.Editor getEditor() {
        return editor;
    }

    public void setEditor(SharedPreferences.Editor editor) {
        this.editor = editor;
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }


    public void initData() {
    }
    /*
    状态栏全透明去阴影
     */

    public void setStatusBarTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
    /*
    状态栏隐藏
     */

    public void setStatusBarHide() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
    /*

     */

    public void replaceFragment(int framelayout_id, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(framelayout_id, fragment).commitAllowingStateLoss();
    }


    public Fragment createFragment(int position) {
        return null;
    }

    /*
          6.0权限检查
           */
    public void checkPermission(String[] permission, String toast_permission, OnPermissionHaveListener onPermissionHaveListener) {
        ActivityCompat.requestPermissions(this, permission, 1);
        this.toast_permission = toast_permission;

        this.onPermissionHaveListener = onPermissionHaveListener;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {

                showToast(toast_permission);
                return;
            }
        }
        if (onPermissionHaveListener != null) {
            onPermissionHaveListener.onPermissionHave();
        }


    }

    public interface OnPermissionHaveListener {
        public void onPermissionHave();
    }
}

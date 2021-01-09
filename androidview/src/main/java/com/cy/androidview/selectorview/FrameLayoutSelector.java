package com.cy.androidview.selectorview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cy.androidview.R;

/**
 * @Description:
 * @Author: cy
 * @CreateDate: 2021/1/9 15:14
 * @UpdateUser:
 * @UpdateDate: 2021/1/9 15:14
 * @UpdateRemark:
 * @Version:
 */
public class FrameLayoutSelector extends FrameLayout {
    private int backgroundID, backgroundCheckedID, bg_color, bg_checked_color;

    private OnCheckedChangeListener onCheckedChangeListener;
    private boolean isChecked = false;

    private boolean isMyListener = true;


    public FrameLayoutSelector(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.FrameLayoutSelector);
        backgroundID = arr.getResourceId(R.styleable.ImageViewSelector_cy_bgUnChecked, -1);//未选中时的背景
        backgroundCheckedID = arr.getResourceId(R.styleable.ImageViewSelector_cy_bgChecked, -1);//选中时的背景

        if (backgroundID == -1) {
            bg_color = arr.getColor(R.styleable.ImageViewSelector_cy_bgUnChecked, 0x00000000);//未选中时的背景颜色

        }
        if (backgroundCheckedID == -1) {
            bg_checked_color = arr.getColor(R.styleable.ImageViewSelector_cy_bgChecked, 0x00000000);//选中时的背景颜色

        }
        isChecked = arr.getBoolean(R.styleable.ImageViewSelector_cy_checked, false);//是否选中


        if (isChecked()) {

            setResOnChecked();

        } else {
            setResOnUnChecked();

        }
        arr.recycle();
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("onClick", "+++++++++++++++++");
                setChecked(!isChecked);
                if (onCheckedChangeListener != null) {
                    onCheckedChangeListener.onCheckedChanged(FrameLayoutSelector.this, isChecked);
                }
            }
        });

    }


    @Override
    public void setOnClickListener(OnClickListener l) {
        if (isMyListener) {
            super.setOnClickListener(l);
            isMyListener = false;
        }
    }

    //监听器使用这个方法
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.onCheckedChangeListener = listener;
    }

    public static interface OnCheckedChangeListener {
        void onCheckedChanged(FrameLayoutSelector iv, boolean isChecked);
    }

    //判断是否选中
    public boolean isChecked() {
        return isChecked;
    }

    //设置是否选中
    public void setChecked(boolean checked) {
        isChecked = checked;
        if (checked) {
            setResOnChecked();
        } else {
            setResOnUnChecked();
        }
    }
    //设置选中时的背景，Src等

    private void setResOnChecked() {
        if (backgroundCheckedID != -1) {

            setBackgroundResource(backgroundCheckedID);
        } else {
            setBackgroundColor(bg_checked_color);
        }
    }

    //设置未选中时的背景，Src等
    private void setResOnUnChecked() {
        if (backgroundID != -1) {
            setBackgroundResource(backgroundID);
        } else {
            setBackgroundColor(bg_color);
        }
    }
}

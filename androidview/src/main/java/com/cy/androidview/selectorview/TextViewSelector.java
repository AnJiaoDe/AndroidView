package com.cy.androidview.selectorview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.TextViewCompat;

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
public class TextViewSelector extends AppCompatTextView {
    private int backgroundID, backgroundCheckedID, tvColorUnChecked, tvColorChecked;

    private OnCheckedChangeListener onCheckedChangeListener;
    private boolean isChecked = false;

    private boolean isMyListener = true;


    public TextViewSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.TextViewSelector);
        backgroundID = arr.getResourceId(R.styleable.TextViewSelector_cy_bgUnChecked, -1);//未选中时的背景
        backgroundCheckedID = arr.getResourceId(R.styleable.TextViewSelector_cy_bgChecked, -1);//选中时的背景
        tvColorUnChecked = arr.getColor(R.styleable.TextViewSelector_cy_tvColorUnChecked, -1);//
        tvColorChecked = arr.getColor(R.styleable.TextViewSelector_cy_tvColorChecked, -1);//

//        if (backgroundID == -1) {
//            bg_color = arr.getColor(R.styleable.TextViewSelector_cy_bgUnChecked, 0x00000000);//未选中时的背景颜色
//        }
//        if (backgroundCheckedID == -1) {
//            bg_checked_color = arr.getColor(R.styleable.TextViewSelector_cy_bgChecked, 0x00000000);//选中时的背景颜色
//        }
        isChecked = arr.getBoolean(R.styleable.TextViewSelector_cy_checked, false);//是否选中


        if (isChecked()) {

            setResOnChecked();

        } else {
            setResOnUnChecked();

        }
        arr.recycle();
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setChecked(!isChecked);
                if (onCheckedChangeListener != null) {
                    onCheckedChangeListener.onCheckedChanged(TextViewSelector.this, isChecked);
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
        void onCheckedChanged(TextViewSelector iv, boolean isChecked);
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
        if (backgroundCheckedID != -1)
            setBackgroundResource(backgroundCheckedID);
        if (tvColorChecked != -1)
            setTextColor(tvColorChecked);
    }

    //设置未选中时的背景，Src等
    private void setResOnUnChecked() {
        if (backgroundID != -1)
            setBackgroundResource(backgroundID);
        if (tvColorUnChecked != -1)
            setTextColor(tvColorUnChecked);
    }
}

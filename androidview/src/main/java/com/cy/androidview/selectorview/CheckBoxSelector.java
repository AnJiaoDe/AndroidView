package com.cy.androidview.selectorview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.CompoundButton;

import androidx.appcompat.widget.AppCompatCheckBox;

import com.cy.androidview.R;


/**
 * Created by lenovo on 2017/7/31.
 */

public class CheckBoxSelector extends AppCompatCheckBox {
    private int backgroundID, backgroundCheckedID, bg_color, bg_checked_color,
            textColorID, textColorCheckedID,
            buttonRes, buttonCheckedRes;
    private SelectorOnCheckedChangeListener selectorOnCheckedChangeListener;

    private boolean myListener = true;


    public CheckBoxSelector(Context context, AttributeSet attrs) {
        super(context, attrs);


        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.CheckBoxSelector);

        backgroundID = arr.getResourceId(R.styleable.CheckBoxSelector_cy_backgroundUnChecked, -1);//未选中的背景资源
        backgroundCheckedID = arr.getResourceId(R.styleable.CheckBoxSelector_cy_backgroundChecked, -1);//选中的背景资源

        if (backgroundID == -1) {
            bg_color = arr.getColor(R.styleable.CheckBoxSelector_cy_backgroundUnChecked, 0x00000000);//未选中的背景颜色

        }
        if (backgroundCheckedID == -1) {
            bg_checked_color = arr.getColor(R.styleable.CheckBoxSelector_cy_backgroundChecked, 0x00000000);//选中的背景颜色

        }

        buttonRes = arr.getResourceId(R.styleable.CheckBoxSelector_cy_buttonUnChecked, -1);//未选中的按钮资源
        buttonCheckedRes = arr.getResourceId(R.styleable.CheckBoxSelector_cy_buttonChecked, -1);//选中的按钮资源

        textColorID = arr.getColor(R.styleable.CheckBoxSelector_cy_textColorUnChecked, getCurrentTextColor());//未选中的文字颜色
        textColorCheckedID = arr.getColor(R.styleable.CheckBoxSelector_cy_textColorChecked, getCurrentTextColor());//选中的文字颜色
        arr.recycle();

        if (isChecked()) {

            setResOnChecked();

        } else {
            setResOnUnChecked();

        }

        setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setResOnChecked();
                } else {
                    setResOnUnChecked();
                }
                if (selectorOnCheckedChangeListener != null) {
                    selectorOnCheckedChangeListener.onCheckedChanged(buttonView, isChecked);
                }
            }
        });

    }

    @Override
    public  void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        if (myListener) {
            super.setOnCheckedChangeListener(listener);
            myListener = false;
        }
    }

    //监听器使用这个方法
    public void setSelectorOnCheckedChangeListener(SelectorOnCheckedChangeListener listener) {
        this.selectorOnCheckedChangeListener = listener;
    }

    public static interface SelectorOnCheckedChangeListener extends OnCheckedChangeListener {
        @Override
        void onCheckedChanged(CompoundButton buttonView, boolean isChecked);
    }

    //设置选中时的背景，文字颜色等
    private void setResOnChecked() {
        if (backgroundCheckedID != -1) {

            setBackgroundResource(backgroundCheckedID);
        } else {
            setBackgroundColor(bg_checked_color);
        }
        if (buttonCheckedRes != -1) {
            setButtonDrawable(buttonCheckedRes);
        }

        setTextColor(textColorCheckedID);
    }
    //设置未选中时的背景，文字颜色等

    private void setResOnUnChecked() {
        if (backgroundID != -1) {

            setBackgroundResource(backgroundID);
        } else {
            setBackgroundColor(bg_color);
        }
        if (buttonRes != -1) {
            setButtonDrawable(buttonRes);
        }
        setTextColor(textColorID);
    }
}

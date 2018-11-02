package com.cy.necessaryview.selectorview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.cy.necessaryview.R;


/**
 * Created by lenovo on 2017/8/30.
 */

public class SelectorImageView extends ImageView {
    private int backgroundID, backgroundCheckedID, bg_color, bg_checked_color,
            srcCheckedID, srcUncheckedID;

    private OnCheckedChangeListener onCheckedChangeListener;
    private boolean isChecked = false;

    private boolean isMyListener = true;

    public SelectorImageView(Context context) {
        this(context, null);
    }


    public SelectorImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.SelectorImageView);
        backgroundID = arr.getResourceId(R.styleable.SelectorImageView_bgUnChecked, -1);//未选中时的背景
        backgroundCheckedID = arr.getResourceId(R.styleable.SelectorImageView_bgChecked, -1);//选中时的背景

        if (backgroundID == -1) {
            bg_color = arr.getColor(R.styleable.SelectorImageView_bgUnChecked, 0x00000000);//未选中时的背景颜色

        }
        if (backgroundCheckedID == -1) {
            bg_checked_color = arr.getColor(R.styleable.SelectorImageView_bgChecked, 0x00000000);//选中时的背景颜色

        }

        srcCheckedID = arr.getResourceId(R.styleable.SelectorImageView_srcChecked, -1);//未选中时的src
        srcUncheckedID = arr.getResourceId(R.styleable.SelectorImageView_srcUnChecked, -1);//选中时的src

        isChecked = arr.getBoolean(R.styleable.SelectorImageView_checked, false);//是否选中


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

                    onCheckedChangeListener.onCheckedChanged(SelectorImageView.this, isChecked);
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

    public interface OnCheckedChangeListener {
        void onCheckedChanged(SelectorImageView iv, boolean isChecked);
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
        if (srcCheckedID != -1) {
            setImageResource(srcCheckedID);
        }

    }

    //设置未选中时的背景，Src等
    private void setResOnUnChecked() {
        if (backgroundID != -1) {

            setBackgroundResource(backgroundID);
        } else {
            setBackgroundColor(bg_color);
        }
        if (srcUncheckedID != -1) {
            setImageResource(srcUncheckedID);
        }
    }
}

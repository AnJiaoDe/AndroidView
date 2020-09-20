package com.cy.androidview.rippleview;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.StyleableRes;

import com.cy.androidview.LogUtils;
import com.cy.androidview.R;

/**
 * @Description:
 * @Author: cy
 * @CreateDate: 2020/6/26 18:30
 * @UpdateUser:
 * @UpdateDate: 2020/6/26 18:30
 * @UpdateRemark:
 * @Version: 1.0
 */
public class Ripple {
    private int colorRipple = 0x22000000;
    private boolean havaRipple = true;
    private TypedArray typedArray;
    private View view;

    public Ripple(View view, TypedArray typedArray) {
        this.view = view;
        this.typedArray = typedArray;
        colorRipple = typedArray.getColor(R.styleable.AttrsRipple_cy_colorRipple, colorRipple);
        havaRipple = typedArray.getBoolean(R.styleable.AttrsRipple_cy_haveRipple, true);
    }

    public Ripple setColorRipple_(int color) {
        colorRipple = color;
        return this;
    }


    public Ripple setHavaRipple_(boolean havaRipple) {
        this.havaRipple = havaRipple;
        return this;
    }

    public int getColorRipple() {
        return colorRipple;
    }

    public boolean isHavaRipple() {
        return havaRipple;
    }

    public Ripple ripple() {
        //5.0以上才有效,
        if (havaRipple && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            RippleDrawable rippleDrawable;
            Drawable drawable = view.getBackground();
            if (drawable == null) {
                ShapeDrawable shapeDrawable = new ShapeDrawable();
                shapeDrawable.setAlpha(0);
                shapeDrawable.setBounds(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
                //当控件设置了点击监听器，并且控件点击有效，时，才能产生水波纹
                rippleDrawable = new RippleDrawable(ColorStateList.valueOf(colorRipple), shapeDrawable, null);
            } else {
                rippleDrawable = new RippleDrawable(ColorStateList.valueOf(colorRipple), drawable, null);
            }
            view.setBackground(rippleDrawable);
        }
        return this;
    }

}

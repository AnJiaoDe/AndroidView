package com.cy.androidview.rippleview;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

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
    private int colorRipple = 0x66000000;
    private boolean havaRipple = true;
    private TypedArray typedArray;
    private View view;

    public Ripple(View view, TypedArray typedArray) {
        this.view = view;
        this.typedArray = typedArray;
    }

    public Ripple setColorRipple(@StyleableRes int index) {
        colorRipple = typedArray.getColor(index, colorRipple);
        return this;
    }

    public Ripple setHavaRipple(@StyleableRes int index) {
        havaRipple = typedArray.getBoolean(index, true);
        return this;
    }

    public int getColorRipple() {
        return colorRipple;
    }

    public boolean isHavaRipple() {
        return havaRipple;
    }

//    public Ripple recycle() {
//        try {
//            typedArray.recycle();
//        } catch (Exception e) {
//            Log.e(getClass().getName() + "Exception:", e.getMessage());
//        }
//        return this;
//    }
//
//    public Ripple rippleAndRecycle() {
//        recycle();
//        return ripple();
//    }

    public Ripple ripple() {
        //5.0以上才有效,
        if (havaRipple && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Drawable drawable = view.getBackground();
            //当控件设置了点击监听器，并且控件点击有效，时，才能产生水波纹
            RippleDrawable rippleDrawable = new RippleDrawable(ColorStateList.valueOf(colorRipple), drawable, null);
            view.setBackground(rippleDrawable);
        }
        return this;
    }

}

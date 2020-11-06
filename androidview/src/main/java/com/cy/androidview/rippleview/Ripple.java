package com.cy.androidview.rippleview;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
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
//        if (havaRipple && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            RippleDrawable rippleDrawable;
//            Drawable drawable_bg = view.getBackground();
//            GradientDrawable drawable_mask = null;
//            if (drawable_bg == null) {
//                drawable_bg = new ShapeDrawable();
//                drawable_bg.setAlpha(0);
//                drawable_bg.setBounds(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
//
////                drawable_mask = new GradientDrawable();
////                drawable_mask.setColor(0xffffffff);
////                if(view.getWidth()>view.getHeight()){
////                    drawable_mask.setBounds((int) (view.getLeft()+view.getWidth()*1f/2-view.getHeight()*1f/2),
////                            view.getTop(),
////                            (int) (view.getRight()-view.getWidth()*1f/2+view.getHeight()*1f/2),
////                            view.getBottom());
////                    drawable_mask.setGradientRadius(10);
////                    drawable_mask.setCornerRadius(view.getHeight() * 1f / 2);
////                }else {
////                    drawable_mask.setBounds(view.getLeft(),
////                            (int) (view.getTop()+view.getHeight()*1f/2-view.getWidth()*1f/2),
////                            view.getRight(),
////                            (int) (view.getBottom()-view.getHeight()*1f/2+view.getWidth()*1f/2));
////                    drawable_mask.setCornerRadius(view.getWidth() * 1f / 2);
////                }
////                drawable_mask.setStroke(1, 0xffffffff);
//            }
//            //当控件设置了点击监听器，并且控件点击有效，时，才能产生水波纹
//            rippleDrawable = new RippleDrawable(ColorStateList.valueOf(colorRipple), drawable_bg, drawable_mask);
//
//            view.setBackground(rippleDrawable);
//        }
        return this;
    }

}

package com.cy.androidview.rippleview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.cy.androidview.R;


/**
 * Created by cy on 2018/10/27.
 */

public class LinearLayoutRipple extends LinearLayout implements IRipple {
    private Ripple ripple;
    public LinearLayoutRipple(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LinearLayoutRipple);
        ripple=ripple(typedArray);
        typedArray.recycle();
    }

    @Override
    public Ripple ripple(TypedArray typedArray) {
        return new Ripple(this, typedArray)
                .setColorRipple(R.styleable.LinearLayoutRipple_colorRipple)
                .setHavaRipple(R.styleable.LinearLayoutRipple_haveRipple).ripple();
    }

    @Override
    public Ripple getRipple() {
        return ripple;
    }
}
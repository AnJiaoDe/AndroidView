package com.cy.androidview.rippleview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.cy.androidview.R;


/**
 * Created by cy on 2018/10/27.
 */

public class RelativeLayoutRipple extends RelativeLayout implements IRipple {
    private Ripple ripple;
    public RelativeLayoutRipple(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RelativeLayoutRipple);
        ripple=ripple(typedArray);
        typedArray.recycle();
    }

    @Override
    public Ripple ripple(TypedArray typedArray) {
        return new Ripple(this, typedArray)
                .setColorRipple(R.styleable.RelativeLayoutRipple_colorRipple)
                .setHavaRipple(R.styleable.RelativeLayoutRipple_haveRipple).ripple();
    }

    @Override
    public Ripple getRipple() {
        return ripple;
    }
}
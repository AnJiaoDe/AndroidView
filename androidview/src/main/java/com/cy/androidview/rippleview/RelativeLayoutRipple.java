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
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        ripple.ripple();
    }
    @Override
    public Ripple ripple(TypedArray typedArray) {
        return new Ripple(this, typedArray)
                .setColorRipple(R.styleable.RelativeLayoutRipple_cy_colorRipple)
                .setHavaRipple(R.styleable.RelativeLayoutRipple_cy_haveRipple);
    }

    @Override
    public Ripple getRipple() {
        return ripple;
    }
}
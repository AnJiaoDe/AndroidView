package com.cy.androidview.rippleview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatImageView;

import com.cy.androidview.R;


/**
 * Created by cy on 2018/10/27.
 */

public class ImageViewRipple extends AppCompatImageView implements IRipple {
    public ImageViewRipple(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImageViewRipple);
        ripple(typedArray);
        typedArray.recycle();
    }

    @Override
    public Ripple ripple(TypedArray typedArray) {
        return new Ripple(this, typedArray)
                .setColorRipple(R.styleable.ImageViewRipple_colorRipple)
                .setHavaRipple(R.styleable.ImageViewRipple_haveRipple).ripple();
    }
}
package com.cy.androidview.rippleview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.cy.androidview.R;


/**
 * Created by cy on 2018/10/27.
 */

public class TextViewRipple extends AppCompatTextView implements IRipple {
    public TextViewRipple(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextViewRipple);
        ripple(typedArray);
        typedArray.recycle();
    }

    @Override
    public Ripple ripple(TypedArray typedArray) {
        return new Ripple(this, typedArray)
                .setColorRipple(R.styleable.TextViewRipple_colorRipple)
                .setHavaRipple(R.styleable.TextViewRipple_haveRipple).ripple();
    }
}
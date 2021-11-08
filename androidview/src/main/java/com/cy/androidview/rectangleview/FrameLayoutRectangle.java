package com.cy.androidview.rectangleview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.cy.androidview.R;
import com.cy.androidview.rippleview.IRipple;
import com.cy.androidview.rippleview.Ripple;


/**
 * 正方形的LinearLayout
 *
 * @author Administrator
 */
public class FrameLayoutRectangle extends FrameLayout implements IRectangle, IRipple {
    private RectangleRatio rectangleRatio;
    private Ripple ripple;

    public FrameLayoutRectangle(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AttrsRectangle);
        ripple = ripple(typedArray);
        rectangleRatio = rectangle(typedArray);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int [] specs=rectangleRatio.rectangle(widthMeasureSpec,heightMeasureSpec);
        super.onMeasure(specs[0], specs[1]);
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        ripple.ripple();
    }
    @Override
    public RectangleRatio rectangle(TypedArray typedArray) {
        return new RectangleRatio(this, typedArray);
    }

    @Override
    public Ripple ripple(TypedArray typedArray) {
        return new Ripple(this, typedArray);
    }

    @Override
    public Ripple getRipple() {
        return ripple;
    }

    @Override
    public RectangleRatio getRectangleRatio() {
        return rectangleRatio;
    }
}
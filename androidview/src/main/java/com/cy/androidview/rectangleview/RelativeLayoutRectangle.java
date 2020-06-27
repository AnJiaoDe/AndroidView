package com.cy.androidview.rectangleview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.cy.androidview.R;
import com.cy.androidview.rippleview.IRipple;
import com.cy.androidview.rippleview.RelativeLayoutRipple;
import com.cy.androidview.rippleview.Ripple;


/**
 * 正方形的LinearLayout
 *
 * @author Administrator
 */
public class RelativeLayoutRectangle extends RelativeLayout implements IRectangle, IRipple {
    private RectangleRatio rectangleRatio;

    public RelativeLayoutRectangle(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RelativeLayoutRectangle);
        ripple(typedArray);
        rectangleRatio = rectangle(typedArray);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        rectangleRatio.rectangle(new RectangleRatio.MeasureSizeCallback() {
            @Override
            public void setMeasuredSize(int measuredWidth, int measuredHeight) {
                setMeasuredDimension(measuredWidth, measuredHeight);
            }
        });
    }

    @Override
    public RectangleRatio rectangle(TypedArray typedArray) {
        return new RectangleRatio(this, typedArray)
                .setBaseOnWidthOrHeight(R.styleable.RelativeLayoutRectangle_baseOnWidthOrHeight)
                .setHeightWidthRatio(R.styleable.RelativeLayoutRectangle_heightWidthRatio);
    }

    @Override
    public Ripple ripple(TypedArray typedArray) {
        return new Ripple(this, typedArray)
                .setColorRipple(R.styleable.RelativeLayoutRectangle_colorRipple)
                .setHavaRipple(R.styleable.RelativeLayoutRectangle_haveRipple).ripple();
    }
}
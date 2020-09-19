package com.cy.androidview.shapeview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.cy.androidview.R;
import com.cy.androidview.rectangleview.IRectangle;
import com.cy.androidview.rectangleview.MeasureSizeCallback;
import com.cy.androidview.rectangleview.RectangleRatio;
import com.cy.androidview.rippleview.IRipple;
import com.cy.androidview.rippleview.LinearLayoutRipple;
import com.cy.androidview.rippleview.Ripple;


/**
 * Created by cy on 2018/10/24.
 */

public class LinearLayoutShape extends LinearLayout implements IShape, IRectangle, IRipple {
    private RectangleRatio rectangleRatio;
    private Ripple ripple;
    private ShapeBackground shapeBackground;
    public LinearLayoutShape(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AttrsShape);
        ripple=ripple(typedArray);
        shapeBackground=shape(typedArray);
        rectangleRatio = rectangle(typedArray);
        typedArray.recycle();
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        rectangleRatio.rectangle(new MeasureSizeCallback() {
            @Override
            public void setMeasuredSize(int measuredWidth, int measuredHeight) {
                setMeasuredDimension(measuredWidth, measuredHeight);
                measureChildren(MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(measuredHeight, MeasureSpec.EXACTLY));
            }
        });
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        ripple.ripple();
    }
    @Override
    public RectangleRatio rectangle(TypedArray typedArray) {
        return new RectangleRatio(this,typedArray)
                .setHeightWidthRatio(R.styleable.AttrsShape_cy_heightWidthRatio,0);
    }

    @Override
    public Ripple ripple(TypedArray typedArray) {
        return new Ripple(this, typedArray);
    }

    @Override
    public ShapeBackground shape(TypedArray typedArray) {
        return new ShapeBackground(this,typedArray)
                .shape();
    }

    @Override
    public Ripple getRipple() {
        return ripple;
    }

    @Override
    public RectangleRatio getRectangleRatio() {
        return rectangleRatio;
    }

    @Override
    public ShapeBackground getShapeBackground() {
        return shapeBackground;
    }
}

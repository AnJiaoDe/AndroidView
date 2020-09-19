package com.cy.androidview.shapeview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

import com.cy.androidview.R;
import com.cy.androidview.rectangleview.IRectangle;
import com.cy.androidview.rectangleview.MeasureSizeCallback;
import com.cy.androidview.rectangleview.RectangleRatio;
import com.cy.androidview.rippleview.IRipple;
import com.cy.androidview.rippleview.Ripple;


/**
 * Created by cy on 2018/10/24.
 */

public class ButtonShape extends AppCompatButton implements IShape, IRectangle, IRipple {
    private RectangleRatio rectangleRatio;
    private Ripple ripple;
    private ShapeBackground shapeBackground;
    public ButtonShape(Context context, AttributeSet attrs) {
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
        return new ShapeBackground(this,typedArray).shape();
    }

    @Override
    public Ripple getRipple() {
        return ripple;
    }

    @Override
    public ShapeBackground getShapeBackground() {
        return shapeBackground;
    }

    @Override
    public RectangleRatio getRectangleRatio() {
        return rectangleRatio;
    }
}

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
import com.cy.androidview.rectangleview.RectangleRatio;
import com.cy.androidview.rippleview.IRipple;
import com.cy.androidview.rippleview.LinearLayoutRipple;
import com.cy.androidview.rippleview.Ripple;


/**
 * Created by cy on 2018/10/24.
 */

public class LinearLayoutShape extends LinearLayout implements IShape, IRectangle, IRipple {
    private RectangleRatio rectangleRatio;
    public LinearLayoutShape(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LinearLayoutShape);
        ripple(typedArray);
        shape(typedArray);
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
        return new RectangleRatio(this,typedArray)
                .setBaseOnWidthOrHeight(R.styleable.LinearLayoutShape_baseOnWidthOrHeight)
                .setHeightWidthRatio(R.styleable.LinearLayoutShape_heightWidthRatio,0);
    }

    @Override
    public Ripple ripple(TypedArray typedArray) {
        return new Ripple(this, typedArray)
                .setColorRipple(R.styleable.LinearLayoutShape_colorRipple)
                .setHavaRipple(R.styleable.LinearLayoutShape_haveRipple).ripple();
    }

    @Override
    public ShapeBackground shape(TypedArray typedArray) {
        return new ShapeBackground(this,typedArray)
                .setAngle(R.styleable.LinearLayoutShape_angle)
                .setCenterX(R.styleable.LinearLayoutShape_centerX)
                .setCenterY(R.styleable.LinearLayoutShape_centerY)
                .setColorCenter(R.styleable.LinearLayoutShape_colorCenter)
                .setColorEnd(R.styleable.LinearLayoutShape_colorEnd)
                .setColorFill(R.styleable.LinearLayoutShape_colorFill)
                .setColorStart(R.styleable.LinearLayoutShape_colorStart)
                .setGradientType(R.styleable.LinearLayoutShape_gradientType)
                .setOrientationGradient(R.styleable.LinearLayoutShape_orientationGradient)
                .setRadiusBottomLeft(R.styleable.LinearLayoutShape_radiusBottomLeft)
                .setRadiusBottomRight(R.styleable.LinearLayoutShape_radiusBottomRight)
                .setRadiusCorner(R.styleable.LinearLayoutShape_radiusCorner)
                .setRadiusGradient(R.styleable.LinearLayoutShape_radiusGradient)
                .setRadiusTopLeft(R.styleable.LinearLayoutShape_radiusTopLeft)
                .setRadiusTopRight(R.styleable.LinearLayoutShape_radiusTopRight)
                .setShapeType(R.styleable.LinearLayoutShape_shapeType)
                .setStrokeColor(R.styleable.LinearLayoutShape_strokeColor)
                .setStrokeDashGap(R.styleable.LinearLayoutShape_strokeDashGap)
                .setStrokeDashWidth(R.styleable.LinearLayoutShape_strokeDashWidth)
                .setStrokePaddingBottom(R.styleable.LinearLayoutShape_strokePaddingBottom)
                .setStrokePaddingLeft(R.styleable.LinearLayoutShape_strokePaddingLeft)
                .setStrokePaddingRight(R.styleable.LinearLayoutShape_strokePaddingRight)
                .setStrokePaddingTop(R.styleable.LinearLayoutShape_strokePaddingTop)
                .setStrokeWidth(R.styleable.LinearLayoutShape_strokeWidth)
                .shape();
    }
}

package com.cy.androidview.shapeview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.cy.androidview.R;
import com.cy.androidview.rectangleview.IRectangle;
import com.cy.androidview.rectangleview.RectangleRatio;
import com.cy.androidview.rippleview.FrameLayoutRipple;
import com.cy.androidview.rippleview.IRipple;
import com.cy.androidview.rippleview.Ripple;


/**
 * Created by cy on 2018/10/24.
 */

public class FrameLayoutShape extends FrameLayout implements IShape, IRectangle, IRipple {
    private RectangleRatio rectangleRatio;
    private Ripple ripple;
    private ShapeBackground shapeBackground;
    public FrameLayoutShape(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FrameLayoutShape);
        ripple=ripple(typedArray);
        shapeBackground=shape(typedArray);
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
                .setBaseOnWidthOrHeight(R.styleable.FrameLayoutShape_cy_baseOnWidthOrHeight)
                .setHeightWidthRatio(R.styleable.FrameLayoutShape_cy_heightWidthRatio,0);
    }

    @Override
    public Ripple ripple(TypedArray typedArray) {
        return new Ripple(this, typedArray)
                .setColorRipple(R.styleable.FrameLayoutShape_cy_colorRipple)
                .setHavaRipple(R.styleable.FrameLayoutShape_cy_haveRipple);
    }

    @Override
    public ShapeBackground shape(TypedArray typedArray) {
        return new ShapeBackground(this,typedArray)
                .setAngle(R.styleable.FrameLayoutShape_cy_angle)
                .setCenterX(R.styleable.FrameLayoutShape_cy_centerX)
                .setCenterY(R.styleable.FrameLayoutShape_cy_centerY)
                .setColorCenter(R.styleable.FrameLayoutShape_cy_colorCenter)
                .setColorEnd(R.styleable.FrameLayoutShape_cy_colorEnd)
                .setColorFill(R.styleable.FrameLayoutShape_cy_colorFill)
                .setColorStart(R.styleable.FrameLayoutShape_cy_colorStart)
                .setGradientType(R.styleable.FrameLayoutShape_cy_gradientType)
                .setOrientationGradient(R.styleable.FrameLayoutShape_cy_orientationGradient)
                .setRadiusBottomLeft(R.styleable.FrameLayoutShape_cy_radiusBottomLeft)
                .setRadiusBottomRight(R.styleable.FrameLayoutShape_cy_radiusBottomRight)
                .setRadiusCorner(R.styleable.FrameLayoutShape_cy_radiusCorner)
                .setRadiusGradient(R.styleable.FrameLayoutShape_cy_radiusGradient)
                .setRadiusTopLeft(R.styleable.FrameLayoutShape_cy_radiusTopLeft)
                .setRadiusTopRight(R.styleable.FrameLayoutShape_cy_radiusTopRight)
                .setShapeType(R.styleable.FrameLayoutShape_cy_shapeType)
                .setStrokeColor(R.styleable.FrameLayoutShape_cy_strokeColor)
                .setStrokeDashGap(R.styleable.FrameLayoutShape_cy_strokeDashGap)
                .setStrokeDashWidth(R.styleable.FrameLayoutShape_cy_strokeDashWidth)
                .setStrokePaddingBottom(R.styleable.FrameLayoutShape_cy_strokePaddingBottom)
                .setStrokePaddingLeft(R.styleable.FrameLayoutShape_cy_strokePaddingLeft)
                .setStrokePaddingRight(R.styleable.FrameLayoutShape_cy_strokePaddingRight)
                .setStrokePaddingTop(R.styleable.FrameLayoutShape_cy_strokePaddingTop)
                .setStrokeWidth(R.styleable.FrameLayoutShape_cy_strokeWidth)
                .shape();
    }

    @Override
    public RectangleRatio getRectangleRatio() {
        return rectangleRatio;
    }

    @Override
    public Ripple getRipple() {
        return ripple;
    }

    @Override
    public ShapeBackground getShapeBackground() {
        return shapeBackground;
    }
}

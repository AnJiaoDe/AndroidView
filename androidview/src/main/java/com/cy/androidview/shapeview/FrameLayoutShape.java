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
    public RectangleRatio rectangle(TypedArray typedArray) {
        return new RectangleRatio(this,typedArray)
                .setBaseOnWidthOrHeight(R.styleable.FrameLayoutShape_baseOnWidthOrHeight)
                .setHeightWidthRatio(R.styleable.FrameLayoutShape_heightWidthRatio,0);
    }

    @Override
    public Ripple ripple(TypedArray typedArray) {
        return new Ripple(this, typedArray)
                .setColorRipple(R.styleable.FrameLayoutShape_colorRipple)
                .setHavaRipple(R.styleable.FrameLayoutShape_haveRipple).ripple();
    }

    @Override
    public ShapeBackground shape(TypedArray typedArray) {
        return new ShapeBackground(this,typedArray)
                .setAngle(R.styleable.FrameLayoutShape_angle)
                .setCenterX(R.styleable.FrameLayoutShape_centerX)
                .setCenterY(R.styleable.FrameLayoutShape_centerY)
                .setColorCenter(R.styleable.FrameLayoutShape_colorCenter)
                .setColorEnd(R.styleable.FrameLayoutShape_colorEnd)
                .setColorFill(R.styleable.FrameLayoutShape_colorFill)
                .setColorStart(R.styleable.FrameLayoutShape_colorStart)
                .setGradientType(R.styleable.FrameLayoutShape_gradientType)
                .setOrientationGradient(R.styleable.FrameLayoutShape_orientationGradient)
                .setRadiusBottomLeft(R.styleable.FrameLayoutShape_radiusBottomLeft)
                .setRadiusBottomRight(R.styleable.FrameLayoutShape_radiusBottomRight)
                .setRadiusCorner(R.styleable.FrameLayoutShape_radiusCorner)
                .setRadiusGradient(R.styleable.FrameLayoutShape_radiusGradient)
                .setRadiusTopLeft(R.styleable.FrameLayoutShape_radiusTopLeft)
                .setRadiusTopRight(R.styleable.FrameLayoutShape_radiusTopRight)
                .setShapeType(R.styleable.FrameLayoutShape_shapeType)
                .setStrokeColor(R.styleable.FrameLayoutShape_strokeColor)
                .setStrokeDashGap(R.styleable.FrameLayoutShape_strokeDashGap)
                .setStrokeDashWidth(R.styleable.FrameLayoutShape_strokeDashWidth)
                .setStrokePaddingBottom(R.styleable.FrameLayoutShape_strokePaddingBottom)
                .setStrokePaddingLeft(R.styleable.FrameLayoutShape_strokePaddingLeft)
                .setStrokePaddingRight(R.styleable.FrameLayoutShape_strokePaddingRight)
                .setStrokePaddingTop(R.styleable.FrameLayoutShape_strokePaddingTop)
                .setStrokeWidth(R.styleable.FrameLayoutShape_strokeWidth)
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

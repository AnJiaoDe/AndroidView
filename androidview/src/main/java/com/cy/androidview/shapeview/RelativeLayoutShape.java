package com.cy.androidview.shapeview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.cy.androidview.R;
import com.cy.androidview.rectangleview.IRectangle;
import com.cy.androidview.rectangleview.RectangleRatio;
import com.cy.androidview.rippleview.IRipple;
import com.cy.androidview.rippleview.RelativeLayoutRipple;
import com.cy.androidview.rippleview.Ripple;


/**
 * Created by cy on 2018/10/24.
 */

public class RelativeLayoutShape extends RelativeLayout implements IShape, IRectangle, IRipple {
    private RectangleRatio rectangleRatio;
    private Ripple ripple;
    private ShapeBackground shapeBackground;
    public RelativeLayoutShape(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RelativeLayoutShape);
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
                .setBaseOnWidthOrHeight(R.styleable.RelativeLayoutShape_baseOnWidthOrHeight)
                .setHeightWidthRatio(R.styleable.RelativeLayoutShape_heightWidthRatio,0);
    }

    @Override
    public Ripple ripple(TypedArray typedArray) {
        return new Ripple(this, typedArray)
                .setColorRipple(R.styleable.RelativeLayoutShape_colorRipple)
                .setHavaRipple(R.styleable.RelativeLayoutShape_haveRipple).ripple();
    }

    @Override
    public ShapeBackground shape(TypedArray typedArray) {
        return new ShapeBackground(this,typedArray)
                .setAngle(R.styleable.RelativeLayoutShape_angle)
                .setCenterX(R.styleable.RelativeLayoutShape_centerX)
                .setCenterY(R.styleable.RelativeLayoutShape_centerY)
                .setColorCenter(R.styleable.RelativeLayoutShape_colorCenter)
                .setColorEnd(R.styleable.RelativeLayoutShape_colorEnd)
                .setColorFill(R.styleable.RelativeLayoutShape_colorFill)
                .setColorStart(R.styleable.RelativeLayoutShape_colorStart)
                .setGradientType(R.styleable.RelativeLayoutShape_gradientType)
                .setOrientationGradient(R.styleable.RelativeLayoutShape_orientationGradient)
                .setRadiusBottomLeft(R.styleable.RelativeLayoutShape_radiusBottomLeft)
                .setRadiusBottomRight(R.styleable.RelativeLayoutShape_radiusBottomRight)
                .setRadiusCorner(R.styleable.RelativeLayoutShape_radiusCorner)
                .setRadiusGradient(R.styleable.RelativeLayoutShape_radiusGradient)
                .setRadiusTopLeft(R.styleable.RelativeLayoutShape_radiusTopLeft)
                .setRadiusTopRight(R.styleable.RelativeLayoutShape_radiusTopRight)
                .setShapeType(R.styleable.RelativeLayoutShape_shapeType)
                .setStrokeColor(R.styleable.RelativeLayoutShape_strokeColor)
                .setStrokeDashGap(R.styleable.RelativeLayoutShape_strokeDashGap)
                .setStrokeDashWidth(R.styleable.RelativeLayoutShape_strokeDashWidth)
                .setStrokePaddingBottom(R.styleable.RelativeLayoutShape_strokePaddingBottom)
                .setStrokePaddingLeft(R.styleable.RelativeLayoutShape_strokePaddingLeft)
                .setStrokePaddingRight(R.styleable.RelativeLayoutShape_strokePaddingRight)
                .setStrokePaddingTop(R.styleable.RelativeLayoutShape_strokePaddingTop)
                .setStrokeWidth(R.styleable.RelativeLayoutShape_strokeWidth)
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

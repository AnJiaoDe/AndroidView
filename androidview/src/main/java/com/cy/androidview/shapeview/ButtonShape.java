package com.cy.androidview.shapeview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

import com.cy.androidview.R;
import com.cy.androidview.rectangleview.IRectangle;
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
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ButtonShape);
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
                .setBaseOnWidthOrHeight(R.styleable.ButtonShape_cy_baseOnWidthOrHeight)
                .setHeightWidthRatio(R.styleable.ButtonShape_cy_heightWidthRatio,0);
    }

    @Override
    public Ripple ripple(TypedArray typedArray) {
        return new Ripple(this, typedArray)
                .setColorRipple(R.styleable.ButtonShape_cy_colorRipple)
                .setHavaRipple(R.styleable.ButtonShape_cy_haveRipple);
    }

    @Override
    public ShapeBackground shape(TypedArray typedArray) {
        return new ShapeBackground(this,typedArray)
                .setAngle(R.styleable.ButtonShape_cy_angle)
                .setCenterX(R.styleable.ButtonShape_cy_centerX)
                .setCenterY(R.styleable.ButtonShape_cy_centerY)
                .setColorCenter(R.styleable.ButtonShape_cy_colorCenter)
                .setColorEnd(R.styleable.ButtonShape_cy_colorEnd)
                .setColorFill(R.styleable.ButtonShape_cy_colorFill)
                .setColorStart(R.styleable.ButtonShape_cy_colorStart)
                .setGradientType(R.styleable.ButtonShape_cy_gradientType)
                .setOrientationGradient(R.styleable.ButtonShape_cy_orientationGradient)
                .setRadiusBottomLeft(R.styleable.ButtonShape_cy_radiusBottomLeft)
                .setRadiusBottomRight(R.styleable.ButtonShape_cy_radiusBottomRight)
                .setRadiusCorner(R.styleable.ButtonShape_cy_radiusCorner)
                .setRadiusGradient(R.styleable.ButtonShape_cy_radiusGradient)
                .setRadiusTopLeft(R.styleable.ButtonShape_cy_radiusTopLeft)
                .setRadiusTopRight(R.styleable.ButtonShape_cy_radiusTopRight)
                .setShapeType(R.styleable.ButtonShape_cy_shapeType)
                .setStrokeColor(R.styleable.ButtonShape_cy_strokeColor)
                .setStrokeDashGap(R.styleable.ButtonShape_cy_strokeDashGap)
                .setStrokeDashWidth(R.styleable.ButtonShape_cy_strokeDashWidth)
                .setStrokePaddingBottom(R.styleable.ButtonShape_cy_strokePaddingBottom)
                .setStrokePaddingLeft(R.styleable.ButtonShape_cy_strokePaddingLeft)
                .setStrokePaddingRight(R.styleable.ButtonShape_cy_strokePaddingRight)
                .setStrokePaddingTop(R.styleable.ButtonShape_cy_strokePaddingTop)
                .setStrokeWidth(R.styleable.ButtonShape_cy_strokeWidth)
                .shape();
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

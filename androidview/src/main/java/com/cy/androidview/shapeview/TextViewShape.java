package com.cy.androidview.shapeview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;

import com.cy.androidview.R;
import com.cy.androidview.rectangleview.IRectangle;
import com.cy.androidview.rectangleview.RectangleRatio;
import com.cy.androidview.rippleview.IRipple;
import com.cy.androidview.rippleview.Ripple;
import com.cy.androidview.rippleview.TextViewRipple;


/**
 * Created by cy on 2018/10/24.
 */

public class TextViewShape extends AppCompatTextView implements IShape, IRectangle, IRipple {
    private RectangleRatio rectangleRatio;
    private ShapeBackground shapeBackground;
    private Ripple ripple;
    public TextViewShape(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextViewShape);
        ripple=ripple(typedArray);
        shapeBackground = shape(typedArray);
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
        return new RectangleRatio(this, typedArray)
                .setBaseOnWidthOrHeight(R.styleable.TextViewShape_cy_baseOnWidthOrHeight)
                .setHeightWidthRatio(R.styleable.TextViewShape_cy_heightWidthRatio, 0);
    }

    @Override
    public Ripple ripple(TypedArray typedArray) {
        return new Ripple(this, typedArray)
                .setColorRipple(R.styleable.TextViewShape_cy_colorRipple)
                .setHavaRipple(R.styleable.TextViewShape_cy_haveRipple);
    }

    @Override
    public ShapeBackground shape(TypedArray typedArray) {
        return new ShapeBackground(this, typedArray)
                .setAngle(R.styleable.TextViewShape_cy_angle)
                .setCenterX(R.styleable.TextViewShape_cy_centerX)
                .setCenterY(R.styleable.TextViewShape_cy_centerY)
                .setColorCenter(R.styleable.TextViewShape_cy_colorCenter)
                .setColorEnd(R.styleable.TextViewShape_cy_colorEnd)
                .setColorFill(R.styleable.TextViewShape_cy_colorFill)
                .setColorStart(R.styleable.TextViewShape_cy_colorStart)
                .setGradientType(R.styleable.TextViewShape_cy_gradientType)
                .setOrientationGradient(R.styleable.TextViewShape_cy_orientationGradient)
                .setRadiusBottomLeft(R.styleable.TextViewShape_cy_radiusBottomLeft)
                .setRadiusBottomRight(R.styleable.TextViewShape_cy_radiusBottomRight)
                .setRadiusCorner(R.styleable.TextViewShape_cy_radiusCorner)
                .setRadiusGradient(R.styleable.TextViewShape_cy_radiusGradient)
                .setRadiusTopLeft(R.styleable.TextViewShape_cy_radiusTopLeft)
                .setRadiusTopRight(R.styleable.TextViewShape_cy_radiusTopRight)
                .setShapeType(R.styleable.TextViewShape_cy_shapeType)
                .setStrokeColor(R.styleable.TextViewShape_cy_strokeColor)
                .setStrokeDashGap(R.styleable.TextViewShape_cy_strokeDashGap)
                .setStrokeDashWidth(R.styleable.TextViewShape_cy_strokeDashWidth)
                .setStrokePaddingBottom(R.styleable.TextViewShape_cy_strokePaddingBottom)
                .setStrokePaddingLeft(R.styleable.TextViewShape_cy_strokePaddingLeft)
                .setStrokePaddingRight(R.styleable.TextViewShape_cy_strokePaddingRight)
                .setStrokePaddingTop(R.styleable.TextViewShape_cy_strokePaddingTop)
                .setStrokeWidth(R.styleable.TextViewShape_cy_strokeWidth)
                .shape();
    }

    public ShapeBackground getShapeBackground() {
        return shapeBackground;
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

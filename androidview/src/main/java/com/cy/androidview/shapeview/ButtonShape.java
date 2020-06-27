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
    public ButtonShape(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ButtonShape);
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
                .setBaseOnWidthOrHeight(R.styleable.ButtonShape_baseOnWidthOrHeight)
                .setHeightWidthRatio(R.styleable.ButtonShape_heightWidthRatio);
    }

    @Override
    public Ripple ripple(TypedArray typedArray) {
        return new Ripple(this, typedArray)
                .setColorRipple(R.styleable.ButtonShape_colorRipple)
                .setHavaRipple(R.styleable.ButtonShape_haveRipple).ripple();
    }

    @Override
    public ShapeBackground shape(TypedArray typedArray) {
        return new ShapeBackground(this,typedArray)
                .setAngle(R.styleable.ButtonShape_angle)
                .setCenterX(R.styleable.ButtonShape_centerX)
                .setCenterY(R.styleable.ButtonShape_centerY)
                .setColorCenter(R.styleable.ButtonShape_colorCenter)
                .setColorEnd(R.styleable.ButtonShape_colorEnd)
                .setColorFill(R.styleable.ButtonShape_colorFill)
                .setColorStart(R.styleable.ButtonShape_colorStart)
                .setGradientType(R.styleable.ButtonShape_gradientType)
                .setOrientationGradient(R.styleable.ButtonShape_orientationGradient)
                .setRadiusBottomLeft(R.styleable.ButtonShape_radiusBottomLeft)
                .setRadiusBottomRight(R.styleable.ButtonShape_radiusBottomRight)
                .setRadiusCorner(R.styleable.ButtonShape_radiusCorner)
                .setRadiusGradient(R.styleable.ButtonShape_radiusGradient)
                .setRadiusTopLeft(R.styleable.ButtonShape_radiusTopLeft)
                .setRadiusTopRight(R.styleable.ButtonShape_radiusTopRight)
                .setShapeType(R.styleable.ButtonShape_shapeType)
                .setStrokeColor(R.styleable.ButtonShape_strokeColor)
                .setStrokeDashGap(R.styleable.ButtonShape_strokeDashGap)
                .setStrokeDashWidth(R.styleable.ButtonShape_strokeDashWidth)
                .setStrokePaddingBottom(R.styleable.ButtonShape_strokePaddingBottom)
                .setStrokePaddingLeft(R.styleable.ButtonShape_strokePaddingLeft)
                .setStrokePaddingRight(R.styleable.ButtonShape_strokePaddingRight)
                .setStrokePaddingTop(R.styleable.ButtonShape_strokePaddingTop)
                .setStrokeWidth(R.styleable.ButtonShape_strokeWidth)
                .shape();
    }
}

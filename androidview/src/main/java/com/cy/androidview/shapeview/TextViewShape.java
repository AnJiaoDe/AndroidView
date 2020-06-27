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
    public TextViewShape(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextViewShape);
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
                .setBaseOnWidthOrHeight(R.styleable.TextViewShape_baseOnWidthOrHeight)
                .setHeightWidthRatio(R.styleable.TextViewShape_heightWidthRatio,0);
    }

    @Override
    public Ripple ripple(TypedArray typedArray) {
        return new Ripple(this, typedArray)
                .setColorRipple(R.styleable.TextViewShape_colorRipple)
                .setHavaRipple(R.styleable.TextViewShape_haveRipple).ripple();
    }

    @Override
    public ShapeBackground shape(TypedArray typedArray) {
        return new ShapeBackground(this,typedArray)
                .setAngle(R.styleable.TextViewShape_angle)
                .setCenterX(R.styleable.TextViewShape_centerX)
                .setCenterY(R.styleable.TextViewShape_centerY)
                .setColorCenter(R.styleable.TextViewShape_colorCenter)
                .setColorEnd(R.styleable.TextViewShape_colorEnd)
                .setColorFill(R.styleable.TextViewShape_colorFill)
                .setColorStart(R.styleable.TextViewShape_colorStart)
                .setGradientType(R.styleable.TextViewShape_gradientType)
                .setOrientationGradient(R.styleable.TextViewShape_orientationGradient)
                .setRadiusBottomLeft(R.styleable.TextViewShape_radiusBottomLeft)
                .setRadiusBottomRight(R.styleable.TextViewShape_radiusBottomRight)
                .setRadiusCorner(R.styleable.TextViewShape_radiusCorner)
                .setRadiusGradient(R.styleable.TextViewShape_radiusGradient)
                .setRadiusTopLeft(R.styleable.TextViewShape_radiusTopLeft)
                .setRadiusTopRight(R.styleable.TextViewShape_radiusTopRight)
                .setShapeType(R.styleable.TextViewShape_shapeType)
                .setStrokeColor(R.styleable.TextViewShape_strokeColor)
                .setStrokeDashGap(R.styleable.TextViewShape_strokeDashGap)
                .setStrokeDashWidth(R.styleable.TextViewShape_strokeDashWidth)
                .setStrokePaddingBottom(R.styleable.TextViewShape_strokePaddingBottom)
                .setStrokePaddingLeft(R.styleable.TextViewShape_strokePaddingLeft)
                .setStrokePaddingRight(R.styleable.TextViewShape_strokePaddingRight)
                .setStrokePaddingTop(R.styleable.TextViewShape_strokePaddingTop)
                .setStrokeWidth(R.styleable.TextViewShape_strokeWidth)
                .shape();
    }
}

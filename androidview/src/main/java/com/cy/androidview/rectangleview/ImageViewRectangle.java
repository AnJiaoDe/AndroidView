package com.cy.androidview.rectangleview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.cy.androidview.R;
import com.cy.androidview.rippleview.IRipple;
import com.cy.androidview.rippleview.ImageViewRipple;
import com.cy.androidview.rippleview.Ripple;


/**
 * 正方形的LinearLayout
 *
 * @author Administrator
 */
public class ImageViewRectangle extends AppCompatImageView implements IRectangle, IRipple {
    private RectangleRatio rectangleRatio;

    public ImageViewRectangle(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImageViewRectangle);
        ripple(typedArray);
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
                .setBaseOnWidthOrHeight(R.styleable.ImageViewRectangle_baseOnWidthOrHeight)
                .setHeightWidthRatio(R.styleable.ImageViewRectangle_heightWidthRatio);
    }

    @Override
    public Ripple ripple(TypedArray typedArray) {
        return new Ripple(this, typedArray)
                .setColorRipple(R.styleable.ImageViewRectangle_colorRipple)
                .setHavaRipple(R.styleable.ImageViewRectangle_haveRipple).ripple();
    }
}
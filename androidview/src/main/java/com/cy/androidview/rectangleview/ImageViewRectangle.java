package com.cy.androidview.rectangleview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.cy.androidview.R;
import com.cy.androidview.rippleview.ImageViewClick;


/**
 * 正方形的LinearLayout
 *
 * @author Administrator
 */
public class ImageViewRectangle extends ImageViewClick {
    private float heightWidthRatio = 1; //高 / 宽（默认是高/宽），或者宽/高 比例
    private boolean baseOnWidthOrHeight = true;//默认true，即默认基于宽

    public ImageViewRectangle(Context context) {
        this(context, null);
    }

    public ImageViewRectangle(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.ImageViewRectangle);
        heightWidthRatio = arr.getFloat(R.styleable.ImageViewRectangle_heightWidthRatio, heightWidthRatio);
        baseOnWidthOrHeight = arr.getBoolean(R.styleable.ImageViewRectangle_baseOnWidthOrHeight, baseOnWidthOrHeight);
        arr.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //默认基于宽，即高会和宽度一致，高由宽决定
        if (baseOnWidthOrHeight) {
            int widthSize = getMeasuredWidth();
            setMeasuredDimension(widthSize,(int) (widthSize * heightWidthRatio));
        } else {
            //基于高，即宽度会和高度一致，宽度由高度决定
            int heightSize = getMeasuredHeight();
            setMeasuredDimension((int) (heightSize * heightWidthRatio),heightSize);
        }
    }


}

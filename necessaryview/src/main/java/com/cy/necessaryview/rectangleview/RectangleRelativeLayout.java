package com.cy.necessaryview.rectangleview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.cy.necessaryview.R;
import com.cy.necessaryview.rippleview.ClickRelativeLayout;


/**
 * 正方形的LinearLayout
 *
 * @author Administrator
 */
public class RectangleRelativeLayout extends ClickRelativeLayout {
    private float heightWidthRatio = 1; //高 / 宽（默认是高/宽），或者宽/高 比例
    private boolean baseOnWidthOrHeight = true;//默认true，即默认基于宽

    public RectangleRelativeLayout(Context context) {
        this(context, null);
    }

    public RectangleRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.RectangleRelativeLayout);
        heightWidthRatio = arr.getFloat(R.styleable.RectangleRelativeLayout_heightWidthRatio, 1F);
        baseOnWidthOrHeight = arr.getBoolean(R.styleable.RectangleRelativeLayout_baseOnWidthOrHeight, true);
        arr.recycle();

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //默认基于宽，即高会和宽度一致，高由宽决定
        if (baseOnWidthOrHeight) {

            int childWidthSize = getMeasuredWidth();
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (childWidthSize * heightWidthRatio), MeasureSpec.EXACTLY);
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
        } else {

            //基于高，即宽度会和高度一致，宽度由高度决定
            int childHeightSize = getMeasuredHeight();
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeightSize, MeasureSpec.EXACTLY);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec((int) (childHeightSize * heightWidthRatio), MeasureSpec.EXACTLY);
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
        }
    }


}

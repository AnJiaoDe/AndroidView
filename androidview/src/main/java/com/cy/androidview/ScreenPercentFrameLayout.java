package com.cy.androidview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.cy.androidview.rippleview.FrameLayoutRipple;

/**
 * @Description:
 * @Author: cy
 * @CreateDate: 2020/9/5 17:20
 * @UpdateUser:
 * @UpdateDate: 2020/9/5 17:20
 * @UpdateRemark:
 * @Version:
 */
public class ScreenPercentFrameLayout extends FrameLayout {
    private float width_percent=0;
    private float height_percent=0;

    public ScreenPercentFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray=context.obtainStyledAttributes(attrs,R.styleable.ScreenPercentFrameLayout);
        width_percent=typedArray.getFloat(R.styleable.ScreenPercentFrameLayout_cy_width_percent,width_percent);
        height_percent=typedArray.getFloat(R.styleable.ScreenPercentFrameLayout_cy_height_percent,height_percent);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth=width_percent==0?getMeasuredWidth():(int)(ScreenUtils.getScreenWidth(getContext())*width_percent);
        int measuredHeigh= height_percent==0?getMeasuredHeight(): (int) (ScreenUtils.getScreenHeight(getContext()) * height_percent);
        setMeasuredDimension(measuredWidth,measuredHeigh);
        measureChildren(MeasureSpec.makeMeasureSpec(measuredWidth,MeasureSpec.EXACTLY),MeasureSpec.makeMeasureSpec(measuredHeigh,MeasureSpec.EXACTLY));
    }
}

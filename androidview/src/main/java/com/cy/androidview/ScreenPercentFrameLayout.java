package com.cy.androidview;

import android.content.Context;
import android.util.AttributeSet;

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
public class ScreenPercentFrameLayout extends FrameLayoutRipple {
    private float width_percent=1f;
    private float height_percent=0;
    public ScreenPercentFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth=(int)(ScreenUtils.getScreenWidth(getContext())*width_percent);
        int measuredHeigh= height_percent==0?MeasureSpec.getSize(heightMeasureSpec): (int) (ScreenUtils.getScreenHeight(getContext()) * height_percent);
        setMeasuredDimension(measuredWidth,measuredHeigh);
    }
}

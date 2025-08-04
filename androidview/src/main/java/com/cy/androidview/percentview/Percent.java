package com.cy.androidview.percentview;

import android.content.res.TypedArray;
import android.view.View;

import com.cy.androidview.R;
import com.cy.androidview.ScreenUtils;

public class Percent {
    private float width_percent = 0;
    private float height_percent = 0;
    private TypedArray typedArray;
    private View view;

    public Percent(View view, TypedArray typedArray) {
        this.view = view;
        this.typedArray = typedArray;
        width_percent = typedArray.getFloat(R.styleable.PercentFrameLayout_cy_width_percent, width_percent);
        height_percent = typedArray.getFloat(R.styleable.PercentFrameLayout_cy_height_percent, height_percent);
    }

    public float getWidth_percent() {
        return width_percent;
    }

    public Percent setWidth_percent(float width_percent) {
        this.width_percent = width_percent;
        return this;
    }

    public float getHeight_percent() {
        return height_percent;
    }

    public Percent setHeight_percent(float height_percent) {
        this.height_percent = height_percent;
        return this;
    }

    public int[] percent(int widthMeasureSpec, int heightMeasureSpec) {
        int[] widthHeightMeasureSpecs = new int[]{widthMeasureSpec, heightMeasureSpec};
        widthHeightMeasureSpecs[0] = width_percent > 0 ?
                View.MeasureSpec.makeMeasureSpec((int) (ScreenUtils.getScreenWidth(view.getContext()) * width_percent), View.MeasureSpec.EXACTLY)
                : widthMeasureSpec;
        widthHeightMeasureSpecs[1] = height_percent > 0 ?
                View.MeasureSpec.makeMeasureSpec((int) (ScreenUtils.getScreenHeight(view.getContext()) * height_percent), View.MeasureSpec.EXACTLY)
                : heightMeasureSpec;
        return widthHeightMeasureSpecs;
    }
}
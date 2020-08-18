package com.cy.androidview.rectangleview;

import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.StyleableRes;

import com.cy.androidview.rippleview.Ripple;

/**
 * @Description:
 * @Author: cy
 * @CreateDate: 2020/6/26 22:11
 * @UpdateUser:
 * @UpdateDate: 2020/6/26 22:11
 * @UpdateRemark:
 * @Version: 1.0
 */
public class RectangleRatio {
    private float heightWidthRatio = 1; //高 / 宽（默认是高/宽），或者宽/高 比例
    private boolean baseOnWidthOrHeight = true;//默认true，即默认基于宽
    private TypedArray typedArray;
    private View view;

    public RectangleRatio(View view, TypedArray typedArray) {
        this.view = view;
        this.typedArray = typedArray;
    }

    public RectangleRatio setHeightWidthRatio(@StyleableRes int index) {
        heightWidthRatio = typedArray.getFloat(index, heightWidthRatio);
        return this;
    }

    public RectangleRatio setHeightWidthRatio_(float heightWidthRatio) {
        this.heightWidthRatio = heightWidthRatio;
        return this;
    }

    public RectangleRatio setHeightWidthRatio(@StyleableRes int index,int defaultValue) {
        heightWidthRatio = typedArray.getFloat(index, defaultValue);
        return this;
    }

    public RectangleRatio setBaseOnWidthOrHeight(@StyleableRes int index) {
        baseOnWidthOrHeight = typedArray.getBoolean(index, baseOnWidthOrHeight);
        return this;
    }

    public RectangleRatio setBaseOnWidthOrHeight_(boolean baseOnWidthOrHeight) {
        this.baseOnWidthOrHeight = baseOnWidthOrHeight;
        return this;
    }

    public float getHeightWidthRatio() {
        return heightWidthRatio;
    }

    public boolean isBaseOnWidthOrHeight() {
        return baseOnWidthOrHeight;
    }

    public RectangleRatio rectangle(MeasureSizeCallback measureSizeCallback) {
        if (heightWidthRatio == 0) return this;
        //默认基于宽，即高会和宽度一致，高由宽决定
        if (baseOnWidthOrHeight) {
            int widthSize = view.getMeasuredWidth();
            measureSizeCallback.setMeasuredSize(widthSize, (int) (widthSize * heightWidthRatio));
        } else {
            //基于高，即宽度会和高度一致，宽度由高度决定
            int heightSize = view.getMeasuredHeight();
            measureSizeCallback.setMeasuredSize((int) (heightSize * heightWidthRatio), heightSize);
        }
        return this;
    }

    public static interface MeasureSizeCallback {
        public void setMeasuredSize(int measuredWidth, int measuredHeight);
    }

}

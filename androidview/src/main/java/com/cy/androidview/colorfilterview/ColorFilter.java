package com.cy.androidview.colorfilterview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.StyleableRes;

import com.cy.androidview.LogUtils;
import com.cy.androidview.R;
import com.cy.androidview.rippleview.Ripple;

/**
 * @Description:
 * @Author: cy
 * @CreateDate: 2020/6/26 19:36
 * @UpdateUser:
 * @UpdateDate: 2020/6/26 19:36
 * @UpdateRemark:
 * @Version: 1.0
 */
public class ColorFilter {

    //正数，变亮
//    public float[] BT_SELECTED_LIGHT = new float[]{
//            1, 0, 0, 0, 30,
//            0, 1, 0, 0, 30,
//            0, 0, 1, 0, 30,
//            0, 0, 0, 1, 0};
//    //恢复
//    public float[] BT_NOT_SELECTED = new float[]{
//            1, 0, 0, 0, 0,
//            0, 1, 0, 0, 0,
//            0, 0, 1, 0, 0,
//            0, 0, 0, 1, 0};

    private TypedArray typedArray;
    private View view;
    private boolean havaFilter = true;
    private boolean lightOrDark = false;
    private float lightNumber=-50;
    //负数，变暗(三个-30，值越大则效果越深)
    private float[] filters = new float[]{
            1, 0, 0, 0, 0,
            0, 1, 0, 0, 0,
            0, 0, 1, 0, 0,
            0, 0, 0, 1, 0};
    public ColorFilter(View view,TypedArray typedArray) {
        this.view = view;
        this.typedArray = typedArray;
    }

    public ColorFilter setHavaFilter(@StyleableRes int index) {
        havaFilter = typedArray.getBoolean(index, havaFilter);//设置是否有滤镜点击效果，默认有
        return this;
    }


    /**
     * //设置滤镜，变亮还是变暗，默认变暗
     *
     * @param index
     * @return
     */
    public ColorFilter setLightOrDark(@StyleableRes int index) {
        lightOrDark = typedArray.getBoolean(index, lightOrDark);
        return this;
    }

    public ColorFilter setLightNumber(@StyleableRes int index) {
        lightNumber = typedArray.getFloat(index, lightOrDark?-lightNumber:lightNumber);
        return this;
    }

    public boolean isHavaFilter() {
        return havaFilter;
    }


    public boolean isLightOrDark() {
        return lightOrDark;
    }

    public float getLightNumber() {
        return lightNumber;
    }

    public float[] getFilters() {
        return filters;
    }

//    public ColorFilter recycle() {
//        try {
//            typedArray.recycle();
//        } catch (Exception e) {
//            Log.e(getClass().getName() + "Exception:", e.getMessage());
//        }
//        return this;
//    }
//
//    public ColorFilter colorFilterAndRecycle() {
//        recycle();
//        return colorFilter();
//    }

    public ColorFilter colorFilter() {
        if(!havaFilter)return this;
        //滤镜，变暗或者变亮，负数，变暗(值越大则效果越深)，正数，变亮
        filters[4] = lightNumber;
        filters[9] = lightNumber;
        filters[14] = lightNumber;
        return this;
    }
}

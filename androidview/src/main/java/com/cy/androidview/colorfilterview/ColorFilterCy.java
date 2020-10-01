package com.cy.androidview.colorfilterview;

import android.content.res.TypedArray;
import android.graphics.ColorMatrixColorFilter;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.StyleableRes;

/**
 * @Description:
 * @Author: cy
 * @CreateDate: 2020/6/26 19:36
 * @UpdateUser:
 * @UpdateDate: 2020/6/26 19:36
 * @UpdateRemark:
 * @Version: 1.0
 */
public class ColorFilterCy {

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
    private ImageView imageView;
    private boolean havaFilter = true;
    private boolean lightOrDark = false;
    private float lightNumber = -50;
    //负数，变暗(三个-30，值越大则效果越深)
    private float[] filters = new float[]{
            1, 0, 0, 0, 0,
            0, 1, 0, 0, 0,
            0, 0, 1, 0, 0,
            0, 0, 0, 1, 0};

    public ColorFilterCy(ImageView imageView, TypedArray typedArray) {
        this.imageView = imageView;
        this.typedArray = typedArray;
    }

    public ColorFilterCy setHavaFilter(@StyleableRes int index) {
        havaFilter = typedArray.getBoolean(index, havaFilter);//设置是否有滤镜点击效果，默认有
        return this;
    }

    public ColorFilterCy setHavaFilter_(boolean havaFilter) {
        this.havaFilter = havaFilter;//设置是否有滤镜点击效果，默认有
        return this;
    }


    /**
     * //设置滤镜，变亮还是变暗，默认变暗
     *
     * @param index
     * @return
     */
    public ColorFilterCy setLightOrDark(@StyleableRes int index) {
        lightOrDark = typedArray.getBoolean(index, lightOrDark);
        return this;
    }
    public ColorFilterCy setLightOrDark_(boolean lightOrDark) {
        this.lightOrDark=lightOrDark;
        return this;
    }

    public ColorFilterCy setLightNumber(@StyleableRes int index) {
        lightNumber = typedArray.getFloat(index, lightOrDark ? -lightNumber : lightNumber);
        return this;
    }
    public ColorFilterCy setLightNumber_(float lightNumber) {
       this.lightNumber=lightNumber;
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


    public ColorFilterCy colorFilter(MotionEvent event) {
        if (!havaFilter) return this;
        //滤镜，变暗或者变亮，负数，变暗(值越大则效果越深)，正数，变亮
        filters[4] = lightNumber;
        filters[9] = lightNumber;
        filters[14] = lightNumber;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                    imageView.setColorFilter(new ColorMatrixColorFilter(filters));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            imageView.clearColorFilter();
                        }
                    }, 100);
                break;
        }
        return this;
    }
}

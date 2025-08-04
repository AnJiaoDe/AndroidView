package com.cy.androidview.percentview;


import android.content.res.TypedArray;

import com.cy.androidview.rectangleview.RectangleRatio;

/**
 * @Description:
 * @Author: cy
 * @CreateDate: 2020/6/26 19:24
 * @UpdateUser:
 * @UpdateDate: 2020/6/26 19:24
 * @UpdateRemark:
 * @Version: 1.0
 */
public interface IPercent {
    public Percent percent(TypedArray typedArray);
    public Percent getPercent();
}

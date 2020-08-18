package com.cy.androidview.shapeview;

import android.content.res.TypedArray;
import android.graphics.drawable.shapes.Shape;

import com.cy.androidview.rippleview.Ripple;

/**
 * @Description:
 * @Author: cy
 * @CreateDate: 2020/6/26 22:58
 * @UpdateUser:
 * @UpdateDate: 2020/6/26 22:58
 * @UpdateRemark:
 * @Version: 1.0
 */
public interface IShape {
    public ShapeBackground shape(TypedArray typedArray);
    public ShapeBackground getShapeBackground();
}

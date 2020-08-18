package com.cy.androidview.rippleview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

/**
 * @Description:
 * @Author: cy
 * @CreateDate: 2020/6/26 18:30
 * @UpdateUser:
 * @UpdateDate: 2020/6/26 18:30
 * @UpdateRemark:
 * @Version: 1.0
 */
public interface IRipple {
    public Ripple ripple(TypedArray typedArray);
    public Ripple getRipple();
}

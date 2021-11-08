package com.cy.androidview.rectangleview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.ColorMatrixColorFilter;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.cy.androidview.R;
import com.cy.androidview.ViewMeasureUtils;
import com.cy.androidview.colorfilterview.ColorFilterCy;
import com.cy.androidview.colorfilterview.IColorFilter;


/**
 * 正方形的LinearLayout
 *
 * @author Administrator
 */
public class ImageViewRectangle extends AppCompatImageView implements IRectangle {
    private RectangleRatio rectangleRatio;
//    private ColorFilterCy colorFilter;
    public ImageViewRectangle(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AttrsRectangle);
//        colorFilter=colorFilter(typedArray);
        rectangleRatio = rectangle(typedArray);
        typedArray.recycle();
    }
    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        int[] specs=rectangleRatio.rectangle(widthMeasureSpec,heightMeasureSpec);
        super.onMeasure(specs[0],specs[1]);
        //不要调用setMeasuredDimension，否则会导致子View的测量不灵，GG
    }
    @Override
    public RectangleRatio rectangle(TypedArray typedArray) {
        return new RectangleRatio(this,typedArray);
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        colorFilter.colorFilter(event);
//        return super.onTouchEvent(event);
//    }

//    @Override
//    public ColorFilterCy colorFilter(TypedArray typedArray) {
//        return new ColorFilterCy(this, typedArray)
//                .setHavaFilter(R.styleable.ImageViewColorFilter_cy_haveFilter)
//                .setLightOrDark(R.styleable.ImageViewColorFilter_cy_lightOrDark)
//                .setLightNumber(R.styleable.ImageViewColorFilter_cy_lightNumber);
//    }

    @Override
    public RectangleRatio getRectangleRatio() {
        return rectangleRatio;
    }

//    @Override
//    public ColorFilterCy getColorFilterCy() {
//        return colorFilter;
//    }
}

package com.cy.androidview.rectangleview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.ColorMatrixColorFilter;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.cy.androidview.R;
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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        rectangleRatio.rectangle(new MeasureSizeCallback() {
            @Override
            public void setMeasuredSize(int measuredWidth, int measuredHeight) {
                setMeasuredDimension(measuredWidth, measuredHeight);
            }
        });
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

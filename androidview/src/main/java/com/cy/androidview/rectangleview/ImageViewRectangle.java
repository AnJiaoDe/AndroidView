package com.cy.androidview.rectangleview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.ColorMatrixColorFilter;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.cy.androidview.R;
import com.cy.androidview.colorfilterview.ColorFilter;
import com.cy.androidview.colorfilterview.IColorFilter;
import com.cy.androidview.rippleview.IRipple;
import com.cy.androidview.rippleview.ImageViewRipple;
import com.cy.androidview.rippleview.Ripple;


/**
 * 正方形的LinearLayout
 *
 * @author Administrator
 */
public class ImageViewRectangle extends AppCompatImageView implements IRectangle, IColorFilter {
    private RectangleRatio rectangleRatio;
    private ColorFilter colorFilter;
    private OnClickListener onClickListener;
    public ImageViewRectangle(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImageViewRectangle);
        colorFilter=colorFilter(typedArray);
        rectangleRatio = rectangle(typedArray);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        rectangleRatio.rectangle(new RectangleRatio.MeasureSizeCallback() {
            @Override
            public void setMeasuredSize(int measuredWidth, int measuredHeight) {
                setMeasuredDimension(measuredWidth, measuredHeight);
            }
        });
    }

    @Override
    public RectangleRatio rectangle(TypedArray typedArray) {
        return new RectangleRatio(this,typedArray)
                .setBaseOnWidthOrHeight(R.styleable.ImageViewRectangle_baseOnWidthOrHeight)
                .setHeightWidthRatio(R.styleable.ImageViewRectangle_heightWidthRatio);
    }
    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        onClickListener = l;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (colorFilter.isHavaFilter())
                    setColorFilter(new ColorMatrixColorFilter(colorFilter.getFilters()));
                return true;
            case MotionEvent.ACTION_UP:
                if (onClickListener != null) onClickListener.onClick(this);
                clearColorFilter();
                return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public ColorFilter colorFilter(TypedArray typedArray) {
        return new ColorFilter(this, typedArray)
                .setHavaFilter(R.styleable.ImageViewColorFilter_haveFilter)
                .setLightOrDark(R.styleable.ImageViewColorFilter_lightOrDark)
                .setLightNumber(R.styleable.ImageViewColorFilter_lightNumber)
                .colorFilter();
    }
}

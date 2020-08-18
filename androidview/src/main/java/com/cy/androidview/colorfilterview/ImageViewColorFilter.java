package com.cy.androidview.colorfilterview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.ColorMatrixColorFilter;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.cy.androidview.R;


/**
 * Created by cy on 2018/10/27.
 */

public class ImageViewColorFilter extends AppCompatImageView implements IColorFilter {

    private ColorFilterCy colorFilter;
    private OnClickListener onClickListener;

    public ImageViewColorFilter(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImageViewColorFilter);
        colorFilter = colorFilter(typedArray);
        typedArray.recycle();
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
    public ColorFilterCy colorFilter(TypedArray typedArray) {
        return new ColorFilterCy(this, typedArray)
                .setHavaFilter(R.styleable.ImageViewColorFilter_haveFilter)
                .setLightOrDark(R.styleable.ImageViewColorFilter_lightOrDark)
                .setLightNumber(R.styleable.ImageViewColorFilter_lightNumber)
                .colorFilter();
    }

    @Override
    public ColorFilterCy getColorFilterCy() {
        return colorFilter;
    }
}

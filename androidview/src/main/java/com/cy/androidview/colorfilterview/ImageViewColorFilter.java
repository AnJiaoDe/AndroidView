package com.cy.androidview.colorfilterview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.ColorMatrixColorFilter;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.cy.androidview.R;


/**
 * Created by cy on 2018/10/27.
 */

public class ImageViewColorFilter extends AppCompatImageView implements IColorFilter {

    private ColorFilterCy colorFilter;

    public ImageViewColorFilter(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImageViewColorFilter);
        colorFilter = colorFilter(typedArray);
        typedArray.recycle();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (colorFilter.isHavaFilter()){
                    setColorFilter(new ColorMatrixColorFilter(colorFilter.getFilters()));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            clearColorFilter();
                        }
                    }, 100);
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public ColorFilterCy colorFilter(TypedArray typedArray) {
        return new ColorFilterCy(this, typedArray)
                .setHavaFilter(R.styleable.ImageViewColorFilter_cy_haveFilter)
                .setLightOrDark(R.styleable.ImageViewColorFilter_cy_lightOrDark)
                .setLightNumber(R.styleable.ImageViewColorFilter_cy_lightNumber)
                .colorFilter();
    }

    @Override
    public ColorFilterCy getColorFilterCy() {
        return colorFilter;
    }
}

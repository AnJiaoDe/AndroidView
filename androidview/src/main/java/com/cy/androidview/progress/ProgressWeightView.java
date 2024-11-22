package com.cy.androidview.progress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.cy.androidview.R;
import com.cy.androidview.ScreenUtils;

public class ProgressWeightView extends View {
    private Paint paintBg, paintFg;
    private int radius;
    private float progress;

    public ProgressWeightView(Context context) {
        this(context, null);
    }

    public ProgressWeightView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paintFg = new Paint();
        paintFg.setAntiAlias(true);

        paintBg = new Paint();
        paintBg.setAntiAlias(true);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressWeightView);
        setBgColor(typedArray.getColor(R.styleable.ProgressWeightView_cy_color_bg, 0x33ffffff));
        setFgColor(typedArray.getColor(R.styleable.ProgressWeightView_cy_color_fg, 0xff2a83fc));
        setRadius(typedArray.getDimensionPixelSize(R.styleable.ProgressWeightView_cy_radiusCorner, ScreenUtils.dpAdapt(context, 10)));
        typedArray.recycle();
    }

    public ProgressWeightView setBgColor(int color) {
        paintBg.setColor(color);
        return this;
    }

    public ProgressWeightView setFgColor(int color) {
        paintFg.setColor(color);
        return this;
    }

    public ProgressWeightView setRadius(int radius) {
        this.radius = radius;
        return this;
    }

    public ProgressWeightView setProgress(float progress){
        this.progress=Math.min(1,Math.max(0,progress));
        invalidate();
        return this;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRoundRect(0, 0, getWidth(), getHeight(), radius, radius, paintBg);
        canvas.drawRoundRect(0, 0, progress*getWidth(), getHeight(), radius, radius, paintFg);
    }
}

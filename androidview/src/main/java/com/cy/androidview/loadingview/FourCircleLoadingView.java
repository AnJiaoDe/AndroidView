package com.cy.androidview.loadingview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import com.cy.androidview.ScreenUtils;

public class FourCircleLoadingView extends View {
    private Paint paint;
    private int[] colors;
    private float radius;

    public FourCircleLoadingView(Context context) {
        this(context, null);
    }

    public FourCircleLoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);

        setColors(new int[]{Color.RED, Color.GREEN, Color.BLUE, Color.BLACK});
        setRadius(ScreenUtils.dpAdapt(context, 4));
    }

    public void setColors(@ColorInt int[] colors) {
        if (colors.length > 4)
            throw new IllegalArgumentException("The length of colors cannot exceed 4");
        this.colors = colors;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float cx, cy;
        for (int i = 0; i < 4; i++) {
            paint.setColor(colors[i]);
            cx = getWidth() * 0.5f + radius * 2 * (i <= 1 ? -1 : 1);
            cy = getHeight() * 0.5f + radius * 2 * (i == 0 || i == 3 ? -1 : 1);
            canvas.drawCircle(cx, cy, radius, paint);
        }
    }
}

package com.cy.androidview.progress;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

import com.cy.androidview.R;

public class RingProgressBarView extends View {

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF rectF = new RectF();

    private int progress = 0;
    private int max = 100;

    private int progressColor = 0xFF48C9B0;
    private int backgroundColor = 0xFFEAEAEA;
    private int textColor = 0xFF333333;

    private float strokeWidth;
    private float textSize;

    public RingProgressBarView(Context context) {
        this(context, null);
    }

    public RingProgressBarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RingProgressBarView(Context context,
                               @Nullable AttributeSet attrs,
                               int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        strokeWidth = dp(8);
        textSize = sp(20);

        if (attrs != null) {
            android.content.res.TypedArray a =
                    context.obtainStyledAttributes(
                            attrs,
                            R.styleable.RingProgressBarView,
                            defStyleAttr,
                            0
                    );

            progress = a.getInt(
                    R.styleable.RingProgressBarView_cy_ringProgress, 0);

            max = a.getInt(
                    R.styleable.RingProgressBarView_cy_ringMax, 100);

            progressColor = a.getColor(
                    R.styleable.RingProgressBarView_cy_ringProgressColor,
                    progressColor);

            backgroundColor = a.getColor(
                    R.styleable.RingProgressBarView_cy_ringBackgroundColor,
                    backgroundColor);

            strokeWidth = a.getDimension(
                    R.styleable.RingProgressBarView_cy_ringStrokeWidth,
                    strokeWidth);

            textColor = a.getColor(
                    R.styleable.RingProgressBarView_cy_ringTextColor,
                    textColor);

            textSize = a.getDimension(
                    R.styleable.RingProgressBarView_cy_ringTextSize,
                    textSize);

            a.recycle();
        }

        if (max <= 0) max = 100;
        progress = Math.max(0, Math.min(progress, max));

        paint.setStrokeWidth(strokeWidth);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.STROKE);
    }

    private float dp(float value) {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                value,
                getResources().getDisplayMetrics());
    }

    private float sp(float value) {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                value,
                getResources().getDisplayMetrics());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float cx = getWidth() / 2f;
        float cy = getHeight() / 2f;
        float radius = Math.min(cx, cy) - strokeWidth / 2f;

        if (radius <= 0) return;

        rectF.set(cx - radius, cy - radius,
                cx + radius, cy + radius);

        // 背景圆环
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(backgroundColor);
        canvas.drawArc(rectF, 0, 360, false, paint);

        // 进度圆环
        paint.setColor(progressColor);
        canvas.drawArc(
                rectF, -90,
                360f * progress / max,
                false, paint);

        // 中间进度文字
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.CENTER);

        Paint.FontMetrics fm = paint.getFontMetrics();
        float baseline = cy - (fm.ascent + fm.descent) / 2f;

        canvas.drawText(
                Math.round(progress * 100f / max) + "%",
                cx, baseline, paint);
    }

    public void setProgress(int progress) {
        this.progress = Math.max(0, Math.min(progress, max));
        invalidate();
    }

    public int getProgress() {
        return progress;
    }

    public void setMax(int max) {
        if (max <= 0) return;
        this.max = max;
        progress = Math.min(progress, max);
        invalidate();
    }
}
package com.cy.androidview.progress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.Layout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.cy.androidview.R;

public class ProgressTextView extends AppCompatTextView {

    private final Paint bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF rectF = new RectF();

    private int progress;
    private int maxProgress;

    private int progressColor;
    private int bgColor;

    private int leftTextColor;
    private int rightTextColor;

    private float cornerRadius;

    public ProgressTextView(Context context) {
        this(context, null);
    }

    public ProgressTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressTextView(Context context,
                            @Nullable AttributeSet attrs,
                            int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(
                attrs,
                R.styleable.ProgressTextView);

        progress = ta.getInt(
                R.styleable.ProgressTextView_cy_progress,
                0);

        maxProgress = ta.getInt(
                R.styleable.ProgressTextView_cy_maxProgress,
                100);

        progressColor = ta.getColor(
                R.styleable.ProgressTextView_cy_progressColor,
                Color.parseColor("#2EA7F7"));

        bgColor = ta.getColor(
                R.styleable.ProgressTextView_cy_bgColor,
                Color.WHITE);

        leftTextColor = ta.getColor(
                R.styleable.ProgressTextView_cy_leftTextColor,
                Color.WHITE);

        rightTextColor = ta.getColor(
                R.styleable.ProgressTextView_cy_rightTextColor,
                Color.parseColor("#2EA7F7"));

        cornerRadius = ta.getDimension(
                R.styleable.ProgressTextView_cy_cornerRadius,
                dp2px(20));

        ta.recycle();

        setIncludeFontPadding(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        float width = getWidth();
        float height = getHeight();

        float progressWidth =
                width * progress / (float) maxProgress;

        //----------------------------------
        // 背景
        //----------------------------------

        rectF.set(0, 0, width, height);

        bgPaint.setColor(bgColor);

        canvas.drawRoundRect(
                rectF,
                cornerRadius,
                cornerRadius,
                bgPaint);

        //----------------------------------
        // 当前进度(独立胶囊)
        //----------------------------------

        if (progress > 0) {

            float pw = progressWidth;

            // 防止宽度太小时圆角异常
            pw = Math.max(
                    pw,
                    Math.min(cornerRadius * 2f, width));

            pw = Math.min(pw, width);

            bgPaint.setColor(progressColor);

            RectF progressRect = new RectF(
                    0,
                    0,
                    pw,
                    height);

            canvas.drawRoundRect(
                    progressRect,
                    cornerRadius,
                    cornerRadius,
                    bgPaint);

            progressWidth = pw;
        }

        //----------------------------------
        // 文字
        //----------------------------------

        drawProgressText(canvas, progressWidth);
    }

    private void drawProgressText(
            Canvas canvas,
            float progressWidth) {

        CharSequence text = getText();

        if (text == null || text.length() == 0) {
            return;
        }

        TextPaint paint = getPaint();

        int oldColor = paint.getColor();

        String str = text.toString();

        Paint.FontMetrics fm =
                paint.getFontMetrics();

        float textWidth =
                paint.measureText(str);

        float x;
        float y;

        int gravity = getGravity();

        //----------------------------------
        // 水平位置
        //----------------------------------

        switch (gravity & Gravity.HORIZONTAL_GRAVITY_MASK) {

            case Gravity.CENTER_HORIZONTAL:
                x = (getWidth() - textWidth) / 2f;
                break;

            case Gravity.RIGHT:
                x = getWidth()
                        - getPaddingRight()
                        - textWidth;
                break;

            default:
                x = getPaddingLeft();
                break;
        }

        //----------------------------------
        // 垂直位置
        //----------------------------------

        switch (gravity & Gravity.VERTICAL_GRAVITY_MASK) {

            case Gravity.CENTER_VERTICAL:
                y = getHeight() / 2f
                        - (fm.ascent + fm.descent) / 2f;
                break;

            case Gravity.BOTTOM:
                y = getHeight()
                        - getPaddingBottom()
                        - fm.descent;
                break;

            default:
                y = getPaddingTop()
                        - fm.ascent;
                break;
        }

        //----------------------------------
        // 左侧颜色
        //----------------------------------

        canvas.save();

        canvas.clipRect(
                0,
                0,
                progressWidth,
                getHeight());

        paint.setColor(leftTextColor);

        canvas.drawText(
                str,
                x,
                y,
                paint);

        canvas.restore();

        //----------------------------------
        // 右侧颜色
        //----------------------------------

        canvas.save();

        canvas.clipRect(
                progressWidth,
                0,
                getWidth(),
                getHeight());

        paint.setColor(rightTextColor);

        canvas.drawText(
                str,
                x,
                y,
                paint);

        canvas.restore();

        paint.setColor(oldColor);
    }
    public void setProgress(int progress) {

        if (progress < 0) {
            progress = 0;
        }

        if (progress > maxProgress) {
            progress = maxProgress;
        }

        this.progress = progress;

        invalidate();
    }

    public int getProgress() {
        return progress;
    }

    public void setMaxProgress(int maxProgress) {

        if (maxProgress <= 0) {
            maxProgress = 1;
        }

        this.maxProgress = maxProgress;

        invalidate();
    }

    public void setProgressColor(int color) {
        this.progressColor = color;
        invalidate();
    }

    public void setBgColor(int color) {
        this.bgColor = color;
        invalidate();
    }

    public void setLeftTextColor(int color) {
        this.leftTextColor = color;
        invalidate();
    }

    public void setRightTextColor(int color) {
        this.rightTextColor = color;
        invalidate();
    }

    public void setCornerRadius(float radius) {
        this.cornerRadius = radius;
        invalidate();
    }

    private float dp2px(float dp) {
        return dp * getResources()
                .getDisplayMetrics()
                .density;
    }
}
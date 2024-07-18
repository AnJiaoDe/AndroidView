package com.cy.androidview.sticker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import com.cy.androidview.ScreenUtils;
import com.cy.androidview.TimeAndAgeUtils;
import com.cy.androidview.VersionUtils;

public class WatermarkStickerView extends StickerView {
    private RectF rectF_text;
    private RectF rectF_time;
    private Paint paint;
    private boolean showTime = true;
    private String date = "";
    private float centerX_time, centerY_time;
    private float centerX_text, centerY_text;
    private String text = "";
    private String text_appName = "";
    private String text_product = "";
    private String text_text = "";
    private float onLineHeight;
    private float margin;
    private boolean show_watermark = true;
    private Thread thread;

    public WatermarkStickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setColor(Color.WHITE);
        paint.setTextSize(ScreenUtils.sp2px(context, ScreenUtils.spAdapt(context, 10)));
        paint.setShadowLayer(1, 1, 1, Color.BLACK);

        text_appName = VersionUtils.getAppName(context);
        text_product = Build.BRAND + "    " + Build.PRODUCT;
        margin = ScreenUtils.dpAdapt(context, 10);

    }

    public WatermarkStickerView setTextSize(float px) {
        paint.setTextSize(px);
        return this;
    }

    public boolean isShowTime() {
        return showTime;
    }

    public WatermarkStickerView setShowTime(boolean showTime) {
        this.showTime = showTime;
        time_invalidate();
        return this;
    }

    public void setMargin(float margin) {
        this.margin = margin;
    }

    public void setText_product(String text_product) {
        this.text_product = text_product;
    }

    public void setText_text(String text_text) {
        this.text_text = text_text;
    }

    public boolean isShow_watermark() {
        return show_watermark;
    }

    public void setShow_watermark(boolean show_watermark) {
        this.show_watermark = show_watermark;
    }

    public WatermarkStickerView setDate(String date) {
        this.date = date;
        return this;
    }
    public WatermarkStickerView setShadowLayer(float radius, float dx, float dy, @ColorInt int shadowColor) {
        paint.setShadowLayer(radius, dx, dy, shadowColor);
        return this;
    }
    private void time_invalidate() {
        if (showTime) {
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!thread.isInterrupted()) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                        }
                        invalidate();
                    }
                }
            });
            thread.start();
        } else {
            if (thread != null) thread.interrupt();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        time_invalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (thread != null) thread.interrupt();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!show_watermark) return;
        onLineHeight = TextUtils.getTextHeightOneLine(paint);

        text = text_appName
                + (android.text.TextUtils.isEmpty(text_product) ? "" : "\n")
                + text_product
                + (android.text.TextUtils.isEmpty(text_text) ? "" : "\n")
                + text_text;
        centerX_text = margin + TextUtils.getTextWidth(false, 0, text, paint) * 0.5f;
        centerY_text = getHeight() - margin - onLineHeight * text.split("\n").length * 0.5f;
        rectF_text = TextUtils.getTextRectF(false, 0, paint, text, centerX_text, centerY_text);
        TextUtils.drawText(false, 0, canvas, paint, text, centerX_text, centerY_text, rectF_text);
        if (showTime) {
            date = TimeAndAgeUtils.timeStamp2DateAll(TimeAndAgeUtils.getCureentTimeStamp());
            centerX_time = getWidth() - margin - TextUtils.getTextWidthOneLine(false, date, paint) * 0.5f;
            centerY_time = getHeight() - margin - onLineHeight * 0.5f;
            rectF_time = TextUtils.getTextRectF(false, 0, paint, date, centerX_time, centerY_time);
            TextUtils.drawText(false, 0, canvas, paint, date, centerX_time, centerY_time, rectF_time);
        }
    }
}

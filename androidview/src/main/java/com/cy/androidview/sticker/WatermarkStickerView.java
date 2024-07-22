package com.cy.androidview.sticker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
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
    private boolean show_time = true;
    private String date = "";
    private float centerX_time, centerY_time;
    private float centerX_text, centerY_text;
    private String text = "";
    private String text_appName_default = "";
    private String text_appName = "";
    private String text_product_default = "";
    private String text_product = "";
    private String text_text = "";
    private float onLineHeight;
    private float margin;
    private Thread thread;

    public WatermarkStickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setColor(Color.WHITE);
        paint.setTextSize(ScreenUtils.sp2px(context, ScreenUtils.spAdapt(context, 10)));
        setTypeface("", Typeface.BOLD);
        paint.setShadowLayer(1, 1, 1, Color.BLACK);

        text_appName_default = VersionUtils.getAppName(context);
        text_product_default = Build.BRAND + "    " + Build.PRODUCT;

        text_appName = text_appName_default;
        text_product = text_product_default;
        margin = ScreenUtils.dpAdapt(context, 10);
    }

    public WatermarkStickerView setTextSize(float sp) {
        sp = Math.max(8, Math.min(200, sp));
        paint.setTextSize(ScreenUtils.sp2px(getContext(), ScreenUtils.spAdapt(getContext(), sp)));
        return this;
    }
    public WatermarkStickerView setTextColor(int color) {
        paint.setColor(color);
        return this;
    }

    public boolean isShowTime() {
        return show_time;
    }

    public WatermarkStickerView setShowTime(boolean showTime) {
        this.show_time = showTime;
        time_invalidate();
        return this;
    }

    public WatermarkStickerView setTypeface(String pathFont, int style) {
        if (android.text.TextUtils.isEmpty(pathFont)) {
            paint.setTypeface(Typeface.defaultFromStyle(style));
            return this;
        }
        paint.setTypeface(
                Typeface.create(Typeface.createFromFile(pathFont), style));
        return this;
    }

    public WatermarkStickerView setMargin(float margin) {
        this.margin = margin;
        return this;
    }

    public WatermarkStickerView setText_appName(String text_appName) {
        this.text_appName = text_appName;
        return this;
    }

    public WatermarkStickerView setText_product(String text_product) {
        this.text_product = text_product;
        return this;
    }

    public WatermarkStickerView setText_text(String text_text) {
        this.text_text = text_text;
        return this;
    }

    public boolean isShow_time() {
        return show_time;
    }

    public String getDate() {
        return date;
    }

    public String getText_appName() {
        return text_appName;
    }

    public String getText_product() {
        return text_product;
    }

    public String getText_text() {
        return text_text;
    }

    public float getMargin() {
        return margin;
    }

    public String getText_appName_default() {
        return text_appName_default;
    }

    public String getText_product_default() {
        return text_product_default;
    }

    public boolean haveWatermark() {
        return show_time
                || !android.text.TextUtils.isEmpty(text_appName)
                || !android.text.TextUtils.isEmpty(text_product)
                || !android.text.TextUtils.isEmpty(text_text);
    }

    public WatermarkStickerView setDate(String date) {
        this.date = date;
        return this;
    }

    public WatermarkStickerView setShadowLayer(float radius, float dx, float dy, @ColorInt int shadowColor) {
        paint.setShadowLayer(radius, dx, dy, shadowColor);
        return this;
    }

    public Paint getPaint() {
        return paint;
    }

    private void time_invalidate() {
        if (show_time) {
            if (thread != null) thread.interrupt();
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!thread.isInterrupted()) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                        }
                        date = TimeAndAgeUtils.timeStamp2DateAll(TimeAndAgeUtils.getCureentTimeStamp());
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
        drawWatermark(canvas);
    }

    public void drawWatermark(Canvas canvas) {
        if (!haveWatermark()) return;

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
        if (show_time) {
            centerX_time = getWidth() - margin - TextUtils.getTextWidthOneLine(false, date, paint) * 0.5f;
            centerY_time = getHeight() - margin - onLineHeight * 0.5f;
            rectF_time = TextUtils.getTextRectF(false, 0, paint, date, centerX_time, centerY_time);
            TextUtils.drawText(false, 0, canvas, paint, date, centerX_time, centerY_time, rectF_time);
        }
    }
}

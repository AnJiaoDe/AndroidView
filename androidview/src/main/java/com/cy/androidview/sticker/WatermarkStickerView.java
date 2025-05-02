package com.cy.androidview.sticker;

import android.content.Context;
import android.graphics.BlurMaskFilter;
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
    private boolean show_time = false;
    private String date = "";
    private float centerX_time, centerY_time;
    private float centerX_text, centerY_text;
    private String text = "";
    private String text_appName_default = "";
    private String text_appName = "";
    private String text_product_default = "";
    private String text_product = "";
    private String text_text = "";
    private float margin_x;
    private float margin_y;
    private Thread thread;
    private float textSizeSp;

    public WatermarkStickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setColor(Color.WHITE);
        setTextSizeSp(12);
        setTypeface("", Typeface.BOLD);
        paint.setShadowLayer(1, 1, 1, Color.BLACK);

        text_appName_default = VersionUtils.getAppName(context);
        text_product_default = Build.BRAND + "    " + Build.PRODUCT;

        text_appName = text_appName_default;
        setMargin_x(ScreenUtils.dpAdapt(context, 10));
        setMargin_y(ScreenUtils.dpAdapt(context, 10));
    }

    /**
     * 注意：只是改变水印的文字大小，不会改变非水印文字的大小
     *
     * @param sp
     * @return
     */
    public WatermarkStickerView setTextSizeSp(float sp) {
//        sp = Math.max(10, Math.min(200, sp));
        this.textSizeSp = sp;
        paint.setTextSize(ScreenUtils.spAdapt(getContext(), sp));
        return this;
    }

    public float getTextSizeSp() {
        return textSizeSp;
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

    public float getMargin_x() {
        return margin_x;
    }

    /**
     * 距离图片左边
     *
     * @param margin_x
     * @return
     */
    public WatermarkStickerView setMargin_x(float margin_x) {
        this.margin_x = margin_x;
        return this;
    }

    public float getMargin_y() {
        return margin_y;
    }

    /**
     * 距离图片底部
     *
     * @param margin_y
     * @return
     */
    public WatermarkStickerView setMargin_y(float margin_y) {
        this.margin_y = margin_y;
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

    /**
     * @param blur_radius 必须>0 否则崩溃
     * @return
     */
    public WatermarkStickerView setMaskFilter(float blur_radius) {
        blur_radius = Math.max(0.0000001f, blur_radius);
        paint.setMaskFilter(new BlurMaskFilter(blur_radius, BlurMaskFilter.Blur.SOLID));
        return this;
    }

//    public Paint getPaint() {
//        return paint;
//    }

    private void time_invalidate() {
        if (show_time) {
            if (thread != null) thread.interrupt();
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!thread.isInterrupted()) {
                        date = TimeAndAgeUtils.timeStamp2DateAll(TimeAndAgeUtils.getCureentTimeStamp());
                        //invalidate() 应该在 UI 线程中调用，用于重绘当前视图。
                        //postInvalidate() 可以在任何线程中调用，用于在 UI 线程上异步地请求重绘视图。
                        //在实际开发中，通常使用 invalidate() 来更新当前视图，而 postInvalidate() 则适用于在后台线程中更新 UI。
                        postInvalidateDelayed(1000);
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
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //StickerView宽高改变后，文字应该按比例改变，否则比例失调
        //注意：第一次oldw为0
        if (oldw > 0) {
            setTextSizeSp(textSizeSp * w / oldw);
            setMargin_x(margin_x * w / oldw);
            setMargin_y(margin_y * h / oldh);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawWatermark(canvas, getWidth(), getHeight());
    }

    public void drawWatermark(Canvas canvas, int width_canvas, int height_canvas) {
        if (!haveWatermark()) return;

        float oneLineHeight = TextUtils.getTextHeightOneLine(paint);

        text = text_appName
                + (android.text.TextUtils.isEmpty(text_product) ? "" : "\n")
                + text_product
                + (android.text.TextUtils.isEmpty(text_text) ? "" : "\n")
                + text_text;
        float textWidth = TextUtils.getTextWidth(false, 0, text, paint);
        margin_x = Math.min(Math.max(0, width_canvas - textWidth), Math.max(0, margin_x));
        margin_y= Math.min(Math.max(0, height_canvas - oneLineHeight * text.split("\n").length), Math.max(0, margin_y));
        centerX_text = margin_x + textWidth * 0.5f;
        centerY_text = height_canvas - margin_y - oneLineHeight * text.split("\n").length * 0.5f;
        rectF_text = TextUtils.getTextRectF(false, 0, paint, text, centerX_text, centerY_text);
        TextUtils.drawText(false, 0, canvas, paint, text, centerX_text, centerY_text, rectF_text);
        if (show_time) {
            centerX_time = width_canvas - margin_x - TextUtils.getTextWidthOneLine(false, date, paint) * 0.5f;
            centerY_time = height_canvas - margin_y - oneLineHeight * 0.5f;
            rectF_time = TextUtils.getTextRectF(false, 0, paint, date, centerX_time, centerY_time);
            TextUtils.drawText(false, 0, canvas, paint, date, centerX_time, centerY_time, rectF_time);
        }
    }
}

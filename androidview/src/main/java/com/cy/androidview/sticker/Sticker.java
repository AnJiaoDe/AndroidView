package com.cy.androidview.sticker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.cy.androidview.BitmapUtils;
import com.cy.androidview.R;
import com.cy.androidview.ScreenUtils;

public class Sticker {
    public static final int TYPE_TEXT = 0;
    public static final int TYPE_LABEL = 1;
    public static final int TYPE_PIC = 2;
    private int type;
    private String text;
    private float centerX, centerY, baseLineX, baseLineY;
    private RectF rectF_box_normal;
    private RectF rectFCloseRotated, rectF3DRotated, rectFRotateRotated, rectFCopyRotated;
    private float rotationX = 0;
    private float rotationY = 0;
    private float rotationZ = 0;
    private float scale = 1;
    private RectF rectF_text_normal;
    private Paint paintText, paintRectF;

    private Context context;

    public Sticker(Context context, int type, String text) {
        this.context = context;
        this.type = type;
        this.text = text;

        paintText = new Paint();
        paintText.setColor(Color.WHITE);
        paintText.setAntiAlias(true);
        paintText.setTextAlign(Paint.Align.CENTER);
        paintText.setTextSize(ScreenUtils.sp2px(context, ScreenUtils.spAdapt(context, 18)));

        paintRectF = new Paint();
        paintRectF.setColor(Color.WHITE);
        paintRectF.setStyle(Paint.Style.STROKE);
        paintRectF.setStrokeWidth(ScreenUtils.dpAdapt(context, 1));
        paintRectF.setAntiAlias(true);
    }

    public Paint getPaintText() {
        return paintText;
    }

    public Paint getPaintRectF() {
        return paintRectF;
    }

    public int getType() {
        return type;
    }

    public Sticker setType(int type) {
        this.type = type;
        return this;
    }

    public String getText() {
        return text;
    }

    public Sticker setText(String text) {
        this.text = text;
        return this;
    }

    public float getRotationX() {
        return rotationX;
    }

    public Sticker setRotationX(float rotationX) {
        this.rotationX = rotationX;
        return this;
    }

    public float getRotationY() {
        return rotationY;
    }

    public Sticker setRotationY(float rotationY) {
        this.rotationY = rotationY;
        return this;
    }

    public float getCenterX() {
        return centerX;
    }

    public Sticker setCenterX(float centerX) {
        this.centerX = centerX;
        return this;
    }

    public float getCenterY() {
        return centerY;
    }

    public Sticker setCenterY(float centerY) {
        this.centerY = centerY;
        return this;
    }

    public float getBaseLineX() {
        return baseLineX;
    }

    public Sticker setBaseLineX(float baseLineX) {
        this.baseLineX = baseLineX;
        return this;
    }

    public float getBaseLineY() {
        return baseLineY;
    }

    public Sticker setBaseLineY(float baseLineY) {
        this.baseLineY = baseLineY;
        return this;
    }

    public RectF getRectF_box_normal() {
        return rectF_box_normal;
    }

    public Sticker setRectF_box_normal(RectF rectF_box_normal) {
        this.rectF_box_normal = rectF_box_normal;
        return this;
    }
//
//    public Bitmap getBitmap_close() {
//        return bitmap_close;
//    }
//
//    public Bitmap getBitmap_rotate_3d() {
//        return bitmap_rotate_3d;
//    }
//
//    public Bitmap getBitma_rotate() {
//        return bitmap_rotate;
//    }
//
//    public Bitmap getBitmap_copy() {
//        return bitmap_copy;
//    }

    public RectF getRectFCloseRotated() {
        return rectFCloseRotated;
    }

    public Sticker setRectFCloseRotated(RectF rectFCloseRotated) {
        this.rectFCloseRotated = rectFCloseRotated;
        return this;
    }

    public RectF getRectF3DRotated() {
        return rectF3DRotated;
    }

    public Sticker setRectF3DRotated(RectF rectF3DRotated) {
        this.rectF3DRotated = rectF3DRotated;
        return this;
    }

    public RectF getRectFRotateRotated() {
        return rectFRotateRotated;
    }

    public Sticker setRectFRotateRotated(RectF rectFRotateRotated) {
        this.rectFRotateRotated = rectFRotateRotated;
        return this;
    }

    public RectF getRectFCopyRotated() {
        return rectFCopyRotated;
    }

    public Sticker setRectFCopyRotated(RectF rectFCopyRotated) {
        this.rectFCopyRotated = rectFCopyRotated;
        return this;
    }

    public float getRotationZ() {
        return rotationZ;
    }

    public Sticker setRotationZ(float rotationZ) {
        this.rotationZ = rotationZ;
        return this;
    }

    public float getScale() {
        return scale;
    }

    public Sticker setScale(float scale) {
        this.scale = scale;
        return this;
    }

    public RectF getRectF_text_normal() {
        return rectF_text_normal;
    }

    public Sticker setRectF_text_normal(RectF rectF_text_normal) {
        this.rectF_text_normal = rectF_text_normal;
        return this;
    }

//    public void setBitmap_close(Bitmap bitmap_close) {
//        this.bitmap_close = bitmap_close;
//    }
//
//    public void setBitmap_copy(Bitmap bitmap_copy) {
//        this.bitmap_copy = bitmap_copy;
//    }
//
//    public Bitmap getBitmap_rotate() {
//        return bitmap_rotate;
//    }
//
//    public void setBitmap_rotate(Bitmap bitmap_rotate) {
//        this.bitmap_rotate = bitmap_rotate;
//    }
//
//    public void setBitmap_rotate_3d(Bitmap bitmap_rotate_3d) {
//        this.bitmap_rotate_3d = bitmap_rotate_3d;
//    }

    public void setPaintText(Paint paintText) {
        this.paintText = paintText;
    }

    public void setPaintRectF(Paint paintRectF) {
        this.paintRectF = paintRectF;
    }


    public Sticker cloneDeep() {
        Sticker sticker = new Sticker(context, type, text);

        sticker.setCenterX(centerX);
        sticker.setCenterY(centerY);
        sticker.setBaseLineX(baseLineX);
        sticker.setBaseLineY(baseLineY);
        sticker.setRectF_box_normal(rectF_box_normal);
        sticker.setRectFCloseRotated(rectFCloseRotated);
        sticker.setRectF3DRotated(rectF3DRotated);
        sticker.setRectFRotateRotated(rectFRotateRotated);
        sticker.setRectFCopyRotated(rectFCopyRotated);
        sticker.setRotationX(rotationX);
        sticker.setRotationY(rotationY);
        sticker.setRotationZ(rotationZ);
        sticker.setScale(scale);
        sticker.setRectF_text_normal(rectF_text_normal);

        return sticker;
    }
}

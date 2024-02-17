package com.cy.androidview.sticker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cy.androidview.BitmapUtils;
import com.cy.androidview.R;
import com.cy.androidview.ScreenUtils;

import java.util.List;

public class StickerAttr {
    private Paint paintMenuBg, paintBitmap;
    private int radius_menu;
    private Bitmap bitmap_close, bitmap_copy, bitmap_rotate, bitmap_rotate_3d;
    private Context context;

    public StickerAttr(Context context, @NonNull AttributeSet attrs) {
        this.context = context;

        paintMenuBg = new Paint();
        paintMenuBg.setAntiAlias(true);

        paintBitmap = new Paint();
        paintBitmap.setAntiAlias(true);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StickerView);
        setMenuBgColor(typedArray.getColor(R.styleable.StickerView_cy_color_bg_menu, Color.BLACK));
        setRadius_menu(typedArray.getDimensionPixelSize(R.styleable.StickerView_cy_radius_menu, ScreenUtils.dpAdapt(context, 10)));

        bitmap_close = BitmapUtils.decodeResourceOrigin(context,
                typedArray.getResourceId(R.styleable.StickerView_cy_src_close, R.drawable.close_white));
        bitmap_copy = BitmapUtils.decodeResourceOrigin(context,
                typedArray.getResourceId(R.styleable.StickerView_cy_src_copy, R.drawable.copy_white));
        bitmap_rotate = BitmapUtils.decodeResourceOrigin(context,
                typedArray.getResourceId(R.styleable.StickerView_cy_src_rotateZ, R.drawable.rotate_white));
        bitmap_rotate_3d = BitmapUtils.decodeResourceOrigin(context,
                typedArray.getResourceId(R.styleable.StickerView_cy_src_rotateXY, R.drawable.rotate_3d_white));

        typedArray.recycle();
    }

    public void setPaintMenuBg(Paint paintMenuBg) {
        this.paintMenuBg = paintMenuBg;
    }

    public void setPaintBitmap(Paint paintBitmap) {
        this.paintBitmap = paintBitmap;
    }

    public void setBitmap_close(Bitmap bitmap_close) {
        this.bitmap_close = bitmap_close;
    }

    public void setBitmap_copy(Bitmap bitmap_copy) {
        this.bitmap_copy = bitmap_copy;
    }

    public void setBitmap_rotate(Bitmap bitmap_rotate) {
        this.bitmap_rotate = bitmap_rotate;
    }

    public void setBitmap_rotate_3d(Bitmap bitmap_rotate_3d) {
        this.bitmap_rotate_3d = bitmap_rotate_3d;
    }

    public void setMenuBgColor(int color) {
        paintMenuBg.setColor(color);
    }

    public void setRadius_menu(int radius_menu) {
        this.radius_menu = radius_menu;
    }

    public Paint getPaintMenuBg() {
        return paintMenuBg;
    }

    public Paint getPaintBitmap() {
        return paintBitmap;
    }

    public int getRadius_menu() {
        return radius_menu;
    }

    public Bitmap getBitmap_close() {
        return bitmap_close;
    }

    public Bitmap getBitmap_copy() {
        return bitmap_copy;
    }

    public Bitmap getBitmap_rotate() {
        return bitmap_rotate;
    }

    public Bitmap getBitmap_rotate_3d() {
        return bitmap_rotate_3d;
    }
}

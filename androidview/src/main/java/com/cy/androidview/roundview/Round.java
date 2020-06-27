package com.cy.androidview.roundview;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.view.View;

import androidx.annotation.StyleableRes;

import com.cy.androidview.R;

/**
 * @Description:
 * @Author: cy
 * @CreateDate: 2020/6/26 18:30
 * @UpdateUser:
 * @UpdateDate: 2020/6/26 18:30
 * @UpdateRemark:
 * @Version: 1.0
 */
public class Round {
    private TypedArray typedArray;
    private View view;
    private float radius = 20;
    private float topLeftRadius = 20;
    private float topRightRadius = 20;
    private float bottomLeftRadius = 20;
    private float bottomRightRadius = 20;
    private Paint roundPaint;
    private Paint imagePaint;

    public Round(View view, TypedArray typedArray) {
        this.view = view;
        this.typedArray = typedArray;
    }

    public Round setRadius(@StyleableRes int index) {
        radius = typedArray.getDimension(index, radius);
        return this;
    }

    public Round setTopLeftRadius(@StyleableRes int index) {
        topLeftRadius = typedArray.getDimension(index, topLeftRadius);
        return this;
    }

    public Round setTopRightRadius(@StyleableRes int index) {
        topRightRadius = typedArray.getDimension(index, topRightRadius);
        return this;
    }

    public Round setBottomLeftRadius(@StyleableRes int index) {
        bottomLeftRadius = typedArray.getDimension(index, bottomLeftRadius);
        return this;
    }

    public Round setBottomRightRadius(@StyleableRes int index) {
        bottomRightRadius = typedArray.getDimension(index, bottomRightRadius);
        return this;
    }

    ////实现1
//    @Override
//    protected void dispatchDraw(Canvas canvas) {
//        int width = getWidth();
//        int height = getHeight();
//        Path path = new Path();
//        path.moveTo(0, topLeftRadius);
//        path.arcTo(new RectF(0, 0, topLeftRadius * 2, topLeftRadius * 2), -180, 90);
//        path.lineTo(width - topRightRadius, 0);
//        path.arcTo(new RectF(width - 2 * topRightRadius, 0, width, topRightRadius * 2), -90, 90);
//        path.lineTo(width, height - bottomRightRadius);
//        path.arcTo(new RectF(width - 2 * bottomRightRadius, height - 2 * bottomRightRadius, width, height), 0, 90);
//        path.lineTo(bottomLeftRadius, height);
//        path.arcTo(new RectF(0, height - 2 * bottomLeftRadius, bottomLeftRadius * 2, height), 90, 90);
//        path.close();
//        canvas.clipPath(path);
//        super.dispatchDraw(canvas);
//    }
    ////实现2
    //    @Override
//    protected void dispatchDraw(Canvas canvas) {
//        super.dispatchDraw(canvas);
//        drawTopLeft(canvas);//用PorterDuffXfermode
//        drawTopRight(canvas);//用PorterDuffXfermode
//        drawBottomLeft(canvas);//用PorterDuffXfermode
//        drawBottomRight(canvas);//用PorterDuffXfermode
//    }
    ////实现3
//    @Override
//    protected void dispatchDraw(Canvas canvas) {
//        Bitmap bitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas newCanvas = new Canvas(bitmap);
//        super.dispatchDraw(newCanvas);
//        drawTopLeft(newCanvas);
//        drawTopRight(newCanvas);
//        drawBottomLeft(newCanvas);
//        drawBottomRight(newCanvas);
//        canvas.drawBitmap(bitmap, 0, 0, imagePaint);
////        invalidate();
//    }
    public Round saveLayer(Canvas canvas){
        roundPaint = new Paint();
        roundPaint.setColor(Color.WHITE);
        roundPaint.setAntiAlias(true);
        roundPaint.setStyle(Paint.Style.FILL);
        roundPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
//        imagePaint = new Paint();
//        imagePaint.setXfermode(null);
        view.setBackgroundColor(0x00000000);

        //禁用硬件加速
        view.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
        //离屏绘制，新建图层
        canvas.saveLayer(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()), null, Canvas.ALL_SAVE_FLAG);
        return this;
    }
    public Round round(Canvas canvas) {
        drawTopLeft(canvas);
        drawTopRight(canvas);
        drawBottomLeft(canvas);
        drawBottomRight(canvas);
        canvas.restore();
        return this;
    }

    private void drawTopLeft(Canvas canvas) {
        if (topLeftRadius > 0) {
            Path path = new Path();
            path.moveTo(0, topLeftRadius);
            path.lineTo(0, 0);
            path.lineTo(topLeftRadius, 0);
            path.arcTo(new RectF(0, 0, topLeftRadius * 2, topLeftRadius * 2), -90, -90);
            path.close();
            canvas.drawPath(path, roundPaint);
        }
    }

    private void drawTopRight(Canvas canvas) {
        if (topRightRadius > 0) {
            int width = view.getWidth();
            Path path = new Path();
            path.moveTo(width - topRightRadius, 0);
            path.lineTo(width, 0);
            path.lineTo(width, topRightRadius);
            path.arcTo(new RectF(width - 2 * topRightRadius, 0, width,
                    topRightRadius * 2), 0, -90);
            path.close();
            canvas.drawPath(path, roundPaint);
        }
    }

    private void drawBottomLeft(Canvas canvas) {
        if (bottomLeftRadius > 0) {
            int height = view.getHeight();
            Path path = new Path();
            path.moveTo(0, height - bottomLeftRadius);
            path.lineTo(0, height);
            path.lineTo(bottomLeftRadius, height);
            path.arcTo(new RectF(0, height - 2 * bottomLeftRadius,
                    bottomLeftRadius * 2, height), 90, 90);
            path.close();
            canvas.drawPath(path, roundPaint);
        }
    }

    private void drawBottomRight(Canvas canvas) {
        if (bottomRightRadius > 0) {
            int height = view.getHeight();
            int width = view.getWidth();
            Path path = new Path();
            path.moveTo(width - bottomRightRadius, height);
            path.lineTo(width, height);
            path.lineTo(width, height - bottomRightRadius);
            path.arcTo(new RectF(width - 2 * bottomRightRadius, height - 2
                    * bottomRightRadius, width, height), 0, 90);
            path.close();
            canvas.drawPath(path, roundPaint);
        }
    }
}

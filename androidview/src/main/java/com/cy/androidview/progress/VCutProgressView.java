package com.cy.androidview.progress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.cy.androidview.R;
import com.cy.androidview.ScreenUtils;

public class VCutProgressView extends View {

    private final Paint paint;
    private final Paint paintProgress;
    private int width, height;
    private Path path;
    private int corner = 0;
    private int widthStroke = 0;
    private RectF rectF, rectFCorner;
    public static final int PROGRESS_MAX = 100;
    private int progress = 0;
    private int[] progressT;

    public VCutProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        // 创建画笔
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true); // 开启抗锯齿

        paintProgress = new Paint();
        paintProgress.setStyle(Paint.Style.STROKE);
        paintProgress.setAntiAlias(true); // 开启抗锯齿

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VCutProgressView);
        setColorBg(typedArray.getColor(R.styleable.VCutProgressView_cy_color_bg, 0xffffff00));
        setColorProgress(typedArray.getColor(R.styleable.VCutProgressView_cy_color_progress, 0xff2a83fc));
        setCorner(typedArray.getDimensionPixelSize(R.styleable.VCutProgressView_cy_radiusCorner, ScreenUtils.dpAdapt(context, 10)));
        setWidthStroke(typedArray.getDimensionPixelSize(R.styleable.VCutProgressView_cy_strokeWidth, ScreenUtils.dpAdapt(context, 3)));
        setProgress(typedArray.getInt(R.styleable.VCutProgressView_cy_progress, 0));
        typedArray.recycle();

        path = new Path();
        rectF = new RectF();
        rectFCorner = new RectF();
    }

    public void setCorner(int corner) {
        this.corner = corner;
    }

    public void setColorBg(int color) {
        paint.setColor(color);
    }

    public void setColorProgress(int color) {
        paintProgress.setColor(color);
    }

    public void setWidthStroke(int widthStroke) {
        this.widthStroke = widthStroke;
        paint.setStrokeWidth(widthStroke);
        paintProgress.setStrokeWidth(widthStroke);
    }

    public void setProgress(int progress) {
        this.progress = Math.min(PROGRESS_MAX, Math.max(0, progress));
        invalidate();
    }

    public int getProgress() {
        return progress;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
        float all = 2 * width + 2 * height;
        //31  50  81  100
        progressT = new int[]{(int) (width / all * PROGRESS_MAX), (int) ((width + height) / all * PROGRESS_MAX),
                (int) ((2 * width + height) / all * PROGRESS_MAX), PROGRESS_MAX};
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        rectF.left = widthStroke * 0.5f;
        rectF.top = rectF.left;
        rectF.right = width - rectF.left;
        rectF.bottom = height - rectF.left;
        canvas.drawRoundRect(rectF, corner, corner, paint);

        path.moveTo(rectF.left + corner, rectF.top);
        path.lineTo(Math.min(rectF.right - corner, rectF.left + corner + (width - 2.0f * corner) * progress / progressT[0]), rectF.top);

        if (progress > progressT[0]) {
            rectFCorner.left = rectF.right - 2 * corner;
            rectFCorner.top = rectF.top;
            rectFCorner.right = rectF.right;
            rectFCorner.bottom = rectF.top + 2 * corner;
            path.arcTo(rectFCorner, 0, -90, true);

            path.moveTo(rectF.right, rectF.top + corner);
            path.lineTo(rectF.right, Math.min(rectF.bottom - corner,
                    rectF.top + corner + (height - 2.0f * corner) * (progress-progressT[0]) / (progressT[1]-progressT[0])));
        }

        if (progress > progressT[1]) {
            rectFCorner.left = rectF.right - 2 * corner;
            rectFCorner.top = rectF.bottom - 2 * corner;
            rectFCorner.right = rectF.right;
            rectFCorner.bottom = rectF.bottom;
            path.arcTo(rectFCorner, 0, 90, true);

            path.moveTo(rectF.right - corner, rectF.bottom);
            path.lineTo(Math.max(rectF.left + corner,
                    rectF.right - corner - (width - 2.0f * corner) * (progress-progressT[1]) / (progressT[2]-progressT[1])), rectF.bottom);
        }

        if (progress > progressT[2]) {
            rectFCorner.left = rectF.left;
            rectFCorner.top = rectF.bottom - 2 * corner;
            rectFCorner.right = rectF.left + 2 * corner;
            rectFCorner.bottom = rectF.bottom;
            path.arcTo(rectFCorner, 90, 90, true);

            path.moveTo(rectF.left, rectF.bottom - corner);
            path.lineTo(rectF.left, Math.max(rectF.top + corner,
                    rectF.bottom - corner - (height-2.0f*corner) * (progress-progressT[2]) / (progressT[3]-progressT[2])));
        }

        if (progress >= progressT[3]) {
            rectFCorner.left = rectF.left;
            rectFCorner.top = rectF.top;
            rectFCorner.right = rectF.left + 2 * corner;
            rectFCorner.bottom = rectF.top + 2 * corner;
            path.arcTo(rectFCorner, 180, 90, true);
        }

        canvas.drawPath(path, paintProgress);
    }

}

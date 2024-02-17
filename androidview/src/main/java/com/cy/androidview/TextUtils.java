package com.cy.androidview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

public class TextUtils {
    public static RectF getTextRectF(String text, Paint paint, float centerX, float centerY) {
        float w = getTextWidth(text, paint);
        float h = getTextHeight(paint);
        return new RectF(centerX - w * 0.5f, centerY - h * 0.5f, centerX + w * 0.5f, centerY + h * 0.5f);
    }

    public static RectF getTextRectF_multi_line(String text, Paint paint, float centerX, float centerY) {
        String[] ts = text.split("\n");
        float w = 0;
        float h = getTextHeight(paint) * ts.length;
        for (int i = 0; i < ts.length; i++) {
            w = Math.max(w, getTextWidth(ts[i], paint));
        }
        return new RectF(centerX - w * 0.5f, centerY - h * 0.5f, centerX + w * 0.5f, centerY + h * 0.5f);
    }

    public static void drawText_multi_line(Canvas canvas, Paint paint, String text, float centerX, float centerY) {
        String[] ts = text.split("\n");
        float h = getTextHeight(paint);
        float o = centerY - h * ts.length * 0.5f + h * 0.5f;
        for (int i = 0; i < ts.length; i++) {
            canvas.drawText(ts[i], getBaseLineX(paint, ts[i], centerX), getBaseLineY(paint, o + h * i), paint);
        }
    }

    public static float getBaseLineX(Paint paint, String text, float centerX) {
        if (paint.getTextAlign() == Paint.Align.CENTER) {
            return centerX;
        } else if (paint.getTextAlign() == Paint.Align.RIGHT) {
            return centerX + paint.measureText(text) * 0.5f;
        } else {
            return centerX - paint.measureText(text) * 0.5f;
        }
    }

    public static float getBaseLineY(Paint paint, float centerY) {
        Paint.FontMetricsInt fontMetricsInt = paint.getFontMetricsInt();
        return centerY - (fontMetricsInt.bottom - fontMetricsInt.top) * 0.5f - fontMetricsInt.top;
    }

    public static float getTextWidth(String text, Paint paint) {
        return paint.measureText(text);
    }

    public static float getTextHeight(Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return fontMetrics.bottom - fontMetrics.top;
    }
}

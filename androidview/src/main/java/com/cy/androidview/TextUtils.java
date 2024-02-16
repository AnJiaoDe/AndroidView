package com.cy.androidview;

import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

public class TextUtils {
    public static RectF getTextRectF(String text, Paint paint, float baseLineX, float baseLineY) {
        Rect rect = new Rect();
        Paint.Align align = paint.getTextAlign();
        paint.getTextBounds(text, 0, text.length(), rect);

        float d = baseLineX;
        if (align == Paint.Align.CENTER) {
            d = baseLineX - rect.width() * 0.5f;
        } else if (align == Paint.Align.RIGHT) {
            d = baseLineX - rect.width();
        }
        rect.left += d;
        rect.right += d;

        rect.top += baseLineY;
        rect.bottom += baseLineY;
        return new RectF(rect);
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
}

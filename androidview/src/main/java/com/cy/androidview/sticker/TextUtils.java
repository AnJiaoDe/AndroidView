package com.cy.androidview.sticker;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class TextUtils {
    public static RectF getTextRectF(boolean vertical, Paint paint, String text, float centerX, float centerY) {
        String[] ts = text.split("\n");
        float w = 0;
        float h ;
        if (!vertical) {
            h = getTextHeight(paint) * ts.length;
            for (int i = 0; i < ts.length; i++) {
                w = Math.max(w, getTextWidth(vertical, ts[i], paint));
            }
        }else{
            int len_max = 0;
            for (int i = 0; i < ts.length; i++) {
                w += getTextWidth(vertical, ts[i], paint);
                len_max = Math.max(len_max, ts[i].length());
            }
            h = getTextHeight(paint) * len_max;
        }
        return new RectF(centerX - w * 0.5f, centerY - h * 0.5f, centerX + w * 0.5f, centerY + h * 0.5f);
    }

    public static void drawText(boolean vertical, Canvas canvas, Paint paint, String text, float centerX, float centerY, RectF rectF) {
        String[] ts = text.split("\n");
        if (!vertical) {
            float h = getTextHeight(paint);
            float o = centerY - h * ts.length * 0.5f + h * 0.5f;
            for (int i = 0; i < ts.length; i++) {
                canvas.drawText(ts[i], getBaseLineX(paint, centerX, rectF), getBaseLineY(paint, o + h * i), paint);
            }
            return;
        }

        float h = getTextHeight(paint);
        float centerX__ = rectF.left;
        float centerY__ = centerY;
        float w ;
        Paint.Align align = paint.getTextAlign();
        for (int i = 0; i < ts.length; i++) {
            w = getTextWidth(vertical, ts[i], paint);
            if (paint.getTextAlign() == Paint.Align.LEFT) {
                centerY__ = rectF.top + h * ts[i].length() * 0.5f;
            } else if (paint.getTextAlign() == Paint.Align.RIGHT) {
                centerY__ = rectF.bottom - h * ts[i].length() * 0.5f;
            }
            centerX__ += w * 0.5f;
            float o = centerY__ - h * ts[i].length() * 0.5f + h * 0.5f;

            //注意：竖直绘制文字，直接align center,baseLineX就是每个竖行的centerX__，绘制完之后必须重置align，否则GG
            paint.setTextAlign(Paint.Align.CENTER);
            for (int kkk = 0; kkk < ts[i].length(); kkk++) {
                canvas.drawText(String.valueOf(ts[i].charAt(kkk)),
                        centerX__,
                        getBaseLineY(paint, o + h * kkk),
                        paint);
            }
            paint.setTextAlign(align);

            centerX__ += w * 0.5f;
        }
    }

    public static float getTextWidth(boolean vertical, String text, Paint paint) {
        if (!vertical)
            return paint.measureText(text);
        float w = 0;
        for (int i = 0; i < text.length(); i++) {
            w = Math.max(w, paint.measureText(String.valueOf(text.charAt(i))));
        }
        return w;
    }

    public static float getTextHeight(Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return fontMetrics.bottom - fontMetrics.top;
    }

    public static float getBaseLineX(Paint paint, float centerX, RectF rectF) {
        if (paint.getTextAlign() == Paint.Align.CENTER) {
            return centerX;
        } else if (paint.getTextAlign() == Paint.Align.RIGHT) {
            return rectF.right;
        } else {
            return rectF.left;
        }
    }

    public static float getBaseLineY(Paint paint, float centerY) {
        Paint.FontMetricsInt fontMetricsInt = paint.getFontMetricsInt();
        return centerY - (fontMetricsInt.bottom - fontMetricsInt.top) * 0.5f - fontMetricsInt.top;
    }
}

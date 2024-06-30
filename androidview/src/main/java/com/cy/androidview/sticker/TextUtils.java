package com.cy.androidview.sticker;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class TextUtils {
    public static RectF getTextRectF(boolean vertical, float lineSpace, Paint paint, String text, float centerX, float centerY) {
        String[] ts = text.split("\n");
        float w = 0;
        float h;
        float textH = getTextHeight(paint);
        if (!vertical) {
            h = textH * ts.length + textH * lineSpace * (ts.length - 1);
            for (int i = 0; i < ts.length; i++) {
                w = Math.max(w, getTextWidth(vertical, ts[i], paint));
            }
        } else {
            int len_max = 0;
            float wT = 0;
            for (int i = 0; i < ts.length; i++) {
                w += wT * lineSpace;
                wT = getTextWidth(vertical, ts[i], paint);
                w += wT;
                len_max = Math.max(len_max, ts[i].length());
            }
            h = textH * len_max + textH * paint.getLetterSpacing() * (len_max - 1);
        }
        return new RectF(centerX - w * 0.5f, centerY - h * 0.5f, centerX + w * 0.5f, centerY + h * 0.5f);
    }

    public static void drawText(boolean vertical, float lineSpace, Canvas canvas, Paint paint, String text, float centerX, float centerY, RectF rectF) {
        String[] ts = text.split("\n");
        float textH = getTextHeight(paint);
        if (!vertical) {
            float hhh = textH * ts.length + textH * lineSpace * (ts.length - 1);
            float o = centerY - hhh * 0.5f + textH * 0.5f;
            for (int i = 0; i < ts.length; i++) {
                canvas.drawText(ts[i], getBaseLineX(paint, centerX, rectF),
                        getBaseLineY(paint, o + textH * i + textH * lineSpace * i), paint);
            }
            return;
        }

        float centerX__ = rectF.left;
        float centerY__;
        float w;
        float h;
        Paint.Align align = paint.getTextAlign();
        for (int i = 0; i < ts.length; i++) {
            w = getTextWidth(vertical, ts[i], paint);
            h = textH * ts[i].length() + textH * paint.getLetterSpacing() * (ts[i].length() - 1);
            if (paint.getTextAlign() == Paint.Align.LEFT) {
                centerY__ = rectF.top;
            } else if (paint.getTextAlign() == Paint.Align.RIGHT) {
                centerY__ = rectF.bottom - h;
            } else {
                centerY__ = centerY - h * 0.5f;
            }
            centerX__ += w * 0.5f;
            float o = centerY__ + textH * 0.5f;

            //注意：竖直绘制文字，直接align center,baseLineX就是每个竖行的centerX__，绘制完之后必须重置align，否则GG
            paint.setTextAlign(Paint.Align.CENTER);
            for (int kkk = 0; kkk < ts[i].length(); kkk++) {
                canvas.drawText(String.valueOf(ts[i].charAt(kkk)),
                        centerX__,
                        getBaseLineY(paint, o + textH * kkk + textH * paint.getLetterSpacing() * kkk),
                        paint);
            }
            paint.setTextAlign(align);

            centerX__ += w * 0.5f + w * lineSpace;
        }
    }

    public static float getTextWidth(boolean vertical, String text, Paint paint) {
        if (!vertical)
            return paint.measureText(text);
        float letterSpacing = paint.getLetterSpacing();
        paint.setLetterSpacing(0);
        float w = 0;
        for (int i = 0; i < text.length(); i++) {
            w = Math.max(w, paint.measureText(String.valueOf(text.charAt(i))));
        }
        paint.setLetterSpacing(letterSpacing);
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

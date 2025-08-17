package com.cy.necessaryviewmaster;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cy.androidview.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

public class PlaybackTimeView extends View {
    private Paint paintText;
    private Paint paintLine;
    private Paint paintIndicator;
    private float height_line_long;
    private float height_line_short;
    private float inter_min;
    private float inter_max;
    private int fps;
    private float tv_margin_top;
    private final int UNIT_H = 0;
    private final int UNIT_M = 1;
    private final int UNIT_S = 2;
    private int unit = UNIT_H;
    //每fps个格子有多少秒
    private int sPerUnit=3600;
    private GestureDetector gestureDetector;
    private ScaleGestureDetector scaleGestureDetector;
    //    private float dx;
    private float velocityThreshold;
    private float maxVelocity;
    private float minVelocity;
    private float radius_indicator;
    private float zoom = 1; // 缩放
    private boolean onScaling = false;
    private float zoom_h_ratio;
    private List<Integer> listDivisors60;
    private float inter;
    private int h_current = 23;
    private int m_current = 59;
    private int s_current = 0;
    private int fps_current = 4;

    public PlaybackTimeView(Context context) {
        this(context, null);
    }

    public PlaybackTimeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        listDivisors60 = MyMathUtils.getDivisors(60);

        paintLine = new Paint();
        paintLine.setAntiAlias(true);

        paintText = new Paint();
        paintText.setAntiAlias(true);

        paintIndicator = new Paint();
        paintIndicator.setAntiAlias(true);

        setHeight_line_long(ScreenUtils.dpAdapt(context, 34));
        setHeight_line_short(ScreenUtils.dpAdapt(context, 20));

        setInter_min(ScreenUtils.dpAdapt(context, 6));
        setInter_max(ScreenUtils.dpAdapt(context, 24));
        setFps(15);
        setTv_margin_top(ScreenUtils.dpAdapt(context, 10));
        setTextSize(ScreenUtils.spAdapt(context, 8));
        setIndicatorColor(0xff4099FF);
        setIndicatorWidth(ScreenUtils.dpAdapt(context, 2));
        setIndicatorRadius(ScreenUtils.dpAdapt(context, 4));
        setPaintLineWidth(ScreenUtils.dpAdapt(context, 0.52f));
        setZoom_h_ratio(2);

        final ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
        maxVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        minVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        velocityThreshold = minVelocity + (maxVelocity - minVelocity) * 0.4f;

        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            private void culculate(float distanceX) {
                LogUtils.log("distanceX", distanceX);
                switch (unit) {
                    case UNIT_H:
                        float cm = h_current * 60 + m_current
                                + distanceX / inter / fps * sPerUnit / 60;
                        cm = Math.max(0, cm);

                        h_current = (int) (cm / 60);

                        // 到 24:00:00，分钟必须为 0
                        m_current = (h_current == 24) ? 0 : (int) (cm % 60);

                        s_current = 0; // 小时模式下不关心秒
                        fps_current = 0;

                        break;

                    case UNIT_M:
                        float cs = h_current * 3600 + m_current * 60 + s_current
                                + distanceX / inter / fps * sPerUnit;
                        cs = Math.max(0, cs);

                        h_current = (int) (cs / 3600);

                        m_current = (h_current == 24) ? 0 : (int) ((cs % 3600) / 60);
                        s_current = (h_current == 24) ? 0 : (int) (cs % 60);
                        fps_current = 0;

                        break;
                    case UNIT_S:
                        float cfps = h_current * 3600 * fps + m_current * 60 * fps + s_current * fps + fps_current
                                + distanceX / inter / fps * sPerUnit * fps;
                        cfps = Math.max(0, cfps);

                        h_current = (int) (cfps / fps / 3600);

                        m_current = (h_current == 24) ? 0 : (int) ((cfps / fps % 3600) / 60);
                        s_current = (h_current == 24) ? 0 : (int) (cfps / fps % 60);
                        fps_current = (h_current == 24) ? 0 : (int) (cfps % fps);
                        break;
                }

                LogUtils.log("h_current", h_current);
                LogUtils.log("m_current", m_current);
                LogUtils.log("s_current", s_current);
                LogUtils.log("fps_current", fps_current);
            }

            @Override
            public boolean onScroll(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
                if (!onScaling) {
                    culculate(distanceX);
                    invalidate();
                }
                return super.onScroll(e1, e2, distanceX, distanceY);
            }

            @Override
            public boolean onFling(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
                if (!onScaling && Math.abs(velocityX) > Math.abs(velocityThreshold)) {
                    ValueAnimator animator = ValueAnimator.ofFloat(1, 0);
                    animator.setDuration(1000);
                    animator.addUpdateListener(animation -> {
                        float distanceX = 70 * (float) animation.getAnimatedValue();
                        culculate(velocityX > 0 ? -distanceX : distanceX);
                        invalidate();
                    });
                    animator.start();
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
        scaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                onScaling = true;

                float z = zoom;
                zoom *= detector.getScaleFactor();
                zoom = Math.max(1, zoom);
                //zoom_h_ratio后面改成用帧率计算
                inter = Math.min(inter_max * 60 * 60, zoom <= 1 ? inter_min : (inter_min + zoom * zoom_h_ratio));
                //unit为秒时，不能无限增大zoom，否则会导致超过放大限制后再放小，需要双指缩放很久才能放小
                if (inter >= inter_max * 60 * 60) zoom = z;

                switch (unit) {
                    case UNIT_H:
                        if (inter > inter_max) {
                            unit = UNIT_M;
                        }
                        break;
                    case UNIT_M:
                        //由于每小时，每分钟都是最多绘制fps格，当格子宽度超过inter_max,就开始往分秒细化，故而用fps判断
                        if (inter > inter_max * fps) {
                            unit = UNIT_S;
                        } else if (inter <= inter_max) {
                            unit = UNIT_H;
                        }
                        break;
                    case UNIT_S:
                        if (inter <= inter_max) {
                            unit = UNIT_H;
                        } else if (inter <= inter_max * fps) {
                            unit = UNIT_M;
                        }
                        break;
                }

                switch (unit) {
                    case UNIT_H:
                        // 每 fps 个格子有多少秒 → 1小时 = 3600 秒
                        sPerUnit = 60 * 60;
                        break;
                    case UNIT_M:
                        // 每 fps 个格子有多少秒 → 1分钟 = 60 秒
                        sPerUnit = 60;
                        break;
                    case UNIT_S:
                        // 每 fps 个格子有多少秒 → 1秒 = 1 秒
                        sPerUnit = 1;
                        break;
                }
                invalidate();
                return true;
            }
        });
    }

    public void setHeight_line_long(float height_line_long) {
        this.height_line_long = height_line_long;
    }

    public void setHeight_line_short(float height_line_short) {
        this.height_line_short = height_line_short;
    }

    public void setInter_min(float inter_min) {
        this.inter_min = inter_min;
    }

    public void setInter_max(float inter_max) {
        this.inter_max = inter_max;
    }

    public void setFps(int fps) {
        this.fps = fps;
    }

    public void setTv_margin_top(float tv_margin_top) {
        this.tv_margin_top = tv_margin_top;
    }

    public void setTextSize(float textSize) {
        paintText.setTextSize(textSize);
    }

    public void setIndicatorColor(@ColorInt int color) {
        paintIndicator.setColor(color);
    }

    public void setIndicatorWidth(int width) {
        paintIndicator.setStrokeWidth(width);
    }

    public void setPaintLineWidth(int width) {
        paintLine.setStrokeWidth(width);
    }

    public void setIndicatorRadius(float radius) {
        this.radius_indicator = radius;
    }

    public void setZoom_h_ratio(float zoom_h_ratio) {
        this.zoom_h_ratio = zoom_h_ratio;
    }

    public void setTimeSelect(int h_current, int m_current, int s_current, int fps_current) {
        this.h_current = h_current;
        this.m_current = m_current;
        this.s_current = s_current;
        this.fps_current = fps_current;
    }

    public int getH_current() {
        return h_current;
    }

    public int getM_current() {
        return m_current;
    }

    public int getS_current() {
        return s_current;
    }

    public int getFps_current() {
        return fps_current;
    }

    private void drawTime(Canvas canvas, int h, int m, int s, int frame, float startX, float startY) {
        switch (unit) {
            case UNIT_H: // 绘制小时刻度
                //不够一小格了，当然是指向小时
                if (m % 60 < sPerUnit / fps / 60) {
                    canvas.drawLine(startX, startY, startX, height_line_long, paintLine);
                    String text = String.format("%02d:00:00", h);
                    RectF rectF_text = TextUtils.getTextRectF(false, 1, paintText, text, startX, height_line_long + tv_margin_top);
                    TextUtils.drawText(false, 1, canvas, paintText, text, startX, height_line_long + tv_margin_top, rectF_text);
                } else {
                    canvas.drawLine(startX, startY, startX, height_line_short, paintLine);
                }
                break;

            case UNIT_M: // 绘制分钟刻度
                //不够一小格了，当然是指向分钟
                if (s % 60 < sPerUnit / fps) {
                    canvas.drawLine(startX, startY, startX, height_line_long, paintLine);
                    String text = String.format("%02d:%02d:00", h, m);
                    RectF rectF_text = TextUtils.getTextRectF(false, 1, paintText, text, startX, height_line_long + tv_margin_top);
                    TextUtils.drawText(false, 1, canvas, paintText, text, startX, height_line_long + tv_margin_top, rectF_text);
                } else {
                    canvas.drawLine(startX, startY, startX, height_line_short, paintLine);
                }
                break;

            case UNIT_S: // 绘制秒刻度
                //不够一小格了，当然是指向秒
                if (frame % fps < sPerUnit) {
                    canvas.drawLine(startX, startY, startX, height_line_long, paintLine);
                    String text = String.format("%02d:%02d:%02d", h, m, s);
                    RectF rectF_text = TextUtils.getTextRectF(false, 1, paintText, text, startX, height_line_long + tv_margin_top);
                    TextUtils.drawText(false, 1, canvas, paintText, text, startX, height_line_long + tv_margin_top, rectF_text);
                } else { // 秒以下只画短线（帧）
                    canvas.drawLine(startX, startY, startX, height_line_short, paintLine);
                }
                break;
        }
    }


    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        //防止尚未scale inter为0
        inter = Math.max(inter, inter_min);

        float startX = getWidth() * 0.5f;
        float startY = 0;
// 每格多少帧
        int frameStep = sPerUnit;   // 因为 sPerUnit 已经是 fps * 秒数
// 转换为总帧数
        int totalFrames = (h_current * 3600 + m_current * 60 + s_current) * fps + fps_current;
// 绘制当前时间
        drawTime(canvas,
                totalFrames / (3600 * fps),                 // 小时
                (totalFrames / (60 * fps)) % 60,            // 分钟
                (totalFrames / fps) % 60,                   // 秒
                totalFrames % fps,                          // 帧
                startX, startY);
// 绘制左边
        for (float x = startX - inter; x >= 0; x -= inter) {
            totalFrames -= frameStep;
            if (totalFrames < 0) break;
            drawTime(canvas,
                    totalFrames / (3600 * fps),
                    (totalFrames / (60 * fps)) % 60,
                    (totalFrames / fps) % 60,
                    totalFrames % fps,
                    x, startY);
        }
// 绘制右边
        totalFrames = (h_current * 3600 + m_current * 60 + s_current) * fps + fps_current;
        for (float x = startX + inter; x <= getWidth(); x += inter) {
            totalFrames += frameStep;
            if (totalFrames > 24 * 3600 * fps) break;

            int h = totalFrames / (3600 * fps);
            int m = (totalFrames / (60 * fps)) % 60;
            int s = (totalFrames / fps) % 60;
            int f = totalFrames % fps;

            drawTime(canvas, h, m, s, f, x, startY);
        }

        canvas.drawLine(getWidth() * 0.5f, 0, getWidth() * 0.5f, getHeight() * 0.5f, paintIndicator);
        canvas.drawCircle(getWidth() * 0.5f, getHeight() * 0.5f, radius_indicator, paintIndicator);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        scaleGestureDetector.onTouchEvent(event);

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_UP:
                onScaling = false;
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }
}

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
    private int sPerUnit;
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
    private int h_current = 5;
    private int m_current;
    private int s_current;
    private int fps_current;

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
                        //每fps个格子有多少秒
                        sPerUnit = 60 * 60;
                        //偏移了多少分钟
                        int dm = (int) (distanceX / inter / fps * sPerUnit / 60);
                        //用这种方式算会把猪脑阔算晕
//                        float dm = h_current * 60 + m_current + m;
                        if (m_current + dm < 60) {
                            m_current += dm;
                        } else {
                            h_current += (m_current + dm) / 60;
                            m_current = (m_current + dm) % 60;
                        }
                        break;
                    case UNIT_M:
                        float s = distanceX / inter / fps * 60;
                        float ds = h_current * 60 * 60 + m_current * 60 + s_current + s;

                        h_current = (int) (ds / 60 / 60);
                        h_current = Math.max(0, Math.min(24, h_current));

                        //都已经指到24:00:00刻度了，分钟必须为0
                        m_current = h_current == 24 ? 0 : (int) (ds % 3600 / 60);
                        m_current = Math.max(0, Math.min(60, m_current));

                        //都已经指到24:00:00刻度了，秒必须为0
                        s_current = h_current == 24 ? 0 : (int) (ds % 3600 % 60);
                        s_current = Math.max(0, Math.min(60, s_current));

                        //1小时可以分多少格分钟，n=2就分出 :30:  n=3 就分出 :20: :40:
                        int n = (int) (inter / inter_min);
                        for (int ll = listDivisors60.size() - 1; ll >= 0; ll--) {
                            if (n >= listDivisors60.get(ll)) {
                                n = listDivisors60.get(ll);
                                break;
                            }
                        }
                        //每fps个格子有多少秒
                        sPerUnit = 60 / n * 60;
                        break;
                    case UNIT_S:

                        break;
                }

                LogUtils.log("h_current", h_current);
                LogUtils.log("m_current", m_current);
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
//                    ValueAnimator animator = ValueAnimator.ofFloat(1, 0);
//                    animator.setDuration(1000);
//                    animator.addUpdateListener(animation -> {
//                        float distanceX = 70 * (float) animation.getAnimatedValue();
//                        LogUtils.log("velocityX", velocityX);
//                        culculate(velocityX > 0 ? -distanceX : distanceX);
//                        invalidate();
//                    });
//                    animator.start();
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
        scaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
//                onScaling = true;
//
//                float z = zoom;
//                zoom *= detector.getScaleFactor();
//                zoom = Math.max(1, zoom);
//                //zoom_h_ratio后面改成用帧率计算
//                inter = Math.min(inter_max * 60 * 60, zoom <= 1 ? inter_min : (inter_min + zoom * zoom_h_ratio));
//                //unit为秒时，不能无限增大zoom，否则会导致超过放大限制后再放小，需要双指缩放很久才能放小
//                if (inter >= inter_max * 60 * 60) zoom = z;
//
//                invalidate();
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

    private void drawTime(Canvas canvas, int h, int m, int s, float startX, float startY) {
        switch (unit) {
            case UNIT_H:
                if (m % 60 == 0) {
                    canvas.drawLine(startX, startY, startX, height_line_long, paintLine);
                    String text = (h < 10 ? "0" + h : h) + ":00:00";
                    RectF rectF_text = TextUtils.getTextRectF(false, 1, paintText, text, startX, height_line_long + tv_margin_top);
                    TextUtils.drawText(false, 1, canvas, paintText, text, startX, height_line_long + tv_margin_top, rectF_text);
                } else {
                    canvas.drawLine(startX, startY, startX, height_line_short, paintLine);
                }
                break;
            case UNIT_M:
                break;
            case UNIT_S:
                break;
        }
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        //防止尚未scale inter为0
        inter = Math.max(inter, inter_min);
//        sPerUnit = Math.max(sPerUnit, 60 * 60);


        sPerUnit = Math.max(sPerUnit, 60 * 60);
        float startX = getWidth() * 0.5f;
        float startY = 0;
        int h = h_current;
        int m = m_current;
        int s = s_current;
        int m_inter = sPerUnit / 60 / fps;

        //绘制当前时间
        drawTime(canvas, h, m, s, startX, startY);
        //绘制当前时间左边的时间
        while (startX >= 0) {
            if (m >= m_inter) {
                m -= m_inter;
            } else {
                h -= 1 + m_inter / 60;
                m = 60 - m_inter % 60;
            }
            startX -= inter;
            drawTime(canvas, h, m, s, startX, startY);
        }
        startX = getWidth() * 0.5f;
        h = h_current;
        m = m_current;
        //绘制当前时间右边的时间
        while (startX <= getWidth()) {
            if (m + m_inter < 60) {
                m += m_inter;
            } else {
                h += (m + m_inter) / 60;
                m = (m + m_inter) % 60;
            }
            startX += inter;
            drawTime(canvas, h, m, s, startX, startY);
        }


//        switch (unit) {
//            case UNIT_H:
//                if (inter > inter_max) {
//                    unit = UNIT_M;
//                }
//                break;
//            case UNIT_M:
//                //由于每小时，每分钟都是最多绘制fps格，当格子宽度超过inter_max,就开始往分秒细化，故而用fps判断
////                if (inter > inter_max * fps) {
////                    unit = UNIT_S;
////                } else if (inter <= inter_max) {
////                    unit = UNIT_H;
////                }
//                break;
//            case UNIT_S:
//                if (inter <= inter_max) {
//                    unit = UNIT_H;
//                } else if (inter <= inter_max * fps) {
//                    unit = UNIT_M;
//                }
//                break;
//        }

//        float startX = getWidth() * 0.5f;
//        float startY = 0;
//        switch (unit) {
//            case UNIT_H:
//                for (int dir = -1; dir <= 1; dir += 1) { // -1 向左, +1 向右
//                    for (int i = 1; ; i++) {
//                        float startX = startX + dir * inter * i;
//                        int m = m_current - m_current % (sPerUnit / 60 / fps) + sPerUnit / 60 / fps * i * dir;
//                        //超出边界的不能绘制，不然容易卡顿，超出0-24小时的也不能绘制
//                        if (startX < 0 || startX > getWidth() || h_current + m / 60.f < 0 || h_current + m / 60.f > 24)
//                            break;
//
//                        int h = h_current + m / 60;
//                        if (m % 60 == 0) {
//                            //当前刻度不用绘制，反正被指针挡住了
//                            if (dir != 0)
//                                canvas.drawLine(startX, startY, startX, height_line_long, paintLine);
//                            String text = (h < 10 ? "0" + h : h) + ":00:00";
//                            RectF rectF_text = TextUtils.getTextRectF(false, 1, paintText, text, startX, height_line_long + tv_margin_top);
//                            TextUtils.drawText(false, 1, canvas, paintText, text, startX, height_line_long + tv_margin_top, rectF_text);
//                        } else {
//                            canvas.drawLine(startX, startY, startX, height_line_short, paintLine);
//                        }
//                        //指针指向的当前刻度，绘制完后结束循环
//                        if (dir == 0) break;
//                    }
//                }
//                break;
//            case UNIT_M:
//                for (int dir = -1; dir <= 1; dir += 1) { // -1 向左, +1 向右
//                    for (int i = 1; ; i++) {
//                        float startX = startX + dir * inter * i;
//                        LogUtils.log("sPerUnit", sPerUnit);
//                        int h;
//                        if (sPerUnit / 60 / fps > 0) {
//                            //每一小格>=1分钟，格数分得还不够细，还是用分钟计算,
//                            int m = m_current - m_current % (sPerUnit / 60 / fps) + sPerUnit / 60 / fps * i * dir;
//                            int s = s_current - s_current % (sPerUnit / fps) + sPerUnit / fps * i * dir;
//                            h = h_current + (m * 60 + s) / 3600;
//                        } else {
//                            int s = s_current - s_current % (sPerUnit / fps) + sPerUnit / fps * i * dir;
//                            h = h_current + (m_current * 60 + s) / 3600;
//                        }
//
////                        int m=m_current+s/60;
//                        //超出边界的不能绘制，不然容易卡顿，超出0-24小时的也不能绘制
//                        if (startX < 0 || startX > getWidth() || h_current + m_current / 60.f + s_current / 3600.f < 0 || h_current + m_current / 60.f + s_current / 3600.f > 24)
//                            break;
//
//                        if ((m_current * 60 + s) % 3600 == 0) {
//                            //当前刻度不用绘制，反正被指针挡住了
//                            if (dir != 0)
//                                canvas.drawLine(startX, startY, startX, height_line_long, paintLine);
//                            String text = (h < 10 ? "0" + h : h) + ":00:00";
//                            RectF rectF_text = TextUtils.getTextRectF(false, 1, paintText, text, startX, height_line_long + tv_margin_top);
//                            TextUtils.drawText(false, 1, canvas, paintText, text, startX, height_line_long + tv_margin_top, rectF_text);
//                        } else {
//                            LogUtils.log("ssss", s);
//                            if ((m_current * 60 + s) % sPerUnit == 0) {
//                                //当前刻度不用绘制，反正被指针挡住了
//                                if (dir != 0)
//                                    canvas.drawLine(startX, startY, startX, height_line_long * 0.8f, paintLine);
//                                int m = m_current + s / 60;
//                                String text = (h < 10 ? "0" + h : h) + ":" + (m < 10 ? "0" + m : m) + ":00";
//                                RectF rectF_text = TextUtils.getTextRectF(false, 1, paintText, text, startX, height_line_long + tv_margin_top);
//                                TextUtils.drawText(false, 1, canvas, paintText, text, startX, height_line_long + tv_margin_top, rectF_text);
//                            } else {
//                                canvas.drawLine(startX, startY, startX, height_line_short, paintLine);
//                            }
//                        }
//                        //指针指向的当前刻度，绘制完后结束循环
//                        if (dir == 0) break;
//                    }
//                }
//                break;
//            case UNIT_S:
//                break;
//        }
//        canvas.drawLine(getWidth() * 0.5f, 0, getWidth() * 0.5f, getHeight() * 0.5f, paintIndicator);
//        canvas.drawCircle(getWidth() * 0.5f, getHeight() * 0.5f, radius_indicator, paintIndicator);
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

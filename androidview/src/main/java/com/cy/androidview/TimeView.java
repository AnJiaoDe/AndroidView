package com.cy.androidview;

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
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class TimeView extends View {
    private Paint paintText;
    private Paint paintLine;
    private Paint paintIndicator;
    private float height_line_long;
    private float height_line_short;
    private float height_line_indicator;
    private float inter_min;
    private float inter_max;
    private int fps;
    private float tv_margin_top;
    private final int UNIT_H = 0;
    private final int UNIT_M = 1;
    private final int UNIT_S = 2;
    private int unit = UNIT_H;
    //每fps个格子有多少秒
    private int sPerUnit = 3600;
    private int count_unit = 1;
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
    private int h_current;
    private int m_current;
    private int s_current;
    private int fps_current;
    private Callback callback;

    public TimeView(Context context) {
        this(context, null);
    }

    public TimeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        listDivisors60 = MyMathUtils.getDivisors(60);

        paintLine = new Paint();
        paintLine.setAntiAlias(true);

        paintText = new Paint();
        paintText.setAntiAlias(true);

        paintIndicator = new Paint();
        paintIndicator.setAntiAlias(true);

        setHeight_line_short(ScreenUtils.dpAdapt(context, 10));
        setHeight_line_long(ScreenUtils.dpAdapt(context, 24));
        setHeight_line_indicator(ScreenUtils.dpAdapt(context, 30));

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
                //整成一个函数
                float dx = inter;
                switch (unit) {
                    case UNIT_M:
                        dx = inter / count_unit;
                        break;
                    case UNIT_S:
                        dx = inter / 60 / count_unit;
                        break;
                }
                switch (unit) {
                    case UNIT_H:
                        float cm = h_current * 60 + m_current
                                + distanceX / dx * (sPerUnit / 60 / fps);
                        cm = Math.max(0, cm);

                        h_current = (int) (cm / 60);

                        // 到 24:00:00，分钟必须为 0
                        m_current = (h_current == 24) ? 0 : (int) (cm % 60);

                        s_current = 0; // 小时模式下不关心秒
                        fps_current = 0;

                        break;

                    case UNIT_M:
                        /**
                         *   //1小时可以分成60格分钟
                         *                         if (inter > inter_max  * 60) {
                         *                             unit = UNIT_S;
                         *                         } else if (inter <= inter_max) {
                         *                             unit = UNIT_H;
                         *                         }
                         *  故而distanceX需要*count_unit，否则滑不动
                         */
                        int cs = (int) (h_current * 3600 + m_current * 60 + s_current
                                + distanceX / dx * (sPerUnit / fps));
                        cs = Math.max(0, cs);

                        h_current = cs / 3600;

                        m_current = (h_current == 24) ? 0 : (cs % 3600) / 60;
                        s_current = (h_current == 24) ? 0 : cs % 60;
                        fps_current = 0;

                        break;
                    case UNIT_S:
                        //同上 distanceX需要*60*count_unit，否则滑不动,sPerUnit=1时，sPerUnit / fps 直接=0了，搞毛，故而不能sPerUnit/fps*fps
                        //往左拖动，距离必须*4，否则拖不动，
                        float dd=distanceX<0?distanceX:distanceX*4;
                        int cfps = (int) (h_current * 3600 * fps + m_current * 60 * fps + s_current * fps + fps_current
                        + dd / dx * sPerUnit);

                        cfps = Math.max(0, cfps);

                        h_current = cfps / fps / 3600;

                        m_current = (h_current == 24) ? 0 : (cfps / fps % 3600) / 60;
                        s_current = (h_current == 24) ? 0 : cfps / fps % 60;
                        fps_current = (h_current == 24) ? 0 : cfps % fps;

                        break;
                }

                if (callback != null)
                    callback.onTimeSelected(h_current, m_current, s_current, fps_current);
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
                    animator.setInterpolator(new DecelerateInterpolator());
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
                inter = Math.min(inter_max * 3600, zoom <= 1 ? inter_min : (inter_min + zoom * zoom_h_ratio));
                //unit为秒时，不能无限增大zoom，否则会导致超过放大限制后再放小，需要双指缩放很久才能放小
                if (inter >= inter_max * 3600) zoom = z;

                int unit_last = unit;
                switch (unit) {
                    case UNIT_H:
                        //一小格超过inter_max就到分钟模式
                        if (inter > inter_max) {
                            unit = UNIT_M;
                        }
                        break;
                    case UNIT_M:
                        //1小时可以分成60格分钟
                        if (inter > inter_max * fps * 60 / fps) {
                            unit = UNIT_S;
                        } else if (inter <= inter_max) {
                            unit = UNIT_H;
                        }
                        break;
                    case UNIT_S:
                        if (inter <= inter_max * fps * 60 / fps) {
                            unit = UNIT_M;
                        } else if (inter <= inter_max) {
                            unit = UNIT_H;
                        }
                        break;
                }
                switch (unit) {
                    case UNIT_H:
                        count_unit = 1;
                        // 每 fps 个格子有多少秒 → 1小时 = 3600 秒
                        sPerUnit = 3600;
                        break;
                    case UNIT_M:
                        //1小时可以分多少格分钟，n=2就分出 :30:  n=3 就分出 :20: :40:
                        count_unit = unit_last == UNIT_M && inter / count_unit <= inter_max && inter / count_unit >= inter_min ?
                                count_unit
                                : (int) (inter / inter_min);

                        for (int ll = listDivisors60.size() - 1; ll >= 0; ll--) {
                            if (count_unit >= listDivisors60.get(ll)) {
                                count_unit = listDivisors60.get(ll);
                                break;
                            }
                        }
                        // 每 fps 个格子有多少秒 → 1分钟 = 60 秒
                        sPerUnit = 3600 / count_unit;
                        break;
                    case UNIT_S:
                        //1分钟可以分多少格秒，n=2就分出 ::30  n=3 就分出 ::20 ::40
                        count_unit = unit_last == UNIT_S && inter / count_unit / 60 <= inter_max && inter / count_unit / 60 >= inter_min ?
                                count_unit
                                : (int) (inter / inter_min / 60);
                        for (int ll = listDivisors60.size() - 1; ll >= 0; ll--) {
                            if (count_unit >= listDivisors60.get(ll)) {
                                count_unit = listDivisors60.get(ll);
                                break;
                            }
                        }
                        // 每 fps 个格子有多少秒 → 1秒 = 1 秒
                        sPerUnit = 3600 / 60 / count_unit;
                        break;
                }
                invalidate();
                return true;
            }
        });
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void setHeight_line_long(float height_line_long) {
        this.height_line_long = height_line_long;
    }

    public void setHeight_line_short(float height_line_short) {
        this.height_line_short = height_line_short;
    }
    public void setHeight_line_indicator(float height_line_indicator) {
        this.height_line_indicator = height_line_indicator;
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
                if (m * 60 * fps < sPerUnit) {
                    canvas.drawLine(startX, startY, startX, height_line_long, paintLine);
                    String text = String.format("%02d:00:00", h);
                    RectF rectF_text = TextUtils.getTextRectF(false, 1, paintText, text, startX, height_line_long + tv_margin_top);
                    TextUtils.drawText(false, 1, canvas, paintText, text, startX, height_line_indicator+2*radius_indicator + tv_margin_top, rectF_text);
                } else {
                    canvas.drawLine(startX, startY, startX, height_line_short, paintLine);
                }
                break;

            case UNIT_M: // 绘制分钟刻度
                //不够一小格了，当然是指向分钟
                //count_unit=4  sPerUnit = 3600 / count_unit;一个unit 15分钟  一个小inter 1分钟
                //count_unit=5  sPerUnit = 3600 / count_unit;一个unit 12分钟  一个小inter <1分钟  故而必须用秒算
                if ((m * 60 + s) % sPerUnit * fps < sPerUnit) {
                    canvas.drawLine(startX, startY, startX, height_line_long, paintLine);
                    String text = String.format("%02d:%02d:00", h, m);
                    RectF rectF_text = TextUtils.getTextRectF(false, 1, paintText, text, startX, height_line_long + tv_margin_top);
                    TextUtils.drawText(false, 1, canvas, paintText, text, startX,  height_line_indicator+2*radius_indicator + tv_margin_top, rectF_text);
                } else {
                    canvas.drawLine(startX, startY, startX, height_line_short, paintLine);
                }
                break;

            case UNIT_S: // 绘制秒刻度
                //不够一小格了，当然是指向秒
                //count_unit=4  sPerUnit =3600 / 60 / count_unit;一个unit 15秒  一个小inter 1秒
                //count_unit=5  sPerUnit =3600 / 60 / count_unit;一个unit 12秒 一个小inter <1秒  故而必须用帧算
                if ((s * fps + frame) % (sPerUnit * fps) * fps / fps < sPerUnit) {
                    canvas.drawLine(startX, startY, startX, height_line_long, paintLine);
                    String text = String.format("%02d:%02d:%02d", h, m, s);
                    RectF rectF_text = TextUtils.getTextRectF(false, 1, paintText, text, startX, height_line_long + tv_margin_top);
                    TextUtils.drawText(false, 1, canvas, paintText, text, startX,  height_line_indicator+2*radius_indicator + tv_margin_top, rectF_text);
                } else { // 秒以下只画短线（帧）
                    canvas.drawLine(startX, startY, startX, height_line_short, paintLine);
                }
                break;
        }
    }

    /**
     * count_unit = (int) (inter / inter_max);
     * for (int ll = listDivisors60.size() - 1; ll >= 0; ll--) {
     * if (count_unit >= listDivisors60.get(ll)) {
     * count_unit = listDivisors60.get(ll);
     * break;
     * }
     * }
     * // 每 fps 个格子有多少秒 → 1分钟 = 60 秒
     * sPerUnit = 3600 / count_unit;
     * break;
     * case UNIT_S:
     * //1分钟可以分多少格秒，n=2就分出 ::30  n=3 就分出 ::20 ::40
     * count_unit = (int) (inter / inter_max / 60);
     *
     * @param canvas the canvas on which the background will be drawn
     */
    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        //防止尚未scale inter为0
        inter = Math.max(inter, inter_min);

        float startX = getWidth() * 0.5f;
        float startY = 0;
        float dx = inter;
        switch (unit) {
            case UNIT_M:
                dx = inter / count_unit;
                break;
            case UNIT_S:
                dx = inter / 60 / count_unit;
                break;
        }
// 每小格多少帧
        int frameStep = sPerUnit * fps / fps;
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
        for (float x = startX - dx; x >= 0; x -= dx) {
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
        for (float x = startX + dx; x <= getWidth(); x += dx) {
            totalFrames += frameStep;
            if (totalFrames >= 24 * 3600 * fps + frameStep) break;

            int h = totalFrames / (3600 * fps);
            int m = (totalFrames / (60 * fps)) % 60;
            int s = (totalFrames / fps) % 60;
            int f = totalFrames % fps;

            drawTime(canvas, h, m, s, f, x, startY);
        }

        canvas.drawLine(getWidth() * 0.5f, 0, getWidth() * 0.5f, height_line_indicator, paintIndicator);
        canvas.drawCircle(getWidth() * 0.5f, height_line_indicator +radius_indicator, radius_indicator, paintIndicator);
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

    public static interface Callback {
        public void onTimeSelected(int h_current, int m_current, int s_current, int fps_current);
    }
}

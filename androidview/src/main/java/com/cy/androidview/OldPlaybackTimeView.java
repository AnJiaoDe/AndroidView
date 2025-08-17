//package com.cy.androidview;
//
//import android.animation.ValueAnimator;
//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.Paint;
//import android.graphics.RectF;
//import android.util.AttributeSet;
//import android.view.GestureDetector;
//import android.view.MotionEvent;
//import android.view.ScaleGestureDetector;
//import android.view.View;
//import android.view.ViewConfiguration;
//
//import androidx.annotation.ColorInt;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//
//import java.util.List;
//
//public class OldPlaybackTimeView extends View {
//    private Paint paintText;
//    private Paint paintLine;
//    private Paint paintIndicator;
//    private float height_line_long;
//    private float height_line_short;
//    private float inter_min;
//    private float inter_max;
//    private int fps;
//    private float tv_margin_top;
//    private final int UNIT_H = 0;
//    private final int UNIT_M = 1;
//    private final int UNIT_S = 2;
//    private int unit = UNIT_H;
//    private GestureDetector gestureDetector;
//    private ScaleGestureDetector scaleGestureDetector;
//    private float dx;
//    private float velocityThreshold;
//    private float maxVelocity;
//    private float minVelocity;
//    private float radius_indicator;
//    private float zoom = 1; // 缩放
//    private boolean onScaling = false;
//    private float zoom_h_ratio;
//    private List<Integer> listDivisors60;
//    private float inter;
//    private int h_current;
//    private int m_current;
//    private int s_current;
//    private int fps_current;
//
//    public OldPlaybackTimeView(Context context) {
//        this(context, null);
//    }
//
//    public OldPlaybackTimeView(Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
//
//        listDivisors60 = MyMathUtils.getDivisors(60);
//
//        paintLine = new Paint();
//        paintLine.setAntiAlias(true);
//
//        paintText = new Paint();
//        paintText.setAntiAlias(true);
//
//        paintIndicator = new Paint();
//        paintIndicator.setAntiAlias(true);
//
//        setHeight_line_long(ScreenUtils.dpAdapt(context, 34));
//        setHeight_line_short(ScreenUtils.dpAdapt(context, 20));
//
//        setInter_min(ScreenUtils.dpAdapt(context, 6));
//        setInter_max(ScreenUtils.dpAdapt(context, 24));
//        setFps(15);
//        setTv_margin_top(ScreenUtils.dpAdapt(context, 10));
//        setTextSize(ScreenUtils.spAdapt(context, 8));
//        setIndicatorColor(0xff4099FF);
//        setIndicatorWidth(ScreenUtils.dpAdapt(context, 2));
//        setIndicatorRadius(ScreenUtils.dpAdapt(context, 4));
//        setPaintLineWidth(ScreenUtils.dpAdapt(context, 0.52f));
//        setZoom_h_ratio(2);
//
//        final ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
//        maxVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
//        minVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
//        velocityThreshold = minVelocity + (maxVelocity - minVelocity) * 0.4f;
//
//        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
//            @Override
//            public boolean onScroll(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
//                if (!onScaling) {
//                    dx -= distanceX;
//                    invalidate();
//                }
//                return super.onScroll(e1, e2, distanceX, distanceY);
//            }
//
//            @Override
//            public boolean onFling(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
//                if (!onScaling && Math.abs(velocityX) > Math.abs(velocityThreshold)) {
//                    ValueAnimator animator = ValueAnimator.ofFloat(1, 0);
//                    animator.setDuration(1000);
//                    animator.addUpdateListener(animation -> {
//                        float f = 70 * (float) animation.getAnimatedValue();
//                        dx = dx + (velocityX > 0 ? f : -f);
//                        invalidate();
//                    });
//                    animator.start();
//                }
//                return super.onFling(e1, e2, velocityX, velocityY);
//            }
//        });
//        scaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleGestureDetector.SimpleOnScaleGestureListener() {
//            @Override
//            public boolean onScale(ScaleGestureDetector detector) {
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
//                return true;
//            }
//        });
//    }
//
//    public void setHeight_line_long(float height_line_long) {
//        this.height_line_long = height_line_long;
//    }
//
//    public void setHeight_line_short(float height_line_short) {
//        this.height_line_short = height_line_short;
//    }
//
//    public void setInter_min(float inter_min) {
//        this.inter_min = inter_min;
//    }
//
//    public void setInter_max(float inter_max) {
//        this.inter_max = inter_max;
//    }
//
//    public void setFps(int fps) {
//        this.fps = fps;
//    }
//
//    public void setTv_margin_top(float tv_margin_top) {
//        this.tv_margin_top = tv_margin_top;
//    }
//
//    public void setTextSize(float textSize) {
//        paintText.setTextSize(textSize);
//    }
//
//    public void setIndicatorColor(@ColorInt int color) {
//        paintIndicator.setColor(color);
//    }
//
//    public void setIndicatorWidth(int width) {
//        paintIndicator.setStrokeWidth(width);
//    }
//
//    public void setPaintLineWidth(int width) {
//        paintLine.setStrokeWidth(width);
//    }
//
//    public void setIndicatorRadius(float radius) {
//        this.radius_indicator = radius;
//    }
//
//    public void setZoom_h_ratio(float zoom_h_ratio) {
//        this.zoom_h_ratio = zoom_h_ratio;
//    }
//
//    public void setTimeSelect(int h_current, int m_current, int s_current, int fps_current) {
//        this.h_current = h_current;
//        this.m_current = m_current;
//        this.s_current = s_current;
//        this.fps_current = fps_current;
//    }
//
//    public int getH_current() {
//        return h_current;
//    }
//
//    public int getM_current() {
//        return m_current;
//    }
//
//    public int getS_current() {
//        return s_current;
//    }
//
//    public int getFps_current() {
//        return fps_current;
//    }
//
//    @Override
//    protected void onDraw(@NonNull Canvas canvas) {
//        super.onDraw(canvas);
//        //防止尚未scale inter为0
//        inter = Math.max(inter, inter_min);
//        switch (unit) {
//            case UNIT_H:
//                if (inter > inter_max) {
//                    unit = UNIT_M;
//                }
//                break;
//            case UNIT_M:
//                //由于每小时，每分钟都是最多绘制fps格，当格子宽度超过inter_max,就开始往分秒细化，故而用fps判断
//                if (inter > inter_max * fps) {
//                    unit = UNIT_S;
//                }
//                break;
//            case UNIT_S:
//                if (inter <= inter_max) {
//                    unit = UNIT_H;
//                } else if (inter <= inter_max * fps) {
//                    unit = UNIT_M;
//                }
//                break;
//        }
//        dx = -inter * fps * (h_current + m_current * 1.f / 60 + s_current * 1.f / 3600 + fps_current * 1.f / 3600 / fps);
//
//        dx = Math.max(-inter * fps * 24, Math.min(0, dx));
//        float startX = getWidth() * 0.5f + dx;
//        float startY = 0;
//        for (int h = 0; h <= 24; h++) {
//            float sX = startX + h * inter * fps;
//            //超出边界的不能绘制，不然容易卡顿，如果下一个都还是<0，说明现在这个不用绘制
//            if (startX + (h + 1) * inter * fps < 0) continue;
//            if (sX > getWidth()) break;
//
//            canvas.drawLine(sX, startY, sX, height_line_long, paintLine);
//            String text;
//            text = (h < 10 ? "0" + h : h) + ":00:00";
//            RectF rectF_text = TextUtils.getTextRectF(false, 1, paintText, text, sX, height_line_long + tv_margin_top);
//            TextUtils.drawText(false, 1, canvas, paintText, text, sX, height_line_long + tv_margin_top, rectF_text);
//
//            if (h == 24) break;
//
//            switch (unit) {
//                case UNIT_H:
//                    for (int jj = 1; jj < fps; jj++) {
//                        float x = sX + jj * inter;
//
//                        //超出边界的不能绘制，不然容易卡顿，如果下一个都还是<0，说明现在这个不用绘制
//                        if (sX + (jj + 1) * inter < 0) continue;
//                        if (x > getWidth()) break;
//
//                        canvas.drawLine(x, startY, x, height_line_short, paintLine);
//                    }
//                    break;
//                case UNIT_M:
//                    //1小时可以分多少格分钟，n=2就分出 :30:  n=3 就分出 :20: :40:
//                    int n = (int) (inter / inter_min);
//                    for (int ll = listDivisors60.size() - 1; ll >= 0; ll--) {
//                        if (n >= listDivisors60.get(ll)) {
//                            n = listDivisors60.get(ll);
//                            break;
//                        }
//                    }
//                    for (int jj = 1; jj < fps * n; jj++) {
//                        float x = sX + jj * inter / n;
//
//                        //超出边界的不能绘制，不然容易卡顿，如果下一个都还是<0，说明现在这个不用绘制
//                        if (sX + (jj + 1) * inter / n < 0) continue;
//                        if (x > getWidth()) break;
//
//                        if (jj % fps == 0) {
//                            canvas.drawLine(x, startY, x, height_line_long, paintLine);
//                            int m = 60 / n * jj / fps;
//                            text = (h < 10 ? "0" + h : h) + ":" + (m < 10 ? "0" + m : m) + ":00";
//                            rectF_text = TextUtils.getTextRectF(false, 1, paintText, text, x, height_line_long + tv_margin_top);
//                            TextUtils.drawText(false, 1, canvas, paintText, text, x, height_line_long + tv_margin_top, rectF_text);
//                        } else {
//                            canvas.drawLine(x, startY, x, height_line_short, paintLine);
//                        }
//                    }
//                    break;
//                case UNIT_S:
//                    int n__ = 60;
//                    for (int jj = 0; jj <= n__; jj++) {
//                        float x = sX + jj * inter / n__ * fps;
//                        //超出边界的不能绘制，不然容易卡顿，如果下一个都还是<0，说明现在这个不用绘制
//                        if (sX + (jj + 1) * inter / n__ * fps < 0) continue;
//                        if (x > getWidth()) break;
//                        //画分钟
//                        canvas.drawLine(x, startY, x, height_line_long, paintLine);
//                        int m = 60 / n__ * jj;
//                        text = (h < 10 ? "0" + h : h) + ":" + (m < 10 ? "0" + m : m) + ":00";
//                        rectF_text = TextUtils.getTextRectF(false, 1, paintText, text, x, height_line_long + tv_margin_top);
//                        TextUtils.drawText(false, 1, canvas, paintText, text, x, height_line_long + tv_margin_top, rectF_text);
//
//                        if (jj == n__) break;
//                        //1分钟可以分多少格秒，n=2就分出 ::30  n=3 就分出 ::30 ::40
//                        int N = (int) (inter / inter_min / n__);
//                        for (int ll = listDivisors60.size() - 1; ll >= 0; ll--) {
//                            if (N >= listDivisors60.get(ll)) {
//                                N = listDivisors60.get(ll);
//                                break;
//                            }
//                        }
//                        for (int ss = 1; ss < fps * N; ss++) {
//                            float xxxx = x + ss * inter / n__ / N;
//                            //超出边界的不能绘制，不然容易卡顿，如果下一个都还是<0，说明现在这个不用绘制
//                            if (x + (ss + 1) * inter / n__ / N < 0) continue;
//                            if (xxxx > getWidth()) break;
//                            if (ss % fps == 0) {
//                                canvas.drawLine(xxxx, startY, xxxx, height_line_long, paintLine);
//                                int s = 60 / N * ss / fps;
//                                text = (h < 10 ? "0" + h : h) + ":" + (m < 10 ? "0" + m : m) + ":" + (s < 10 ? "0" + s : s);
//                                rectF_text = TextUtils.getTextRectF(false, 1, paintText, text, xxxx, height_line_long + tv_margin_top);
//                                TextUtils.drawText(false, 1, canvas, paintText, text, xxxx, height_line_long + tv_margin_top, rectF_text);
//                            } else {
////                                canvas.drawLine(xxxx, startY, xxxx, N > 1 ? height_line_fps : height_line_s, paintLine);
//                                canvas.drawLine(xxxx, startY, xxxx, height_line_short, paintLine);
//                            }
//                        }
//                    }
//
//                    break;
//            }
//        }
//        canvas.drawLine(getWidth() * 0.5f, 0, getWidth() * 0.5f, getHeight() * 0.5f, paintIndicator);
//        canvas.drawCircle(getWidth() * 0.5f, getHeight() * 0.5f, radius_indicator, paintIndicator);
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        gestureDetector.onTouchEvent(event);
//        scaleGestureDetector.onTouchEvent(event);
//
//        switch (event.getActionMasked()) {
//            case MotionEvent.ACTION_DOWN:
//                onScaling = false;
//                break;
//            case MotionEvent.ACTION_POINTER_DOWN:
//                break;
//            case MotionEvent.ACTION_MOVE:
//                break;
//            case MotionEvent.ACTION_CANCEL:
//                break;
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_POINTER_UP:
//                break;
//        }
//        return true;
//    }
//
//}

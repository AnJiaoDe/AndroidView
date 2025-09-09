package com.cy.androidview.loadingview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Four2 extends View {
    private Paint paint;
    private Paint textPaint;
    private float radius;
    private float rotation;
    private float trans;
    private float[] scale = new float[4]; // 小球缩放
    private float textPulseScale = 1f;   // 文字呼吸缩放

    private int[] colors = new int[4];
    private float globalHue = 0;

    private float[] haloRadius = new float[4];
    private int[] haloAlpha = new int[4];

    private static final int MAX_TRAIL_POINTS = 30;
    private List<PointF>[] trailPoints = new ArrayList[4];

    private BlurMaskFilter strongBlur;
    private BlurMaskFilter softBlur;

    private List<Particle> particles = new ArrayList<>();
    private Random random = new Random();

    public Four2(Context context) {
        this(context, null);
    }

    public Four2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(dp2px(context, 28));
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD));

        setRadius(dp2px(context, 6));

        strongBlur = new BlurMaskFilter(dp2px(context, 18), BlurMaskFilter.Blur.SOLID);
        softBlur = new BlurMaskFilter(dp2px(context, 8), BlurMaskFilter.Blur.SOLID);

        initTrails();
        initAnimators();
        initHaloAnimators();
        initRainbowAnimator();
        initPulseAnimators();
        initTextPulseAnimator();
    }

    private void initTrails() {
        for (int i = 0; i < 4; i++) {
            trailPoints[i] = new ArrayList<>();
        }
    }

    private void initAnimators() {
        ValueAnimator rotationAnimator = ValueAnimator.ofFloat(0, 360);
        rotationAnimator.setRepeatCount(ValueAnimator.INFINITE);
        rotationAnimator.setDuration(2500);
        rotationAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        rotationAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                rotation = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        rotationAnimator.start();

        ValueAnimator transAnimator = ValueAnimator.ofFloat(0.6f, 1.2f, 0.6f);
        transAnimator.setRepeatCount(ValueAnimator.INFINITE);
        transAnimator.setDuration(2000);
        transAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        transAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                trans = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        transAnimator.start();
    }

    private void initHaloAnimators() {
        for (int i = 0; i < 4; i++) {
            final int index = i;
            ValueAnimator haloAnimator = ValueAnimator.ofFloat(0, radius * 2);
            haloAnimator.setRepeatCount(ValueAnimator.INFINITE);
            haloAnimator.setDuration(1200);
            haloAnimator.setStartDelay(i * 300);
            haloAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            haloAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();
                    haloRadius[index] = value;
                    haloAlpha[index] = (int) (200 * (1 - value / (radius * 2)));
                    invalidate();
                }
            });
            haloAnimator.start();
        }
    }

    private void initRainbowAnimator() {
        ValueAnimator rainbow = ValueAnimator.ofFloat(0, 360);
        rainbow.setRepeatCount(ValueAnimator.INFINITE);
        rainbow.setDuration(4000);
        rainbow.setInterpolator(new AccelerateDecelerateInterpolator());
        rainbow.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                globalHue = (float) animation.getAnimatedValue();
                for (int i = 0; i < 4; i++) {
                    float hue = (globalHue + i * 90) % 360;
                    colors[i] = Color.HSVToColor(new float[]{hue, 0.9f, 1f});
                }
                invalidate();
            }
        });
        rainbow.start();
    }

    private void initPulseAnimators() {
        for (int i = 0; i < 4; i++) {
            final int index = i;
            ValueAnimator pulse = ValueAnimator.ofFloat(0.6f, 1.4f, 0.6f);
            pulse.setRepeatCount(ValueAnimator.INFINITE);
            pulse.setDuration(1000);
            pulse.setStartDelay(i * 200);
            pulse.setInterpolator(new AccelerateDecelerateInterpolator());
            pulse.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    scale[index] = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            pulse.start();
        }
    }

    private void initTextPulseAnimator() {
        ValueAnimator textPulse = ValueAnimator.ofFloat(0.85f, 1.15f, 0.85f);
        textPulse.setRepeatCount(ValueAnimator.INFINITE);
        textPulse.setDuration(1500);
        textPulse.setInterpolator(new AccelerateDecelerateInterpolator());
        textPulse.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                textPulseScale = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        textPulse.start();
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    private float dp2px(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);
        canvas.rotate(rotation, getWidth() * 0.5f, getHeight() * 0.5f);

        float cx, cy;
        for (int i = 0; i < 4; i++) {
            cx = getWidth() * 0.5f + radius * 3 * (i <= 1 ? -1 : 1) * trans;
            cy = getHeight() * 0.5f + radius * 3 * (i == 0 || i == 3 ? -1 : 1) * trans;

            // 拖尾
            trailPoints[i].add(new PointF(cx, cy));
            if (trailPoints[i].size() > MAX_TRAIL_POINTS) trailPoints[i].remove(0);
            if (trailPoints[i].size() > 1) {
                Paint trailPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                trailPaint.setStyle(Paint.Style.STROKE);
                trailPaint.setStrokeCap(Paint.Cap.ROUND);
                trailPaint.setStrokeWidth(radius * 0.6f);

                Path trailPath = new Path();
                for (int j = 0; j < trailPoints[i].size(); j++) {
                    PointF p = trailPoints[i].get(j);
                    if (j == 0) trailPath.moveTo(p.x, p.y);
                    else trailPath.lineTo(p.x, p.y);
                }
                int headColor = colors[i];
                int tailColor = Color.argb(0, Color.red(colors[i]), Color.green(colors[i]), Color.blue(colors[i]));
                Shader shader = new LinearGradient(trailPoints[i].get(0).x, trailPoints[i].get(0).y,
                        cx, cy, tailColor, headColor, Shader.TileMode.CLAMP);
                trailPaint.setShader(shader);
                trailPaint.setMaskFilter(softBlur);
                canvas.drawPath(trailPath, trailPaint);
                trailPaint.setMaskFilter(strongBlur);
                canvas.drawPath(trailPath, trailPaint);
            }

            // 光环
            if (haloRadius[i] > 0) {
                Paint haloPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                haloPaint.setStyle(Paint.Style.STROKE);
                haloPaint.setStrokeWidth(radius * 0.5f);
                haloPaint.setColor(colors[i]);
                haloPaint.setAlpha(haloAlpha[i]);
                haloPaint.setMaskFilter(softBlur);
                canvas.drawCircle(cx, cy, haloRadius[i], haloPaint);
                haloPaint.setMaskFilter(strongBlur);
                canvas.drawCircle(cx, cy, haloRadius[i], haloPaint);
            }

            // 小球
            paint.setColor(colors[i]);
            paint.setMaskFilter(strongBlur);
            canvas.drawCircle(cx, cy, radius * scale[i], paint);
            paint.setMaskFilter(softBlur);
            canvas.drawCircle(cx, cy, radius * scale[i], paint);
        }

        // 绘制文字
        String text = "LOADING...";
        float x = getWidth() *0.25f;
        float y = getHeight() / 2f - (textPaint.descent() + textPaint.ascent()) / 2;
        canvas.save();
        canvas.scale(textPulseScale, textPulseScale, x, y);
        for (int i = 0; i < 360; i += 60) {
            int hue = (int) ((globalHue + i) % 360);
            int color = Color.HSVToColor(new float[]{hue, 0.9f, 1f});
            textPaint.setColor(color);
            textPaint.setMaskFilter(softBlur);
            canvas.drawText(text, x, y, textPaint);
            textPaint.setMaskFilter(strongBlur);
            canvas.drawText(text, x, y, textPaint);
        }
        canvas.restore();

        // 绘制粒子
        for (int i = particles.size() - 1; i >= 0; i--) {
            Particle p = particles.get(i);
            paint.setColor(p.color);
            paint.setAlpha(p.alpha);
            canvas.drawCircle(p.x, p.y, p.size, paint);
            p.update();
            if (p.alpha <= 0) particles.remove(i);
        }

        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            createParticles(event.getX(), event.getY());
            return true;
        }
        return super.onTouchEvent(event);
    }

    private void createParticles(float x, float y) {
        for (int i = 0; i < 30; i++) {
            particles.add(new Particle(x, y, colors[random.nextInt(4)]));
        }
    }

    private class Particle {
        float x, y;
        float vx, vy;
        float size;
        int color;
        int alpha;

        Particle(float x, float y, int color) {
            this.x = x;
            this.y = y;
            this.color = color;
            this.alpha = 255;
            this.size = dp2px(getContext(), random.nextInt(3) + 2);
            float angle = (float) (random.nextFloat() * 2 * Math.PI);
            float speed = random.nextFloat() * dp2px(getContext(), 4) + dp2px(getContext(), 2);
            vx = (float) Math.cos(angle) * speed;
            vy = (float) Math.sin(angle) * speed;
        }

        void update() {
            x += vx;
            y += vy;
            alpha -= 8;
        }
    }
}

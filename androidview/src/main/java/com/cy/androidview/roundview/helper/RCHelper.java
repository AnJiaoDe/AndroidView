package com.cy.androidview.roundview.helper;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Build;
import android.view.View;
import android.widget.Checkable;

import com.cy.androidview.R;

import java.util.ArrayList;

public class RCHelper {

    public float[] radii = new float[8];

    public Path mClipPath;

    public Paint mPaint;

    public boolean mRoundAsCircle = false;

    public int mDefaultStrokeColor;

    public int mStrokeColor;

    public ColorStateList mStrokeColorStateList;

    public int mStrokeWidth;

    public boolean mClipBackground;

    public Region mAreaRegion;

    public RectF mLayer;

    // Stroke 四边开关
    public boolean mStrokeTop = true;
    public boolean mStrokeRight = true;
    public boolean mStrokeBottom = true;
    public boolean mStrokeLeft = true;


    public void initAttrs(
            Context context,
            TypedArray typedArray) {

        mRoundAsCircle = typedArray.getBoolean(
                R.styleable.AttrsRound_cy_round_as_circle,
                false
        );

        mStrokeColorStateList = typedArray.getColorStateList(
                R.styleable.AttrsRound_cy_stroke_color
        );

        if (mStrokeColorStateList != null) {

            mStrokeColor =
                    mStrokeColorStateList.getDefaultColor();

            mDefaultStrokeColor =
                    mStrokeColorStateList.getDefaultColor();

        } else {

            mStrokeColor = Color.WHITE;
            mDefaultStrokeColor = Color.WHITE;
        }


        mStrokeWidth = typedArray.getDimensionPixelSize(
                R.styleable.AttrsRound_cy_stroke_width,
                0
        );


        mClipBackground = typedArray.getBoolean(
                R.styleable.AttrsRound_cy_clip_background,
                true
        );


        int roundCorner =
                typedArray.getDimensionPixelSize(
                        R.styleable.AttrsRound_cy_radiusCorner,
                        0
                );


        int roundCornerTopLeft =
                typedArray.getDimensionPixelSize(
                        R.styleable.AttrsRound_cy_radiusTopLeft,
                        roundCorner
                );


        int roundCornerTopRight =
                typedArray.getDimensionPixelSize(
                        R.styleable.AttrsRound_cy_radiusTopRight,
                        roundCorner
                );


        int roundCornerBottomLeft =
                typedArray.getDimensionPixelSize(
                        R.styleable.AttrsRound_cy_radiusBottomLeft,
                        roundCorner
                );


        int roundCornerBottomRight =
                typedArray.getDimensionPixelSize(
                        R.styleable.AttrsRound_cy_radiusBottomRight,
                        roundCorner
                );


        radii[0] = roundCornerTopLeft;
        radii[1] = roundCornerTopLeft;

        radii[2] = roundCornerTopRight;
        radii[3] = roundCornerTopRight;

        radii[4] = roundCornerBottomRight;
        radii[5] = roundCornerBottomRight;

        radii[6] = roundCornerBottomLeft;
        radii[7] = roundCornerBottomLeft;


        mStrokeTop = typedArray.getBoolean(
                R.styleable.AttrsRound_cy_stroke_top,
                true
        );

        mStrokeRight = typedArray.getBoolean(
                R.styleable.AttrsRound_cy_stroke_right,
                true
        );

        mStrokeBottom = typedArray.getBoolean(
                R.styleable.AttrsRound_cy_stroke_bottom,
                true
        );

        mStrokeLeft = typedArray.getBoolean(
                R.styleable.AttrsRound_cy_stroke_left,
                true
        );


        mLayer = new RectF();

        mClipPath = new Path();

        mAreaRegion = new Region();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mPaint.setColor(Color.WHITE);

        mPaint.setStyle(Paint.Style.FILL);
    }


    public void onSizeChanged(
            View view,
            int w,
            int h) {

        mLayer.set(
                0,
                0,
                w,
                h
        );

        refreshRegion(view);
    }


    public void refreshRegion(View view) {

        int w = (int) mLayer.width();
        int h = (int) mLayer.height();

        if (w <= 0 || h <= 0) {
            return;
        }


        RectF areas = new RectF(
                0,
                0,
                w,
                h
        );


        mClipPath.reset();


        if (mRoundAsCircle) {

            float d = Math.min(
                    areas.width(),
                    areas.height()
            );

            float r = d / 2f;

            PointF center = new PointF(
                    w / 2f,
                    h / 2f
            );


            if (Build.VERSION.SDK_INT
                    <= Build.VERSION_CODES.O_MR1) {

                mClipPath.addCircle(
                        center.x,
                        center.y,
                        r,
                        Path.Direction.CW
                );

                mClipPath.moveTo(0, 0);
                mClipPath.moveTo(w, h);

            } else {

                float y = h / 2f - r;

                mClipPath.moveTo(
                        areas.left,
                        y
                );

                mClipPath.addCircle(
                        center.x,
                        y + r,
                        r,
                        Path.Direction.CW
                );
            }

        } else {

            mClipPath.addRoundRect(
                    areas,
                    radii,
                    Path.Direction.CW
            );
        }


        Region clip = new Region(
                0,
                0,
                w,
                h
        );


        mAreaRegion.setPath(
                mClipPath,
                clip
        );
    }


    /**
     * 绘制 Stroke
     */
    public void onClipDraw(Canvas canvas) {

        if (mStrokeWidth <= 0) {
            return;
        }


        if (mStrokeTop
                && mStrokeRight
                && mStrokeBottom
                && mStrokeLeft) {

            drawFullStroke(canvas);

        } else {

            drawPartialStroke(canvas);
        }
    }


    /**
     * 四边完整 Stroke
     */
    private void drawFullStroke(Canvas canvas) {

        float half = mStrokeWidth / 2f;

        Path path = createInsetRoundPath(half);

        prepareStrokePaint();

        canvas.drawPath(
                path,
                mPaint
        );
    }


    /**
     * 部分边 Stroke
     *
     * 核心：
     *
     * 某一条边关闭后，相邻的 Stroke 延伸到 View 的真实边界。
     *
     * bottom=false：
     *
     * left/right -> y = height
     *
     * top=false：
     *
     * left/right -> y = 0
     *
     * left=false：
     *
     * top/bottom -> x = 0
     *
     * right=false：
     *
     * top/bottom -> x = width
     */
    private void drawPartialStroke(Canvas canvas) {

        if (mRoundAsCircle) {
            drawCirclePartialStroke(canvas);
            return;
        }


        float width = mLayer.width();
        float height = mLayer.height();

        if (width <= 0 || height <= 0) {
            return;
        }


        float half = mStrokeWidth / 2f;


        /*
         * 默认：
         *
         * Stroke 中心线向 View 内缩 half。
         */
        float left = half;
        float top = half;
        float right = width - half;
        float bottom = height - half;


        /*
         * 关键逻辑：
         *
         * 如果某条边关闭，
         * 与它相邻的 Stroke 延伸到 View 边界。
         */


        // top 关闭
        if (!mStrokeTop) {
            top = 0;
        }


        // bottom 关闭
        if (!mStrokeBottom) {
            bottom = height;
        }


        // left 关闭
        if (!mStrokeLeft) {
            left = 0;
        }


        // right 关闭
        if (!mStrokeRight) {
            right = width;
        }


        RectF bounds = new RectF(
                left,
                top,
                right,
                bottom
        );


        /*
         * 计算圆角。
         */
        float maxRadius =
                Math.min(
                        width,
                        height
                ) / 2f;


        float topLeftRadius =
                Math.min(
                        Math.min(
                                radii[0],
                                radii[1]
                        ),
                        maxRadius
                );


        float topRightRadius =
                Math.min(
                        Math.min(
                                radii[2],
                                radii[3]
                        ),
                        maxRadius
                );


        float bottomRightRadius =
                Math.min(
                        Math.min(
                                radii[4],
                                radii[5]
                        ),
                        maxRadius
                );


        float bottomLeftRadius =
                Math.min(
                        Math.min(
                                radii[6],
                                radii[7]
                        ),
                        maxRadius
                );


        /*
         * 只有正常的 Stroke 边界才需要减 half。
         *
         * 例如 bottom=false：
         *
         * bottom = height
         *
         * 这时候底部已经是 View 边界，
         * 不再减 half。
         */
        if (mStrokeTop) {

            topLeftRadius =
                    Math.max(
                            0,
                            topLeftRadius - half
                    );

            topRightRadius =
                    Math.max(
                            0,
                            topRightRadius - half
                    );
        }


        if (mStrokeBottom) {

            bottomRightRadius =
                    Math.max(
                            0,
                            bottomRightRadius - half
                    );

            bottomLeftRadius =
                    Math.max(
                            0,
                            bottomLeftRadius - half
                    );
        }


        /*
         * 如果相邻边关闭，
         * 当前边必须直接到达 View 边界。
         *
         * 因此这个角不能再保留圆角。
         */


        // Top 关闭
        if (!mStrokeTop) {

            topLeftRadius = 0;
            topRightRadius = 0;
        }


        // Bottom 关闭
        if (!mStrokeBottom) {

            bottomLeftRadius = 0;
            bottomRightRadius = 0;
        }


        // Left 关闭
        if (!mStrokeLeft) {

            topLeftRadius = 0;
            bottomLeftRadius = 0;
        }


        // Right 关闭
        if (!mStrokeRight) {

            topRightRadius = 0;
            bottomRightRadius = 0;
        }


        /*
         * 创建四条边。
         */
        Path topPath = createTopPath(
                bounds,
                topLeftRadius,
                topRightRadius
        );


        Path rightPath = createRightPath(
                bounds,
                topRightRadius,
                bottomRightRadius
        );


        Path bottomPath = createBottomPath(
                bounds,
                bottomRightRadius,
                bottomLeftRadius
        );


        Path leftPath = createLeftPath(
                bounds,
                bottomLeftRadius,
                topLeftRadius
        );


        prepareStrokePaint();


        if (mStrokeTop) {

            canvas.drawPath(
                    topPath,
                    mPaint
            );
        }


        if (mStrokeRight) {

            canvas.drawPath(
                    rightPath,
                    mPaint
            );
        }


        if (mStrokeBottom) {

            canvas.drawPath(
                    bottomPath,
                    mPaint
            );
        }


        if (mStrokeLeft) {

            canvas.drawPath(
                    leftPath,
                    mPaint
            );
        }
    }


    /**
     * Top Stroke
     */
    private Path createTopPath(
            RectF bounds,
            float topLeftRadius,
            float topRightRadius) {

        Path path = new Path();


        /*
         * 左上角
         */
        if (topLeftRadius > 0) {

            path.moveTo(
                    bounds.left,
                    bounds.top + topLeftRadius
            );

            RectF rect = new RectF(
                    bounds.left,
                    bounds.top,
                    bounds.left + topLeftRadius * 2,
                    bounds.top + topLeftRadius * 2
            );

            path.arcTo(
                    rect,
                    180,
                    90
            );

        } else {

            path.moveTo(
                    bounds.left,
                    bounds.top
            );
        }


        /*
         * 顶边
         */
        path.lineTo(
                bounds.right - topRightRadius,
                bounds.top
        );


        /*
         * 右上角
         */
        if (topRightRadius > 0) {

            RectF rect = new RectF(
                    bounds.right - topRightRadius * 2,
                    bounds.top,
                    bounds.right,
                    bounds.top + topRightRadius * 2
            );

            path.arcTo(
                    rect,
                    -90,
                    90
            );
        }


        return path;
    }


    /**
     * Right Stroke
     */
    private Path createRightPath(
            RectF bounds,
            float topRightRadius,
            float bottomRightRadius) {

        Path path = new Path();


        /*
         * 右上角
         */
        if (topRightRadius > 0) {

            path.moveTo(
                    bounds.right - topRightRadius,
                    bounds.top
            );

            RectF rect = new RectF(
                    bounds.right - topRightRadius * 2,
                    bounds.top,
                    bounds.right,
                    bounds.top + topRightRadius * 2
            );

            path.arcTo(
                    rect,
                    -90,
                    90
            );

        } else {

            path.moveTo(
                    bounds.right,
                    bounds.top
            );
        }


        /*
         * 右边
         */
        path.lineTo(
                bounds.right,
                bounds.bottom - bottomRightRadius
        );


        /*
         * 右下角
         */
        if (bottomRightRadius > 0) {

            RectF rect = new RectF(
                    bounds.right - bottomRightRadius * 2,
                    bounds.bottom - bottomRightRadius * 2,
                    bounds.right,
                    bounds.bottom
            );

            path.arcTo(
                    rect,
                    0,
                    90
            );
        }


        return path;
    }


    /**
     * Bottom Stroke
     */
    private Path createBottomPath(
            RectF bounds,
            float bottomRightRadius,
            float bottomLeftRadius) {

        Path path = new Path();


        /*
         * 右下角
         */
        if (bottomRightRadius > 0) {

            path.moveTo(
                    bounds.right,
                    bounds.bottom - bottomRightRadius
            );

            RectF rect = new RectF(
                    bounds.right - bottomRightRadius * 2,
                    bounds.bottom - bottomRightRadius * 2,
                    bounds.right,
                    bounds.bottom
            );

            path.arcTo(
                    rect,
                    0,
                    90
            );

        } else {

            path.moveTo(
                    bounds.right,
                    bounds.bottom
            );
        }


        /*
         * 底边
         */
        path.lineTo(
                bounds.left + bottomLeftRadius,
                bounds.bottom
        );


        /*
         * 左下角
         */
        if (bottomLeftRadius > 0) {

            RectF rect = new RectF(
                    bounds.left,
                    bounds.bottom - bottomLeftRadius * 2,
                    bounds.left + bottomLeftRadius * 2,
                    bounds.bottom
            );

            path.arcTo(
                    rect,
                    90,
                    90
            );
        }


        return path;
    }


    /**
     * Left Stroke
     */
    private Path createLeftPath(
            RectF bounds,
            float bottomLeftRadius,
            float topLeftRadius) {

        Path path = new Path();


        /*
         * 左下角
         */
        if (bottomLeftRadius > 0) {

            path.moveTo(
                    bounds.left,
                    bounds.bottom - bottomLeftRadius
            );

            RectF rect = new RectF(
                    bounds.left,
                    bounds.bottom - bottomLeftRadius * 2,
                    bounds.left + bottomLeftRadius * 2,
                    bounds.bottom
            );

            path.arcTo(
                    rect,
                    90,
                    90
            );

        } else {

            path.moveTo(
                    bounds.left,
                    bounds.bottom
            );
        }


        /*
         * 左边
         */
        path.lineTo(
                bounds.left,
                bounds.top + topLeftRadius
        );


        /*
         * 左上角
         */
        if (topLeftRadius > 0) {

            RectF rect = new RectF(
                    bounds.left,
                    bounds.top,
                    bounds.left + topLeftRadius * 2,
                    bounds.top + topLeftRadius * 2
            );

            path.arcTo(
                    rect,
                    180,
                    90
            );
        }


        return path;
    }


    /**
     * Stroke Paint
     */
    private void prepareStrokePaint() {

        mPaint.setXfermode(null);

        mPaint.setColor(
                mStrokeColor
        );

        mPaint.setStyle(
                Paint.Style.STROKE
        );

        mPaint.setStrokeWidth(
                mStrokeWidth
        );

        mPaint.setStrokeJoin(
                Paint.Join.ROUND
        );

        mPaint.setStrokeCap(
                Paint.Cap.BUTT
        );
    }


    /**
     * 创建向内缩 half 的完整圆角 Path。
     *
     * 四边都开启时使用。
     */
    private Path createInsetRoundPath(
            float half) {

        float width = mLayer.width();
        float height = mLayer.height();


        RectF rect = new RectF(
                half,
                half,
                width - half,
                height - half
        );


        Path path = new Path();


        if (mRoundAsCircle) {

            float radius =
                    Math.min(
                            rect.width(),
                            rect.height()
                    ) / 2f;


            path.addCircle(
                    rect.centerX(),
                    rect.centerY(),
                    radius,
                    Path.Direction.CW
            );

            return path;
        }


        float maxRadius =
                Math.min(
                        rect.width(),
                        rect.height()
                ) / 2f;


        float topLeft =
                Math.min(
                        Math.min(
                                radii[0],
                                radii[1]
                        ),
                        maxRadius
                );


        float topRight =
                Math.min(
                        Math.min(
                                radii[2],
                                radii[3]
                        ),
                        maxRadius
                );


        float bottomRight =
                Math.min(
                        Math.min(
                                radii[4],
                                radii[5]
                        ),
                        maxRadius
                );


        float bottomLeft =
                Math.min(
                        Math.min(
                                radii[6],
                                radii[7]
                        ),
                        maxRadius
                );


        topLeft = Math.max(
                0,
                topLeft - half
        );

        topRight = Math.max(
                0,
                topRight - half
        );

        bottomRight = Math.max(
                0,
                bottomRight - half
        );

        bottomLeft = Math.max(
                0,
                bottomLeft - half
        );


        float[] insetRadii = new float[]{
                topLeft,
                topLeft,

                topRight,
                topRight,

                bottomRight,
                bottomRight,

                bottomLeft,
                bottomLeft
        };


        path.addRoundRect(
                rect,
                insetRadii,
                Path.Direction.CW
        );


        return path;
    }


    /**
     * 圆形情况下的部分 Stroke
     */
    private void drawCirclePartialStroke(
            Canvas canvas) {

        float half =
                mStrokeWidth / 2f;


        float centerX =
                mLayer.centerX();

        float centerY =
                mLayer.centerY();


        float radius =
                Math.min(
                        mLayer.width(),
                        mLayer.height()
                ) / 2f - half;


        if (radius <= 0) {
            return;
        }


        RectF circleRect = new RectF(
                centerX - radius,
                centerY - radius,
                centerX + radius,
                centerY + radius
        );


        prepareStrokePaint();


        if (mStrokeTop) {

            canvas.drawArc(
                    circleRect,
                    -90,
                    90,
                    false,
                    mPaint
            );
        }


        if (mStrokeRight) {

            canvas.drawArc(
                    circleRect,
                    0,
                    90,
                    false,
                    mPaint
            );
        }


        if (mStrokeBottom) {

            canvas.drawArc(
                    circleRect,
                    90,
                    90,
                    false,
                    mPaint
            );
        }


        if (mStrokeLeft) {

            canvas.drawArc(
                    circleRect,
                    180,
                    90,
                    false,
                    mPaint
            );
        }
    }


    // -------------------------------------------------------------------------
    // Selector 支持
    // -------------------------------------------------------------------------

    public boolean mChecked;

    public OnCheckedChangeListener mOnCheckedChangeListener;


    public void drawableStateChanged(View view) {

        if (view instanceof RCAttrs) {

            ArrayList<Integer> stateListArray =
                    new ArrayList<>();


            if (view instanceof Checkable) {

                stateListArray.add(
                        android.R.attr.state_checkable
                );


                if (((Checkable) view).isChecked()) {

                    stateListArray.add(
                            android.R.attr.state_checked
                    );
                }
            }


            if (view.isEnabled()) {

                stateListArray.add(
                        android.R.attr.state_enabled
                );
            }


            if (view.isFocused()) {

                stateListArray.add(
                        android.R.attr.state_focused
                );
            }


            if (view.isPressed()) {

                stateListArray.add(
                        android.R.attr.state_pressed
                );
            }


            if (view.isHovered()) {

                stateListArray.add(
                        android.R.attr.state_hovered
                );
            }


            if (view.isSelected()) {

                stateListArray.add(
                        android.R.attr.state_selected
                );
            }


            if (view.isActivated()) {

                stateListArray.add(
                        android.R.attr.state_activated
                );
            }


            if (view.hasWindowFocus()) {

                stateListArray.add(
                        android.R.attr.state_window_focused
                );
            }


            if (mStrokeColorStateList != null
                    && mStrokeColorStateList.isStateful()) {

                int[] stateList =
                        new int[stateListArray.size()];


                for (int i = 0;
                     i < stateListArray.size();
                     i++) {

                    stateList[i] =
                            stateListArray.get(i);
                }


                int stateColor =
                        mStrokeColorStateList.getColorForState(
                                stateList,
                                mDefaultStrokeColor
                        );


                ((RCAttrs) view).setStrokeColor(
                        stateColor
                );
            }
        }
    }


    public interface OnCheckedChangeListener {

        void onCheckedChanged(
                View view,
                boolean isChecked
        );
    }
}
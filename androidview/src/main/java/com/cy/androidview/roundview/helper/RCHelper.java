package com.cy.androidview.roundview.helper;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Build;
import android.view.View;
import android.widget.Checkable;

import com.cy.androidview.R;

import java.util.ArrayList;

public class RCHelper {

    public float[] radii = new float[8];   // top-left, top-right, bottom-right, bottom-left
    public Path mClipPath;                 // 剪裁区域路径
    public Paint mPaint;                   // 画笔
    public boolean mRoundAsCircle = false; // 圆形
    public int mDefaultStrokeColor;        // 默认描边颜色
    public int mStrokeColor;               // 描边颜色
    public ColorStateList mStrokeColorStateList;// 描边颜色的状态
    public int mStrokeWidth;               // 描边宽度
    public boolean mClipBackground;        // 是否剪裁背景
    public Region mAreaRegion;             // 内容区域
    public RectF mLayer;                   // 画布图层大小

    // Stroke 四边开关
    public boolean mStrokeTop = true;
    public boolean mStrokeRight = true;
    public boolean mStrokeBottom = true;
    public boolean mStrokeLeft = true;

    public void initAttrs(Context context, TypedArray typedArray) {
        mRoundAsCircle = typedArray.getBoolean(
                R.styleable.AttrsRound_cy_round_as_circle,
                false
        );

        mStrokeColorStateList = typedArray.getColorStateList(
                R.styleable.AttrsRound_cy_stroke_color
        );

        if (null != mStrokeColorStateList) {
            mStrokeColor = mStrokeColorStateList.getDefaultColor();
            mDefaultStrokeColor = mStrokeColorStateList.getDefaultColor();
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

        int roundCorner = typedArray.getDimensionPixelSize(
                R.styleable.AttrsRound_cy_radiusCorner,
                0
        );

        int roundCornerTopLeft = typedArray.getDimensionPixelSize(
                R.styleable.AttrsRound_cy_radiusTopLeft,
                roundCorner
        );

        int roundCornerTopRight = typedArray.getDimensionPixelSize(
                R.styleable.AttrsRound_cy_radiusTopRight,
                roundCorner
        );

        int roundCornerBottomLeft = typedArray.getDimensionPixelSize(
                R.styleable.AttrsRound_cy_radiusBottomLeft,
                roundCorner
        );

        int roundCornerBottomRight = typedArray.getDimensionPixelSize(
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

        // Stroke 四边开关
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

        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setAntiAlias(true);
    }

    public void onSizeChanged(View view, int w, int h) {
        mLayer.set(0, 0, w, h);
        refreshRegion(view);
    }

    public void refreshRegion(View view) {
        int w = (int) mLayer.width();
        int h = (int) mLayer.height();

        RectF areas = new RectF();
        areas.left = view.getPaddingLeft();
        areas.top = view.getPaddingTop();
        areas.right = w - view.getPaddingRight();
        areas.bottom = h - view.getPaddingBottom();

        mClipPath.reset();

        if (mRoundAsCircle) {
            float d = areas.width() >= areas.height()
                    ? areas.height()
                    : areas.width();

            float r = d / 2;

            PointF center = new PointF(w / 2f, h / 2f);

            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {
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
                (int) areas.left,
                (int) areas.top,
                (int) areas.right,
                (int) areas.bottom
        );

        mAreaRegion.setPath(
                mClipPath,
                clip
        );
    }

    public void onClipDraw(Canvas canvas) {

        if (mStrokeWidth > 0) {

            if (mStrokeTop
                    && mStrokeRight
                    && mStrokeBottom
                    && mStrokeLeft) {

                // 四边全部绘制，保持原来的逻辑
                mPaint.setXfermode(
                        new PorterDuffXfermode(
                                PorterDuff.Mode.DST_OUT
                        )
                );

                mPaint.setColor(Color.WHITE);
                mPaint.setStrokeWidth(mStrokeWidth * 2);
                mPaint.setStyle(Paint.Style.STROKE);

                canvas.drawPath(
                        mClipPath,
                        mPaint
                );

                mPaint.setXfermode(
                        new PorterDuffXfermode(
                                PorterDuff.Mode.SRC_OVER
                        )
                );

                mPaint.setColor(mStrokeColor);
                mPaint.setStrokeWidth(mStrokeWidth);
                mPaint.setStyle(Paint.Style.STROKE);

                canvas.drawPath(
                        mClipPath,
                        mPaint
                );

            } else {

                // 部分边绘制
                drawPartialStroke(canvas);
            }
        }

        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setXfermode(null);

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {
            mPaint.setXfermode(
                    new PorterDuffXfermode(
                            PorterDuff.Mode.DST_IN
                    )
            );

            canvas.drawPath(
                    mClipPath,
                    mPaint
            );
        } else {
            mPaint.setXfermode(
                    new PorterDuffXfermode(
                            PorterDuff.Mode.DST_OUT
                    )
            );

            final Path path = new Path();

            path.addRect(
                    0,
                    0,
                    (int) mLayer.width(),
                    (int) mLayer.height(),
                    Path.Direction.CW
            );

            path.op(
                    mClipPath,
                    Path.Op.DIFFERENCE
            );

            canvas.drawPath(
                    path,
                    mPaint
            );
        }

        mPaint.setXfermode(null);
    }

    /**
     * 绘制部分边 Stroke
     *
     * 不再通过 clipRect 截 mClipPath。
     *
     * 原来的 clipRect 会把圆角弧线截断，例如：
     *
     *   Top + Left
     *
     * 左上角的圆弧只能显示一小部分。
     *
     * 这里直接根据圆角半径构造四条独立的 Path，
     * 每一条 Path 都包含对应边以及两侧的圆角。
     */
    private void drawPartialStroke(Canvas canvas) {

        RectF bounds = new RectF();
        mClipPath.computeBounds(bounds, true);

        float left = bounds.left;
        float top = bounds.top;
        float right = bounds.right;
        float bottom = bounds.bottom;

        /*
         * mClipPath 使用的是 radii，
         * 所以这里直接使用 radii。
         *
         * radii:
         *
         * [0] [1] = 左上
         * [2] [3] = 右上
         * [4] [5] = 右下
         * [6] [7] = 左下
         */
        float topLeftRadius = Math.min(
                Math.min(radii[0], radii[1]),
                Math.min(bounds.width(), bounds.height()) / 2f
        );

        float topRightRadius = Math.min(
                Math.min(radii[2], radii[3]),
                Math.min(bounds.width(), bounds.height()) / 2f
        );

        float bottomRightRadius = Math.min(
                Math.min(radii[4], radii[5]),
                Math.min(bounds.width(), bounds.height()) / 2f
        );

        float bottomLeftRadius = Math.min(
                Math.min(radii[6], radii[7]),
                Math.min(bounds.width(), bounds.height()) / 2f
        );

        /*
         * 如果是圆形，
         * 四个角实际上是一整条圆。
         *
         * 圆形情况下，直接使用完整 Path，
         * 再根据四个方向截取对应圆弧。
         */
        if (mRoundAsCircle) {
            drawCirclePartialStroke(
                    canvas,
                    bounds
            );
            return;
        }

        /*
         * =========================================================
         * 构造四条 Stroke Path
         * =========================================================
         */

        Path topPath = new Path();
        Path rightPath = new Path();
        Path bottomPath = new Path();
        Path leftPath = new Path();

        /*
         * ---------------------------------------------------------
         * Top
         *
         * 左上圆弧
         * +
         * 上边
         * +
         * 右上圆弧
         * ---------------------------------------------------------
         */
        topPath.moveTo(
                left + topLeftRadius,
                top
        );

        topPath.lineTo(
                right - topRightRadius,
                top
        );

        if (topRightRadius > 0) {
            RectF topRightRect = new RectF(
                    right - topRightRadius * 2,
                    top,
                    right,
                    top + topRightRadius * 2
            );

            topPath.arcTo(
                    topRightRect,
                    -90,
                    90
            );
        }

        /*
         * ---------------------------------------------------------
         * Right
         *
         * 右上圆弧
         * +
         * 右边
         * +
         * 右下圆弧
         * ---------------------------------------------------------
         */
        rightPath.moveTo(
                right,
                top + topRightRadius
        );

        rightPath.lineTo(
                right,
                bottom - bottomRightRadius
        );

        if (bottomRightRadius > 0) {
            RectF bottomRightRect = new RectF(
                    right - bottomRightRadius * 2,
                    bottom - bottomRightRadius * 2,
                    right,
                    bottom
            );

            rightPath.arcTo(
                    bottomRightRect,
                    0,
                    90
            );
        }

        /*
         * ---------------------------------------------------------
         * Bottom
         *
         * 右下圆弧
         * +
         * 下边
         * +
         * 左下圆弧
         * ---------------------------------------------------------
         */
        bottomPath.moveTo(
                right - bottomRightRadius,
                bottom
        );

        bottomPath.lineTo(
                left + bottomLeftRadius,
                bottom
        );

        if (bottomLeftRadius > 0) {
            RectF bottomLeftRect = new RectF(
                    left,
                    bottom - bottomLeftRadius * 2,
                    left + bottomLeftRadius * 2,
                    bottom
            );

            bottomPath.arcTo(
                    bottomLeftRect,
                    90,
                    90
            );
        }

        /*
         * ---------------------------------------------------------
         * Left
         *
         * 左下圆弧
         * +
         * 左边
         * +
         * 左上圆弧
         * ---------------------------------------------------------
         */
        leftPath.moveTo(
                left,
                bottom - bottomLeftRadius
        );

        leftPath.lineTo(
                left,
                top + topLeftRadius
        );

        if (topLeftRadius > 0) {
            RectF topLeftRect = new RectF(
                    left,
                    top,
                    left + topLeftRadius * 2,
                    top + topLeftRadius * 2
            );

            leftPath.arcTo(
                    topLeftRect,
                    180,
                    90
            );
        }

        /*
         * =========================================================
         * 第一阶段：
         * DST_OUT
         *
         * 把原内容中的 Stroke 区域挖掉。
         * =========================================================
         */

        mPaint.setXfermode(
                new PorterDuffXfermode(
                        PorterDuff.Mode.DST_OUT
                )
        );

        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);

        /*
         * 这里仍然保持你原来的逻辑：
         *
         * Stroke 实际宽度 = mStrokeWidth
         *
         * 清理区域使用 2 倍。
         */
        mPaint.setStrokeWidth(
                mStrokeWidth * 2f
        );

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

        /*
         * =========================================================
         * 第二阶段：
         * SRC_OVER
         *
         * 绘制真正的 Stroke。
         * =========================================================
         */

        mPaint.setXfermode(
                new PorterDuffXfermode(
                        PorterDuff.Mode.SRC_OVER
                )
        );

        mPaint.setColor(mStrokeColor);
        mPaint.setStyle(Paint.Style.STROKE);

        /*
         * 这里是实际 Stroke 宽度。
         */
        mPaint.setStrokeWidth(
                mStrokeWidth
        );

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

        mPaint.setXfermode(null);
    }

    /**
     * 圆形情况下绘制部分 Stroke
     */
    private void drawCirclePartialStroke(
            Canvas canvas,
            RectF bounds
    ) {

        float centerX = bounds.centerX();
        float centerY = bounds.centerY();

        float radius = Math.min(
                bounds.width(),
                bounds.height()
        ) / 2f;

        /*
         * Android Canvas 角度：
         *
         *      -90
         *        |
         *        |
         * 180 ---+--- 0
         *        |
         *        |
         *       90
         *
         * Top    = -90 -> 0
         * Right  = 0 -> 90
         * Bottom = 90 -> 180
         * Left   = 180 -> 270
         */

        RectF circleRect = new RectF(
                centerX - radius,
                centerY - radius,
                centerX + radius,
                centerY + radius
        );

        Path topPath = new Path();
        Path rightPath = new Path();
        Path bottomPath = new Path();
        Path leftPath = new Path();

        if (mStrokeTop) {
            topPath.addArc(
                    circleRect,
                    -90,
                    90
            );
        }

        if (mStrokeRight) {
            rightPath.addArc(
                    circleRect,
                    0,
                    90
            );
        }

        if (mStrokeBottom) {
            bottomPath.addArc(
                    circleRect,
                    90,
                    90
            );
        }

        if (mStrokeLeft) {
            leftPath.addArc(
                    circleRect,
                    180,
                    90
            );
        }

        /*
         * DST_OUT
         */
        mPaint.setXfermode(
                new PorterDuffXfermode(
                        PorterDuff.Mode.DST_OUT
                )
        );

        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(
                mStrokeWidth * 2f
        );

        if (mStrokeTop) {
            canvas.drawPath(topPath, mPaint);
        }

        if (mStrokeRight) {
            canvas.drawPath(rightPath, mPaint);
        }

        if (mStrokeBottom) {
            canvas.drawPath(bottomPath, mPaint);
        }

        if (mStrokeLeft) {
            canvas.drawPath(leftPath, mPaint);
        }

        /*
         * SRC_OVER
         */
        mPaint.setXfermode(
                new PorterDuffXfermode(
                        PorterDuff.Mode.SRC_OVER
                )
        );

        mPaint.setColor(mStrokeColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(
                mStrokeWidth
        );

        if (mStrokeTop) {
            canvas.drawPath(topPath, mPaint);
        }

        if (mStrokeRight) {
            canvas.drawPath(rightPath, mPaint);
        }

        if (mStrokeBottom) {
            canvas.drawPath(bottomPath, mPaint);
        }

        if (mStrokeLeft) {
            canvas.drawPath(leftPath, mPaint);
        }

        mPaint.setXfermode(null);
    }

    //--- Selector 支持 ----------------------------------------------------------------------------

    public boolean mChecked;
    public OnCheckedChangeListener mOnCheckedChangeListener;

    public void drawableStateChanged(View view) {
        if (view instanceof RCAttrs) {
            ArrayList<Integer> stateListArray = new ArrayList<>();

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

                for (int i = 0; i < stateListArray.size(); i++) {
                    stateList[i] = stateListArray.get(i);
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
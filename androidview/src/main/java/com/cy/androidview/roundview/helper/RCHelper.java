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
import com.cy.androidview.roundview.helper.RCAttrs;

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


    public void initAttrs(Context context, TypedArray typedArray) {

        mRoundAsCircle = typedArray.getBoolean(
                R.styleable.AttrsRound_cy_round_as_circle,
                false
        );

        mStrokeColorStateList = typedArray.getColorStateList(
                R.styleable.AttrsRound_cy_stroke_color
        );

        if (mStrokeColorStateList != null) {
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


    public void onSizeChanged(View view, int w, int h) {

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
     * 绘制圆角 View。
     *
     * 重点：
     *
     * Stroke 不再使用 DST_OUT。
     *
     * Stroke 中心线向 View 内缩 halfStroke，
     * 从而保证整个 Stroke 都位于 View 内部。
     */
    public void onClipDraw(Canvas canvas) {

        if (mStrokeWidth > 0) {

            if (mStrokeTop
                    && mStrokeRight
                    && mStrokeBottom
                    && mStrokeLeft) {

                drawFullStroke(canvas);

            } else {

                drawPartialStroke(canvas);
            }
        }
    }


    /**
     * 四边完整 Stroke
     */
    private void drawFullStroke(Canvas canvas) {

        float half = mStrokeWidth / 2f;

        Path path = createInsetRoundPath(
                half
        );

        mPaint.setXfermode(null);

        mPaint.setColor(mStrokeColor);

        mPaint.setStyle(Paint.Style.STROKE);

        mPaint.setStrokeWidth(mStrokeWidth);

        mPaint.setStrokeJoin(Paint.Join.ROUND);

        mPaint.setStrokeCap(Paint.Cap.BUTT);

        canvas.drawPath(
                path,
                mPaint
        );
    }


    /**
     * 部分边 Stroke
     */
    private void drawPartialStroke(Canvas canvas) {

        if (mRoundAsCircle) {

            drawCirclePartialStroke(
                    canvas
            );

            return;
        }

        float half = mStrokeWidth / 2f;

        RectF bounds = new RectF(
                half,
                half,
                mLayer.width() - half,
                mLayer.height() - half
        );

        float maxRadius = Math.min(
                bounds.width(),
                bounds.height()
        ) / 2f;


        float topLeftRadius = Math.min(
                Math.min(
                        radii[0],
                        radii[1]
                ),
                maxRadius
        );

        float topRightRadius = Math.min(
                Math.min(
                        radii[2],
                        radii[3]
                ),
                maxRadius
        );

        float bottomRightRadius = Math.min(
                Math.min(
                        radii[4],
                        radii[5]
                ),
                maxRadius
        );

        float bottomLeftRadius = Math.min(
                Math.min(
                        radii[6],
                        radii[7]
                ),
                maxRadius
        );


        /*
         * 因为整个 Path 向内缩 half，
         * 所以圆角半径也要相应减少 half。
         */
        topLeftRadius = Math.max(
                0,
                topLeftRadius - half
        );

        topRightRadius = Math.max(
                0,
                topRightRadius - half
        );

        bottomRightRadius = Math.max(
                0,
                bottomRightRadius - half
        );

        bottomLeftRadius = Math.max(
                0,
                bottomLeftRadius - half
        );


        Path topPath = new Path();
        Path rightPath = new Path();
        Path bottomPath = new Path();
        Path leftPath = new Path();


        /*
         * Top
         */
        topPath.moveTo(
                bounds.left + topLeftRadius,
                bounds.top
        );

        topPath.lineTo(
                bounds.right - topRightRadius,
                bounds.top
        );

        if (topRightRadius > 0) {

            RectF rect = new RectF(
                    bounds.right - topRightRadius * 2,
                    bounds.top,
                    bounds.right,
                    bounds.top + topRightRadius * 2
            );

            topPath.arcTo(
                    rect,
                    -90,
                    90
            );
        }


        /*
         * Right
         */
        rightPath.moveTo(
                bounds.right,
                bounds.top + topRightRadius
        );

        rightPath.lineTo(
                bounds.right,
                bounds.bottom - bottomRightRadius
        );

        if (bottomRightRadius > 0) {

            RectF rect = new RectF(
                    bounds.right - bottomRightRadius * 2,
                    bounds.bottom - bottomRightRadius * 2,
                    bounds.right,
                    bounds.bottom
            );

            rightPath.arcTo(
                    rect,
                    0,
                    90
            );
        }


        /*
         * Bottom
         */
        bottomPath.moveTo(
                bounds.right - bottomRightRadius,
                bounds.bottom
        );

        bottomPath.lineTo(
                bounds.left + bottomLeftRadius,
                bounds.bottom
        );

        if (bottomLeftRadius > 0) {

            RectF rect = new RectF(
                    bounds.left,
                    bounds.bottom - bottomLeftRadius * 2,
                    bounds.left + bottomLeftRadius * 2,
                    bounds.bottom
            );

            bottomPath.arcTo(
                    rect,
                    90,
                    90
            );
        }


        /*
         * Left
         */
        leftPath.moveTo(
                bounds.left,
                bounds.bottom - bottomLeftRadius
        );

        leftPath.lineTo(
                bounds.left,
                bounds.top + topLeftRadius
        );

        if (topLeftRadius > 0) {

            RectF rect = new RectF(
                    bounds.left,
                    bounds.top,
                    bounds.left + topLeftRadius * 2,
                    bounds.top + topLeftRadius * 2
            );

            leftPath.arcTo(
                    rect,
                    180,
                    90
            );
        }


        mPaint.setXfermode(null);

        mPaint.setColor(mStrokeColor);

        mPaint.setStyle(Paint.Style.STROKE);

        mPaint.setStrokeWidth(
                mStrokeWidth
        );

        mPaint.setStrokeJoin(Paint.Join.ROUND);

        mPaint.setStrokeCap(Paint.Cap.BUTT);


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
     * 创建向内缩 half 的完整圆角 Path。
     *
     * 原始：
     *
     * 0 ---------------- width
     *
     * Stroke 中心在线上，
     * 会有一半跑到 View 外面。
     *
     * 现在：
     *
     * half ---------------- width-half
     *
     * Stroke 整体都在 View 内。
     */
    private Path createInsetRoundPath(float half) {

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

            float radius = Math.min(
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


        float maxRadius = Math.min(
                rect.width(),
                rect.height()
        ) / 2f;


        float topLeft = Math.min(
                Math.min(
                        radii[0],
                        radii[1]
                ),
                maxRadius
        );

        float topRight = Math.min(
                Math.min(
                        radii[2],
                        radii[3]
                ),
                maxRadius
        );

        float bottomRight = Math.min(
                Math.min(
                        radii[4],
                        radii[5]
                ),
                maxRadius
        );

        float bottomLeft = Math.min(
                Math.min(
                        radii[6],
                        radii[7]
                ),
                maxRadius
        );


        /*
         * Stroke 中心线向内移动 half，
         * 对应圆角也减少 half。
         */
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
    private void drawCirclePartialStroke(Canvas canvas) {

        float half = mStrokeWidth / 2f;

        float centerX = mLayer.centerX();
        float centerY = mLayer.centerY();

        float radius = Math.min(
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


        mPaint.setXfermode(null);

        mPaint.setColor(mStrokeColor);

        mPaint.setStyle(Paint.Style.STROKE);

        mPaint.setStrokeWidth(
                mStrokeWidth
        );

        mPaint.setStrokeJoin(Paint.Join.ROUND);

        mPaint.setStrokeCap(Paint.Cap.BUTT);


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


    //--- Selector 支持 -------------------------------------------------------------

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


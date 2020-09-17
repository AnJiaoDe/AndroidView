package com.cy.androidview.roundview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.cy.androidview.R;
import com.cy.androidview.rectangleview.IRectangle;
import com.cy.androidview.rectangleview.RectangleRatio;
import com.cy.androidview.rippleview.IRipple;
import com.cy.androidview.rippleview.Ripple;


/**
 * @Description:
 * @Author: cy
 * @CreateDate: 2020/6/9 15:50
 * @UpdateUser:
 * @UpdateDate: 2020/6/9 15:50
 * @UpdateRemark:
 * @Version:
 */
public class RelativeLayoutRound extends RelativeLayout implements IRound, IRectangle, IRipple {


    private RectangleRatio rectangleRatio;
    private Round round;
    private Ripple ripple;

    public RelativeLayoutRound(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RelativeLayoutRound);
        ripple=ripple(typedArray);
        round=round(typedArray);
        rectangleRatio = rectangle(typedArray);
        typedArray.recycle();
    }
    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(0x00000000);
    }

    @Override
    public void setBackgroundResource(int resid) {
    }

    //实现4
    @Override
    protected void dispatchDraw(Canvas canvas) {
        round.saveLayer(canvas);
        super.dispatchDraw(canvas);
        round.round(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        rectangleRatio.rectangle(new RectangleRatio.MeasureSizeCallback() {
            @Override
            public void setMeasuredSize(int measuredWidth, int measuredHeight) {
                setMeasuredDimension(measuredWidth, measuredHeight);
                measureChildren(MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(measuredHeight, MeasureSpec.EXACTLY));
            }
        });
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        ripple.ripple();
    }
    @Override
    public Round round(TypedArray typedArray) {
        return new Round(this,typedArray)
                .setRadius(R.styleable.RelativeLayoutRound_radiusCorner)
                .setTopLeftRadius(R.styleable.RelativeLayoutRound_radiusTopLeft)
                .setTopRightRadius(R.styleable.RelativeLayoutRound_radiusTopRight)
                .setBottomLeftRadius(R.styleable.RelativeLayoutRound_radiusBottomLeft)
                .setBottomRightRadius(R.styleable.RelativeLayoutRound_radiusBottomRight);
    }
    @Override
    public RectangleRatio rectangle(TypedArray typedArray) {
        return new RectangleRatio(this,typedArray)
                .setBaseOnWidthOrHeight(R.styleable.RelativeLayoutRound_baseOnWidthOrHeight)
                .setHeightWidthRatio(R.styleable.RelativeLayoutRound_heightWidthRatio,0);
    }


    @Override
    public Ripple ripple(TypedArray typedArray) {
        return new Ripple(this, typedArray)
                .setColorRipple(R.styleable.RelativeLayoutRound_colorRipple)
                .setHavaRipple(R.styleable.RelativeLayoutRound_haveRipple);
    }

    @Override
    public Ripple getRipple() {
        return ripple;
    }

    @Override
    public RectangleRatio getRectangleRatio() {
        return rectangleRatio;
    }

    @Override
    public Round getRound() {
        return round;
    }
}

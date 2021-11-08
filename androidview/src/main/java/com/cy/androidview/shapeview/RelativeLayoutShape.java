package com.cy.androidview.shapeview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.cy.androidview.R;
import com.cy.androidview.ViewMeasureUtils;
import com.cy.androidview.rectangleview.IRectangle;
import com.cy.androidview.rectangleview.RectangleRatio;
import com.cy.androidview.rippleview.IRipple;
import com.cy.androidview.rippleview.Ripple;


/**
 * Created by cy on 2018/10/24.
 */

public class RelativeLayoutShape extends RelativeLayout implements IShape, IRectangle, IRipple {
    private RectangleRatio rectangleRatio;
    private Ripple ripple;
    private ShapeBackground shapeBackground;
    public RelativeLayoutShape(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AttrsShape);
        ripple=ripple(typedArray);
        shapeBackground=shape(typedArray);
        rectangleRatio = rectangle(typedArray);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        int[] specs=rectangleRatio.rectangle(widthMeasureSpec,heightMeasureSpec);
        super.onMeasure(specs[0],specs[1]);
        //不要调用setMeasuredDimension，否则会导致子View的测量不灵，GG
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        ripple.ripple();
    }
    @Override
    public RectangleRatio rectangle(TypedArray typedArray) {
        return new RectangleRatio(this,typedArray)
                .setHeightWidthRatio(R.styleable.AttrsShape_cy_heightWidthRatio,0);
    }

    @Override
    public Ripple ripple(TypedArray typedArray) {
        return new Ripple(this, typedArray);
    }

    @Override
    public ShapeBackground shape(TypedArray typedArray) {
        return new ShapeBackground(this,typedArray)
                .shape();
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
    public ShapeBackground getShapeBackground() {
        return shapeBackground;
    }
}

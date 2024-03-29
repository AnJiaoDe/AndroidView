package com.cy.androidview.rectangleview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatButton;

import com.cy.androidview.R;
import com.cy.androidview.ViewMeasureUtils;
import com.cy.androidview.rippleview.IRipple;
import com.cy.androidview.rippleview.Ripple;


/**
 * 正方形的LinearLayout  类似桥接模式，避免多重继承带来的维护成本
 *
 * @author Administrator
 */
public class ButtonRectangle extends AppCompatButton implements IRectangle, IRipple {
    private RectangleRatio rectangleRatio;
    private Ripple ripple;
    public ButtonRectangle(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AttrsRectangle);
        ripple=ripple(typedArray);
        rectangleRatio=rectangle(typedArray);
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
        return new RectangleRatio(this, typedArray);
    }

    @Override
    public Ripple ripple(TypedArray typedArray) {
        return new Ripple(this, typedArray);
    }

    @Override
    public Ripple getRipple() {
        return ripple;
    }

    @Override
    public RectangleRatio getRectangleRatio() {
        return rectangleRatio;
    }
}

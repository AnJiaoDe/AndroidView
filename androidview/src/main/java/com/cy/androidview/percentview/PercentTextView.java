
package com.cy.androidview.percentview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;

import com.cy.androidview.R;

/**
 * @Description:
 * @Author: cy
 * @CreateDate: 2020/9/5 17:20
 * @UpdateUser:
 * @UpdateDate: 2020/9/5 17:20
 * @UpdateRemark:
 * @Version:
 */
public class PercentTextView extends AppCompatTextView implements IPercent {
    private Percent percent;

    public PercentTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AttrsPercent);
        percent = percent(typedArray);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int[] wh = percent.percent(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(wh[0], wh[1]);
    }

    @Override
    public Percent percent(TypedArray typedArray) {
        return new Percent(this, typedArray);
    }

    @Override
    public Percent getPercent() {
        return percent;
    }
}

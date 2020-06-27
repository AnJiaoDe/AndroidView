package com.cy.androidview.textview;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * Created by lenovo on 2017/7/22.
 */

public class LineMiddleTextView extends AppCompatTextView {
    public LineMiddleTextView(Context context) {
        this(context,null);
    }

    public LineMiddleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);
        setSingleLine();
    }
}

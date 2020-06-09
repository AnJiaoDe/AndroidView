package com.cy.androidview.textview;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.cy.androidview.rippleview.TextViewClick;

/**
 * Created by lenovo on 2017/7/22.
 */

public class LineMiddleTextView extends TextViewClick {
    public LineMiddleTextView(Context context) {
        this(context,null);
    }

    public LineMiddleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);
        setSingleLine();
    }
}

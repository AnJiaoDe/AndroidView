package com.cy.androidview.textview;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.cy.androidview.rippleview.TextViewClick;


/**
 * Created by lenovo on 2017/8/12.
 */

public class MultiColorTextView extends TextViewClick {
    private StringBuilder stringBuilder;

    public MultiColorTextView(Context context) {
        this(context, null);
    }

    public MultiColorTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        stringBuilder = new StringBuilder();
    }

    public MultiColorTextView add(String text,int color) {
        stringBuilder.append(String.format("<font color=\""+color+"\">%s", text));
        return this;
    }

    public void setText() {
        setText(Html.fromHtml(stringBuilder.toString()));
    }
}

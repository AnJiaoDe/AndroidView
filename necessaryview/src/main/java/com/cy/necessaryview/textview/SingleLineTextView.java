package com.cy.necessaryview.textview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by lenovo on 2017/8/12.
 */

public class SingleLineTextView extends TextView {
    public SingleLineTextView(Context context) {
        this(context,null);
    }

    public SingleLineTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setSingleLine();
    }
}

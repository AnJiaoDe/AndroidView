package com.cy.androidview.textview;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * Created by lenovo on 2017/8/12.
 */

public class SingleLineTextView extends AppCompatTextView {
    public SingleLineTextView(Context context) {
        this(context,null);
    }
    public SingleLineTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setSingleLine();
    }
}

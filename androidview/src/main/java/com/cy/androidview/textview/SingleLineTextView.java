package com.cy.androidview.textview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.cy.androidview.rippleview.TextViewClick;

/**
 * Created by lenovo on 2017/8/12.
 */

public class SingleLineTextView extends TextViewClick {
    public SingleLineTextView(Context context) {
        this(context,null);
    }

    public SingleLineTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setSingleLine();
    }
}

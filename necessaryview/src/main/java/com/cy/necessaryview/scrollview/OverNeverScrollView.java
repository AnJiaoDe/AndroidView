package com.cy.necessaryview.scrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class OverNeverScrollView extends ScrollView {

    public OverNeverScrollView(Context context) {
        this(context,null);
    }

    public OverNeverScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOverScrollMode(OVER_SCROLL_NEVER);
    }
}

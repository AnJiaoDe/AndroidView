package com.cy.androidview.scrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class ScrollViewOverNever extends ScrollView {

    public ScrollViewOverNever(Context context) {
        this(context,null);
    }

    public ScrollViewOverNever(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOverScrollMode(OVER_SCROLL_NEVER);
    }
}

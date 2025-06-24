package com.cy.necessaryviewmaster;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class RoundedCornerLayout extends FrameLayout {
    private float cornerRadius = 30f; // 圆角半径
    private Path clipPath = new Path();
    private RectF rectF = new RectF();

    public RoundedCornerLayout(Context context) {
        super(context);
        init();
    }

    public RoundedCornerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RoundedCornerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
//        setWillNotDraw(false);
//        setLayerType(LAYER_TYPE_HARDWARE, null); // 或 SOFTWARE（兼容 clipPath）
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
//        int save = canvas.save();

        rectF.set(0, 0, getWidth(), getHeight());
        clipPath.reset();
        clipPath.addRoundRect(rectF, cornerRadius, cornerRadius, Path.Direction.CW);
        canvas.clipPath(clipPath); // 裁剪子View绘制区域

        super.dispatchDraw(canvas);
//        canvas.restoreToCount(save);
    }
}

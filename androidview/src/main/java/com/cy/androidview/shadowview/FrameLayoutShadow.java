//package com.cy.androidview.shadowview;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.util.AttributeSet;
//import android.view.View;
//import android.widget.FrameLayout;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//public class FrameLayoutShadow extends FrameLayout implements IShadow {
//    private Paint paintShadow;
//
//    public FrameLayoutShadow(@NonNull Context context) {
//        this(context, null);
//    }
//
//    public FrameLayoutShadow(@NonNull Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
//        //禁用硬件加速
//        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        paintShadow = new Paint();
//        paintShadow.setColor(Color.BLACK);
//        paintShadow.setShadowLayer(1,10,10,Color.RED);
//    }
//}

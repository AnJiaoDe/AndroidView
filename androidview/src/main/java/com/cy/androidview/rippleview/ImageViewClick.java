package com.cy.androidview.rippleview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatImageView;

import com.cy.androidview.R;


/**
 * Created by cy on 2018/10/27.
 */

public class ImageViewClick extends AppCompatImageView {
    //负数，变暗(三个-30，值越大则效果越深)
    public float[] filters = new float[]{
            1, 0, 0, 0, -30,
            0, 1, 0, 0, -30,
            0, 0, 1, 0, -30,
            0, 0, 0, 1, 0};
    //正数，变亮
//    public float[] BT_SELECTED_LIGHT = new float[]{
//            1, 0, 0, 0, 30,
//            0, 1, 0, 0, 30,
//            0, 0, 1, 0, 30,
//            0, 0, 0, 1, 0};
//    //恢复
//    public float[] BT_NOT_SELECTED = new float[]{
//            1, 0, 0, 0, 0,
//            0, 1, 0, 0, 0,
//            0, 0, 1, 0, 0,
//            0, 0, 0, 1, 0};

    private boolean havaFilter;

    private boolean haveRipple;

    public ImageViewClick(Context context) {
        this(context, null);
    }

    public ImageViewClick(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.ImageViewClick);
        //水波纹的颜色,默认是0x66000000，建议自定义水波纹颜色的时候，用argb,rgb都设置为0，a可随意，调整透明度为了水波纹看起来更美观
        int colorRipple = arr.getColor(R.styleable.ImageViewClick_colorRipple, 0x66000000);
        havaFilter = arr.getBoolean(R.styleable.ImageViewClick_haveFilter, true);//设置是否有滤镜点击效果，默认有
        //设置是否有水波纹点击效果，默认无,优先级比haveFilter高
        haveRipple = arr.getBoolean(R.styleable.ImageViewClick_haveRipple, false);
        boolean rippleOver=arr.getBoolean(R.styleable.ImageViewClick_rippleOver, true);//波纹是否超过边界
        //设置点击效果：颜色滤镜，还是波纹（波纹效果，只有在图片没有将控件全部填充的时候有效），默认是颜色滤镜

//        //(具体小编也不是很清楚，这个概念，小编也很模糊)，
//        int rippleMaskSize = arr.getDimensionPixelSize(R.styleable.RippleView_rippleMaskSize, 100);
        //设置滤镜，变亮还是变暗，默认变暗
        boolean filterLightOrDark = arr.getBoolean(R.styleable.ImageViewClick_filterLightOrDark, false);


        if (haveRipple) {
            havaFilter = false;
            //5.0以上才有效,
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                Drawable drawable = getBackground();

                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setColor(colorRipple);

                //当控件设置了点击监听器，并且控件点击有效，时，才能产生水波纹
                RippleDrawable rippleDrawable = new RippleDrawable(ColorStateList.valueOf(colorRipple), drawable, rippleOver?null:gradientDrawable);

                setBackground(rippleDrawable);
            }
        } else {
            //滤镜，变暗或者变亮，负数，变暗(值越大则效果越深)，正数，变亮
            float filterLight = arr.getFloat(R.styleable.ImageViewClick_filterLightNumber, filterLightOrDark ? 30 : -30);
            filters[4] = filterLight;
            filters[9] = filterLight;
            filters[14] = filterLight;
        }

        arr.recycle();


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (haveRipple) return super.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (havaFilter)
                    setColorFilter(new ColorMatrixColorFilter(filters));
                return true;
            case MotionEvent.ACTION_UP:
                clearColorFilter();
                break;
        }
        return super.onTouchEvent(event);
    }
}
